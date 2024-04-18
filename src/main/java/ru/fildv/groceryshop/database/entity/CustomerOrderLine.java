package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_order_line")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CustomerOrderLine extends AuditingEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "sum", nullable = false, precision = 12, scale = 2)
    private BigDecimal sum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quality_id")
    private Quality quality;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_unit_id")
    private ProductUnit productUnit;

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
        this.customerOrder.getCustomerOrderLines().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        this.price = product.getPurchasePrice();
        if (Objects.nonNull(this.productUnit))
            if (this.product != this.productUnit.getProduct())
                this.productUnit = null;
    }
}
