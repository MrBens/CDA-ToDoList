package com.todolist.todolist.record.userRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(

        @Email(message = "email must correspond to email format")
        String email,

        @NotNull(message = "firstName cannot be null")
        @NotEmpty(message = "firstName cannot be empty")
        String firstName,

        @NotNull(message = "last name cannot be null")
        @NotEmpty(message = "last name cannot be empty")
        String lastName,

        @NotEmpty(message = "phone number cannot be empty")
        @NotNull(message = "phone number cannot be null")
        @Pattern(regexp = "^0[1-9](\\d{2}){4}$", message = "phone number must contains 10 numbers")
        String phoneNumber,

        @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.]).{8,}$" ,
                message = "Password must contains at least 8 character that includes Uppercase, LowerCase, Numbers and a special character")
        String password,

        @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.]).{8,}$" ,
                message = "newPassword must contains at least 8 character that includes Uppercase, LowerCase, Numbers and a special character")
        String newPassword,

        @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.]).{8,}$" ,
                message = "validNewPassword must contains at least 8 character that includes Uppercase, LowerCase, Numbers and a special character")
        String validNewPassword,

        Integer state
) {
}
