package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Product implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 127)
    private String name;

    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "image", length = 64)
    private String image;

    @Column(precision = 12, scale = 2)
    private BigDecimal storeAmount;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id")
    private Unit baseUnit;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product",
            orphanRemoval = true)
    @ToString.Exclude
    private List<ProductUnit> productUnits = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "product_comment")
    @OrderColumn(name = "comments_order")
    @Column(name = "comment", nullable = false)
    @ToString.Exclude
    private List<String> comments = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Version
    private Long version;
}
