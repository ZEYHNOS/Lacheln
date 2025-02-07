package aba3.lucid.packag.repository;

import aba3.lucid.packag.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {

}
