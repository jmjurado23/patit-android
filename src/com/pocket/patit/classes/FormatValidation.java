package com.pocket.patit.classes;

/**
 * {@link FormatValidation}
 * 
 * Clase auxiliar para validar el formato de una URL
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class FormatValidation {

	public static boolean isValidDestination(String route) {
		
		if(route.equals(""))
			return false;
		
		if(route.contains("/"))
			return false;
			
		return  true;
	}

}
