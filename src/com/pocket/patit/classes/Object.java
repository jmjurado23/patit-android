package com.pocket.patit.classes;

import java.io.Serializable;

/**
 * {@link Object}
 * 
 * Clase que representa los datos de un objeto. Id, nombew, descripción, tipo y fechas.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class Object implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id_objet;	//id del objeto en el servidor
	String type;		//tipo del objeto
	String name;		//nombre del objeto
	String descript;	//descripción
	String od_object;	//información adicional del objeto
	String c_date;		//fecha de creación
	String last_mod;	//fecha de última de modificación
	String url;			//url
	String id_pocket; 	//se usa sólo para crear nuevos objectos
	String data;		//dirección del elemento
	
	/**
	 * Primer constructor parametrizado del objeto. Completo
	 * @param id_objet
	 * @param type
	 * @param name
	 * @param descript
	 * @param od_object
	 * @param c_date
	 * @param last_mod
	 * @param url
	 * @param id_pocket
	 * @param n_mod
	 * @param data
	 */
	public Object(String id_objet, String type, String name, String descript,
			String od_object, String c_date, String last_mod, String url,
			String id_pocket, int n_mod, String data) {
		super();
		this.id_objet = id_objet;
		this.type = type;
		this.name = name;
		this.descript = descript;
		this.od_object = od_object;
		this.c_date = c_date;
		this.last_mod = last_mod;
		this.url = url;
		this.id_pocket = id_pocket;
		this.n_mod = n_mod;
		this.data= data;
	}
	
	/**
	 * Segundo constructor parametrizado del objeto. Parcial
	 * @param type
	 * @param name
	 * @param descript
	 * @param id_pocket
	 * @param data
	 */
	public Object(String type, String name, String descript,
			String id_pocket, String data){
		super();
		this.id_objet = "";
		this.type = type;
		this.name = name;
		this.descript = descript;
		this.od_object = "";
		this.c_date = "";
		this.last_mod = null;
		this.url = "";
		this.id_pocket = id_pocket;
		this.n_mod = -1;
		this.data =  data;
		
	}
	
	/*Getter and Setter para todos los elementos */
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId_pocket() {
		return id_pocket;
	}
	public void setId_pocket(String id_pocket) {
		this.id_pocket = id_pocket;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	int n_mod;
	
	public String getId_objet() {
		return id_objet;
	}
	public void setId_objet(String id_objet) {
		this.id_objet = id_objet;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getOd_object() {
		return od_object;
	}
	public void setOd_object(String od_object) {
		this.od_object = od_object;
	}
	public String getC_date() {
		return c_date;
	}
	public void setC_date(String c_date) {
		this.c_date = c_date;
	}
	public String getLast_mod() {
		return last_mod;
	}
	public void setLast_mod(String last_mod) {
		this.last_mod = last_mod;
	}
	public int getN_mod() {
		return n_mod;
	}
	public void setN_mod(int n_mod) {
		this.n_mod = n_mod;
	}	
}
