package br.com.api_mediway.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "tb_password_reset_code")
@Data
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    private String code;
    private Instant expiration;
    private boolean used;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
