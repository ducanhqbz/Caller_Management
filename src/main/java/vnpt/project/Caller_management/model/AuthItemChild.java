package vnpt.project.Caller_management.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
@Table(name = "auth_item_child")

public class AuthItemChild {
@Id
@Column(name = "parent")
    private String parent;

@Column(name = "child")
    private String child;

}
