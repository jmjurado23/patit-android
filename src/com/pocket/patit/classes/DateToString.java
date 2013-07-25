package com.pocket.patit.classes;


/**
 * {@link DateToString}
 * 
 * Clase para pasar de cadena Date de pytho a cadena de elementos en java. Se usa para
 * las fechas
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DateToString {
	
	public static String getYear(String date){
		return date.substring(0, 4);
	}
	public static String getMonth(String date){
		return date.substring(5, 7);
	}
	public static String getDay(String date){
		return date.substring(8, 10);
	}
	public static String getHour(String date){
		return date.substring(11, 13);
	}
	public static String getMinute(String date){
		return date.substring(14, 16);
	}
	public static String getSecond(String date){
		return date.substring(17, 19);
	}
}
