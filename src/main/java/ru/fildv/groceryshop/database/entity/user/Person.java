package ru.fildv.groceryshop.database.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Entity
@DiscriminatorValue("person")
public class Person extends User {

}
