/**
 * proyecto........ Persistencia
 * archivo ........ ConexionUtilImpl.java
 * fecha   ........ 30/12/2010
 * autor   ........ elviejomago
 *
 */

package com.componente.persistencia.conexion.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.componente.anotaciones.ConstantesTipoDatos;
import com.componente.anotaciones.tipoDato;
import com.componente.persistencia.conexion.ConexionUtil;
import com.componente.persistencia.impl.PersistenciaImpl;
import com.componente.util.Util;

public class ConexionUtilImpl implements ConexionUtil{

	private static final String OBTENER = "obtener";
	private static final String SET_ID = "setId";
	private static final String ORDER_BY = "order by";

	/**
	 * Este metodo actualiza los campos que se pasa como parametro 
	 * y segun las condiciones deseadas
	 * El parametro campos esta agrupado en de 2 en 2
	 * ...... el 1 es el nombre del campo
	 * ...... el 2 es el valor que se va a actualizar 
	 * El parametro condiciones es una lista agrupada de 4 en 4 
	 * ...... el 1 es el condicional OR o AND
	 * ...... el 2 es el nombre del campo
	 * ...... el 3 es el operador
	 * ...... el 4 es el valor
	 * El parametro objeto es el Objeto que se va ha consultar
	 * El parametro persitencia contiene la conexion
	 * 
	 * @param valores
	 * @param condiciones
	 * @param objeto
	 * @param persistencia
	 * @return
	 * @throws Exception
	 */
	public static boolean actualizarPorCondicion(List<Object> campos, List<Object> condiciones, 
			Object objeto, PersistenciaImpl persistencia) throws Exception {

		//Variables
		boolean resultado = false;
		StringBuilder sql = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>();

		try{
			sql.append("update "+Util.getNombreTabla(objeto.getClass())+"\n set ");

			for (int i = 0; i < campos.size(); i+=2) {
				if(i == 0){
					sql.append(campos.get(i).toString()+" =  ? ");
					parametros.add(campos.get(i+1));
				}else{
					sql.append(", \n"+campos.get(i).toString()+" =  ? ");
					parametros.add(campos.get(i+1));
				}
			}

			if(!condiciones.isEmpty()){
				sql.append("\n where ");

				//Sacamos del datos de las condiciones para armar el sql
				for (int i = 0; i < condiciones.size(); i+=4) {
					if(i == 0){
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}else{
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}
				}
			}

			//Una vez creado el sql lo ejecutamos
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			pst.execute();

			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return resultado;
	}

	/**
	 * Este metodo obtiene los registros de un objeto bajo condiciones dadas
	 * El parametro condiciones es una lista agrupada de 4 en 4 
	 * ...... el 1 es el condicional OR o AND
	 * ...... el 2 es el nombre del campo
	 * ...... el 3 es el operador
	 * ...... el 4 es el valor
	 * El parametro objeto es el Objeto que se va ha consultar
	 * El parametro persitencia contiene la conexion
	 * 
	 * @param condiciones
	 * @param objeto
	 * @param persistencia
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static List<Object> obtenerPorCondicion(List<Object> condiciones, 
			Object objeto, PersistenciaImpl persistencia) throws Exception {

		//Variables
		List<Object> resultado = new ArrayList<Object>();
		List<Object> parametros = new ArrayList<Object>();
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		try{
			sql.append("select * from ");

			if(Util.verificarSuperClase(objeto)){
				sql.append(Util.getNombreTabla(objeto.getClass())+", "+Util.getNombreTabla(objeto.getClass().getSuperclass()));
			}else{
				sql.append(Util.getNombreTabla(objeto.getClass()));
			}

			if(!condiciones.isEmpty()){
				sql.append("\n where ");
				if(Util.verificarSuperClase(objeto)){
					sql.append(Util.getNombreTabla(objeto.getClass())+".id="+Util.getNombreTabla(objeto.getClass().getSuperclass())+".id and ");
				}
				//Sacamos del datos de las condiciones para armar el sql
				for (int i = 0; i < condiciones.size(); i+=4) {
					if(i == 0){
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}else{
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}
				}
			}

			//Una vez creado el select respectivo lo ejecutamos y obtenemos el Resulset respectivo 
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			Object object = null;
			while(rs.next()){			
				//object = this.getClass().newInstance();
				Constructor c = objeto.getClass().getDeclaredConstructor(persistencia.getClass());
				object = c.newInstance(persistencia);

				//Obtenemos los metodos que tiene el objeto 
				Method metodo = null;

				if(Util.verificarSuperClase(objeto)){
					metodo = objeto.getClass().getSuperclass().getSuperclass().getMethod(OBTENER, null);
				}else{
					metodo = objeto.getClass().getSuperclass().getMethod(OBTENER, null);
				}

				Method setId = objeto.getClass().getMethod(SET_ID, Object.class);
				setId.invoke(object, rs.getObject(1));

				metodo.invoke(object, null);

				resultado.add(object);
			}
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			if(rs!=null)
				rs.close();
		}

		return resultado;
	}

	@SuppressWarnings("rawtypes")
	public static List<Object> obtenerPorCondicionOrdenado(List<Object> condiciones, 
			Object objeto, String ordenar, PersistenciaImpl persistencia) throws Exception {

		//Variables
		List<Object> resultado = new ArrayList<Object>();
		List<Object> parametros = new ArrayList<Object>();
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		try{
			sql.append("select * from ");

			if(Util.verificarSuperClase(objeto)){
				sql.append(Util.getNombreTabla(objeto.getClass())+", "+Util.getNombreTabla(objeto.getClass().getSuperclass()));
			}else{
				sql.append(Util.getNombreTabla(objeto.getClass()));
			}

			if(!condiciones.isEmpty()){
				sql.append("\n where ");
				if(Util.verificarSuperClase(objeto)){
					sql.append(Util.getNombreTabla(objeto.getClass())+".id="+Util.getNombreTabla(objeto.getClass().getSuperclass())+".id and ");
				}
				//Sacamos del datos de las condiciones para armar el sql
				for (int i = 0; i < condiciones.size(); i+=4) {
					if(i == 0){
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2));
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}else{
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}
				}
			}

			if(ordenar != null && !"".equals(ordenar))
				sql.append("\n "+ORDER_BY+" "+ordenar);

			//Una vez creado el select respectivo lo ejecutamos y obtenemos el Resulset respectivo
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			Object object = null;
			while(rs.next()){			
				//object = this.getClass().newInstance();
				Constructor c = objeto.getClass().getDeclaredConstructor(persistencia.getClass());
				object = c.newInstance(persistencia);

				//Obtenemos los metodos que tiene el objeto 
				Method metodo = null;

				if(Util.verificarSuperClase(objeto)){
					metodo = objeto.getClass().getSuperclass().getSuperclass().getMethod(OBTENER, null);
				}else{
					metodo = objeto.getClass().getSuperclass().getMethod(OBTENER, null);
				}

				Method setId = objeto.getClass().getMethod(SET_ID, Object.class);
				setId.invoke(object, rs.getObject(1));

				metodo.invoke(object, null);

				resultado.add(object);
			}
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			if(rs!=null)
				rs.close();
		}
		
		return resultado;
	}

	/**
	 * En los nombres de campo a mostrar y campos de condiciones se deben colocar la referencia del objeto
	 * al que pertenecen para evitar ambiguedades. Ejemplo: persona.nombres
	 * 
	 * @param condiciones
	 * @param objetos
	 * @param campos
	 * @param persistencia
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static List<List<Object>> obtenerConsultaPorObjetos(List<Object> condiciones, 
			List<String> objetos, List<String> campos, PersistenciaImpl persistencia) throws Exception {

		//Variables
		StringBuilder sql = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>();
		List<Object> registros = null;
		List<List<Object>> resultado = new ArrayList<List<Object>>();
		ResultSet rs = null;
		int i =0;

		try{
			sql.append("select ");

			//Colocamos los campos que se van a mostrar como resultado
			if(!campos.isEmpty()){
				i=0;
				for (String campo : campos) {
					if(i==0){
						sql.append(campo);
					}else{
						sql.append(", "+campo);
					}
					i++;
				}
			}else{
				sql.append("* ");
			}

			//Colocamos los objetos que van a intervenir
			if(!objetos.isEmpty()){
				sql.append("\nfrom ");
				i=0;
				for (String objeto : objetos) {
					if(i==0){
						sql.append(objeto);
					}else{
						sql.append(", "+objeto);
					}
					i++;
				}
			}else{
				throw new Exception("No se existen objetos para la consulta");
			}

			//Colocamos las uniones entre objetos
			if(!condiciones.isEmpty()){
				sql.append("\nwhere ");
				for (int j = 0; j < condiciones.size(); j+=4) {
					if(j == 0){
						if(condiciones.get(j).equals(ConstanteOperadores.JOIN)){
							sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2)+" "+condiciones.get(j+3));
						}else{
							//parte modificada por jponce, para q se pueda aceptar los is y is not null
							if(condiciones.get(j+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(j+2).equals(ConstanteOperadores.IS_NOT_NULL)){
								sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2));
							}else{
								if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
									sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2) );
									sql.append(" " + condiciones.get(i+3) + " ");
								}else{
									sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2)+" ? ");
									parametros.add(condiciones.get(j+3));
								}
							}
						}
					}else{
						if(condiciones.get(j).equals(ConstanteOperadores.JOIN)){
							sql.append("\n "+ConstanteOperadores.AND+" "+condiciones.get(j+1)+" "+condiciones.get(j+2)+" "+condiciones.get(j+3));
						}else{
							//parte modificada por jponce, para q se pueda aceptar los is y is not null
							if(condiciones.get(j+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(j+2).equals(ConstanteOperadores.IS_NOT_NULL)){
								sql.append("\n "+condiciones.get(j)+" "+condiciones.get(j+1)+" "+condiciones.get(j+2));
							}else{
								if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
									sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
									sql.append(" " + condiciones.get(i+3) + " ");
								}else{
									sql.append("\n "+condiciones.get(j)+" "+condiciones.get(j+1)+" "+condiciones.get(j+2)+" ? ");
									parametros.add(condiciones.get(j+3));
								}
							}
						}
					}
				}
			}

			//Una vez creado el select respectivo lo ejecutamos y obtenemos el Resulset respectivo
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			while(rs.next()){
				registros = new ArrayList<Object>();
				i=1;
				for (String campo : campos) {
					if(campo != null && campo.contains(ConstantesTipoDatos.TIMESTAMP.toString().toLowerCase())){
						registros.add(rs.getTimestamp(i));
					}else{
						registros.add(rs.getObject(i));
					}
					i++;
				}
				resultado.add(registros);
			}
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			if(rs!=null)
				rs.close();
		}

		return resultado;
	}

	/**
	 * En los nombres de campo a mostrar y campos de condiciones se deben colocar la referencia del objeto
	 * al que pertenecen para evitar ambiguedades. Ejemplo: persona.nombres
	 * 
	 * @param condiciones
	 * @param objetos
	 * @param campos
	 * @param ordenar
	 * @param persistencia
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static List<List<Object>> obtenerConsultaPorObjetosOrdenado(List<Object> condiciones, 
			List<String> objetos, List<String> campos, String ordenar, PersistenciaImpl persistencia) throws Exception {

		//Variables
		StringBuilder sql = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>();
		List<Object> registros = null;
		List<List<Object>> resultado = new ArrayList<List<Object>>();
		ResultSet rs = null;
		int i =0;

		try{
			sql.append("select ");

			//Colocamos los campos que se van a mostrar como resultado
			if(!campos.isEmpty()){
				i=0;
				for (String campo : campos) {
					if(i==0){
						sql.append(campo);
					}else{
						sql.append(", "+campo);
					}
					i++;
				}
			}else{
				sql.append("* ");
			}

			//Colocamos los objetos que van a intervenir
			if(!objetos.isEmpty()){
				sql.append("\nfrom ");
				i=0;
				for (String objeto : objetos) {
					if(i==0){
						sql.append(objeto);
					}else{
						sql.append(", "+objeto);
					}
					i++;
				}
			}else{
				throw new Exception("No se existen objetos para la consulta");
			}

			//Colocamos las uniones entre objetos
			if(!condiciones.isEmpty()){
				sql.append("\nwhere ");
				for (int j = 0; j < condiciones.size(); j+=4) {
					if(j == 0){
						if(condiciones.get(j).equals(ConstanteOperadores.JOIN)){
							sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2)+" "+condiciones.get(j+3));
						}else{
							//parte modificada por jponce, para q se pueda aceptar los is y is not null
							if(condiciones.get(j+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(j+2).equals(ConstanteOperadores.IS_NOT_NULL)){
								sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2));
							}else{
								if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
									sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2) );
									sql.append(" " + condiciones.get(i+3) + " ");
								}else{
									sql.append(condiciones.get(j+1)+" "+condiciones.get(j+2)+" ? ");
									parametros.add(condiciones.get(j+3));
								}
							}
						}
					}else{
						if(condiciones.get(j).equals(ConstanteOperadores.JOIN)){
							sql.append("\n "+ConstanteOperadores.AND+" "+condiciones.get(j+1)+" "+condiciones.get(j+2)+" "+condiciones.get(j+3));
						}else{
							//parte modificada por jponce, para q se pueda aceptar los is y is not null
							if(condiciones.get(j+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(j+2).equals(ConstanteOperadores.IS_NOT_NULL)){
								sql.append("\n "+condiciones.get(j)+" "+condiciones.get(j+1)+" "+condiciones.get(j+2));
							}else{
								if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
									sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
									sql.append(" " + condiciones.get(i+3) + " ");
								}else{
									sql.append("\n "+condiciones.get(j)+" "+condiciones.get(j+1)+" "+condiciones.get(j+2)+" ? ");
									parametros.add(condiciones.get(j+3));
								}
							}
						}
					}
				}
			}

			if(ordenar != null && !"".equals(ordenar))
				sql.append("\n "+ORDER_BY+" "+ordenar);

			//Una vez creado el select respectivo lo ejecutamos y obtenemos el Resulset respectivo
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			while(rs.next()){
				registros = new ArrayList<Object>();
				i=1;
				for (String campo : campos) {
					if(campo != null && campo.contains(ConstantesTipoDatos.TIMESTAMP.toString().toLowerCase())){
						registros.add(rs.getTimestamp(i));
					}else{
						registros.add(rs.getObject(i));
					}
					i++;
				}
				resultado.add(registros);
			}
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			if(rs!=null)
				rs.close();
		}

		return resultado;
	}

	//@ideb.jpatino
	public static boolean eliminarPorCondicion(List<Object> condiciones, 
			Object objeto, PersistenciaImpl persistencia) throws Exception {

		//Variables
		boolean resultado = false;
		StringBuilder sql = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>();

		try{
			sql.append("delete from "+Util.getNombreTabla(objeto.getClass())+"\n ");

			if(!condiciones.isEmpty()){
				sql.append("\n where ");

				//Sacamos del datos de las condiciones para armar el sql
				for (int i = 0; i < condiciones.size(); i+=4) {
					if(i == 0){
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append(condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}else{
						//parte modificada por jponce, para q se pueda aceptar los is y is not null
						if(condiciones.get(i+2).equals(ConstanteOperadores.IS_NULL) || condiciones.get(i+2).equals(ConstanteOperadores.IS_NOT_NULL)){
							sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2));
						}else{
							if(condiciones.get(i+2).equals(ConstanteOperadores.IN) || condiciones.get(i+2).equals(ConstanteOperadores.NOT_IN)){
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2) );
								sql.append(" " + condiciones.get(i+3) + " ");
							}else{
								sql.append("\n "+condiciones.get(i)+" "+condiciones.get(i+1)+" "+condiciones.get(i+2)+" ? ");
								parametros.add(condiciones.get(i+3));
							}
						}
					}
				}
			}

			//Una vez creado el sql lo ejecutamos
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			pst.execute();

			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return resultado;
	}

}
