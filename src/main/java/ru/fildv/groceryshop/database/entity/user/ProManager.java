package ru.fildv.groceryshop.database.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import ru.fildv.groceryshop.database.entity.Project;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder
@Entity
@DiscriminatorValue("pro_manager")
public class ProManager extends User {
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proManager")
    private List<Project> projects;
}
