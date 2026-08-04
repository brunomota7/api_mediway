package br.com.api_mediway.user.entity;

import br.com.api_mediway.auth.dto.request.LoginRequestDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String number;
    private String password;
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public boolean isLoginCorrect(LoginRequestDTO dto, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(dto.password(), this.password);
    }

}
