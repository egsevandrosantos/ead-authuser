package com.ead.authuser.dtos;

import com.ead.authuser.dtos.validators.annotations.UsernameConstraint;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDTO {
    public interface UserView {}
    public interface Create extends UserView {}
    public interface Update extends UserView {}
    public interface UpdatePassword extends UserView {}
    public interface UpdateImage extends UserView {}

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonView(Create.class)
    @NotBlank(groups = Create.class)
    @Size(min = 4, max = 50, groups = Create.class)
    @UsernameConstraint(groups = Create.class)
    private String username;

    @JsonView(Create.class)
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = Create.class)
    @Email(groups = Create.class)
    private String email;

    @JsonView({Create.class, UpdatePassword.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(groups = {Create.class, UpdatePassword.class})
    @Size(min = 6, max = 32, groups = {Create.class, UpdatePassword.class})
    private String password;

    @JsonView(UpdatePassword.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(groups = UpdatePassword.class)
    @Size(min = 6, max = 32, groups = UpdatePassword.class)
    private String oldPassword;

    @JsonView({Create.class, Update.class})
    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 150, groups = {Create.class, Update.class})
    private String name;

    @JsonView({Create.class, Update.class})
    @Size(min = 13, max = 13, groups = {Create.class, Update.class})
    @Digits(integer = 13, fraction = 0, groups = {Create.class, Update.class})
    private String phone;

    @JsonView({Create.class, Update.class})
    @Size(min = 11, max = 11, groups = {Create.class, Update.class})
    @Digits(integer = 11, fraction = 0, groups = {Create.class, Update.class})
    private String cpf;

    @JsonView(UpdateImage.class)
    @NotBlank(groups = UpdateImage.class)
    private String imageUrl;

    @JsonView({Create.class, Update.class})
    private UserType type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}
