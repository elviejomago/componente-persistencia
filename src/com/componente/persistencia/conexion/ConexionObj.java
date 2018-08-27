/**
* proyecto........ Persistencia
* archivo ........ ConexionObj.java
* fecha   ........ 30/12/2010
* autor   ........ elviejomago
*
*/

package com.componente.persistencia.conexion;

import java.util.List;

public interface ConexionObj {
	public boolean guardar() throws Exception;

	public boolean eliminar() throws Exception;

	public Object obtener() throws Exception;

	public List<Object> obtenerLista() throws Exception;
}
