package com.erosagreda.spring.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.erosagreda.spring.modelo.Usuario;
import com.erosagreda.spring.repositorios.UsuarioRepository;

@Service
public class UsuarioServicio {
	
	@Autowired
	UsuarioRepository repositorio;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	public Usuario registrar(Usuario u) {
		
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		return repositorio.save(u);
	}
	
	public Usuario findById(long id) {
		
		return repositorio.findById(id).orElse(null);
	}
	
	public Usuario buscarPorEmail(String email) {
		
		return repositorio.findFirstByEmail(email);
	}

}
