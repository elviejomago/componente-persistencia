/**
* proyecto........ ComponentePersistencia
* archivo ........ campo.java
* fecha   ........ 15/01/2011
* autor   ........ elviejomago
*
*/

package com.componente.anotaciones;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface campo {
	String nombre();
}
