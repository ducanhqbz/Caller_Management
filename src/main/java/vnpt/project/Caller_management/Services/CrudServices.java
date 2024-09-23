package vnpt.project.Caller_management.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vnpt.project.Caller_management.model.Auth_Assignment;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class CrudServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CrudServices crudServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthAssignmentRepository authAssignmentRepository;

    public long getUnixTimeStamp() {
        // Step 1: Get the current LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();

        // Step 2: Convert LocalDateTime to an Instant (using UTC offset)
        long unixTimestamp = localDateTime.toEpochSecond(ZoneOffset.UTC);
        return unixTimestamp;
    }

    public boolean Update(String email, String username, String firstName, String lastName, int departmentId) {

        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setEmail(email);

            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setDepartmentId(departmentId);
            user.setActive(1);
            user.setUpdateAt(crudServices.getUnixTimeStamp());
            User userSave = userRepository.save(user);

            return true;
        } else {
            return false;
        }
    }
public boolean AddTask(int id , int deparment , String task){
        Auth_Assignment authAssignment = new Auth_Assignment();
        authAssignment.setUserId(id);
        authAssignment.setDepartment(deparment);
        authAssignment.setName(task);
    System.out.println(authAssignment);
        Auth_Assignment auth_assignmentSave = authAssignmentRepository.save(authAssignment);
    System.out.println(auth_assignmentSave);
        if (auth_assignmentSave != null) {
            return true;
        }

        return false;
}
    public boolean Insert(String email, String password, String username, String firstName, String lastName, int departmentId, String assignment) {
        if (userRepository.existsByEmail(email) == true) {
            return false;

        }
        if (userRepository.existsByUsername(username) == true) {
            return false;

        }

        User user = new User();
        user.setEmail(email);
        user.setPassword_hash(passwordEncoder.encode(password));
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFullName(firstName + " " + lastName);
        user.setDepartmentId(departmentId);
        user.setActive(1);
        user.setCreatedAt(crudServices.getUnixTimeStamp());
        user.setUpdateAt(crudServices.getUnixTimeStamp());
        User userSave = userRepository.save(user);
        if (userSave != null) {
            Auth_Assignment authAssignment = new Auth_Assignment();
            authAssignment.setName(assignment);
            authAssignment.setDepartment(departmentId);
            authAssignment.setUserId(userSave.getId());
            authAssignmentRepository.save(authAssignment);

            return true;
        } else {

            return false;
        }
    }
}