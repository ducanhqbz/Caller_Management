    package vnpt.project.Caller_management.model;

    import jakarta.persistence.*;
    import lombok.*;


    @Getter
    @Setter
    @NoArgsConstructor  // Constructor không tham số
    @AllArgsConstructor // Constructor có tất cả các tham số
    @ToString          // Tự động tạo phương thức toString()
    @Entity
    @Table(name = "departments_v2")
    public class Departments {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "department_name")
        private String department_name;

    }
