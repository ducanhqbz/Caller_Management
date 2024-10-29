package vnpt.project.Caller_management.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vnpt.project.Caller_management.model.Auth_Assignment;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CrudServices crudServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthAssignmentRepository authAssignmentRepository;

    @Autowired
    AuthAssignmentRepository authRepo;

    public boolean EditTask(int id, String asssignment) {
        Auth_Assignment auth = authAssignmentRepository.findById(id).get();
        if (auth != null) {
            auth.setName(asssignment);

            authAssignmentRepository.save(auth);
            return true;

        } else {
            return false;

        }


    }

    public boolean updateDepartment(int idUser, int idDepartment, String authName, int departmentIdNew) {
        // Tìm đối tượng Auth_Assignment hiện tại


        // Nếu tìm thấy, ta sẽ tạo một đối tượng mới với department mới

            Auth_Assignment newAuthAssignment = new Auth_Assignment();
            newAuthAssignment.setUserId(idUser);
            newAuthAssignment.setName(authName);
            newAuthAssignment.setDepartment(departmentIdNew);  // Gán department mới

            // Lưu đối tượng mới vào cơ sở dữ liệu
            authAssignmentRepository.save(newAuthAssignment);
            return true;
      // Trả về false nếu không tìm thấy đối tượng
    }

    public boolean DeleteDeparment(int idUser, int idDepartment,String authName) {
    Auth_Assignment auth = authAssignmentRepository.findAuthAssignmentByUserIdAndDepartmentAndName(idUser,idDepartment,authName);
    if(auth!=null) {
        authAssignmentRepository.delete(auth);
        return true;
    }

    return false;
        }

    public boolean AddTask(List<String> Role, List<String> Permission, int departmentId, int UserId) {
        User userLogin = crudServices.getCurrentUser();

        // Xử lý danh sách Role nếu Role không null
        if (Role != null) {
            Iterator<String> roleIterator = Role.iterator();
            while (roleIterator.hasNext()) {
                String role = roleIterator.next();

                // Kiểm tra xem role đã tồn tại trong db hay chưa
                boolean roleExists = authAssignmentRepository.existsByUserIdAndName(UserId, role);
                if (!roleExists) {
                    Auth_Assignment authAssignment = new Auth_Assignment();
                    authAssignment.setDepartment(departmentId);
                    authAssignment.setName(role);
                    authAssignment.setUserId(UserId);
                    authAssignmentRepository.save(authAssignment);
                }

                // Xóa phần tử sau khi xử lý
                roleIterator.remove();
            }
        }

        // Xử lý danh sách Permission nếu Permission không null
        if (Permission != null) {
            Iterator<String> permissionIterator = Permission.iterator();
            while (permissionIterator.hasNext()) {
                String permission = permissionIterator.next();

                // Kiểm tra xem permission đã tồn tại trong db hay chưa
                boolean permissionExists = authAssignmentRepository.existsByUserIdAndName(UserId, permission);
                if (!permissionExists) {
                    Auth_Assignment authAssignment = new Auth_Assignment();
                    authAssignment.setDepartment(departmentId);
                    authAssignment.setName(permission);
                    authAssignment.setUserId(UserId);
                    authAssignmentRepository.save(authAssignment);
                }

                // Xóa phần tử sau khi xử lý
                permissionIterator.remove();
            }
        }

        // Kiểm tra nếu cả Role và Permission đều đã trống (hoặc ban đầu null)
        boolean roleEmpty = (Role == null || Role.isEmpty());
        boolean permissionEmpty = (Permission == null || Permission.isEmpty());

        return roleEmpty && permissionEmpty;
    }


}