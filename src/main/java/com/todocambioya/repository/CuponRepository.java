package com.todocambioya.repository;

import com.todocambioya.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, UUID> {

    Optional<Cupon> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
