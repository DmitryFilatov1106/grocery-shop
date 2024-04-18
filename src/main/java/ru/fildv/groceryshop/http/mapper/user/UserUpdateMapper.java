package ru.fildv.groceryshop.http.mapper.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.database.entity.user.Manager;
import ru.fildv.groceryshop.database.entity.user.Person;
import ru.fildv.groceryshop.database.entity.user.ProManager;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.user.UserEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserUpdateMapper implements Mapper<UserEditDto, User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserEditDto from, User to) {
        copy(from, to);
        return to;
    }

    @Override
    public User map(UserEditDto from) {
        User user;
        var role = Role.valueOf(from.getRole());
        switch (role) {
            case MANAGER -> user = new Manager();
            case PROJECT_MANAGER -> user = new ProManager();
            default -> user = new Person();
        }
        copy(from, user);
        return user;
    }

    private void copy(UserEditDto from, User to) {
        to.setUsername(from.getUsername());
        to.setFirstname(from.getFirstname());
        to.setLastname(from.getLastname());
        to.setBirthDay(from.getBirthDay());
        to.setRole(Role.valueOf(from.getRole()));

        Optional.ofNullable(from.getRawPassord())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(to::setPassword);

        Optional.ofNullable(from.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> to.setImage(image.getOriginalFilename()));
    }
}
