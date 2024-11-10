package com.java.backend.onboarding_requirement.security.userdetails;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<UserAuthority> roles = user.getRoles();

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (UserAuthority role : roles) {
            String authority = role.getRole().getAuthority();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
            authorities.add(simpleGrantedAuthority);
        }

        return authorities;
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
        return true;
    }
}