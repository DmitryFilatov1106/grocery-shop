package ru.fildv.groceryshop.database.entity.user;

import jakarta.persistence.*;
import lombok.*;
import ru.fildv.groceryshop.database.entity.Address;
import ru.fildv.groceryshop.database.entity.enums.Role;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"username", "role"})
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 16)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(length = 128)
    private String password;

    @Column(length = 64)
    private String firstname;

    @Column(length = 64)
    private String lastname;

    private LocalDate birthDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role;

    @Embedded
    private Address address;

    @Column(name = "image", length = 64)
    private String image;
}
