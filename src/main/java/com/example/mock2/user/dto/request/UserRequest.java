package com.example.mock2.user.dto.request;

import com.example.mock2.user.model.RoleEnum;
import com.example.mock2.user.model.UserSexEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotNull(message = "User is required")
    @NotBlank(message = "User is required")
    @Size(min = 4, max = 20)
    private String username;

    @NotNull(message = "Address is required")
    @NotBlank(message = "Address is required")
    private String address;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate birthdate;

    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Phone is required")
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone is not valid")
    private String phone;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,20}$",
            message = "Password must include uppercase, lowercase and number.")
    private String password;

    private UserSexEnum sex;

    private List<RoleEnum> roles;
}
