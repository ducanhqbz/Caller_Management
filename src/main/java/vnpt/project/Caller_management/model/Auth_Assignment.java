package vnpt.project.Caller_management.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "auth_assignment_v2")
public class Auth_Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id") // Tên cột trong cơ sở dữ liệu vẫn là user_id
    private int userId; // Tên biến trong class sử dụng camelCase

    @Column(name = "item_name")
    private String name;

    @Column(name = "department")
    private int department;
}