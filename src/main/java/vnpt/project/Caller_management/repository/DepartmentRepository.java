package vnpt.project.Caller_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt.project.Caller_management.model.Departments;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer> {
    Departments findByid(int id);
  Page<Departments> findAll(Pageable pageable);
 List<Departments> findAll();

}
