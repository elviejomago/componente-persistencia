/**
 * proyecto........ ComponentePersistencia
 * archivo ........ Util.java
 * fecha   ........ 26/01/2011
 * autor   ........ elviejomago
 *
 */

package com.componente.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.componente.anotaciones.campo;
import com.componente.anotaciones.clavePrimaria;
import com.componente.anotaciones.noCampo;
import com.componente.anotaciones.superClase;
import com.componente.anotaciones.tabla;

public class Util {

	public static boolean debug = false;

	public static String getNombreTabla(Object objeto) throws Exception{
		String result = "";
		try{
			if(objeto.getClass().isAnnotationPresent(tabla.class)){
				result = objeto.getClass().getAnnotation(tabla.class).nombre();
			}else
				result = objeto.getClass().getSimpleName();

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static String getNombreTabla(Class clase) throws Exception{
		String result = "";
		try{
			if(clase.isAnnotationPresent(tabla.class)){
				tabla a = (tabla)clase.getAnnotation(tabla.class);
				result = a.nombre();
			}else
				result = clase.getSimpleName();

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static String getNombreTablaSuperClase(Object objeto) throws Exception{
		String result = "";
		try{
			if(objeto.getClass().getSuperclass().isAnnotationPresent(tabla.class)){
				result = objeto.getClass().getSuperclass().getAnnotation(tabla.class).nombre();
			}else
				result = objeto.getClass().getSuperclass().getSimpleName();

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static String getNombreCampo(Object objeto, String field) throws Exception{
		String result = "";
		try{
			if(objeto.getClass().getDeclaredField(field).isAnnotationPresent(campo.class)){
				result = objeto.getClass().getDeclaredField(field).getAnnotation(campo.class).nombre();
			}else{
				Field f = objeto.getClass().getDeclaredField(field);
				result = f.getName();
			}

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static String getNombreCampoSuperClase(Object objeto, String field) throws Exception{
		String result = "";
		try{
			if(objeto.getClass().getSuperclass().getDeclaredField(field).isAnnotationPresent(campo.class)){
				result = objeto.getClass().getSuperclass().getDeclaredField(field).getAnnotation(campo.class).nombre();
			}else{
				Field f = objeto.getClass().getSuperclass().getDeclaredField(field);
				result = f.getName();
			}

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<String> obtenerCampos(Class clase) throws Exception{
		List<String> result=null;
		try{
			Field fs[] = clase.getDeclaredFields();
			result = new ArrayList<String>();
			for(int i=0,j=1;i<fs.length;i++){
				if(!fs[i].isAnnotationPresent(noCampo.class)){
					if(fs[i].isAnnotationPresent(clavePrimaria.class))
						result.add(0, fs[i].getName());
					else{
						if(fs[i].getName().toLowerCase().equals("id"))
							result.add(0, fs[i].getName());
						else{
							if(j == fs.length)
								break;
							result.add(j, fs[i].getName());
							j = j+1;
						}
					}
				}
			}
			if(result == null)
				throw new Exception("ERROR: No se encuentra una clave primaria asignada a la tabla.");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static boolean exiteCampo(Class clase, String nombreCampo) throws Exception{
		boolean result=false;
		try{
			Field fs[] = clase.getDeclaredFields();

			for(int i=0;i<fs.length;i++){
				if(fs[i].getName().toLowerCase().equals(nombreCampo))
					result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public static List<Object> setCondicionesCondulta(List<Object> condiciones, String condicional, String campo, String operador, Object valor){

		condiciones.add(condicional);
		condiciones.add(campo);
		condiciones.add(operador);
		condiciones.add(valor);

		return condiciones;
	}

	public static List<Object> setCamposActualizar(List<Object> campos, String campo, Object valor){

		campos.add(campo);
		campos.add(valor);

		return campos;
	}

	public static void asignarValorId(Object clase, Object valorId) throws Exception{
		try{
			Field fs[] = clase.getClass().getDeclaredFields();
			Method ms[] = clase.getClass().getDeclaredMethods();
			Method met = null;
			for(int i=0;i<fs.length;i++){
				for(int j=0;j<ms.length;j++){
					if(ms[j].getName().toLowerCase().substring(3).equalsIgnoreCase(fs[i].getName())){
						if(fs[i].isAnnotationPresent(clavePrimaria.class)){
							if(ms[j].getName().startsWith("set")){
								met = ms[j];
								break;
							}
						}
						else{
							if(fs[i].getName().toLowerCase().equals("id")){
								if(ms[j].getName().startsWith("set")){
									met = ms[j];
									break;
								}
							}
						}
					}
				}
				if(met != null)
					break;
			}
			if(met == null)
				throw new Exception("ERROR: No se encuentra una clave primaria asignada a la tabla.");
			else
				met.invoke(clase, valorId);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static void asignarValorId(Object objeto, Class clase, Object valorId) throws Exception{
		try{
			Field fs[] = clase.getDeclaredFields();
			Method ms[] = clase.getDeclaredMethods();
			Method met = null;
			for(int i=0;i<fs.length;i++){
				for(int j=0;j<ms.length;j++){
					if(ms[j].getName().toLowerCase().substring(3).equalsIgnoreCase(fs[i].getName())){
						if(fs[i].isAnnotationPresent(clavePrimaria.class)){
							if(ms[j].getName().startsWith("set")){
								met = ms[j];
								break;
							}
						}
						else{
							if(fs[i].getName().toLowerCase().equals("id")){
								if(ms[j].getName().startsWith("set")){
									met = ms[j];
									break;
								}
							}
						}
					}
				}
				if(met != null)
					break;
			}
			if(met == null)
				throw new Exception("ERROR: No se encuentra una clave primaria asignada a la tabla.");
			else
				met.invoke(objeto, valorId);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static boolean verificarSuperClase(Object obj){
		boolean tieneSuperClase = false;
		try{
			if(obj.getClass().isAnnotationPresent(superClase.class)){
				tieneSuperClase = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return tieneSuperClase;
	}
	
	public static Field getCampo(Object objeto, String field) throws Exception{
		Field campo = null;
		try{
			campo = objeto.getClass().getDeclaredField(field);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return campo;
	}

	public static Field getCampoSuperClase(Object objeto, String field) throws Exception{
		Field campo = null;
		try{
			campo = objeto.getClass().getSuperclass().getDeclaredField(field);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return campo;
	}

}