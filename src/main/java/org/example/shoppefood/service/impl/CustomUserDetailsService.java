package org.example.shoppefood.service.impl;

import org.example.shoppefood.entity.UserEntity;
import org.example.shoppefood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.getStatus()) {
            throw new UsernameNotFoundException("User is disabled");
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        return new CustomUserDetails(user, authorities);
    }


    public class CustomUserDetails implements UserDetails {
        private Long id; // Thêm trường id
        private String email;
        private String password;
        private boolean status;
        List<GrantedAuthority> authorities;

        // Constructor
        public CustomUserDetails(UserEntity user, List<GrantedAuthority> authorities) {
            this.id = user.getUserId();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.status = user.getStatus();
            this.authorities = authorities;
        }

        // Getter methods
        public Long getUserId() {
            return id;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
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
            return status;
        }
    }
    }
