package com.todocambioya.repository;

import com.todocambioya.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDniRuc(String dniRuc);
    List<Usuario> findByRegionId(Integer regionId);
    List<Usuario> findByActivoTrue();
}
