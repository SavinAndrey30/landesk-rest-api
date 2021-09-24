package ru.t1.asavin.techSupportAutomation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.asavin.techSupportAutomation.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByName(String roleName);
}
