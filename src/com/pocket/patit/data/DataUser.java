package com.pocket.patit.data;

import java.io.Serializable;
import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataUser}
 * 
 * Clase que alamacena la información del usuario
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Pocket> your_pockets;	//lista con los bolsillos del usuario
	
	/**
	 * Constructor vacío
	 */
	public DataUser(){
		
	}

	public List<Pocket> getYour_pockets() {
		return your_pockets;
	}

	public void setYour_pockets(List<Pocket> your_pockets) {
		this.your_pockets = your_pockets;
	}
	
	public void addPocket(Pocket pocket){
		your_pockets.add(pocket);
	}
}
