package vnpt.project.Caller_management.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnpt.project.Caller_management.Services.CrudServices;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAPI {
    @Autowired
    private UserRepository userRepository;
@Autowired
private CrudServices  crudServices;
    @RequestMapping(value = "/GetAllUser", method = RequestMethod.GET)
    public ResponseEntity<Page<User>> getAllUser(@RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> listUser = userRepository.findAll(pageable);
        if (listUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listUser);

    }

    @RequestMapping(value = "/getUserwithid/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> GetUserwithId(@PathVariable int id) {
        User listUser = userRepository.findById(id);
        System.out.println(listUser);
        return ResponseEntity.ok(listUser);

    }
@RequestMapping(value = "/DeleteUser/{id}", method = RequestMethod.DELETE)
public ResponseEntity<String> DeleteUser(@PathVariable int id) {
        if (!userRepository.existsById(id)) {

            return ResponseEntity.notFound().build();

        }
    userRepository.deleteById(id);
    return ResponseEntity.ok("Delete Success");
}

@RequestMapping(value = "/UpdateUser/{email}", method = RequestMethod.PUT)
    public ResponseEntity<String> UpdateUser(@PathVariable String email, @RequestBody User user) {
        boolean  status = crudServices.Update(email, user.getUsername(),user.getFirstName(),user.getLastName(),user.getDepartmentId());
    if (status==false){
        return ResponseEntity.notFound().build();

    }

        return ResponseEntity.ok("Update Success");}

}
