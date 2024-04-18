package ru.fildv.groceryshop.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Address {
    @Column(length = 32)
    private String region;

    @Column(length = 32)
    private String city;

    @Column(length = 64)
    private String street;

    @Column(length = 8)
    private String house;

    @Column(length = 6)
    private String postalCode;
}
