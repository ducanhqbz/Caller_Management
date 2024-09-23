package vnpt.project.Caller_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vnpt.project.Caller_management.model.Departments;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.DepartmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthAssignmentRepository auth;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/loginPage")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/Dashboard")
    public String Dashboard() {
        return "Dashboard";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping( "/ShowAll")
    public String getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "5") int size,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "userForEdit", required = false) User userForEdit,

                           Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size); // khai bao
        User user = userRepository.findByEmail(userDetails.getUsername());
        Page<User> list = userRepository.findAllByDepartmentId(user.getDepartmentId(),pageable);
        List<Departments> listDepartment = departmentRepository.findAll();
        List<String> listAuth = auth.findDistinctItemName();
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listAuth", listAuth);
        model.addAttribute("list", list); //
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("userLogin", user);
        if (status != null) {
            model.addAttribute("status", status);  // Trạng thái nếu có
        }
        if (userForEdit != null) {
            System.out.println("This is Show ALl");
            System.out.println(userForEdit);
            model.addAttribute("userForEdit", userForEdit);
        }

        return "ShowAll";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; // Trả về trang accessDenied.html
    }

}
