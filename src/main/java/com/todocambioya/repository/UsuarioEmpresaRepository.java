package com.todocambioya.repository;

import com.todocambioya.entity.UsuarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresa, UUID> {

    List<UsuarioEmpresa> findByEmpresaId(UUID empresaId);
    List<UsuarioEmpresa> findByUsuarioId(UUID usuarioId);
    boolean existsByEmpresaIdAndUsuarioId(UUID empresaId, UUID usuarioId);
}
