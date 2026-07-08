package com.coep.FeePortal.config;

import com.coep.FeePortal.entity.Student;
import com.coep.FeePortal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tells Spring Security how to load a user from the database.
 * Email is used as the "username".
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPassword()) // already BCrypt-hashed in DB
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + student.getRole())))
                .build();
    }
}
