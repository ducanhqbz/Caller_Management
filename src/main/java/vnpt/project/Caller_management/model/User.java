package vnpt.project.Caller_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor  // Constructor không tham số
@AllArgsConstructor // Constructor có tất cả các tham số
@ToString          // Tự động tạo phương thức toString()
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

  // Một người dùng thuộc về một phòng ban
    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "active")
    private int active;

  @Column(name = "created_at")
  private long createdAt;
  @Column(name = "updated_at")
  private long updateAt;
}
