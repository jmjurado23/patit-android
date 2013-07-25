package com.pocket.patit.classes;

/**
 * {@link PocketTheme}
 * 
 *Clase con los datos de imagen y nombre de los temas de los bolsillos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class PocketTheme {
	String name;		//nombre del tema del bolsillo
	int image_back;		//imagen de fondo
	
	/**
	 * Constructor parametrizado
	 * @param name
	 * @param image_back
	 */
	public PocketTheme(String name, Integer image_back) {
		this.name=name;
		this.image_back = image_back;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImage_back() {
		return image_back;
	}
	public void setImage_back(int image_back) {
		this.image_back = image_back;
	}
	
}
