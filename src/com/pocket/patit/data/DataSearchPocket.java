package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataRandomPocket}
 * 
 * Clase que alamacena en memoria los bolsillos buscados con el buscador. Se almacenan de forma pública 
 * y estática para acceder a ellos desde cualquier parte del código
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataSearchPocket {
	private static List<Pocket> list_pockets;		//lista de bolsillos buscados
	private static List<String> id_search_pockets;	//id de los elementos featured

	public static List<String> getId_sponsored_pockets() {
		return id_search_pockets;
	}

	public static void setId_sponsored_pockets(List<String> id_sponsored_pockets) {
		DataSearchPocket.id_search_pockets = id_search_pockets;
	}

	/**
	 * Constructor parametrizado
	 * @param list_pockets
	 */
	public DataSearchPocket(List<Pocket> list_pockets) {
		this.setList_pockets(list_pockets);
	}

	public static List<Pocket> getList_pockets() {
		return list_pockets;
	}

	public static void setList_pockets(List<Pocket> list_pockets) {
		DataSearchPocket.list_pockets = list_pockets;
	}

	
}
