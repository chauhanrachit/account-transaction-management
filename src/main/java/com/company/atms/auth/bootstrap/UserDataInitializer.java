package com.company.atms.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.company.atms.auth.entity.Role;
import com.company.atms.auth.entity.User;
import com.company.atms.auth.repository.UserRepository;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.existsByUsername("admin")) {
                return;
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        };
    }
}
