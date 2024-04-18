package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.fildv.groceryshop.database.entity.enums.Status;
import ru.fildv.groceryshop.database.entity.user.Manager;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"name", "contract", "status"})
@Entity
@Table(name = "customer")
public class Customer implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 127)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private Status status;

    @Column(name = "customer_since", nullable = false)
    private LocalDate customerSince;

    @Column(length = 127)
    private String contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Embedded
    private Address address;
}
