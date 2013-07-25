package com.pocket.patit.classes;

/**
 * {@link ElementType}
 * 
 * Clase auxiliar con los datos de un elemento.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ElementType {
	String name;
	int img;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public ElementType(String name, int img) {
		super();
		this.name = name;
		this.img = img;
	}
	
}
