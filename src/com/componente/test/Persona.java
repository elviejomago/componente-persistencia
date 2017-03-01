/**
* proyecto........ Persistencia
* archivo ........ Persona.java
* fecha   ........ 06/01/2011
* autor   ........ elviejomago
*
*/

package com.componente.test;

import java.sql.Date;
import java.sql.SQLException;

import javax.xml.ws.Action;

import com.componente.anotaciones.ConstantesAnotaciones;
import com.componente.anotaciones.campo;
import com.componente.anotaciones.claveForanea;
import com.componente.anotaciones.clavePrimaria;
import com.componente.anotaciones.idGenerator;
import com.componente.anotaciones.secuencia;
import com.componente.anotaciones.tabla;
import com.componente.persistencia.conexion.impl.ConexionObjImpl;
import com.componente.persistencia.impl.PersistenciaImpl;

@tabla(nombre="PERSONA")
public class Persona extends ConexionObjImpl{
	@clavePrimaria
	private String id;
	@campo(nombre="nombre")
	private String nombrePersona;
	@claveForanea(tablaReferida="DIRECCION")
	private String identificacion;
	private Date fechaNacimiento;

	public Persona(PersistenciaImpl persistencia) throws SQLException {
		super(persistencia);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	
}
