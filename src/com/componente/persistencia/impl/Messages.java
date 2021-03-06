/**
* proyecto........ ComponentePersistencia
* archivo ........ Messages.java
* fecha   ........ 09/01/2011
* autor   ........ elviejomago
*
*/

package com.componente.persistencia.impl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.componente.persistencia.propiedades";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
