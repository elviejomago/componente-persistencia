/**
 * proyecto........ ComponentePersistencia
 * archivo ........ GeneradorId.java
 * fecha   ........ 09/01/2011
 * autor   ........ elviejomago
 *
 */

package com.componente.persistencia.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GeneradorId {

	protected static String generarIdSecuencia(String nombreSecuencia, PersistenciaImpl persistencia) throws Exception {
		ResultSet rs = null;
		String siguiente = null;
		String error = "No existe la secuencia";
		try {
			StringBuilder sqlSecuencia = new StringBuilder();

			String lenguaje = Messages.getString("com.componente.persistencia.lenguaje");

			if (lenguaje == null) {
				sqlSecuencia.append("SELECT nextval(.'" + nombreSecuencia + "')");
			} else if (lenguaje.equals(PersistenciaImpl.LENGUAJE_POSTGRES)) {
				sqlSecuencia.append("SELECT nextval('" + nombreSecuencia + "')");
			} else if (lenguaje.equals(PersistenciaImpl.LENGUAJE_ORACLE)) {
				sqlSecuencia.append("SELECT " + nombreSecuencia + ".nextval FROM dual");
			}
			PreparedStatement pst = persistencia.getConexion().prepareStatement(sqlSecuencia.toString());
			rs = pst.executeQuery();

			if (rs.next())
				siguiente = rs.getString(1);
			else
				error = "No se puede obtener el siguinte valor de la secuencia " + nombreSecuencia;
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println(error);
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		return siguiente;

	}
}
