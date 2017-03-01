/**
 * proyecto........ Persistencia
 * archivo ........ ConexionObjImpl.java
 * fecha   ........ 30/12/2010
 * autor   ........ elviejomago
 *
 */

package com.componente.persistencia.conexion.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.componente.anotaciones.ConstantesTipoDatos;
import com.componente.anotaciones.campo;
import com.componente.anotaciones.tabla;
import com.componente.anotaciones.tipoDato;
import com.componente.mapeo.impl.MapeoImpl;
import com.componente.persistencia.conexion.ConexionObj;
import com.componente.persistencia.impl.PersistenciaImpl;
import com.componente.util.Util;

public class ConexionObjImpl implements ConexionObj {
	private PersistenciaImpl persistencia;

	private static final String OBTENER = "obtener";
	private static final String SET_ID = "setId";
	private static final String INVALIDO = "-1";

	public ConexionObjImpl(PersistenciaImpl persistencia) throws SQLException {
		super();
		this.persistencia = persistencia;
	}

	public boolean guardar() throws Exception{
		boolean result = false;
		boolean transaccion = this.persistencia.existeTransaccionActiva();

		try{
			if(Util.verificarSuperClase(this)){


				if(!transaccion)
					this.persistencia.iniciarTransaccion();

				boolean esActualizar = this.guardarSuperClase();
				this.guardarClase(true, esActualizar);

				if(!transaccion)
					this.persistencia.terminarTransaccion();

				result = true;
			}else{
				this.guardarClase(false, false);
				result = true;
			}
		}catch (Exception e) {
			if(this.persistencia.existeTransaccionActiva())
				this.persistencia.deshacerTransaccion();
			e.printStackTrace();
			throw new Exception(e);
		}

		return result;
	}

	public boolean eliminar() throws Exception{
		boolean result = false;
		boolean transaccion = this.persistencia.existeTransaccionActiva();

		try{
			if(Util.verificarSuperClase(this)){
				if(!transaccion)
					this.persistencia.iniciarTransaccion();

				this.eliminarClase();
				this.eliminarSuperClase();

				if(!transaccion)
					this.persistencia.terminarTransaccion();

				result = true;
			}else{
				this.eliminarClase();
				result = true;
			}
		}catch (Exception e) {
			if(this.persistencia.existeTransaccionActiva())
				this.persistencia.deshacerTransaccion();
			e.printStackTrace();
			throw new Exception(e);
		}

		return result;
	}

	public Object obtener() throws Exception{
		Object objeto = null;

		try{
			if(Util.verificarSuperClase(this)){
				this.obtenerSuperClase();
				this.obtenerClase();
			}else{
				this.obtenerClase();
			}
			objeto = this;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}

		return objeto;
	}

	private boolean actualizarClase() throws Exception {
		boolean result = false;
		//		realiza la actualizacion en la bdd
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this);
			StringBuilder sql = new StringBuilder("update ");
			sql.append(Util.getNombreTabla(this));
			sql.append(" set ");
			//Field fs[] = this.getClass().getDeclaredFields();
			String fs[] = (String[]) Util.obtenerCampos(this.getClass()).toArray(new String[Util.obtenerCampos(this.getClass()).size()]);
			List<Object> valores = new ArrayList<Object>();
			String id = null;
			Object valorId= new Object();
			for(int i=0; i<fs.length;i++){
				if(i == 0){
					id = Util.getNombreCampo(this, fs[i]);
					valorId = retorno.get(fs[i].toLowerCase());
				}else{
					sql.append(Util.getNombreCampo(this, fs[i]));
					sql.append("=");
					sql.append("?");
					sql.append((i+1)<fs.length?",":"");
					valores.add(retorno.get(fs[i].toLowerCase()));
				}
			}
			if(valorId == null){
				result = false;
				throw new Exception("ERROR: Para actualizar se necesita asignar un valor al id de la tabla.");
			}
			valores.add(valorId); 
			sql.append(" where " + id + "=?");

			result = persistencia.ejecutar(sql.toString(), valores);		

			if(Util.debug)
				System.out.println(sql);			

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	private boolean actualizarSuperClase() throws Exception {
		boolean result = false;
		//		realiza la actualizacion en la bdd
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this, this.getClass().getSuperclass());
			StringBuilder sql = new StringBuilder("update ");
			sql.append(Util.getNombreTablaSuperClase(this));
			sql.append(" set ");
			//Field fs[] = this.getClass().getDeclaredFields();
			String fs[] = (String[]) Util.obtenerCampos(this.getClass().getSuperclass()).toArray(new String[Util.obtenerCampos(this.getClass().getSuperclass()).size()]);
			List<Object> valores = new ArrayList<Object>();
			String id = null;
			Object valorId= new Object();
			for(int i=0; i<fs.length;i++){
				if(i == 0){
					id = Util.getNombreCampoSuperClase(this, fs[i]);
					valorId = retorno.get(fs[i].toLowerCase());
				}else{
					sql.append(Util.getNombreCampoSuperClase(this, fs[i]));
					sql.append("=");
					sql.append("?");
					sql.append((i+1)<fs.length?",":"");
					valores.add(retorno.get(fs[i].toLowerCase()));
				}
			}
			if(valorId == null){
				result = false;
				throw new Exception("ERROR: Para actualizar se necesita asignar un valor al id de la tabla.");
			}
			valores.add(valorId); 
			sql.append(" where " + id + "=?");

			result = persistencia.ejecutar(sql.toString(), valores);		

			if(Util.debug)
				System.out.println(sql);			

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	private boolean eliminarClase() throws Exception {
		boolean result = false;
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this);
			StringBuilder sql = new StringBuilder("delete from ");
			sql.append(Util.getNombreTabla(this));
			String fs[] = (String[]) Util.obtenerCampos(this.getClass()).toArray(new String[Util.obtenerCampos(this.getClass()).size()]);
			String id = null;
			List<Object> valores = new ArrayList<Object>();

			id = Util.getNombreCampo(this, fs[0]);
			if(retorno.get(fs[0].toLowerCase()) != null)
				valores.add(retorno.get(fs[0].toLowerCase()));
			else
				throw new Exception("ERROR: No se a asignado ningun valor al id de la tabla. No se puede eliminar.");

			sql.append(" where " + id + "=" + "?");

			result = persistencia.ejecutar(sql.toString(),valores);

			if(Util.debug)
				System.out.println(sql);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	private boolean eliminarSuperClase() throws Exception {
		boolean result = false;
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this);
			StringBuilder sql = new StringBuilder("delete from ");
			sql.append(Util.getNombreTablaSuperClase(this));
			String fs[] = (String[]) Util.obtenerCampos(this.getClass().getSuperclass()).toArray(new String[Util.obtenerCampos(this.getClass().getSuperclass()).size()]);
			String id = null;
			List<Object> valores = new ArrayList<Object>();

			id = Util.getNombreCampoSuperClase(this, fs[0]);
			if(retorno.get(fs[0].toLowerCase()) != null)
				valores.add(retorno.get(fs[0].toLowerCase()));
			else
				throw new Exception("ERROR: No se a asignado ningun valor al id de la tabla. No se puede eliminar.");

			sql.append(" where " + id + "=" + "?");

			result = persistencia.ejecutar(sql.toString(),valores);

			if(Util.debug)
				System.out.println(sql);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public boolean guardarClase(boolean tieneSuperClase, boolean esActualizar) throws Exception {
		boolean result = false;
		//		realiza la inserccion en la bdd
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this);

			if(esActualizar && tieneSuperClase){
				this.actualizarClase();
			}else if(!tieneSuperClase && retorno.containsKey("id") && retorno.get("id") != null){
				this.actualizarClase();
			}else{
				StringBuilder sql = new StringBuilder("insert into ");
				sql.append(Util.getNombreTabla(this));
				sql.append("(");
				//Field fs[] = this.getClass().getDeclaredFields();
				String fs[] = (String[]) Util.obtenerCampos(this.getClass()).toArray(new String[Util.obtenerCampos(this.getClass()).size()]);
				StringBuilder sql1 = new StringBuilder();
				List<Object> valores = new ArrayList<Object>();

				for(int i=0; i<fs.length;i++){
					sql.append(Util.getNombreCampo(this, fs[i]));
					//este sql1 contiene los ?
					sql1.append("?");
					if(i == 0){//el primero contiene el id.. de ley
						Object p = null;
						if(!tieneSuperClase)
							p = persistencia.generarId(this);
						else
							p = retorno.get("id");
						if(p == null){
							valores.add(p);
						}else if(!p.equals(INVALIDO)){
							valores.add(p);
							Util.asignarValorId(this, p);
						}else
							throw new Exception("ERROR: No se pudo generar el valor para el Id de la tabla.");
					}else
						valores.add(retorno.get(fs[i].toLowerCase()));

					sql.append((i+1)<fs.length?",":"");				
					sql1.append((i+1)<fs.length?",":"");
				}
				sql.append(") values(");
				sql.append(sql1.toString());
				sql.append(")");

				if(Util.debug)
					System.out.println(sql);

				result = persistencia.ejecutar(sql.toString(),valores);
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	private boolean guardarSuperClase() throws Exception {
		boolean esActualizar = false;
		//		realiza la inserccion en la bdd
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this, this.getClass().getSuperclass());

			if(retorno.containsKey("id") && retorno.get("id") != null){
				this.actualizarSuperClase();
				esActualizar = true;
			}else{
				StringBuilder sql = new StringBuilder("insert into ");
				sql.append(Util.getNombreTablaSuperClase(this));
				sql.append("(");
				//Field fs[] = this.getClass().getDeclaredFields();
				String fs[] = (String[]) Util.obtenerCampos(this.getClass().getSuperclass()).toArray(new String[Util.obtenerCampos(this.getClass().getSuperclass()).size()]);
				StringBuilder sql1 = new StringBuilder();
				List<Object> valores = new ArrayList<Object>();

				for(int i=0; i<fs.length;i++){
					if(fs[i] != null){
						sql.append(Util.getNombreCampoSuperClase(this, fs[i]));
						//este sql1 contiene los ?
						sql1.append("?");
						if(i == 0){//el primero contiene el id.. de ley
							Object p = persistencia.generarIdSuperClase(this);
							if(p == null){
								valores.add(p);
							}else if(!p.equals(INVALIDO)){
								valores.add(p);
								Util.asignarValorId(this, this.getClass().getSuperclass(), p);
							}else
								throw new Exception("ERROR: No se pudo generar el valor para el Id de la tabla.");
						}else
							valores.add(retorno.get(fs[i].toLowerCase()));

						sql.append((i+1)<fs.length?",":"");				
						sql1.append((i+1)<fs.length?",":"");
					}
				}
				sql.append(") values(");
				sql.append(sql1.toString());
				sql.append(")");

				if(Util.debug)
					System.out.println(sql);

				persistencia.ejecutar(sql.toString(),valores);
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return esActualizar;
	}

	private Object obtenerClase() throws Exception {
		//realiza la obtencion de un solo objeto
		Object objeto = null;
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this);
			StringBuilder sql = new StringBuilder("select ");

			String fs[] = (String[]) Util.obtenerCampos(this.getClass()).toArray(new String[Util.obtenerCampos(this.getClass()).size()]);
			String id = null;
			List<Object> valores = new ArrayList<Object>();
			for(int i=0; i<fs.length;i++){
				if(i == 0){//aqui esta almacenado el id de la tabla
					id = Util.getNombreCampo(this, fs[i]);
					if(retorno.get(fs[i].toLowerCase()) != null)
						valores.add(retorno.get(fs[i].toLowerCase()));
					else
						throw new Exception("ERROR: No se a asignado ningun valor al id de la tabla.");
				}
				sql.append(Util.getNombreCampo(this, fs[i]));
				sql.append((i+1)<fs.length?",":"");
			}
			sql.append(" from ");
			sql.append(Util.getNombreTabla(this));

			if(valores.size() > 0)
				sql.append(" where " + id + "=" + "?");

			ResultSet rs = null;  

			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = valores.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			if(rs.next()){
				retorno.clear();
				for(int i=0; i<fs.length;i++){
					Field campo = Util.getCampo(this, fs[i]);
					Object dato = null;
					if(campo.isAnnotationPresent(tipoDato.class)){
						if(campo.getAnnotation(tipoDato.class).nombre().equalsIgnoreCase(ConstantesTipoDatos.TIMESTAMP.toString())){
							dato = rs.getTimestamp(Util.getNombreCampo(this, fs[i]));
						}
					}else{
						dato = rs.getObject(Util.getNombreCampo(this, fs[i]));
					}
					retorno.put(fs[i].toLowerCase(), dato);
				}
				MapeoImpl.persistenciaObjeto(this, retorno);
				objeto = this;
			}
			rs.close();
			pst.close();

			if(Util.debug)
				System.out.println(sql);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}

		return objeto;
	}

	private Object obtenerSuperClase() throws Exception {
		//realiza la obtencion de un solo objeto
		Object objeto = null;
		try{
			HashMap<String, Object> retorno = MapeoImpl.objetoPersistencia(this, this.getClass().getSuperclass());
			StringBuilder sql = new StringBuilder("select ");
			String fs[] = (String[]) Util.obtenerCampos(this.getClass().getSuperclass()).toArray(new String[Util.obtenerCampos(this.getClass().getSuperclass()).size()]);
			String id = null;
			List<Object> valores = new ArrayList<Object>();
			for(int i=0; i<fs.length;i++){
				if(i == 0){//aqui esta almacenado el id de la tabla
					id = Util.getNombreCampoSuperClase(this, fs[i]);
					if(retorno.get(fs[i].toLowerCase()) != null)
						valores.add(retorno.get(fs[i].toLowerCase()));
					else
						throw new Exception("ERROR: No se a asignado ningun valor al id de la tabla.");
				}
				sql.append(Util.getNombreCampoSuperClase(this, fs[i]));
				sql.append((i+1)<fs.length?",":"");
			}
			sql.append(" from ");
			sql.append(Util.getNombreTablaSuperClase(this));

			if(valores.size() > 0)
				sql.append(" where " + id + "=" + "?");

			ResultSet rs = null; 
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			int j= 1;
			for (Iterator<Object> iterator = valores.iterator(); iterator.hasNext();) {
				pst.setObject(j, iterator.next());
				j++;
			}

			rs = pst.executeQuery();

			if(rs.next()){
				retorno.clear();
				for(int i=0; i<fs.length;i++){
					Field campo = Util.getCampoSuperClase(this, fs[i]);
					Object dato = null;
					if(campo.isAnnotationPresent(tipoDato.class)){
						if(campo.getAnnotation(tipoDato.class).nombre().equalsIgnoreCase(ConstantesTipoDatos.TIMESTAMP.toString())){
							dato = rs.getTimestamp(Util.getNombreCampoSuperClase(this, fs[i]));
						}
					}else{
						dato = rs.getObject(Util.getNombreCampoSuperClase(this, fs[i]));
					}
					retorno.put(fs[i].toLowerCase(), dato);
				}
				MapeoImpl.persistenciaObjeto(this, this.getClass().getSuperclass(), retorno);
				objeto = this;
			}
			rs.close();
			pst.close();

			if(Util.debug)
				System.out.println(sql);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return objeto;
	}

	@SuppressWarnings("rawtypes")
	public List<Object> obtenerLista() throws Exception {
		//realiza la obtencion de los onjetos
		List<Object> lista = new ArrayList<Object>();
		try{
			StringBuilder sql = new StringBuilder("select ");
			//Field fs[] = this.getClass().getDeclaredFields();
			String fs[] = (String[]) Util.obtenerCampos(this.getClass()).toArray(new String[Util.obtenerCampos(this.getClass()).size()]);
			for(int i=0; i<fs.length;i++){
				sql.append(Util.getNombreCampo(this, fs[i]));
				sql.append((i+1)<fs.length?",":"");
			}
			sql.append(" from ");
			sql.append(Util.getNombreTabla(this));

			ResultSet rs = null; 
			PreparedStatement  pst = persistencia.getConexion().prepareStatement(sql.toString());
			rs = pst.executeQuery();

			Object object = null;
			while(rs.next()){			
				//object = this.getClass().newInstance();
				Constructor c = this.getClass().getDeclaredConstructor(persistencia.getClass());
				object = c.newInstance(persistencia);

				//Obtenemos los metodos que tiene el objeto 
				Method metodo = null;

				if(Util.verificarSuperClase(this)){
					metodo = this.getClass().getSuperclass().getSuperclass().getMethod(OBTENER, null);
				}else{
					metodo = this.getClass().getSuperclass().getMethod(OBTENER, null);
				}

				Method setId = this.getClass().getMethod(SET_ID, Object.class);
				setId.invoke(object, rs.getObject(1));

				metodo.invoke(object, null);

				lista.add(object);
			}
			rs.close();

			if(Util.debug)
				System.out.println(sql);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return lista;
	}
	
	/**
	 * @return the persistencia
	 */
	public PersistenciaImpl getPersistencia() {
		return persistencia;
	}

	/**
	 * @param persistencia the persistencia to set
	 */
	public void setPersistencia(PersistenciaImpl persistencia) {
		this.persistencia = persistencia;
	}

}
