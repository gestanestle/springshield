package com.krimo.BackendService.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "code")
@EqualsAndHashCode
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name="user_account_id")
    private User user;

    public static ActivationCode of(String code, User user) {
        return new ActivationCode(code, LocalDateTime.now().plusHours(2), user);
    }

    public ActivationCode(String code, LocalDateTime expiresAt, User user) {
        this.code = code;
        this.expiresAt = expiresAt;
        this.user = user;
    }

}
