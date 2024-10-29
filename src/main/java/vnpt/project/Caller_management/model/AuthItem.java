    package vnpt.project.Caller_management.model;

    import jakarta.persistence.*;
    import lombok.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Entity
    @Table(name = "auth_item")
    public class AuthItem {

        @Id
        @Column(name = "name")
        private String name;

        @Column(name = "type")
        private int type;
    }
