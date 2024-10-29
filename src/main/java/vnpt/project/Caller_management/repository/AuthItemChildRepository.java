package vnpt.project.Caller_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vnpt.project.Caller_management.model.AuthItemChild;

import java.util.List;

public interface AuthItemChildRepository extends JpaRepository<AuthItemChild, String> {
    
    List<AuthItemChild> findByParent(String parent);

    List<AuthItemChild> findByChild(String child);
}
