package com.todocambioya.repository;

import com.todocambioya.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Integer> {

    List<Banco> findByActivoTrue();
    List<Banco> findByTipo(String tipo); // 'banco' | 'caja_mun'
}
