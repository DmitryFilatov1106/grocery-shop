package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_order")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CustomerOrder extends AuditingEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    private Boolean commit;

    private String comment;

    @Column(name = "total_sum", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalSum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerOrder", orphanRemoval = true)
    @ToString.Exclude
    @NotAudited
    private List<CustomerOrderLine> customerOrderLines = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
