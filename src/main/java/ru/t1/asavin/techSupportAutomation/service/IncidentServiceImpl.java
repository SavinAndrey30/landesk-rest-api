package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.t1.asavin.techSupportAutomation.dao.CategoryRepository;
import ru.t1.asavin.techSupportAutomation.dao.IncidentRepository;
import ru.t1.asavin.techSupportAutomation.dao.PriorityRepository;
import ru.t1.asavin.techSupportAutomation.dao.UserRepository;
import ru.t1.asavin.techSupportAutomation.dto.IncidentDto;
import ru.t1.asavin.techSupportAutomation.entity.*;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;
import ru.t1.asavin.techSupportAutomation.exception.ScreenshotFileIncorrectException;
import ru.t1.asavin.techSupportAutomation.mqActive.producer.MQProducerService;
import ru.t1.asavin.techSupportAutomation.util.FileUploadUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "incidents")
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final CategoryRepository categoryRepository;
    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final MQProducerService MQProducerService;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public List<IncidentDto> findAll() {
        return incidentRepository.findAll().stream().map(IncidentDto::new).collect(Collectors.toList());
    }

    @Cacheable(key = "#incidentId")
    public IncidentDto findById(Long incidentId) {
        Optional<Incident> incidentOptional = incidentRepository.findById(incidentId);
        if (incidentOptional.isPresent()) {
            return new IncidentDto(incidentOptional.get());
        } else {
            log.error("Get incident by id: incident with the id " + incidentId + " is not found");
            throw new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
        }
    }

    @Override
    @CachePut(key = "#incident.id")
    public IncidentDto update(Incident incident) {
        Long incidentId = incident.getId();
        if (incidentRepository.existsById(incidentId)) {
            log.info("Updating incident with the id " + incidentId);
            return new IncidentDto(incidentRepository.save(incident));
        } else {
            log.error("Updating incident: incident with the id " + incidentId + " is not found");
            throw new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
        }
    }

    @Override
    @CachePut(key = "#incidentId")
    public IncidentDto updateIncidentCategory(Long incidentId, int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return incidentRepository.findById(incidentId)
                .map(incident -> {
                    if (categoryOptional.isPresent()) {
                        incident.setCategory(categoryOptional.get());

                        log.info("Updating incident category: category with the id " + categoryId + " is set to the incident with the id " + incidentId);
                        return new IncidentDto(incidentRepository.save(incident));
                    } else {
                        log.error("Updating incident category: category with the id " + categoryId + " is not found");
                        throw new EntityNotFoundException("Category with the id " + categoryId + " is not found");
                    }
                })
                .orElseThrow(() -> {
                    log.error("Updating incident category: incident with the id " + incidentId + " is not found");
                    return new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
                });
    }

    @Override
    @CachePut(key = "#incidentId")
    public IncidentDto updateIncidentStatus(Long incidentId, Status status) {
        return incidentRepository.findById(incidentId)
                .map(incident -> {
                    status.setIncident(incident);
                    incident.addStatusUpdates(status);

                    String statusName = status.getName();
                    if (statusName.equals(Stage.CLOSED.getName())) {
                        incident.setDateClosed(LocalDateTime.now());
                        log.debug("Set dateClosed field");
                    }

                    log.info("Updating incident status: status with the name " + statusName + " is set to the incident with the id " + incidentId);
                    return new IncidentDto(incidentRepository.save(incident));
                })
                .orElseThrow(() -> {
                    log.error("Updating incident status: incident with the id " + incidentId + " is not found");
                    return new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
                });
    }

    @Override
    @CachePut(key = "#incidentId")
    public IncidentDto updateIncidentPriority(Long incidentId, int priorityId) {
        Optional<Priority> priorityOptional = priorityRepository.findById(priorityId);
        return incidentRepository.findById(incidentId)
                .map(incident -> {
                    if (priorityOptional.isPresent()) {
                        incident.setPriority(priorityOptional.get());

                        log.info("Updating incident priority: priority with the id " + priorityId + " is set to the incident with the id " + incidentId);
                        return new IncidentDto(incidentRepository.save(incident));
                    } else {
                        log.error("Updating incident priority: priority with the id " + priorityId + " is not found");
                        throw new EntityNotFoundException("Priority with the id " + priorityId + " is not found");
                    }
                })
                .orElseThrow(() -> {
                    log.error("Updating incident priority: incident with the id " + incidentId + " is not found");
                    return new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
                });
    }

    @Override
    @CachePut(key = "#incidentId")
    public IncidentDto updateIncidentAnalyst(Long incidentId, Long analystId) {
        Optional<User> userOptional = userRepository.findById(analystId);
        return incidentRepository.findById(incidentId)
                .map(incident -> {
                    if (userOptional.isPresent()) {
                        User assignedAnalyst = userOptional.get();
                        incident.setAssignedAnalyst(assignedAnalyst);

                        String[] list = new String[]{incidentId.toString(), assignedAnalyst.getUsername(), assignedAnalyst.getUserEmail()};
                        MQProducerService.sendMessage(list);

                        log.info("Updating incident analyst: user with the id " + analystId + " is set to the incident with the id " + incidentId);
                        return new IncidentDto(incidentRepository.save(incident));
                    } else {
                        log.error("Updating incident analyst: user with the id " + analystId + " is not found");
                        throw new EntityNotFoundException("User with the id " + analystId + " is not found");
                    }
                })
                .orElseThrow(() -> {
                    log.error("Updating incident analyst: incident with the id " + incidentId + " is not found");
                    return new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
                });
    }

    @Override
    @CachePut(key = "#incident.id")
    public IncidentDto save(Incident incident, MultipartFile screenshotFile) {
        String fileName;
        boolean screenshotUploaded = screenshotFile != null && !screenshotFile.isEmpty();

        if (screenshotUploaded) {
            log.debug("Saving incident request contains screenshot to save");
            String originalFilename = screenshotFile.getOriginalFilename();

            if (screenshotFile.getSize() > 1048576) {
                log.error("The screenshot file is too big");
                throw new ScreenshotFileIncorrectException("The uploaded screenshot file doesn't match the requirements");
            }

            fileName = StringUtils.cleanPath(originalFilename);

        } else {
            fileName = null;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByUsername(auth.getName()).get();
        log.debug("Incident initiator is the user with the username" + loggedInUser.getUsername());

        LocalDateTime initialDateTime = LocalDateTime.now();

        setIncidentFields(incident, fileName, loggedInUser, initialDateTime);

        Incident incidentToSave = incidentRepository.save(incident);
        log.info("New incident saved");

        if (screenshotUploaded) {
            String uploadDir = uploadPath + "/" + incident.getId();
            log.info("saving incident screenshot to the directory " + uploadDir);
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, screenshotFile);
            } catch (IOException e) {
                log.error("Error during saving incident screenshot", e);
            }
        }

        return new IncidentDto(incidentToSave);
    }

    @Override
    @CacheEvict(key = "#incidentId")
    public IncidentDto delete(Long incidentId) {
        return incidentRepository.findById(incidentId)
                .map(incident -> {

                    String screenshotFileName = incident.getScreenshotFilename();

                    if (!(screenshotFileName == null)) {
                        String uploadDir = uploadPath + "/" + incidentId;
                        Path uploadPath = Path.of(uploadDir);

                        try {
                            FileSystemUtils.deleteRecursively(uploadPath);
                        } catch (IOException e) {
                            log.error("couldn't remove the folder with screenshot for deleted incident with the id " + incidentId, e);
                        }
                    }

                    incidentRepository.deleteById(incidentId);
                    log.info("Incident with the id " + incidentId + " is deleted");
                    return new IncidentDto(incident);
                })
                .orElseThrow(() -> {
                    log.error("Deleting incident: incident with the id " + incidentId + " is not found");
                    return new EntityNotFoundException("Incident with the id " + incidentId + " is not found");
                });
    }

    private void setIncidentFields(Incident incident, String filename, User loggedInUser, LocalDateTime initialDateTime) {
        incident.setDateCreated(initialDateTime);
        incident.setDeadline(initialDateTime.plusDays(3));
        Status initialStatus = new Status(Stage.OPEN.getName());
        initialStatus.setIncident(incident);
        incident.setStatusUpdates(Collections.singletonList(initialStatus));
        incident.setScreenshotFilename(filename);
        incident.setInitiatorFullName(loggedInUser.getUserFullName());
        incident.setInitiatorUsername(loggedInUser.getUsername());
        incident.setInitiatorPhone(loggedInUser.getUserPhone());
        incident.setInitiatorLocation(loggedInUser.getUserLocation());
        incident.setInitiatorEmail(loggedInUser.getUserEmail());
    }
}
