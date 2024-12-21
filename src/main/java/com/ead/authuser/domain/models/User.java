package com.ead.authuser.domain.models;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ead.authuser.domain.enumerations.UserStatus;
import com.ead.authuser.domain.enumerations.UserType;
import com.ead.authuser.domain.models.listeners.UserListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@EntityListeners(value = { UserListener.class, AuditingEntityListener.class })
public class User implements BaseEntity {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(nullable = false, unique = true, length = 50)
	private String username;
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	@Column(nullable = false, length = 255)
	private String password;
	@Column(nullable = false, length = 150)
	private String name;
	@Column(nullable = false, length = 1)
	private UserStatus status;
	@Column(nullable = false, length = 1)
	private UserType type;
	@Column(length = 20)
	private String phone;
	@Column(length = 11)
	private String cpf;
	@Column
	private String imageUrl;
	@Column(nullable = false, updatable = false)
	@CreatedDate
	private Instant createdAt;
	@Column(nullable = false)
	@LastModifiedDate
	private Instant updatedAt;
}
