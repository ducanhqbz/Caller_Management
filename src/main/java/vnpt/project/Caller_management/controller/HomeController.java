package vnpt.project.Caller_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vnpt.project.Caller_management.Services.CrudServices;
import vnpt.project.Caller_management.model.Auth_Assignment;
import vnpt.project.Caller_management.model.Departments;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.DepartmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthAssignmentRepository auth;
    @Autowired
    CrudServices crudServices;

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
    public String register(Model model){
        User user = crudServices.getCurrentUser();
    List<Auth_Assignment> list = user.getListPermission();
    model.addAttribute("list",list);
    return "/register";
  }

    @GetMapping("/CallManager")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String CallManager() {
        return "CallManager";
    }

    @GetMapping("/ShowAll")
    public String getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "5") int size,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "userForEdit", required = false) User userForEdit,
                           // Nhận listAuth từ FlashAttributes
                           Model model) {

        // Lấy thông tin người dùng hiện tại
        User currentUser = crudServices.getCurrentUser();

        if (currentUser == null) {
            // Xử lý trường hợp người dùng không đăng nhập hoặc không tồn tại
            return "redirect:/login";  // Chuyển hướng đến trang đăng nhập
        }

        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Tìm tất cả người dùng theo phòng ban của người dùng hiện tại
        Page<User> lisuser = userRepository.findAllByDepartmentId(currentUser.getDepartmentId(), pageable);

        // Khởi tạo danh sách phòng ban
        List<Departments> listDepartment = new ArrayList<>();

        // Chỉ thêm phòng ban nếu quyền là SYSADMIN
        for (Auth_Assignment authAssignment : currentUser.getListPermission()) {
            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                Departments department = departmentRepository.findByid(authAssignment.getDepartment());
                if (department != null) {
                    listDepartment.add(department);
                }
            }
        }

        // Add dữ liệu vào model
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listAuth", auth.findDistinctItemName());  // Lấy danh sách quyền

        model.addAttribute("list", lisuser);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", lisuser.getTotalPages());
        model.addAttribute("userLogin", currentUser);

        // Trạng thái nếu có
        if (status != null) {
            model.addAttribute("status", status);
        }

        // Xử lý nếu có thông tin người dùng để chỉnh sửa
        if (userForEdit != null) {
            model.addAttribute("userForEdit", userForEdit);
        }

        // Xử lý listAuth nếu nó không rỗng

        return "ShowAll";
    }


    @GetMapping("/GetUserEachDeparment/{id}")
    public String GetUserEachDeparment(@PathVariable("id") int id,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size, Model model) {
        User currentUser = crudServices.getCurrentUser();
        if (currentUser == null) {
            // Xử lý trường hợp người dùng không đăng nhập hoặc không tồn tại
            return "redirect:/login";  //
        }
        Departments selectedDepartment = departmentRepository.findById(id).orElse(null); // Tìm department được chọn
        model.addAttribute("selectedDepartment", selectedDepartment); // Thêm vào model

        Pageable pageable = PageRequest.of(page, size);

        // Tìm tất cả người dùng theo phòng ban của người dùng hiện tại
        Page<User> lisuser = userRepository.findAllByDepartmentId(id, pageable);
        List<Auth_Assignment> listAuth = auth.findAll();

        // Khởi tạo danh sách phòng ban
        List<Departments> listDepartment = new ArrayList<>();

        // Lấy danh sách quyền của người dùng hiện tại
        List<Auth_Assignment> userAuthAssignments = auth.findByUserId(currentUser.getId());

        // Chỉ thêm phòng ban nếu quyền là SYSADMIN
        for (Auth_Assignment authAssignment : userAuthAssignments) {
            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                Departments department = departmentRepository.findByid(authAssignment.getDepartment());
                if (department != null) {
                    listDepartment.add(department);
                }
            }
        }

        // Add dữ liệu vào model
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listAuth", auth.findDistinctItemName());

        model.addAttribute("list", lisuser);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", lisuser.getTotalPages());
        model.addAttribute("userLogin", currentUser);

        return "ShowAll";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; // Trả về trang accessDenied.html
    }

}
