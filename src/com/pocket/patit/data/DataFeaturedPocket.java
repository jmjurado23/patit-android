package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataFeaturedPocket}
 * 
 * Clase que alamacena en memoria los elementos destacados. Se almacenan de forma pública 
 * y estática para acceder a ellos desde cualquier parte del código
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataFeaturedPocket {
	private static List<Pocket> list_pockets;			//lista de bolsillos
	private static List<String> id_featured_pockets;	//lista con los id's de los bolsillo


	/**
	 * Constructor parametrizado
	 * @param list_pockets
	 */
	public DataFeaturedPocket(List<Pocket> list_pockets) {
		this.setList_pockets(list_pockets);
	}

	public static List<Pocket> getList_pockets() {
		return list_pockets;
	}

	public static void setList_pockets(List<Pocket> list_pockets) {
		if(list_pockets!=null)
			DataFeaturedPocket.list_pockets = list_pockets;
	}
	public static List<String> getId_featured_pockets() {
		return id_featured_pockets;
	}

	public static void setId_featured_pockets(List<String> id_featured_pockets) {
		if(id_featured_pockets!=null)
			DataFeaturedPocket.id_featured_pockets = id_featured_pockets;
	}

	
}
