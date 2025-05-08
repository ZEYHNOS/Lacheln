package aba3.lucid.domain.packages.entity;

import aba3.lucid.domain.description.Description;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "package_description")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PackageDescriptionEntity extends Description {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;
}

