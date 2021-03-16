package ua.kiev.prog;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @NoArgsConstructor
public class CustomUser {
    @Id
    @GeneratedValue
    private Long id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String email;
    private String phone;
    private Integer age;
    private String code;

    public CustomUser(String login, String password, UserRole role, String email, String phone, Integer age) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.code = null;
    }

    public CustomUser(String login, String password, UserRole role, String email, String phone, Integer age, String code) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.code = code;
    }
}
