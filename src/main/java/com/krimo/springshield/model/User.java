package com.krimo.springshield.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;

    private Boolean enabled;

    @Column(name = "created_at")
    private LocalDate createdAt;


    public User(String lastName, String firstName, String middleName, LocalDate birthdate, String email, String password, UserRole role, LocalDate createdAt) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.userRole = role;
        this.createdAt = createdAt;
    }

    public static User create(String lastName, String firstName, String middleName,
                              LocalDate birthdate, String email, String password) {
        return new User(lastName, firstName, middleName, birthdate, email, password, UserRole.USER, LocalDate.now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.name()));
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
