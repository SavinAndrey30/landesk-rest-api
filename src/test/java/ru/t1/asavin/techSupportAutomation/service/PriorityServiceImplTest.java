package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.asavin.techSupportAutomation.dao.PriorityRepository;
import ru.t1.asavin.techSupportAutomation.dto.PriorityDto;
import ru.t1.asavin.techSupportAutomation.entity.Priority;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PriorityServiceImplTest {

    private static final int ID = 1;
    private static final String NAME = "Высокий";

    @Mock
    private PriorityRepository priorityRepository;

    @InjectMocks
    private PriorityServiceImpl priorityService;

    @Test
    void findAll() {
        Priority priority = createPriorityObject(ID, NAME);
        List<Priority> priorities = List.of(priority);
        List<PriorityDto> actualPriorities = List.of(new PriorityDto(priority));

        doReturn(priorities).when(priorityRepository).findAll();

        List<PriorityDto> expectedPrioritiesDto = priorityService.findAll();

        assertThat(expectedPrioritiesDto).isEqualTo(actualPriorities);
    }

    @Test
    void update() {
        Priority priority = createPriorityObject(ID, NAME);

        doReturn(true).when(priorityRepository).existsById(ID);
        doReturn(priority).when(priorityRepository).save(priority);

        PriorityDto updatedPriorityDto = priorityService.update(priority);
        assertThat(updatedPriorityDto).isNotNull();

        verify(priorityRepository).save(any(Priority.class));
    }

    @Test
    void save() {
        Priority priority = createPriorityObject(ID, NAME);
        PriorityDto priorityDto = new PriorityDto(priority);

        doReturn(priority).when(priorityRepository).save(priority);

        PriorityDto savedPriorityDto = priorityService.save(priority);

        assertEquals(savedPriorityDto, priorityDto);
        assertNotNull(priorityRepository.findById(savedPriorityDto.getId()));
    }

    @Test
    void save_shouldThrowEntityAlreadyExistsException() {
        Priority priority = createPriorityObject(ID, NAME);

        doReturn(true).when(priorityRepository).existsByName(NAME);

        assertThrows(EntityAlreadyExistsException.class, () -> priorityService.save(priority));
    }

    @Test
    void delete() {
        Priority priorityToDelete = createPriorityObject(ID, NAME);
        PriorityDto priorityToDeleteDto = new PriorityDto(priorityToDelete);

        doReturn(Optional.of(priorityToDelete)).when(priorityRepository).findById(ID);

        PriorityDto deletedPriorityDto = priorityService.delete(ID);
        assertThat(priorityRepository.existsById(ID)).isFalse();
        assertEquals(priorityToDeleteDto, deletedPriorityDto);
    }

    public Priority createPriorityObject(int id, String name) {
        Priority priority = new Priority();
        priority.setId(id);
        priority.setName(name);

        return priority;
    }
}