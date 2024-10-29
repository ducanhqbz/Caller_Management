package vnpt.project.Caller_management.controller;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnpt.project.Caller_management.Services.CrudServices;

import vnpt.project.Caller_management.model.Auth_Assignment;
import vnpt.project.Caller_management.model.User;
import vnpt.project.Caller_management.repository.AuthAssignmentRepository;
import vnpt.project.Caller_management.repository.UserRepository;

import java.util.List;


@Controller
public class CrudController {
    //--------------------------------------------------------------------
    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    CrudServices crudServices;

    @Autowired
    AuthAssignmentRepository authAssignmentRepository;

    //--------------------------------------------------------------------
    @GetMapping("/deleteEmail/{email}")
    public String delete(@PathVariable String email, RedirectAttributes redirectAttributes) {
        // Lấy thông tin người dùng hiện tại (current user)
        User userLogin = crudServices.getCurrentUser();

        // Tìm kiếm user cần xóa theo email
        User userToDelete = userRepository.findByEmail(email);

        // Kiểm tra xem user cần xóa có tồn tại không
        if (userToDelete == null) {
            redirectAttributes.addAttribute("status", "Don't have Account with email: " + email);
            return "redirect:/ShowAll";
        }

        // Kiểm tra xem user hiện tại (current user) có quyền SYSADMIN và thuộc cùng phòng ban với user cần xóa không
        boolean hasSysAdminPermissionInSameDepartment = false;

        for (Auth_Assignment authAssignment : userLogin.getListPermission()) {


            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")
                    && authAssignment.getDepartment() == userToDelete.getDepartmentId()) {
                hasSysAdminPermissionInSameDepartment = true;
                // Nếu tìm thấy quyền SYSADMIN và cùng phòng ban, dừng kiểm tra
            } else {
                System.out.println("bo may deo thich run day");

            }
        }
        System.out.println(hasSysAdminPermissionInSameDepartment);
        // Nếu không có quyền SYSADMIN trong cùng phòng ban, không cho phép xóa
        if (!hasSysAdminPermissionInSameDepartment) {
            redirectAttributes.addAttribute("status", "You don't have permission to delete this user in the same department.");
            return "redirect:/ShowAll";
        }

        // Thực hiện xóa user nếu có quyền và cùng phòng ban
        boolean status = userRepository.deleteUserByEmail(email);
        if (status) {
            boolean authStatus = authAssignmentRepository.deleteByUserId(userToDelete.getId());
            if (authStatus) {
                redirectAttributes.addAttribute("status", "Delete success");
            } else {
                redirectAttributes.addAttribute("status", "Cannot delete Auth assignment");
            }
        } else {
            redirectAttributes.addAttribute("status", "Delete Failed");
        }

        return "redirect:/ShowAll";
    }

    //--------------------------------------------------------------------
    @GetMapping("/GetObject/{id}")

    public String getObject(@PathVariable int id, RedirectAttributes redirectAttributes) {
        User userLogin = crudServices.getCurrentUser();
        for (Auth_Assignment authAssignment : userLogin.getListPermission()) {

            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                User user = userRepository.findById(id);
                if (user != null) {
                    System.out.println("This is Get object");
                    System.out.println(user);
                    redirectAttributes.addAttribute("userForEdit", user);
                } else {
                    redirectAttributes.addAttribute("status", "Dont have Account with id: " + id);
                }
            }
        }

        return "redirect:/ShowAll";
    }



    //--------------------------------------------------------------------





    //--------------------------------------------------------------------
    @PostMapping("/Update")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String Update(@RequestParam("username") String username,
                         @RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("email") String email,
                         @RequestParam("department") int departmentId,
                         RedirectAttributes redirectAttributes) {
        User userLogin = crudServices.getCurrentUser();
        for (Auth_Assignment authAssignment : userLogin.getListPermission()) {

            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN") &&
                    authAssignment.getDepartment() == departmentId
            ) {
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

            }
        }
        // Tìm user theo email

        return "redirect:/ShowAll";
    }

    //--------------------------------------------------------------------
    @PostMapping("/Insert")
    public String Insert(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("username") String username,
                         @RequestParam("first") String firstName,
                         @RequestParam("last") String lastName,
                         @RequestParam("department") int departmentId,
                         @RequestParam("assignment") String assignment,
                         RedirectAttributes redirectAttributes) {

        User currentUser = crudServices.getCurrentUser();

        // Kiểm tra nếu người dùng không đăng nhập hoặc không hợp lệ
        if (currentUser == null) {
            redirectAttributes.addAttribute("status", "You are not logged in.");
            return "redirect:/login";  // Điều hướng đến trang đăng nhập
        }

        // Kiểm tra xem người dùng có quyền Sysadmin hay không
        List<Auth_Assignment> listPermission = authAssignmentRepository.findByUserIdAndDepartment(currentUser.getId(), departmentId);
        boolean hasSysadminRole = listPermission.stream().anyMatch(x -> x.getName().equalsIgnoreCase("SYSADMIN"));
        System.out.println(hasSysadminRole);
        if (hasSysadminRole) {
            boolean status = crudServices.Insert(email, password, username, firstName, lastName, departmentId, assignment);
            if (status) {
                redirectAttributes.addAttribute("status", "Insert success");
            } else {
                redirectAttributes.addAttribute("status", "Insert failed");
            }
            return "redirect:/ShowAll";
        } else {
            redirectAttributes.addAttribute("status", "You do not have permission to access this page.");
            return "redirect:/error";  // Điều hướng đến trang lỗi phân quyền
        }
    }

}

