package vnpt.project.Caller_management.controller;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnpt.project.Caller_management.Services.CrudServices;
import vnpt.project.Caller_management.Services.TaskServices;
import vnpt.project.Caller_management.model.*;
import vnpt.project.Caller_management.repository.*;

import java.util.*;

@Controller
public class TaskController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    CrudServices crudServices;

    @Autowired
    AuthAssignmentRepository authAssignmentRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    TaskServices tServices;
    @Autowired
    AuthItemRepository authItemRepository;
    @Autowired
    private TaskServices taskServices;

    @Autowired
    private AuthItemChildRepository authItemChildRepository;

    @GetMapping("/deleteAuth/{id}/{departmentid}/{userId}")
    public String deleteAuth(@PathVariable int id, @PathVariable int departmentid, @PathVariable int userId, RedirectAttributes redirectAttributes) {
        System.out.println("Delete Auth nè");
        User userLogin = crudServices.getCurrentUser();
        boolean isSysAdmin = userLogin.getListPermission().stream()
                .anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN")
                        && authAssignment.getDepartment() == departmentid);

        if (isSysAdmin == true) {
            System.out.println("xoa nhe");
            boolean status = authAssignmentRepository.deleteAuthAssignmentById(id);
            if (status) {
                redirectAttributes.addAttribute("status", "Delete Success");
            } else {
                redirectAttributes.addAttribute("status", "Delete Fail");

            }

        } else {
            redirectAttributes.addAttribute("status", "You don't have permission");

        }
        User userTask = userRepository.findById(userId);
        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();

    }

    //=================================================================================================
    @GetMapping("/GetTask/{id}/{departmentid}")
    public String getAllTask(@PathVariable int id, @PathVariable int departmentid,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "20") int size,
                             @RequestParam(value = "status", defaultValue = "false") String status,
                             @RequestParam(value = "AuthEdit", required = false) Auth_Assignment AuthEdit,
                             @RequestParam(value = "ListDepartmentChoose", required = false) List<Integer> departmentsList,
                             @RequestParam(value = "AuthName", required = false) String AuthName,
                             Model model) {

        // Tạo Pageable để phân trang với các thông số page và size
        Pageable pageable = PageRequest.of(page, size);

        // Lấy thông tin người dùng đang đăng nhập
        User userLogin = crudServices.getCurrentUser();

        // Kiểm tra quyền hạn của người dùng và department
        boolean isSysAdmin = userLogin.getListPermission().stream()
                .anyMatch(authAssignment ->
                        authAssignment.getName().equalsIgnoreCase("SYSADMIN") &&
                                authAssignment.getDepartment() == departmentid);

        if (isSysAdmin) {
            List<Departments> listDepartment = new ArrayList<>();

            // Chỉ thêm phòng ban nếu quyền là SYSADMIN
            for (Auth_Assignment authAssignment : userLogin.getListPermission()) {
                if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                    Departments department = departmentRepository.findByid(authAssignment.getDepartment());
                    if (department != null) {
                        listDepartment.add(department);
                    }
                }
            }

            Page<Auth_Assignment> listAuth = authAssignmentRepository.findByUserId(id, pageable);

            // Nếu danh sách có dữ liệu, đưa vào model để truyền sang view
            if (!listAuth.isEmpty()) {

                model.addAttribute("listAuth", listAuth);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", listAuth.getTotalPages());
                model.addAttribute("size", size);


            } else {

                model.addAttribute("StatusListAuth", "No tasks found for this user.");
                // Gán giá trị mặc định cho currentPage để tránh lỗi null
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", size);
            }
            model.addAttribute("listDepartment", listDepartment);
            model.addAttribute("listInsertAuth", authAssignmentRepository.findDistinctItemName());
            model.addAttribute("departmentID", departmentid);
            model.addAttribute("UserInTask", userRepository.findById(id));
        } else {
            model.addAttribute("status", "You do not have permission to view tasks for this department.");
        }

        // Kiểm tra và thêm status vào model nếu có giá trị "true"
        if ("true".equalsIgnoreCase(status)) {
            model.addAttribute("status", "Delete success");
        }
        if (AuthEdit != null) {
            System.out.println("day la gettask de lay authedit");
            System.out.println(AuthEdit);
            model.addAttribute("AuthEdit", AuthEdit);
        }
        if (departmentsList != null) {

            model.addAttribute("AuthName", AuthName);
            model.addAttribute("ListDepartmentChoose", departmentsList);
        }

        // Trả về template AuthManager để hiển thị dữ liệu
        return "AuthManager";
    }

    @GetMapping("/getTaskForEdit/{id}")
    public String getTaskForEdit(@PathVariable int id, RedirectAttributes redirectAttributes) {
        User userLogin = crudServices.getCurrentUser();
        Optional<Auth_Assignment> authAssignment1 = authAssignmentRepository.findById(id);
        boolean isSysadmin = userLogin.getListPermission().stream().anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN") && authAssignment1.get().getDepartment() == authAssignment.getDepartment());
        if (isSysadmin == true) {

            System.out.println("Da co quyen sysadmin");
            if (authAssignment1 != null) {
                redirectAttributes.addAttribute("AuthEdit", authAssignment1.get());
                System.out.println(authAssignment1);
            }
        } else {
            redirectAttributes.addAttribute("status", "You don't have permission to Get Information of  this task.");
        }
        User userTask = userRepository.findById(authAssignmentRepository.findById(id).get().getUserId());
        System.out.println(userTask);
        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();
    }

    @PostMapping("/AddTask")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String submitTask(@RequestParam(value = "roles", required = false) List<String> roles,
                             @RequestParam(value = "permissions", required = false) List<String> permissions,
                             @RequestParam("userID") int userID,
                             @RequestParam(value = "department", required = false, defaultValue = "0") int department,

                             RedirectAttributes redirectAttributes) {
        if (department == 0) {
            redirectAttributes.addAttribute("status", "Please choose department");
            return "redirect:/ChooseTask" + "/" + department + "/" + userID;
        }
        User userLogin = crudServices.getCurrentUser();
        System.out.println(department);
        // Kiểm tra quyền SYSADMIN và department
        boolean isSysAdmin = userLogin.getListPermission().stream()
                .anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN")
                        && authAssignment.getDepartment() == department);

        if (isSysAdmin) {


            // Xử lý trường hợp roles hoặc permissions là null
            List<String> finalRoles = roles != null ? roles : new ArrayList<>();
            List<String> finalPermissions = permissions != null ? permissions : new ArrayList<>();

            // Gọi phương thức AddTask từ taskServices
            boolean status = taskServices.AddTask(finalRoles, finalPermissions, department, userID);

            // Kiểm tra trạng thái và thêm vào redirectAttributes
            if (status) {
                redirectAttributes.addAttribute("status", "Add task Successful");
            } else {
                redirectAttributes.addAttribute("status", "Add Task Failed");
            }

            // Tìm User và điều hướng
            User userTaskOptional = userRepository.findById(userID);
            if (userTaskOptional != null) {
                return "redirect:/GetTask/" + userTaskOptional.getId() + "/" + userTaskOptional.getDepartmentId();
            } else {
                redirectAttributes.addAttribute("status", "User not found");
                return "redirect:/error";
            }
        } else {
            // Không có quyền
            redirectAttributes.addAttribute("status", "You don't have permission to add task for this department.");
            return "redirect:/error";
        }
    }


    //=================================================================================================
//    @PostMapping("/AddTask")
//    @PreAuthorize("hasRole('SYSADMIN')")
//    public String AddTask(@RequestParam("idUser") int idUser,
//                          @RequestParam("department") int department,
//                          @RequestParam("assignment") String assignment,
//                          RedirectAttributes redirectAttributes) {
//        User userLogin = crudServices.getCurrentUser();
//        boolean isSysadmin = userLogin.getListPermission().stream().anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN")
//                && authAssignment.getDepartment() == department);
//            if (isSysadmin == true) {
//            boolean status = crudServices.AddTask(idUser, department, assignment);
//            if (status == true) {
//                redirectAttributes.addAttribute("status", "Add Task successfull");
//
//            } else {
//                redirectAttributes.addAttribute("status", "Add Task failed");
//
//            }
//        } else {
//            redirectAttributes.addAttribute("status", "You don't have permission to add tasks for this department.");
//
//        }
//
//        User userTask = userRepository.findById(idUser);
//        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();
//    }
    @GetMapping("/GetDepartment/{UserID}/{AuthName}")
    public String GetDepartment(@PathVariable int UserID, @PathVariable String AuthName, RedirectAttributes redirectAttributes) {
        List<Integer> listDepartmentID = authAssignmentRepository.findDepartmentByUserIdAndName(UserID, AuthName);

        redirectAttributes.addAttribute("AuthName", AuthName);
        redirectAttributes.addAttribute("ListDepartmentChoose", listDepartmentID);
        User userTask = userRepository.findById(UserID);
        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();
    }

@GetMapping("/GetChild/{itemName}")
@ResponseBody
public Map<String,List<String>> GetTask(@PathVariable String itemName) {
    HashMap<String,List<String>> response = new HashMap<>();
    List<String> listautim = authItemChildRepository.findByParent(itemName);
    response.put("list",listautim);
    return  response;

}

    @GetMapping("/ChooseTask/{departmentId}/{userId}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String ChooseTask(@PathVariable int departmentId,
                             @PathVariable int userId, Model model,
                             @RequestParam(value = "status", required = false) String status
    ) {

        User userLogin = crudServices.getCurrentUser();
        boolean isSysadmin = userLogin.getListPermission().stream().anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN")
                && authAssignment.getDepartment() == departmentId);
        if (isSysadmin == true) {
            List<Departments> listDepartment = new ArrayList<>();


            for (Auth_Assignment authAssignment : userLogin.getListPermission()) {
                if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                    Departments department = departmentRepository.findByid(authAssignment.getDepartment());
                    if (department != null) {
                        listDepartment.add(department);
                    }
                }
            }

            List<String> ListRole = authItemRepository.findByType(1);
            model.addAttribute("listDepartment", listDepartment);

            model.addAttribute("ListRole", ListRole);

            model.addAttribute("UserInTask", userRepository.findById(userId));
        }
        if (status != null) {
            model.addAttribute("status", status);
        }
        return "AddTask";
    }

    ;

//    @GetMapping("/GetTaskEdit/{departmentId}/{userId}")
//    public String GetTaskEdit(@PathVariable int departmentId, @PathVariable int userId, @RequestParam(value = "page", defaultValue = "0") int page,
//                              @RequestParam(value = "size", defaultValue = "20") int size,Model model) {
//        User userLogin = crudServices.getCurrentUser();
//        boolean isSysadmin = userLogin.getListPermission().stream().anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN")
//                && authAssignment.getDepartment() == departmentId);
//        Pageable pageable = PageRequest.of(page, size);
//        if (isSysadmin == true) {
//            List<Departments> listDepartment = new ArrayList<>();
//
//            // Chỉ thêm phòng ban nếu quyền là SYSADMIN
//            for (Auth_Assignment authAssignment : userLogin.getListPermission()) {
//                if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
//                    Departments department = departmentRepository.findByid(authAssignment.getDepartment());
//                    if (department != null) {
//                        listDepartment.add(department);
//                    }
//                }
//            }
//List<Auth_Assignment> TaskEdit = authAssignmentRepository.findByUserId(userId);
//            Page<Auth_Assignment> listAuth = authAssignmentRepository.findByUserId(userId, pageable);
//
//            model.addAttribute("TaskEdit", TaskEdit);
//            model.addAttribute("listAuth", listAuth);
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", listAuth.getTotalPages());
//            model.addAttribute("size", size);
//        } else {
//model.addAttribute("status" , "You Dont have Permission ");
//
//        }
//
//
//      return "/AuthManager";
//    }


    @PostMapping("/EditTask")
    public String EditTask(@RequestParam("id") int id, @RequestParam("assignment") String assignment, RedirectAttributes redirectAttributes) {
        User userLogin = crudServices.getCurrentUser();
        Auth_Assignment authAssignment1 = authAssignmentRepository.findById(id).get();
        boolean isSysadmin = userLogin.getListPermission().stream().anyMatch(authAssignment -> authAssignment.getName().equalsIgnoreCase("SYSADMIN") && authAssignment1.getDepartment() == authAssignment.getDepartment());
        if (isSysadmin == true) {
            boolean status = tServices.EditTask(id, assignment);
            if (status == true) {

                redirectAttributes.addAttribute("status", "Edit Task successfull");
            } else {
                redirectAttributes.addAttribute("status", "Edit Task failed");

            }
        } else {

            redirectAttributes.addAttribute("status", "You dont have permission to edit tasks for this department.");
        }
        User userTask = userRepository.findById(authAssignmentRepository.findById(id).get().getUserId());
        System.out.println(userTask);
        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();
    }

    @PostMapping("/UpdateDepartment")
    public String updateDepartment(
            @RequestParam("AuthName") String authName,
            @RequestParam("UserID") int userId,
            @RequestParam(value = "department", required = false) List<Integer> departmentList, // List quyền chỉnh sửa, có thể null
            RedirectAttributes redirectAttributes) {

        // Lấy user hiện tại
        User userLogin = crudServices.getCurrentUser();
        System.out.println("AuthName: " + authName);
        System.out.println("UserID: " + userId);

        List<Integer> listDepartment = new ArrayList<>();

        // Chỉ thêm phòng ban nếu quyền là SYSADMIN
        for (Auth_Assignment authAssignment : userLogin.getListPermission()) {
            if (authAssignment.getName().equalsIgnoreCase("SYSADMIN")) {
                Departments department = departmentRepository.findByid(authAssignment.getDepartment());
                if (department != null) {
                    listDepartment.add(department.getId());
                }
            }
        }

        // Lấy danh sách phòng ban hiện tại theo quyền
        List<Integer> departmentCurrent = authAssignmentRepository.findDepartmentByUserIdAndName(userId, authName);

        // Xử lý logic cập nhật phòng ban
        for (int departmentId : listDepartment) {
            System.out.println("Department ID: " + departmentId);

            if (departmentList != null && departmentList.contains(departmentId) && departmentCurrent.contains(departmentId)) {
                // Nếu đã có phòng ban trong danh sách hiện tại và danh sách được chọn, tiếp tục
                continue;
            } else if (departmentList != null && departmentList.contains(departmentId) && !departmentCurrent.contains(departmentId)) {
                // Nếu có trong danh sách được chọn nhưng chưa có trong hiện tại, cập nhật
                boolean statusUpdate = taskServices.updateDepartment(userId, departmentId, authName, departmentId);
                System.out.println("Update Status: " + statusUpdate);
            } else if (departmentList == null || (!departmentList.contains(departmentId) && departmentCurrent.contains(departmentId))) {
                // Nếu không có trong danh sách được chọn nhưng có trong hiện tại, xóa
                boolean statusDelete = taskServices.DeleteDeparment(userId, departmentId, authName);
                System.out.println("Delete Status: " + statusDelete);
            }
        }

        // Tìm user sau khi thực hiện cập nhật
        User userTask = userRepository.findById(userId);
        System.out.println(userTask);

        // Chuyển hướng đến trang tiếp theo
        return "redirect:/GetTask/" + userTask.getId() + "/" + userTask.getDepartmentId();
    }



}
