package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataSponsoredPocket}
 * 
 * Clase que alamacena en memoria los bolsillos esponsorizados. Se almacenan de forma pública 
 * y estática para acceder a ellos desde cualquier parte del código
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataSponsoredPocket {
	private static List<Pocket> list_pockets;			//lista con los bolsillos esponsorizados
	private static List<String> id_sponsored_pockets;	//id con los featured de los bolsillos

	public static List<String> getId_sponsored_pockets() {
		return id_sponsored_pockets;
	}

	public static void setId_sponsored_pockets(List<String> id_sponsored_pockets) {
		DataSponsoredPocket.id_sponsored_pockets = id_sponsored_pockets;
	}

	/**
	 * Constructor parametrizado
	 * @param list_pockets
	 */
	public DataSponsoredPocket(List<Pocket> list_pockets) {
		this.setList_pockets(list_pockets);
	}

	public static List<Pocket> getList_pockets() {
		return list_pockets;
	}

	public static void setList_pockets(List<Pocket> list_pockets) {
		DataSponsoredPocket.list_pockets = list_pockets;
	}

	
}
