package aba3.lucid.hashtag.repository;

import aba3.lucid.hashtag.model.HashTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<String, HashTagEntity> {

}
