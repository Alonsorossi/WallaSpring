package com.erosagreda.spring.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.erosagreda.spring.modelo.Producto;
import com.erosagreda.spring.modelo.Usuario;
import com.erosagreda.spring.servicios.ProductoServicio;
import com.erosagreda.spring.servicios.UsuarioServicio;
import com.erosagreda.spring.upload.StorageService;

/**
 * CLASE ProductoController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) de todas las funcionalidades relacionadas con la
 * gestion de Productos por parte del Usuario en toda la Zona PRIVADA ("/app") 
 * de la Aplicacion (lo que se puede ver una vez el Usuario esta Registrado y
 * Autenticado).
 * 
 *
 */
@Controller
@RequestMapping("/app")
public class ProductosController {
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * productoServicio se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Producto de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */

	
	@Autowired 
	ProductoServicio productoServicio;
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * usuarioServicio se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Usuario de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */

	
	@Autowired
	UsuarioServicio usuarioServicio;
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * storageService se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona los mecanismos necesarios para poder almacenar
	 * las imagenes tanto de los Avatares de los Usuarios como de los Productos en
	 * un fichero de almacenamiento interno del sistema.
	 */

	
	@Autowired
	StorageService storageService;
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean POJO Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * usuarioPropietario se inyecta como Dependencia en la Clase
	 * ProductoController). Este Objeto Usuario es una Dependencia de la Clase
	 * Producto. representa al Usuario (AUTENTICADO en el momento de realizar 
	 * las operaciones) que tiene en Propiedad el Producto
	 */

	
	private Usuario usuario;
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute inyectan en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("mis_productos") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model. 
	 * Con este Metodo podemos acceder desde la VISTA a la Lista de Productos que son
	 * propiedad del Usuario que se encuentra Autenticado, ya que los inyecta de 
	 * inicio en el Model.
	 * 
	 * @return
	 */
	
	@ModelAttribute("misproductos")
	public List<Producto> misProductos() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		usuario = usuarioServicio.buscarPorEmail(email);
		return productoServicio.productosDeUnPropietario(usuario);
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos". Nos muestra
	 * los productos propiedad del Usuario que se encuentra autenticado. Recibe por
	 * Parametros una Query que no es obligatoria y que si tiene algun valor sera
	 * porque estamos buscando algun producto concreto DE NUESTRA LISTA, y un objeto
	 * Model (Model es un Map que nos permite pasar Objetos del Controlador a la
	 * Vista). Si la Query trae consigo algun valor, se lo inyectamos al Model con
	 * la Clave "mis_productos" y Valor el devuelto por la busqueda realizada por el
	 * Servicio hacia la BD usando el Repositorio. Si lo encuentra, lo muestra. De
	 * no haber ningun producto que se corresponda con el buscado o no existir una
	 * query (no hemos querido buscar nada), la Clave "mis_productos" del Model
	 * seguira mostrando todos los productos. El metodo devuelve un String que es la
	 * ruta de la plantilla html (sin indicar la extension de la misma) que muestra
	 * la Lista de Productos propiedad del Usuario.
	 * 
	 * @param model
	 * @param query
	 * @return
	 */

	@GetMapping("/misproductos")
	public String list(Model model, @RequestParam(name = "q", required = false) String query) {
		if (query != null)
			model.addAttribute("misproductos", productoServicio.buscarMisProductos(query, usuario));

		return "app/producto/lista";
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos/nuevo" para
	 * INSERTAR un Nuevo Producto en el Sistema. Se inyecta en el Model un Command
	 * Object, que es el Bean que recogera la informacion que se introduzca en el
	 * Formulario. El Command Object tendra tantos atributos como campos tenga el
	 * formulario, siendo generalmente una Clase POJO (simple, solamente atributos,
	 * contructor, getters, setters...) que, en este caso, servira posteriormente
	 * para Mapear una Entidad de la Base de Datos (Producto). Es por ello que el
	 * Command Object sera un Objeto Vacio de la Clase Producto del Modelo. El
	 * Command Object se Mapea en la Vista en la plantilla correspondiente (En este
	 * caso en la plantilla producto_form.html) a través de la Clave del Model (recordemos
	 * que es un Map Clave-Valor) "producto" y se accedera en la propia Vista a los
	 * Atributos del mismo usando Thymeleaf (ver comentarios en producto_form.html).
	 * Finalmente el metodo devuelve la URL "app/producto/form" a mostrar en el
	 * Navegador donde cargara la plantilla asociada a dicha URL (producto_form.html).
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/misproductos/{id}/eliminar")
	public String eliminar(@PathVariable Long id) {
		Producto p = productoServicio.findById(id);
		if (p.getCompra() == null)
			productoServicio.borrar(p);
		return "redirect:/app/misproductos";
	}
	
	@GetMapping("/producto/nuevo")
	public String nuevoProductoForm(Model model) {
		model.addAttribute("producto", new Producto());
		return "app/producto/form";
	}
	
	@PostMapping("/producto/nuevo/submit")
	public String nuevoProductoSubmit(@ModelAttribute Producto producto, @RequestParam("file") MultipartFile file) {		
		if (!file.isEmpty()) {
			String imagen = storageService.store(file);
			producto.setImagen(MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "serveFile", imagen).build().toUriString());
		}
		producto.setPropietario(usuario);
		productoServicio.insertar(producto);
		return "redirect:/app/misproductos";
	}

}
