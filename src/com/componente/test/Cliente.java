/************************************************************************
Archivo: modelo/entecso/pandora/mantenimiento/Cliente.java   
Autor:   ENTECSO

Proyecto:
SISTEMA DE FACTURACION E INVENTARIOS - PANDORA

Todos los derechos reservados a ENTECSO. Prohibida su reproduccion parcial
o total sin autorizacion del autor.

 **************************************************************************/

package com.componente.test;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.componente.anotaciones.superClase;
import com.componente.persistencia.impl.PersistenciaImpl;

@superClase(nombre="Entidad")
public class Cliente extends Entidad {

	private String id;
	private Timestamp fechaModifica;
	private String usuarioModifica;
	private String estadoId;

	public Cliente() throws SQLException {
		super();
		// TODO Auto-generated constructor stub
	}
	public Cliente(PersistenciaImpl persistencia) throws Exception {
		super(persistencia);
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getFechaModifica() {
		return fechaModifica;
	}
	public void setFechaModifica(Timestamp fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	public String getUsuarioModifica() {
		return usuarioModifica;
	}
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	public String getEstadoId() {
		return estadoId;
	}
	public void setEstadoId(String estadoId) {
		this.estadoId = estadoId;
	}
	
}
