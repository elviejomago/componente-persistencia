package com.componente.anotaciones;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Proyecto ......... ComponentePersistencia
 * Archivo  ......... tipoDato.java
 *
 * Autor    ......... Jorge Patino
 * Creado   ......... 28/05/2013
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface tipoDato {
	String nombre();
}
