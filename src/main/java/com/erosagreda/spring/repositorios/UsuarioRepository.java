package com.erosagreda.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erosagreda.spring.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findFirstByEmail(String email);

}
