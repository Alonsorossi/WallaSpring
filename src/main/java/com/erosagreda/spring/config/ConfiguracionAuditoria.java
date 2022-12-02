package com.erosagreda.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * CLASE de Configuracion de Auditoria para que funcionen
 * automaticamente las fechas de las entidades que usan 
 * fechas. Se insertan automaticamente las del sistema.
 * 
 *
 */

@Configuration
@EnableJpaAuditing
public class ConfiguracionAuditoria {
}