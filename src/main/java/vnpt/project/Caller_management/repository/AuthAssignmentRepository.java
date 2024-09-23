package vnpt.project.Caller_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vnpt.project.Caller_management.model.Auth_Assignment;

import java.util.List;

public interface AuthAssignmentRepository extends JpaRepository<Auth_Assignment, Integer> {
    List<Auth_Assignment> findByUserIdAndDepartment(int userId, int department);
    Auth_Assignment findByUserId(int userId);
    @Query("SELECT DISTINCT a.name FROM Auth_Assignment a")
    List<String> findDistinctItemName();

}
