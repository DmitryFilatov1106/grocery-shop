package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "provider_order_line")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProviderOrderLine extends AuditingEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer amount;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_order_id")
    private ProviderOrder providerOrder;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quality_id")
    private Quality quality;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_unit_id")
    private ProductUnit productUnit;

    public void setProviderOrder(ProviderOrder providerOrder) {
        this.providerOrder = providerOrder;
        this.providerOrder.getProviderOrderLines().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        if (Objects.nonNull(this.product) && Objects.nonNull(this.productUnit))
            if (this.product != this.productUnit.getProduct())
                this.productUnit = null;
    }
}
