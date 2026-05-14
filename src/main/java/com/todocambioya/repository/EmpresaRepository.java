package com.todocambioya.repository;

import com.todocambioya.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    Optional<Empresa> findByRuc(String ruc);
    boolean existsByRuc(String ruc);
    List<Empresa> findByActivoTrue();
    List<Empresa> findByAdminUsuarioId(UUID adminUsuarioId);
}
