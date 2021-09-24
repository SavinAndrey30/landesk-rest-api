package ru.t1.asavin.techSupportAutomation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.asavin.techSupportAutomation.entity.Incident;

@Repository
@Transactional
public interface IncidentRepository extends JpaRepository<Incident, Long> {

}
