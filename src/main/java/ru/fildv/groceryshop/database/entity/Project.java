package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.fildv.groceryshop.database.entity.user.ProManager;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project")
public class Project implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 64)
    private String name;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_manager_id")
    private ProManager proManager;
}
