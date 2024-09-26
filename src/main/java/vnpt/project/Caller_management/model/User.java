package vnpt.project.Caller_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

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

  @Transient
  private List<Auth_Assignment> listPermission = new ArrayList<>();

  // Thêm một phương thức để thêm quyền vào danh sách
  public void addAuthAssignment(Auth_Assignment auth_assignment){
    listPermission.add(auth_assignment);
  }
}
