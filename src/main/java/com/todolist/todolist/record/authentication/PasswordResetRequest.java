package com.todolist.todolist.record.authentication;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PasswordResetRequest(
        @NotEmpty(message = "password cannot be empty")
        @NotNull(message = "password cannot be null")
        @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.]).{8,}$" ,
                message = "Password must contains at least 8 character that includes Uppercase, LowerCase, Numbers and a special character")
        String newPassword,
        @NotEmpty(message = "password cannot be empty")
        @NotNull(message = "password cannot be null")
        @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.]).{8,}$" ,
                message = "Password must contains at least 8 character that includes Uppercase, LowerCase, Numbers and a special character")
        String validNewPassword
) {
}
