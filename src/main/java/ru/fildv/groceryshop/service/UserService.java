package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.database.repository.user.UserRepository;
import ru.fildv.groceryshop.http.dto.address.AddressEditDto;
import ru.fildv.groceryshop.http.dto.address.AddressInfoDto;
import ru.fildv.groceryshop.http.dto.user.UserEditDto;
import ru.fildv.groceryshop.http.dto.user.UserFilterDto;
import ru.fildv.groceryshop.http.dto.user.UserReadDto;
import ru.fildv.groceryshop.http.mapper.address.AddressUpdateMapper;
import ru.fildv.groceryshop.http.mapper.user.UserReadMapper;
import ru.fildv.groceryshop.http.mapper.user.UserUpdateMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final AddressUpdateMapper addressUpdateMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.admin.name}")
    private String INIT_ADMIN;

    @Value("${app.init.admin.login}")
    private String INIT_EMAIL_ADMIN = "admin";

    @Value("${app.init.admin.password}")
    private String INIT_PASSWORD_ADMIN = "admin";

    @Value("${app.page.size}")
    private int sizePage;

    public Page<UserReadDto> findAll(UserFilterDto filter, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return userRepository.findAllByFilter(filter, PageRequest.of(page, sizePage, sort))
                .map(userReadMapper::map);
    }

    public List<UserReadDto> findAllProManagers() {
        return userRepository.findAll().stream()
                .filter(entity -> entity.getRole() == Role.PROJECT_MANAGER)
                .map(userReadMapper::map)
                .collect(toList());
    }

    public List<UserReadDto> findAllManagers() {
        return userRepository.findAll().stream()
                .filter(entity -> entity.getRole() == Role.MANAGER)
                .map(userReadMapper::map)
                .collect(toList());
    }

    public Optional<UserReadDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> update(Integer id, UserEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.getImage());
                    return userUpdateMapper.map(userDto, entity);
                })
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public UserReadDto create(UserEditDto userDto) {
        return Optional.of(userDto)
                .map(dto -> {
                    var image = dto.getImage();
                    if (image != null)
                        uploadImage(dto.getImage());
                    return userUpdateMapper.map(userDto);
                })
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public Optional<AddressInfoDto> getAddress(Integer id) {
        return userRepository.findAddressById(id);
    }

    @Transactional
    public boolean setAddress(Integer id, AddressEditDto address) {
        return userRepository.findById(id)
                .map(entity -> {
                    entity.setAddress(addressUpdateMapper.map(address));
                    return entity;
                })
                .map(userRepository::saveAndFlush).isPresent();
    }

    @Transactional
    public boolean setPassword(String newPassword) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .map(entity -> {
                    entity.setPassword(passwordEncoder.encode(newPassword));
                    return entity;
                })
                .map(userRepository::saveAndFlush).isPresent();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty())
            imageService.upload(image.getOriginalFilename(), image.getInputStream(), ImageService.ImageType.USER);
    }

    public Optional<byte[]> findImage(Integer id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(s -> imageService.get(s, false));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(entity -> new org.springframework.security.core.userdetails.User(
                        entity.getUsername(),
                        entity.getPassword(),
                        Collections.singleton(entity.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed get user: " + username));
    }

    @Transactional
    public void checkAdmin() {
        if (userRepository.findFirstByRole(Role.ADMIN).isEmpty()) {
            var userDto = new UserEditDto(null, INIT_EMAIL_ADMIN, INIT_PASSWORD_ADMIN, INIT_ADMIN, INIT_ADMIN, LocalDate.of(2000, 1, 1), "ADMIN", null);
            var user = create(userDto);
            log.info("Added user with role ADMIN: [ " + user.getUsername() + " ]");
        }
    }
}
