package com.company.atms.auth.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
	
	 @Id
	    @UuidGenerator
	    @Setter(AccessLevel.NONE)
	    private UUID id;

	    @Column(nullable = false, unique = true, length = 50)
	    private String username;

	    @Column(name = "password_hash", nullable = false)
	    private String passwordHash;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 20)
	    private Role role;

	    @Column(nullable = false)
	    private boolean active = true;
}
