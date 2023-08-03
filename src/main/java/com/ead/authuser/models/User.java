package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.enums.converters.UserStatusConverter;
import com.ead.authuser.enums.converters.UserTypeConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 32)
    private String password;
    @Column(nullable = false, length = 150)
    private String name;
    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false)
    private UserStatus status;
    @Convert(converter = UserTypeConverter.class)
    @Column(nullable = false)
    private UserType type;
    @Column(length = 13)
    private String phone;
    @Column(length = 11)
    private String cpf;
    @Column
    private String imageUrl;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;
}
