package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

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
@Table(name = "provider_order")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProviderOrder extends AuditingEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    private Boolean commit;

    private String comment;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerOrder", orphanRemoval = true)
    @ToString.Exclude
    @NotAudited
    private List<ProviderOrderLine> providerOrderLines = new ArrayList<>();
}
