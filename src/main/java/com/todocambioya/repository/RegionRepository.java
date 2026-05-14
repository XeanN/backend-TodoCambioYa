package com.todocambioya.repository;

import com.todocambioya.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
