package vnpt.project.Caller_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
public class car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
