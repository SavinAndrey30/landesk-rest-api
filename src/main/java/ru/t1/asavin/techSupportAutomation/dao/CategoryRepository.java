package ru.t1.asavin.techSupportAutomation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.asavin.techSupportAutomation.entity.Category;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByName(String categoryName);
}
