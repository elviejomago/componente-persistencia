/**
* proyecto........ Persistencia
* archivo ........ Test.java
* fecha   ........ 06/01/2011
* autor   ........ elviejomago
*
*/

package com.componente.test;

import java.util.ArrayList;
import java.util.List;

import com.componente.persistencia.conexion.impl.ConexionUtilImpl;
import com.componente.persistencia.conexion.impl.ConstanteOperadores;
import com.componente.persistencia.impl.PersistenciaImpl;
import com.componente.util.Util;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try{
		PersistenciaImpl persistencia = new PersistenciaImpl();
//		Cliente cliente = new Cliente(persistencia);

//		//Campos que se actualizan
//		List<String> campos = new ArrayList<String>();
//		campos.add("nombre");
//		campos.add("Jorge Luis Patino");
//		campos.add("identificacion");
//		campos.add("1717732760");
//		//Armar las condiciones
//		List<String> condiciones = new ArrayList<String>();
//		//Condicion 1
//		condiciones.add(ConstanteOperadores.AND);//el primer condicional no se lo toma en cuenta asi que siempre dene ser and 
//		condiciones.add("nombre");
//		condiciones.add(ConstanteOperadores.LIKE);
//		condiciones.add("%ge%");
//		//Condicion 2
//		condiciones.add(ConstanteOperadores.AND);
//		condiciones.add("identificacion");
//		condiciones.add(ConstanteOperadores.IGUAL);
//		condiciones.add("1234567890");
//
//		ConexionUtilImpl.actualizarPorCondicion(campos, condiciones, persona, persistencia);

//
//		for (Object object : prueba) {
//			System.out.println(object);
//		}

//		persona.setIdentificacion("21312312");
//		persona.setNombre("jojojo");
//		persona.guardar();
//		
		//cliente.setId("1052");
		//List<Object> lista = cliente.obtenerLista();
//		cliente.setNombre("jorge luis patino");
//		cliente.setIdentificacion("121212");
//		cliente.setTipoIdentificacion("C");
//		cliente.setNacionalidad("ec");
//		cliente.setPaginaWeb("");
//		cliente.setTipoEntidadId("1");
//		cliente.setDireccionId("1");
//		cliente.setEstadoId("1");
//		cliente.setUsuarioModifica("1");
//		cliente.setFechaModifica(new Timestamp(23000));
//		
//		cliente.guardar();
		
//		condiciones = com.componente.util.Util.setCondicionesCondulta(condiciones, ConstanteOperadores.AND, "estadoId", 
//				ConstanteOperadores.IGUAL, "1");
//
//		List<Object> usuarios = ConexionUtilImpl.obtenerPorCondicion(condiciones, new Cliente(), "nombre",persistencia);


		
		List<Object> condiciones = new ArrayList<Object>();
		//Condicion
		condiciones = Util.setCondicionesCondulta(condiciones, ConstanteOperadores.JOIN, "p.id",ConstanteOperadores.IGUAL, "pd.id_producto");
		condiciones = Util.setCondicionesCondulta(condiciones, ConstanteOperadores.JOIN, "trim(to_char(pd.id, '999999999999'))",ConstanteOperadores.IGUAL, "pb.detalleproductoid");
		condiciones = Util.setCondicionesCondulta(condiciones, ConstanteOperadores.JOIN, "b.id",ConstanteOperadores.IGUAL, "pb.bodegaid");
		condiciones = Util.setCondicionesCondulta(condiciones, ConstanteOperadores.AND, "b.id",ConstanteOperadores.IGUAL, "1");
		//Objetos
		List<String> objetos = new ArrayList<String>();
		objetos.add("producto p");
		objetos.add("producto_detalle pd");
		objetos.add("invbodega b");
		objetos.add("invproductosxbodega pb");
		//campos
		List<String> campos = new ArrayList<String>();
		campos.add("b.nombre");
		campos.add("p.producto");

		
		List<List<Object>> prueba = ConexionUtilImpl.obtenerConsultaPorObjetos(condiciones, objetos, campos, persistencia);

		persistencia.cerrarConexion();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
