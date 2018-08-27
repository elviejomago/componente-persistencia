/**
 * proyecto........ Persistencia
 * archivo ........ MapeoImpl.java
 * fecha   ........ 04/01/2011
 * autor   ........ elviejomago
 *
 */

package com.componente.mapeo.impl;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.componente.mapeo.Mapeo;

public class MapeoImpl implements Mapeo {
	private static final String GET = "get";
	private static final String SET = "set";

	public static HashMap<String, Object> objetoPersistencia(Object clase) throws Exception {
		HashMap<String, Object> resultado = null;

		try {
			resultado = new HashMap<String, Object>();

			Method metodos[] = clase.getClass().getDeclaredMethods();
			for (int i = 0; i < metodos.length; i++) {
				Method metodo = metodos[i];

				if (metodo.getName().startsWith(GET)) {
					Object retorno = metodo.invoke(clase); // Obtenemos el valor que devuelve el metodo get
					resultado.put(metodo.getName().substring(3).toLowerCase(), retorno); // Colocamos ese valor en el
																							// mapa que sera devuelto
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return resultado;
	}

	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> objetoPersistencia(Object objeto, Class clase) throws Exception {
		HashMap<String, Object> resultado = null;

		try {
			resultado = new HashMap<String, Object>();

			Method metodos[] = clase.getDeclaredMethods();
			for (int i = 0; i < metodos.length; i++) {
				Method metodo = metodos[i];

				if (metodo.getName().startsWith(GET)) {
					Object retorno = metodo.invoke(objeto);
					resultado.put(metodo.getName().substring(3).toLowerCase(), retorno);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return resultado;
	}

	public static boolean persistenciaObjeto(Object clase, HashMap<String, Object> datos) throws Exception {
		boolean resultado = true;

		try {
			Method metodos[] = clase.getClass().getDeclaredMethods();

			for (int i = 0; i < metodos.length; i++) {
				Method metodo = metodos[i];

				if (metodo.getName().startsWith(SET)) {
					metodo.invoke(clase, datos.get(metodo.getName().substring(3).toLowerCase()));
				}
			}
		} catch (Exception e) {
			resultado = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return resultado;
	}

	@SuppressWarnings("rawtypes")
	public static boolean persistenciaObjeto(Object objeto, Class clase, HashMap<String, Object> datos)
			throws Exception {
		boolean resultado = true;

		try {
			Method metodos[] = clase.getDeclaredMethods();

			for (int i = 0; i < metodos.length; i++) {
				Method metodo = metodos[i];

				if (metodo.getName().startsWith(SET)) {
					metodo.invoke(objeto, datos.get(metodo.getName().substring(3).toLowerCase()));
				}
			}
		} catch (Exception e) {
			resultado = false;
			e.printStackTrace();
			throw new Exception(e);
		}
		return resultado;
	}
}