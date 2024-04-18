package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stock")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Stock implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quality_id")
    private Quality quality;

    @Column(precision = 12, scale = 2)
    private BigDecimal storeAmount;

    @Version
    private Long version;
}
