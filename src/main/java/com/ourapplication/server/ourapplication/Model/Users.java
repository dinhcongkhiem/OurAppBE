package com.ourapplication.server.ourapplication.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @NaturalId(mutable = true)
    private String email;


    @Column(nullable = false)
    private String password;
    private String phoneNumber;
    private String nickname;
    private String activeKey;
    private boolean isEnabled = false;
    private String pushNotifyToken;
    private Long IdOfPartner;
    private Date createDate = new Date();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}
