package com.example.mock2.user.dto.response;

import com.example.mock2.user.model.User;
import com.example.mock2.user.model.UserSexEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserResponse {
    Integer id;
    String username;
    String email;
    String name;
    String phone;
    String address;
    private LocalDate birthdate;
    private UserSexEnum sex;

    public static UserResponse of(User user){
        return new UserResponse()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setPhone(user.getPhone())
                .setAddress(user.getAddress())
                .setBirthdate(user.getBirthdate())
                .setSex(user.getSex());
    }
}
