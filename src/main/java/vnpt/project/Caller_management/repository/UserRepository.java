package vnpt.project.Caller_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt.project.Caller_management.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);
    User findByEmail(String email);


    User findByUsername(String username);


    Page<User> findAll(Pageable pageable);


    Page<User> findAllByDepartmentId(int departmentId, Pageable pageable);



    boolean existsByEmail(String email);


    boolean existsByUsername(String username);
void deleteByEmail(String email);

    @Transactional
    default boolean deleteUserByEmail(String email) {
        if (existsByEmail(email)) {
            deleteByEmail(email);
            return true;
        }
        return false;
    }
}
