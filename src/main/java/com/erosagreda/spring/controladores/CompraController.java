package com.erosagreda.spring.controladores;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.erosagreda.spring.modelo.Compra;
import com.erosagreda.spring.modelo.Producto;
import com.erosagreda.spring.modelo.Usuario;
import com.erosagreda.spring.servicios.CompraServicio;
import com.erosagreda.spring.servicios.ProductoServicio;
import com.erosagreda.spring.servicios.UsuarioServicio;

/**
 * CLASE CompraController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) en toda la Zona PRIVADA ("/app") de la aplicacion 
 * (lo que se ve una vez REGISTRADO y AUTENTICADO)
 * 
 *
 */
@Controller
@RequestMapping("/app")
public class CompraController {
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * compraServicio se inyecta como Dependencia en la Clase CompraController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Compra de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */
	
	@Autowired
	CompraServicio compraServicio;
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
	 * Bean de Sesion Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * session (Sesion del Usuario a la que va asociado el Carrito)
	 * se inyecta como Dependencia en la Clase CompraController)
	 */
	
	@Autowired
	HttpSession session;
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * documentGeneratorService (Servicio que genera un informe en PDF de la Compra)
	 * se inyecta como Dependencia en la Clase CompraController)
	 */
	
	private Usuario usuario;
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("carrito") sera leida desde la VISTA y en ella se podra extraer el VALOR
	 * devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * El contenido del Carrito lo extraemos de la Sesion y es un Listado de ID de Productos.	
	 *
	 * @return
	 */
	
	@ModelAttribute("carrito")
	public List<Producto> productosCarrito() {
		List<Long> contenido = (List<Long>) session.getAttribute("carrito");
		return (contenido == null) ? null : productoServicio.variosPorId(contenido);
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("items_carrito") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * El numero de items (productos) del Carrito lo extraemos invocando al metodo 
	 * productosCarrito() y, si no esta vacio, obtenemos el tamaño del List (devuelve
	 * el numero de elementos dentro del listado) y lo parseamos el tipo de dato a String
	 * para despues, poderlo escribir en el navbar de la vista.
	 *
	 * @return Numero de items en el Carrito
	 */
	
	@ModelAttribute("total_carrito")
	public Double totalCarrito() {
		
		List<Producto> productosCarrito = productosCarrito();
		if(productosCarrito != null)
			return productosCarrito.stream()
					.mapToDouble(p -> p.getPrecio())
					.sum();
		return 0.0;
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("mis_compras") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * Extraemos el Username (email) del Usuario que esta Autenticado.
	 * Buscamos en la Base de Datos al Propietario y consultamos todas
	 * las compras asociadas a ese Email que se ha registrado en la Sesion.
	 * 
	 * @return 
	 */
	
	@ModelAttribute("mis_compras")
	public List<Compra> misCompras() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		usuario = usuarioServicio.buscarPorEmail(email);
		return compraServicio.porPropietario(usuario);
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que mapea una Peticion GET en la ruta /carrito para mostrar la 
	 * plantilla que muestra el contenido del Carrito.
	 * Como ya tenemos los Metodos (productosCarrito() y totalCarrito()) anotados
	 * con @ModelAttribute, que se encargan de obtener la informacion del Carrito
	 * e inyectarla en el Model de "carrito" y "total_carrito" respectivamente...
	 * tan solo nos queda dirigir a la plantilla carrito.html.
	 * 
	 * @param model
	 * @return
	 */
	
	@GetMapping("/carrito")
	public String verCarrito(Model model) {
		return "app/compra/carrito";
	}
	/**
	 * METODO PARA ANYADIR PRODUCTOS AL CARRITO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/add/{id}" para
	 * Insertar un Producto en el Carrito. Recibe una Variable en la URL que es 
	 * el Identificador del Producto que se quiere anyadir al Carrito, se pasa
	 * por Parametros con la anotacion @PathVariable y el tipo de dato de la misma 
	 * y que se usara para realizar una busqueda realizada por el Servicio hacia 
	 * la BD usando el Repositorio de Productos.
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, generamos una Lista para anyadir Productos en 
	 * ella. Si NO esta vacio, comprobamos que el Producto que queremos anyadir
	 * no esta previamente anyadido (esto es asi porque solo podemos Comprar UN
	 * Producto con UN id). Si no esta previamente, lo anyadimos al Carrito.
	 * Inyectamos en la Sesion del Usuario Autenticado el Producto agregado al 
	 * Carrito e indicamos el Tamanyo de la Lista de Productos que hay en el.
	 * Finalmente, redireccionamos a la Pagina del Carrito.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	
	@GetMapping("/carrito/add/{id}")
	public String addCarrito(Model model, @PathVariable Long id) {
		List<Long> contenido = (List<Long>) session.getAttribute("carrito");
		if(contenido == null)
			contenido = new ArrayList<>();
		if(!contenido.contains(id))
			contenido.add(id);
		session.setAttribute("carrito", contenido);
		return "redirect:/app/carrito";
	}
	/**
	 * METODO PARA ELIMINAR PRODUCTOS DEL CARRITO DE LA COMPRA
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/eliminar/{id}" para
	 * Eliminar un Producto del Carrito. Recibe una Variable en la URL que es 
	 * el Identificador del Producto que se quiere anyadir al Carrito, se pasa
	 * por Parametros con la anotacion @PathVariable y el tipo de dato de la misma 
	 * y que se usara para realizar una busqueda realizada por el Servicio hacia 
	 * la BD usando el Repositorio de Productos.
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, redireccionamos a la Pagina Principal.
	 * Si NO esta vacio, Borramos de la Lista el Producto con el ID indicado.
	 * Si despues de Borrar el Producto, el Carrito queda Vacio, eliminamos
	 * el Carrito de la Sesion.
	 * Si despues de Borrar el Producto, quedan Productos en el Carrito,
	 * ACTUALIZAMOS el Contenido del Carrito y el Numero de Productos que
	 * contiene, inyectandolo en la Sesion.
	 * Finalmente, redireccionamos a la Pagina del Carrito.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	
	
	@GetMapping("/carrito/eliminar/{id}")
	public String borrarDeCarrito(Model model, @PathVariable Long id) {
		List<Long> contenido = (List<Long>) session.getAttribute("carrito");
		if(contenido == null)
			return "redirect:/public";
		contenido.remove(id);
		if(contenido.isEmpty())
			session.removeAttribute("carrito");
		else
		session.setAttribute("carrito", contenido);
		return "redirect:/app/carrito";
	}
	/**
	 * METODO PARA FINALIZAR LA COMPRA
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/finalizar" para
	 * FINALIZAR EL PROCESO DE COMPRA asociado al contenido del Carrito. 
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, redireccionamos a la Pagina Principal.
	 * Si NO esta vacio, Buscamos los Productos del Listado de ID que contiene
	 * el Carrito en la Base de Datos, usando el Metodo productosCarrito(), que
	 * nos devuelve un LISTADO DE OBJETOS Producto.
	 * Insertamos los Productos del Listado de Objetos en un Objeto de Tipo COMPRA,
	 * que primero INICIALIZAMOS asignandole el Usuario que esta Autenticado y
	 * realizando la operacion en ese momento.
	 * Posteriormente vamos ASIGNANDO A CADA PRODUCTO DEL CARRITO LA COMPRA A
	 * LA QUE PERTENECE (Cada Producto tiene un Atributo Objeto Compra a la que 
	 * Pertenece y que hasta este era NULL PORQUE NO EXISTIA COMPRA A LA QUE
	 * VINCULAR EL PRODUCTO) 
	 * El Carrito queda "Vacio" (esto es, cada Producto tiene una Compra asignada
	 * por tanto eliminamos el Carrito de la Sesion).
	 * Finalmente, redireccionamos a la Pagina de la Factura, en la cual se indica
	 * el Identificador de la Compra realizada.
	 * 
	 * @return
	 */
	
	@GetMapping("/carrito/finalizar")
	public String checkout() {
		List<Long> contenido = (List<Long>) session.getAttribute("carrito");
		if (contenido == null)
			return "redirect:/public";
		
		List<Producto> productos = productosCarrito();
		
		Compra c = compraServicio.insertar(new Compra(), usuario);
		
		productos.forEach(p -> compraServicio.addProductoCompra(p, c));
		session.removeAttribute("carrito");
		
		return "redirect:/app/compra/factura/"+c.getId();
		
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_compras". Nos muestra
	 * los productos propiedad del Usuario que se encuentra autenticado (Una vez
	 * han sido COMPRADOS).
	 * El metodo redirige a la pagina "app/compra/compra_list" donde se muestra la 
	 * lista de productos propiedad del Usuario (Una vez COMPRADOS).
	 * 
	 * @param model
	 * @return
	 */
	
	
	@GetMapping("/miscompras")
	public String verMisCompras(Model model) {
		return "/app/compra/listado";
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_compras/factura/{id}". 
	 * El PathVariable que se indica es el Identificador de la Compra (Se genera
	 * al Finalizar una Compra).
	 * Primero Recuperamos la Compra de la Base de Datos buscando por su ID.
	 * Despues Recuperamos la Lista de Productos asociados a la Compra.
	 * Recorremos la Lista para calcular el Precio Total de la Compra.
	 * Ahora vamos Inyectando cada uno de los Atributos que queremos mostrar en 
	 * el Model ("productos", "compra" y "total_compra") con su Valor, para 
	 * Mapearlos en la Plantilla Correspondiente (factura.html) y poder 
	 * mostrarlos en la Vista.
	 * Finalmente redirigimos a la Pagina factura.html para mostrarlo.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	
	
	@GetMapping("/compra/factura/{id}")
	public String factura(Model model, @PathVariable Long id) {
		Compra c = compraServicio.buscarPorId(id);
		List<Producto> productos = productoServicio.productosDeUnaCompra(c);
		model.addAttribute("productos", productos);
		model.addAttribute("compra", c);
		model.addAttribute("total_compra", productos.stream().mapToDouble(p -> p.getPrecio()).sum());
		return "/app/compra/factura";
	}
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion HTTP usando la anotacion @RequestMapping
	 * (anotacion con cierta antiguedad, de versiones de Spring previas) donde
	 * indica que es de tipo GET en los Parametros. Escucha en la direccion
	 * "/mis_compras/factura/pdf/{id}".
	 * Su funcion es la de generar un Informe en formato PDF, haciendo uso de la 
	 * Libreria iTexT para Java, la cual hemos importado como dependencia Maven en 
	 * el pom.xml.
	 * Para mostrar los datos en el informe, recabamos la informacion que deseamos
	 * haciendo uso de los Servicios especificos que nos proporcionan acceso a las
	 * distintas Entidades. Buscamos la Compra de la que queremos obtener su factura
	 * por su ID, Extraemos una Lista de los Productos que pertenecen a dicha Compra
	 * y calculamos el Importe Total de la Compra.
	 * Haciendo uso de los Metodos del paquete "reports", generamos la factura en PDF.
	 * 
	 * @param id
	 * @return
	 */
	
	

}
