package vnpt.project.Caller_management.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import vnpt.project.Caller_management.model.Auth_Assignment;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LoginServices implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private AuthAssignmentRepository authRepo;

    private static final Logger logger = Logger.getLogger(LoginServices.class.getName());

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm kiếm user qua email
        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(email));

        if (userOptional.isEmpty()) {
            logger.warning("Email không tồn tại: " + email);
            // Trả về một người dùng mặc định với quyền "ROLE_Guests"
            return new org.springframework.security.core.userdetails.User(
                    "",
                    "",
                    List.of(new SimpleGrantedAuthority("ROLE_Guests"))
            );
        }

        User user = userOptional.get();

        // Lấy danh sách quyền của người dùng
        List<GrantedAuthority> authorities = getAuthorities(user.getId(), user.getDepartmentId());

        // Trả về UserDetails với danh sách quyền
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword_hash(), authorities);
    }


    public List<GrantedAuthority> getAuthorities(int userID, int departmentID) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<Auth_Assignment> list = authRepo.findByUserIdAndDepartment(userID, departmentID);
        if (list.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return authorities;

        }

        for (Auth_Assignment auth : list)
            if ("sysadmin".equalsIgnoreCase(auth.getName())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_SYSADMIN"));

            }
        return authorities;
    }
}