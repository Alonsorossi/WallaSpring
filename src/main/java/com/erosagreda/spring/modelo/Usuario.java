package com.erosagreda.spring.modelo;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
/**
 * CLASE Usuario
 * -------------------------------------------------------------------------------------------
 * Clase POJO (Plain Old Java Object - Una Clase simple que no implementa ni
 * extiende de nada) que Representa la ENTIDAD Usuario de la Base de Datos y que
 * conforma un MODELO de Datos con el que poder trabajar en el Lenguaje de
 * Programacion, una vez salvado el desfase Objeto-Relacional existente entre un
 * Lenguaje orientado a Objetos y el Modelo Relacional de la Base de Datos. El
 * Mapeo Objeto-Relacional que nos proporciona Objetos Java que se corresponden
 * con Entidades de la Base de Datos lo realiza Hybernate usando el estandar JPA
 * (Java Persistance API) y es transparante al Programador.
 * Haremos uso de Lombok, una solución que nos permite evitar tener que escribir 
 * código repetitivo en nuestras Clases. Getters y Setters se reducen a una 
 * única línea de código.
 * 
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Usuario {
	/**
	 * ATRIBUTO id
	 * ---------------------------------------------------------------------------------------
	 * Atributo Identificador de la Clase POJO Usuario que representa la Entidad que
	 * se mapea con la Columna Id de la Tabla Usuario de la Base de Datos. Su Valor
	 * es Auto-Generado por JPA. Almacena el identificador del Usuario almacenado en
	 * la Base de Datos. Comienza en el 1 y los valores no son reciclados al
	 * eliminar tuplas de la BD. JPA Trata los Identificadores como tipo de dato
	 * Long por defecto.
	 */
	
	@Id @GeneratedValue
	private long id;
	/**
	 * ATRIBUTO nombre
	 * ---------------------------------------------------------------------------------------
	 * Atributo nombre de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Nombre de la tabla Usuario de la Base de Datos. Almacena
	 * el nombre del Usuario almacenado en la Base de Datos. Usamos la
	 * anotacion @NotEmpty del estándar JSR-303/JSR-380 Bean Validation API, que es
	 * configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor no puede ser Nulo.
	 */
	
	private String nombre;
	
	/**
	 * ATRIBUTO apellidos
	 * ---------------------------------------------------------------------------------------
	 * Atributo apellidos de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Apellidos de la tabla Usuario de la Base de Datos.
	 * Almacena los apellidos del Usuario almacenado en la Base de Datos. Usamos la
	 * anotacion @NotEmpty del estándar JSR-303/JSR-380 Bean Validation API, que es
	 * configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor no puede ser Nulo.
	 */

	private String apellidos;
	
	/**
	 * ATRIBUTO avatar
	 * ---------------------------------------------------------------------------------------
	 * Atributo avatar de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Avatar de la tabla Usuario de la Base de Datos. 
	 * Almacena la URL donde se encuentra el fichero de imagen asociado al avatar 
	 * del Usuario, por eso es de Tipo String.
	 * Haremos uso de un Servicio Web Externo que, en caso de que el Usuario no 
	 * suba explicitamente una imagen de avatar, le asignara uno auto-generado
	 * que genera en base al email que ha introducido.
	 */
	private String avatar;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaAlta;
	
	/**
	 * ATRIBUTO email
	 * ---------------------------------------------------------------------------------------
	 * Atributo email de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna email de la tabla Usuario de la Base de Datos. Este
	 * atributo es ESPECIALMENTE IMPORTANTE ya que se trata de una CLAVE UNICA, es
	 * decir, no se emplea como Clave Primaria pero es un identificador de segundo
	 * nivel, que por tanto no pueden existir dos iguales y que nos sirve para
	 * IDENTIFICAR UNIVOCAMENTE USUARIOS (De hecho, el usuario se AUTENTICA
	 * introduciendo su EMAIL). Este Atributo se mapea en la Capa de Seguridad
	 * (Autenticacion) como identificador de Usuario. Usamos la anotacion @Email del
	 * estándar JSR-303/JSR-380 Bean Validation API, que es configurado por
	 * Hibernate para su implementacion. Esta anotacion VERIFICA que el Email tiene
	 * un formato correcto y nos ahorra a nosotros tener que implementar un metodo
	 * de validacion de cadena de caracteres con una estructura determinada,
	 * haciendo uso, por ejemplo, de Expresiones Regulares para ello.
	 * Ademas, aseguramos que la direccion de correo electronico no se repite
	 * anyadiendo la restriccion Unique en la estructura DDL de la Base de Datos.
	 * Esto convierte este Atributo en una Clave Alternativa que identifica univocamente
	 * a un Usuario y que utilizaremos en la parte de Seguridad como "UserName" o 
	 * nombre de usuario, para identificar al Usuario que se va a Autenticar.
	 * Esto es asi tambien porque es mas facil a la hora de introducir su nombre
	 * para loguearse que recuerde el email con el que se registro que no que tenga
	 * que memorizar el ID que le asigna el Sistema en la Base de Datos.
	 */
	
	private String email;
	private String password;
	
	
	public Usuario () {
		
	}


	public Usuario(String nombre, String apellidos, String avatar, String email, String password) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.avatar = avatar;
		this.email = email;
		this.password = password;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public Date getFechaAlta() {
		return fechaAlta;
	}


	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public int hashCode() {
		return Objects.hash(apellidos, avatar, email, fechaAlta, id, nombre, password);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(avatar, other.avatar)
				&& Objects.equals(email, other.email) && Objects.equals(fechaAlta, other.fechaAlta) && id == other.id
				&& Objects.equals(nombre, other.nombre) && Objects.equals(password, other.password);
	}


	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", avatar=" + avatar
				+ ", fechaAlta=" + fechaAlta + ", email=" + email + ", password=" + password + "]";
	}
	
	

}
