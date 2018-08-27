/************************************************************************
Archivo: modelo/entecso/pandora/mantenimiento/Entidad.java   
Autor:   ENTECSO

Proyecto:
SISTEMA DE FACTURACION E INVENTARIOS - PANDORA

Todos los derechos reservados a ENTECSO. Prohibida su reproduccion parcial
o total sin autorizacion del autor.

 **************************************************************************/

package com.componente.test;

import java.sql.SQLException;

import com.componente.persistencia.conexion.impl.ConexionObjImpl;
import com.componente.persistencia.impl.PersistenciaImpl;

public class Entidad extends ConexionObjImpl{

	private String id;
	private String nombre;
	private String identificacion;
	private String tipoIdentificacion;
	private String nacionalidad;
	private String paginaWeb;
	private String tipoEntidadId;
	private String direccionId;
	
	public Entidad(PersistenciaImpl persistencia) 
	throws SQLException, Exception {
		super(persistencia);
	}

	public Entidad() throws SQLException {
		super(null);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getPaginaWeb() {
		return paginaWeb;
	}
	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}
	public String getTipoEntidadId() {
		return tipoEntidadId;
	}
	public void setTipoEntidadId(String tipoEntidadId) {
		this.tipoEntidadId = tipoEntidadId;
	}
	public String getDireccionId() {
		return direccionId;
	}
	public void setDireccionId(String direccionId) {
		this.direccionId = direccionId;
	}

}
