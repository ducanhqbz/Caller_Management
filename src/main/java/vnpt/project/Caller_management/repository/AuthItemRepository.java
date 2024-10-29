package vnpt.project.Caller_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vnpt.project.Caller_management.model.AuthItem;

import java.util.List;

public interface AuthItemRepository extends JpaRepository<AuthItem, String> {
    @Query("SELECT DISTINCT a.name FROM AuthItem a where a.type = :type")
    List<String> findByType(int type);


    @Query(value = "SELECT a FROM AuthItem a WHERE a.name = :name")
    AuthItem findByName(String name);
}
