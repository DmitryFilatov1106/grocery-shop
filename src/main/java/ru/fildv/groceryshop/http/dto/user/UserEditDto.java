package ru.fildv.groceryshop.http.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.groceryshop.http.validation.UserInfo;
import ru.fildv.groceryshop.http.validation.group.CreateAction;

import java.time.LocalDate;

@Value
@UserInfo(groups = CreateAction.class, message = "{firstnameLastname.filled}")
public class UserEditDto {
    Integer id;

    @Email(message = "{email}")
    @Size(min = 1, max = 64, message = "{login.Size}")
    String username;

    String rawPassord;

    @Size(max = 64, message = "{firstname.Size}")
    String firstname;

    @Size(max = 64, message = "{lastname.Size}")
    String lastname;

    @Past(message = "{birthday}")
    LocalDate birthDay;

    @NotEmpty
    String role;

    MultipartFile image;
}
