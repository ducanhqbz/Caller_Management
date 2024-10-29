package vnpt.project.Caller_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vnpt.project.Caller_management.model.Auth_Assignment;

import java.util.List;

public interface AuthAssignmentRepository extends JpaRepository<Auth_Assignment, Integer> {
    List<Auth_Assignment> findByUserIdAndDepartment(int userId, int department);

    List<Auth_Assignment> findByUserId(int userId);

    @Query("SELECT DISTINCT a.name FROM Auth_Assignment a")
    public List<String> findDistinctItemName();

    boolean existsByUserIdAndDepartment(int userId, int department);

    boolean existsByUserIdAndName(int userId, String name);

    @Query("SELECT a.department FROM Auth_Assignment a WHERE a.userId = :userId and a.name=:name")
    List<Integer> findDepartmentByUserIdAndName(@Param("userId") int userId, @Param("name") String name);

    Page<Auth_Assignment> findByUserId(int userId, Pageable pageable);

    @Transactional
    default boolean deleteByUserIdAndDepartment(int userId, int departmentId) {
        // Tìm danh sách các bản ghi Auth_Assignment cần xóa
        List<Auth_Assignment> list = findByUserIdAndDepartment(userId, departmentId);

        // Kiểm tra nếu không có bản ghi nào
        if (list.isEmpty()) {
            return true; // Không có bản ghi nào để xóa
        }

        // Xóa tất cả các bản ghi tìm thấy
        for (Auth_Assignment auth : list) {
            delete(auth);
        }

        return true; // Xóa thành công
    }

    @Transactional
    default boolean deleteByUserId(int userId) {
        // Tìm danh sách các bản ghi Auth_Assignment cần xóa dựa trên userId
        List<Auth_Assignment> list = findByUserId(userId);

        // Kiểm tra nếu không có bản ghi nào
        if (list.isEmpty()) {
            return true; // Không có bản ghi nào để xóa
        }

        // Xóa tất cả các bản ghi tìm thấy
        for (Auth_Assignment auth : list) {
            delete(auth); // Gọi phương thức delete để xóa từng bản ghi
        }

        return true; // Xóa thành công
    }

    @Transactional
    @Modifying
    @Query("UPDATE Auth_Assignment a SET a.department = :newDepartment WHERE a.userId = :userId AND a.department = :oldDepartment")
    void updateDepartment(@Param("userId") int userId, @Param("newDepartment") int newDepartment,
            @Param("oldDepartment") int oldDepartment);

    @Transactional
    default boolean deleteAuthAssignmentById(int id) {
        if (existsById(id)) {
            deleteById(id);
            return true; // Xóa thành công
        } else {
            return false; // Không tồn tại đối tượng để xóa
        }
    }

    @Query("SELECT a FROM Auth_Assignment a WHERE a.userId = :userId AND a.department = :department AND a.name = :name")
    Auth_Assignment findAuthAssignmentByUserIdAndDepartmentAndName(
            @Param("userId") int userId,
            @Param("department") int department,
            @Param("name") String name);
}
