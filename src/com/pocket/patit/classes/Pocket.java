package com.pocket.patit.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.pocket.patit.classes.Object;

/**
 * {@link Pocket}
 * 
 * Clase que representa un bolsillo. Esta tiene todos los datos que tiene un bolsillo.
 * Además tiene una lista con los elementos de otros tipos que posee el bolsillo como son
 * los objetos y los comentarios
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class Pocket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id_pocket;			//id del bolsillo en patit
	private String name;				//nombre
	private String type;				//tipo
	private int n_elements;				//número de elementos
	private String user;				//nick del usuario
	private String description;			//descripción
	private String create_date;			//fecha de creación
	private String last_mod;			//última modificación
	private boolean is_new_pocket;		//bariable para indicar si es nuevo FLAG
	private int new_com;				//nuevo comentario FLAG
	private List<Comment> comments;		//lista de comentarios
	private List<Object> objects;		//lista de objetos
	private boolean featured;			//destacado o no
	private String featured_id;			//id del elemento destacado
	private int pos_votes;				//n votos positivos
	private int neg_votes;				//n votos negativos
	
	/* getter y setter de los elementos */
	
	public int getPos_votes() {
		return pos_votes;
	}
	public int getNeg_votes() {
		return neg_votes;
	}
	public void setPos_votes(int pos_votes) {
		this.pos_votes = pos_votes;
	}
	public void setNeg_votes(int neg_votes) {
		this.neg_votes = neg_votes;
	}
	public String getFeatured_id() {
		return featured_id;
	}
	public void setFeatured_id(String featured_id) {
		this.featured_id = featured_id;
	}
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}
	public List<Object> getObjects() {
		return objects;
	}
	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public int getNew_com() {
		return new_com;
	}
	public void setNew_com(int new_com) {
		this.new_com = new_com;
	}
	public boolean getIs_new_pocket() {
		return is_new_pocket;
	}
	public void setIs_new_pocket(boolean is_new_pocket) {
		this.is_new_pocket = is_new_pocket;
	}
	public String getCreate_date() {
		return create_date;
	}
	public String getLast_mod() {
		return last_mod;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public void setLast_mod(String last_mod) {
		this.last_mod = last_mod;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	public int getNComments(){
		if(comments!=null)
			return comments.size();
		else
			return 0;
	}
	public int getNObjects(){
		if(objects!=null)
			return objects.size();
		else
			return 0;
	}
	public String getId_pocket() {
		return id_pocket;
	}
	public void setId_pocket(String id_pocket) {
		this.id_pocket = id_pocket;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getN_elements() {
		return n_elements;
	}
	public void setN_elements(int n_elements) {
		this.n_elements = n_elements;
	}
	
	/**
	 * Constructor vacío
	 */
	public Pocket(){
		comments = new ArrayList<Comment>();
		objects = new ArrayList<Object>();
		is_new_pocket = false;
		new_com = 0;
		setN_elements(0);
		featured=false;
		featured_id="";
	
	}
	

	
	/***
	 * Constructor parametrizado
	 * @param id_pocket
	 * @param name
	 * @param type
	 * @param user
	 * @param description
	 * @param create_date
	 * @param last_mod
	 * @param n
	 * @param featured
	 * @param votes_pos
	 * @param votes_neg
	 */
	public Pocket(String id_pocket, String name, String type, String user, String description, String create_date, String last_mod, int n,boolean featured, int votes_pos, int votes_neg) {
		super();
		this.id_pocket = id_pocket;
		this.name = name;
		this.type = type;
		this.user = user;
		this.setN_elements(n);
		this.comments = new ArrayList<Comment>();
		this.objects = new ArrayList<Object>();
		this.description = description;
		this.create_date = create_date;
		this.last_mod = last_mod;
		this.featured = featured;
		this.pos_votes = votes_pos;
		this.neg_votes = votes_neg;
	}
	
	/**
	 * Constructor parametrizado
	 * @param id_pocket
	 * @param name
	 * @param type
	 * @param user
	 * @param description
	 * @param create_date
	 * @param last_mod
	 * @param n
	 * @param featured
	 * @param featured_id_p
	 * @param votes_pos
	 * @param votes_neg
	 */
	public Pocket(String id_pocket, String name, String type, String user, String description, String create_date, String last_mod, int n,boolean featured,String featured_id_p, int votes_pos, int votes_neg) {
		this.id_pocket = id_pocket;
		this.name = name;
		this.type = type;
		this.user = user;
		this.setN_elements(n);
		this.comments = new ArrayList<Comment>();
		this.objects = new ArrayList<Object>();
		this.description = description;
		this.create_date = create_date;
		this.last_mod = last_mod;
		this.featured = featured;
		this.featured_id = featured_id_p;
		this.pos_votes = votes_pos;
		this.neg_votes = votes_neg;
		
	}
	
}
