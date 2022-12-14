package com.erosagreda.spring.modelo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/**
 * CLASE Producto
 * -------------------------------------------------------------------------------------------
 * Clase POJO (Plain Old Java Object - Una Clase simple que no implementa ni extiende de nada) 
 * que Representa la ENTIDAD Producto de la Base de Datos y que conforma un MODELO de Datos 
 * con el que poder trabajar en el Lenguaje de Programacion, una vez salvado el desfase 
 * Objeto-Relacional existente entre un Lenguaje orientado a Objetos y el Modelo Relacional 
 * de la Base de Datos.
 * El Mapeo Objeto-Relacional que nos proporciona Objetos Java que se corresponden con Entidades
 * de la Base de Datos lo realiza Hybernate usando el estandar JPA (Java Persistance API) y es
 * transparante al Programador.
 * Haremos uso de Lombok, una solución que nos permite evitar tener que escribir 
 * código repetitivo en nuestras Clases. Getters y Setters se reducen a una 
 * única línea de código.
 * Esta Clase Producto tiene DEPENDENCIAS asociadas, como son las instancias de los Objetos
 * Usuario y Compra. Esto es asi porque Cada PRODUCTO tiene 1 PROPIETARIO (Que lo VENDE o lo COMPRA)
 * y porque a 1 PRODUCTO, en el Instante de su Compra, se le asocia dicho Objeto Compra.
 * Sera el CONTENEDOR DE INVERSION DE CONTROL el encargado de buscar en el CONTEXTO los BEANS
 * requeridos por esta Clase para su funcionamiento, empleando asi el PATRON DE DISEÑO
 * DE INVERSION DE CONTROL, USANDO INYECCION DE DEPENDENCIAS (Fundamento Esencial de Spring).
 * 
 *
 */

@Entity
public class Producto {
	/**
	 * ATRIBUTO id
	 * ---------------------------------------------------------------------------------------
	 * Atributo Identificador de la Clase POJO Producto que representa la Entidad que
	 * se mapea con la Columna Id de la Tabla Producto de la Base de Datos. Su Valor
	 * es Auto-Generado por JPA. Almacena el identificador de un Producto almacenado 
	 * en la Base de Datos. Comienza en el 1 y los valores no son reciclados al
	 * eliminar tuplas de la BD. JPA Trata los Identificadores como tipo de dato
	 * Long por defecto.
	 */
	
	@Id @GeneratedValue
	private long id;
	/**
	 * ATRIBUTO nombre
	 * ---------------------------------------------------------------------------------------
	 * Atributo nombre de la Clase POJO Producto que representa la Entidad que se
	 * mapea con la Columna Nombre de la Tabla Producto de la Base de Datos. 
	 * Almacena el nombre del Producto almacenado en la Base de Datos. 
	 * Usamos la anotacion @NotEmpty del estándar JSR-303/JSR-380 Bean Validation API, 
	 * que es configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor no puede ser Nulo.
	 */
	
	private String nombre;
	/**
	 * ATRIBUTO precio
	 * ---------------------------------------------------------------------------------------
	 * Atributo precio de la Clase POJO Producto que representa la Entidad que se
	 * mapea con la Columna Precio de la Tabla Producto de la Base de Datos. 
	 * Almacena el precio de Venta del Producto almacenado en la Base de Datos. 
	 * Usamos la anotacion @Min del estándar JSR-303/JSR-380 Bean Validation API, 
	 * que es configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor del precio no puede ser menor que el indicado en el parametro value.
	 * De no cumplirse la restriccion, se genera un mensaje de error, que mapearemos a un
	 * fichero de properties especifico para los mensajes de error.
	 */
	
	private float precio;
	/**
	 * ATRIBUTO imagen
	 * ---------------------------------------------------------------------------------------
	 * Atributo imagen de la Clase POJO Producto que representa la Entidad que se
	 * mapea con la Columna Imagen de la tabla Producto de la Base de Datos. 
	 * Almacena la URL donde se encuentra el fichero de imagen asociado al Producto,
	 * por eso es de Tipo String.
	 */
	
	
	private String imagen;
	/**
	 * ATRIBUTO propietario
	 * ---------------------------------------------------------------------------------------
	 * Atributo usuarioPropietario de la Clase POJO Producto que REPRESENTA 
	 * LA RELACION que se establece entre la Entidad Usuario y la Entidad Producto.
	 * JPA nos permite Mapear los valores sobre asociaciones entre Entidades, para
	 * ello primero debemos conocer la Multiplicidad de la Relación, que es:
	 * (1 USUARIO puede tener M PRODUCTOS <-> 1 PRODUCTO SOLO PERTENECE A 1 USUARIO)
	 * De este estudio extraemos la conclusion de que la Relacion es MUCHOS A UNO
	 * y para ello hacemos uso de la Anotacion de JPA @ManyToOne
	 */
	
	@ManyToOne
	private Usuario propietario;
	/**
	 * ATRIBUTO compra
	 * ---------------------------------------------------------------------------------------
	 * Atributo compra de la Clase POJO Producto que REPRESENTA LA RELACION que se
	 * establece entre la Entidad Compra y la Entidad Producto.
	 * JPA nos permite Mapear los valores sobre asociaciones entre Entidades, para
	 * ello primero debemos conocer la Multiplicidad de la Relación, que es:
	 * (1 COMPRA puede tener M PRODCUTOS <-> 1 PRODUCTO SOLO PUEDE ESTAR EN 1 COMPRA)
	 * De este estudio extraemos la conclusion de que la Relacion es MUCHOS A UNO
	 * y para ello hacemos uso de la Anotacion de JPA @ManyToOne
	 */
	
	@ManyToOne
	private Compra compra;
	
	/**
	 * CONSTRUCTOR PARAMETRIZADO
	 * ---------------------------------------------------------------------------------------
	 * Si bien Lombok genera por nosotros el Constructor por defecto (sin parametros)
	 * usando la anotacion @NoArgsConstructor, a nosotros nos hara falta usar otro 
	 * Constructor al que NO se le pase por Parametros el valor del ID (JPA lo Auto-Genera) 
	 * ni TAMPOCO el Objeto COMPRA, que sera Null mientras no se produzca Compra alguna
	 * del producto en cuestion.
	 * Observese que los Objetos que tiene como DEPENDENCIAS, no son instanciados en 
	 * ningun momento en esta Clase (Ello implicaria un Alto Acoplamiento, y la violacion
	 * del principio de INVERSION DE CONTROL e INYECCION DE DEPENDENCIAS)
	 * 
	 * @param nombre
	 * @param precio
	 * @param imagen
	 * @param usuarioVendedor
	 */
	
	public Producto() {
		
	}

	public Producto(String nombre, float precio, String imagen, Usuario propietario) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.propietario = propietario;
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

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Usuario getPropietario() {
		return propietario;
	}

	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	@Override
	public int hashCode() {
		return Objects.hash(compra, id, imagen, nombre, precio, propietario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return Objects.equals(compra, other.compra) && id == other.id && Objects.equals(imagen, other.imagen)
				&& Objects.equals(nombre, other.nombre)
				&& Float.floatToIntBits(precio) == Float.floatToIntBits(other.precio)
				&& Objects.equals(propietario, other.propietario);
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", imagen=" + imagen
				+ ", propietario=" + propietario + ", compra=" + compra + "]";
	}
	
	

}
