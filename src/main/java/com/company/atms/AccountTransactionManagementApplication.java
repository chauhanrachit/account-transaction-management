package com.company.atms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountTransactionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountTransactionManagementApplication.class, args);
	}
	
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//	    http
//	        .authorizeHttpRequests(auth -> auth
//	            .requestMatchers("/h2-console/**").permitAll()
//	            .anyRequest().authenticated()
//	        )
//	        .csrf(csrf -> csrf
//	            .ignoringRequestMatchers("/h2-console/**")
//	        )
//	        .headers(headers -> headers
//	            .frameOptions(frame -> frame.disable())
//	        );
//
//	    return http.build();
//	}

}
