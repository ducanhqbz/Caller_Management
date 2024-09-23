package vnpt.project.Caller_management.controller;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnpt.project.Caller_management.Services.CrudServices;

import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;


@Controller
public class CrudController {
    //--------------------------------------------------------------------
    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    CrudServices crudServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthAssignmentRepository authAssignmentRepository;
    //--------------------------------------------------------------------
    @GetMapping("/deleteEmail/{email}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String delete(@PathVariable String email, Model model, RedirectAttributes redirectAttributes) {
        boolean status = userRepository.deleteUserByEmail(email);
        if (status == true) {

            redirectAttributes.addAttribute("status", "Delete success");
        } else {

            redirectAttributes.addAttribute("status", "Delete Failed");
        }

        return "redirect:/ShowAll";
    }
    //--------------------------------------------------------------------
    @GetMapping("/GetObject/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String getObject(@PathVariable int id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id);
        if (user != null) {
            System.out.println("This is Get object");
            System.out.println(user);
            redirectAttributes.addAttribute("userForEdit", user);
        } else {
            redirectAttributes.addAttribute("status", "Dont have Account with id: " + id);
        }
        return "redirect:/ShowAll";
    }
    //--------------------------------------------------------------------
    @PostMapping("/AddTask")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String AddTask(@RequestParam("idUser") int idUser,
                          @RequestParam("department") int department,
                          @RequestParam("assignment") String assignment,
                          Model model,RedirectAttributes redirectAttributes) {
        System.out.println(idUser);
        System.out.println(department);
        System.out.println(assignment);
boolean status = crudServices.AddTask(idUser,department,assignment);
if (status==true) {
    redirectAttributes.addAttribute("status", "Add Task successfull");

} else {
    redirectAttributes.addAttribute("status", "Add Task failed");

}
        return "redirect:/ShowAll";
    }


    //--------------------------------------------------------------------
    @PostMapping("/Update")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String Update(@RequestParam("username") String username,
                         @RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("fullName") String fullName,
                         @RequestParam("email") String email,
                         @RequestParam("department") int departmentId,
                         Model model, RedirectAttributes redirectAttributes) {
        // Tìm user theo email
        User user = userRepository.findByEmail(email);

        // Kiểm tra nếu user không tồn tại
        if (user == null) {
            redirectAttributes.addAttribute("status", "User not found");
            return "redirect:/ShowAll";
        }


        // Cập nhật thông tin người dùng
        boolean status = crudServices.Update(email, username, firstName, lastName, departmentId);

        // Kiểm tra trạng thái cập nhật
        if (status) {
            redirectAttributes.addAttribute("status", "Edit success");
        } else {
            redirectAttributes.addAttribute("status", "Edit failed");
        }

        return "redirect:/ShowAll";
    }
    //--------------------------------------------------------------------
    @PostMapping("/Insert")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String Insert(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("username") String username,
                         @RequestParam("first") String firstName,
                         @RequestParam("last") String lastName,
                         @RequestParam("department") int departmentId,
                         @RequestParam("assignment") String assignment, Model model, RedirectAttributes redirectAttributes) {
        boolean status = crudServices.Insert(email, password, username, firstName, lastName, departmentId, assignment);
        if (status == true) {
            redirectAttributes.addAttribute("status", "Insert success");
        } else {
            redirectAttributes.addAttribute("status", "Insert Failed");
        }
        return "redirect:/ShowAll";
    }
}

