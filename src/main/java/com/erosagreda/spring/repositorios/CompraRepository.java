package com.erosagreda.spring.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erosagreda.spring.modelo.Compra;
import com.erosagreda.spring.modelo.Usuario;

public interface CompraRepository extends JpaRepository<Compra, Long> {

	List<Compra> findByPropietario(Usuario propietario);
}
