/**
 * proyecto........ Persistencia
 * archivo ........ Persistencia.java
 * fecha   ........ 29/12/2010
 * autor   ........ elviejomago
 *
 */

package com.componente.persistencia.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.componente.anotaciones.ConstantesAnotaciones;
import com.componente.anotaciones.idGenerator;
import com.componente.anotaciones.secuencia;
import com.componente.persistencia.Persistencia;

public class PersistenciaImpl implements Persistencia {
	public static final String LENGUAJE_POSTGRES = "POSTGRES";
	public static final String LENGUAJE_ORACLE = "ORACLE";
	public static final String LENGUAJE_MYSQL = "MYSQL";

	private Connection conexion;

	public PersistenciaImpl() throws Exception {
		String lenguaje = Messages.getString("com.componente.persistencia.lenguaje");
		if(lenguaje == null){
			if(!this.crearConexionPostgres())
				throw new SQLException();
		}else if(lenguaje.equals(LENGUAJE_POSTGRES)){
			if(!this.crearConexionPostgres())
				throw new SQLException();
		}else if(lenguaje.equals(LENGUAJE_ORACLE)){
			if(!this.crearConexionOracle())
				throw new SQLException();
		}else if(lenguaje.equals(LENGUAJE_MYSQL)){
			if(!this.crearConexionMySql())
				throw new SQLException();
		}
	}

	public Connection getConexion() {
		return conexion;
	}

	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#crearConexion()
	 */
	private boolean crearConexionPostgres() throws Exception{
		boolean creada = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("com.componente.persistencia.driver.postgres"));
			con = DriverManager.getConnection(Messages.getString("com.componente.persistencia.cadena.postgres"));
			this.setConexion(con);
			creada = true;
		} catch (ClassNotFoundException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		} catch (SQLException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return creada;
	}

	private boolean crearConexionOracle() throws Exception{
		boolean creada = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("com.componente.persistencia.driver.oracle"));
			con = DriverManager.getConnection(Messages.getString("com.componente.persistencia.cadena.oracle"), 
					Messages.getString("com.componente.persistencia.usuario.oracle"), 
					Messages.getString("com.componente.persistencia.clave.oracle"));
			this.setConexion(con);
			creada = true;
		} catch (ClassNotFoundException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		} catch (SQLException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return creada;
	}

	private boolean crearConexionMySql() throws Exception{
		boolean creada = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("com.componente.persistencia.driver.mysql"));
			con = DriverManager.getConnection(Messages.getString("com.componente.persistencia.cadena.mysql"), 
					Messages.getString("com.componente.persistencia.usuario.mysql"), 
					Messages.getString("com.componente.persistencia.clave.mysql"));
			this.setConexion(con);
			creada = true;
		} catch (ClassNotFoundException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		} catch (SQLException e) {
			creada = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return creada;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#cerrarConexion()
	 */
	public boolean cerrarConexion() throws Exception{
		boolean cerrada = false;
		try {
			this.getConexion().close();
			cerrada = true;
		} catch (SQLException e) {
			cerrada = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return cerrada;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#iniciarTransaccion()
	 */
	public boolean iniciarTransaccion() throws Exception{
		boolean iniciar = false;
		try {
			if(this.getConexion().getAutoCommit()){
				this.getConexion().setAutoCommit(false);
				iniciar = true;
			}else{
				throw new Exception("Error: Existe una transaccion activa"); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return iniciar;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#terminarTransaccion()
	 */
	public boolean terminarTransaccion() throws Exception{
		boolean terminar = false;
		try {
			if(!this.getConexion().getAutoCommit()){
				this.getConexion().commit();
				this.getConexion().setAutoCommit(true);
				terminar = true;
			}else{
				throw new Exception("Error: NO Existe una transaccion activa"); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return terminar;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#deshacerTransaccion()
	 */
	public boolean deshacerTransaccion() throws Exception{
		boolean deshacer = false;
		try {
			if(!this.getConexion().getAutoCommit()){
				this.getConexion().rollback();
				this.getConexion().setAutoCommit(true);
				deshacer = true;
			}else{
				throw new Exception("Error: NO Existe una transaccion activa"); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return deshacer;
	}

	public boolean existeTransaccionActiva() throws Exception{
		boolean existe = false;
		try {
			if(!this.getConexion().getAutoCommit()){
				existe = true;
			}else{
				existe = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return existe;
	}

	/* (non-Javadoc)
	 * @see com.componente.persistencia.Persistencia#generarId()
	 */
	public Object generarId(Object objeto) throws Exception{
		//VAriables iniciales
		idGenerator generador = null;
		secuencia secuencia = null;
		String tipoGeneracion = null;
		String nombreSecuencia = null;
		Object id = new Object();

		try{
			id = "-1";
			generador = objeto.getClass().getAnnotation(idGenerator.class);

			//Si existe una anotacion para definir la forma de generar el id
			//se toma esa como prioritaria caso contratio se toma la definida en el properties
			if(generador != null){
				tipoGeneracion = generador.value().name();
			}else{
				tipoGeneracion = Messages.getString("com.componente.persistencia.idGenerator");
			}

			if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.SECUENCIA.toString())){
				if(generador != null){
					secuencia = objeto.getClass().getAnnotation(secuencia.class);
					nombreSecuencia = secuencia.nombre();
				}else{
					nombreSecuencia = Messages.getString("com.componente.persistencia.nombreSecuencia");
				}

				id = GeneradorId.generarIdSecuencia(nombreSecuencia, this);

			}else if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.BASE_DE_DATOS.toString())){
				id = null;
			}else if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.SECUENCIA_NUMERICO.toString())){
				secuencia = objeto.getClass().getAnnotation(secuencia.class);
				if(secuencia != null){
					nombreSecuencia = secuencia.nombre();
				}else{
					nombreSecuencia = Messages.getString("com.componente.persistencia.nombreSecuencia");
				}

				id = new BigDecimal(GeneradorId.generarIdSecuencia(nombreSecuencia, this));
			}

		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return id;
	}

	public Object generarIdSuperClase(Object objeto) throws Exception{
		//VAriables iniciales
		idGenerator generador = null;
		secuencia secuencia = null;
		String tipoGeneracion = null;
		String nombreSecuencia = null;
		Object id = new Object();

		try{
			id = "-1";
			generador = objeto.getClass().getSuperclass().getAnnotation(idGenerator.class);

			//Si existe una anotacion para definir la forma de generar el id
			//se toma esa como prioritaria caso contratio se toma la definida en el properties
			if(generador != null){
				tipoGeneracion = generador.value().name();
			}else{
				tipoGeneracion = Messages.getString("com.componente.persistencia.idGenerator");
			}

			if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.SECUENCIA.toString())){
				if(generador != null){
					secuencia = objeto.getClass().getSuperclass().getAnnotation(secuencia.class);
					nombreSecuencia = secuencia.nombre();
				}else{
					nombreSecuencia = Messages.getString("com.componente.persistencia.nombreSecuencia");
				}

				id = GeneradorId.generarIdSecuencia(nombreSecuencia, this);
			}else if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.BASE_DE_DATOS.toString())){
				id = null;
			}else if(tipoGeneracion.toUpperCase().equals(ConstantesAnotaciones.SECUENCIA_NUMERICO.toString())){
				if(generador != null){
					secuencia = objeto.getClass().getSuperclass().getAnnotation(secuencia.class);
					nombreSecuencia = secuencia.nombre();
				}else{
					nombreSecuencia = Messages.getString("com.componente.persistencia.nombreSecuencia");
				}

				id = new BigDecimal(GeneradorId.generarIdSecuencia(nombreSecuencia, this));
			}

		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return id;
	}

	//Ejecuta una consulta sql con los parametros que se le pasa 
	public ResultSet ejecutarConsulta(String sql, List<Object> parametros) throws Exception{
		PreparedStatement pst = null;
		ResultSet rs = null;

		try{
			pst = this.getConexion().prepareStatement(sql);
			int i= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(i, iterator.next());
				i++;
			}

			rs = pst.executeQuery();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return rs;
	}

	//Ejecuta una operacion de sql insertar, actualizar, eliminar
	public boolean ejecutar(String sql, List<Object> parametros) throws Exception{
		PreparedStatement pst = null;
		boolean exito = false;

		try{
			pst = this.getConexion().prepareStatement(sql);
			int i= 1;
			for (Iterator<Object> iterator = parametros.iterator(); iterator.hasNext();) {
				pst.setObject(i, iterator.next());
				i++;
			}

			pst.execute();
			exito = true;
		}catch (SQLException e) {
			exito = false;
			e.printStackTrace();
			throw new Exception(e);
		}catch (Exception e) {
			exito = false;
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			if(pst != null){
				pst.close();
			}
		}

		return exito;
	}

}
