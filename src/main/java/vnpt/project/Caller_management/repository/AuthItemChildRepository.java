package vnpt.project.Caller_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vnpt.project.Caller_management.model.AuthItemChild;

import java.util.List;

public interface AuthItemChildRepository extends JpaRepository<AuthItemChild, Integer> {
    @Query("select distinct child from AuthItemChild aic where aic.parent=:parent")
            List<String> findByParent(String parent);


            List<AuthItemChild> findByChild(String child);
}
