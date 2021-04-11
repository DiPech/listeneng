package ru.dipech.listeneng.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
@NoArgsConstructor
public class NewUserDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 255)
    private String password;

    @Builder
    public NewUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
