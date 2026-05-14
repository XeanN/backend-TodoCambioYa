package com.todocambioya.repository;

import com.todocambioya.entity.Referido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReferidoRepository extends JpaRepository<Referido, UUID> {

    List<Referido> findByReferidorId(UUID referidorId);
    boolean existsByReferidorIdAndReferidoId(UUID referidorId, UUID referidoId);
}
