package com.erosagreda.spring;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.erosagreda.spring.modelo.Producto;
import com.erosagreda.spring.modelo.Usuario;
import com.erosagreda.spring.servicios.ProductoServicio;
import com.erosagreda.spring.servicios.UsuarioServicio;
import com.erosagreda.spring.upload.StorageService;


/**
 * CLASE WallaSpringApplication
 * -------------------------------------------------------------------------------------------
 * Contiene todo lo necesario para poder lanzar la Aplicacion.
 * 
 * @SpringBootApplication : Esta anotación se encarga de CONFIGURAR 
 * nuestra aplicación en función de las DEPENDENCIAS que encuentre 
 * en el CLASSPATH del proyecto. 
 * Le dice a Spring Boot que comience a agregar BEANS según sea 
 * la configuración del Classpath, de las Properties, etc...
 * Por ejemplo, si Spring-Web MVC está en el Classpath 
 * (en forma de dependencia starter), esta anotación le dice a 
 * la Aplicación que se va a comportar como una aplicación web 
 * y activa comportamientos clave, como configurar un 
 * DispatcherServlet o saber donde buscar las 
 * propiedades de Almacenamiento, etc.
 * 
 * @EnableConfigurationProperties : Esta anotación le dice a
 * SpringBoot que inyecte como Beans de Configuracion la 
 * configuracion del Servicio de Almacenamiento
 * (Basicamente la ruta donde vamos a almacenar los ficheros)
 * 
 * 
 */
@SpringBootApplication
public class Application {
	 /**
		 * METODO MAIN - PUNTO DE ENTRADA A LA APLICACION WEB
		 * ---------------------------------------------------------------------------------------
		 * Arranca la Aplicacion cargando la configuracion necesaria proporcionada por la propia
		 * Clase y los Argumentos que se le pasan como parametros de inicio (cargar datos de 
		 * prueba proporcionados por el Metodo CommandLineRunner initData() en este nuestro caso)
		 * 
		 */

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * METODO DE CARGA DE DATOS DE INICIO EN LA APLICACION
	 * ---------------------------------------------------------------------------------------
	 * Carga algunos Datos de Inicio haciendo uso de los Servicios Implementados que comunican 
	 * la Capa de Persistencia con la Lógica de Negocio. 
	 * Al inicio no existe ninguna entidad Compra porque estas entidades se inicializan cuando 
	 * ocurre la accion de FINALIZAR LA COMPRA, ya dentro de la Aplicación. (ver CompraController).
	 * 
	 * IMPORTANTE: Este metodo y su ejecucion, una vez queramos desplegar la Aplicacion, 
	 * hay que DEJARLO COMENTADO porque los registros que genera en la Base de Datos 
	 * de MySQL ya quedan persistidos.
	 * 
	 * @param usuarioServicio
	 * @param productoServicio
	 * @return
	 */
	
	@Bean
	public CommandLineRunner initData(UsuarioServicio usuarioServicio, ProductoServicio productoServicio) {
		return args -> {

			Usuario usuario = new Usuario("Luis Miguel", "López Magaña", null, "luismi.lopez@openwebinars.net", "luismi");
			usuario = usuarioServicio.registrar(usuario);

			Usuario usuario2 = new Usuario("Antonio", "García Martín", null, "antonio.garcia@openwebinars.net", "antonio");
			usuario2 = usuarioServicio.registrar(usuario2);

			
			List<Producto> listado = Arrays.asList(new Producto("Bicicleta de montaña", 100.0f,
					"https://www.decathlon.es/media/835/8350582/big_23c25284-2810-415d-8bcc-e6bebdb536fc.jpg", usuario),
					new Producto("Golf GTI Serie 2", 2500.0f,
							"https://www.minicar.es/large/Volkswagen-Golf-GTi-G60-Serie-II-%281990%29-Norev-1%3A18-i22889.jpg",
							usuario),
					new Producto("Raqueta de tenis", 10.5f, "https://imgredirect.milanuncios.com/fg/2311/04/tenis/Raqueta-tenis-de-segunda-mano-en-Madrid-231104755_1.jpg?VersionId=T9dPhTf.3ZWiAFjnB7CvGKsvbdfPLHht", usuario),
					new Producto("Xbox One X", 425.0f, "https://images.vibbo.com/635x476/860/86038583196.jpg", usuario2),
					new Producto("Trípode flexible", 10.0f, "https://images.vibbo.com/635x476/860/86074256163.jpg", usuario2),
					new Producto("Iphone 7 128 GB", 350.0f, "https://store.storeimages.cdn-apple.com/4667/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone7/rosegold/iphone7-rosegold-select-2016?wid=470&hei=556&fmt=jpeg&qlt=95&op_usm=0.5,0.5&.v=1472430205982", usuario2));
			
			listado.forEach(productoServicio::insertar);
			

		};
	}
	
	/**
	 * Este bean se inicia al lanzar la aplicación. Nos permite inicializar el almacenamiento
	 * secundario del proyecto
	 * 
	 * @param storageService Almacenamiento secundario del proyecto
	 * @return
	 */
	@Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
