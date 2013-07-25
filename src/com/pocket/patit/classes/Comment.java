package com.pocket.patit.classes;

import java.io.Serializable;

import com.pocket.patit.activities.YourPocketsActivity;

/**
 * {@link Comment}
 * 
 * Clase que guarda un comentario. El comentario guarda el nombre del usuario, la fecha
 * y el autor, así como el id del comentario.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String date;		//fecha
	String user;		//usuario
	String comments;	//comentario
	int id;				//id del comentario en el servidor
	
	public Comment(String user, String comments, String create_date,int id ) {
		this.user = user;
		this.comments = comments;
		this.date = create_date;
		this.id =id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
