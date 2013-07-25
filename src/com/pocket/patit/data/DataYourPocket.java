package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataYourPocket}
 * 
 * Clase que alamacena en memoria de la aplicación los datos de los bolsillos de usuario. 
 * Esto se utiliza para poder ser accedida cuando no hay conexión para utilizar la aplicación.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataYourPocket {
	private static List<Pocket> list_pockets;

	/**
	 * Constructor parametrizado
	 * @param list_pockets
	 */
	public DataYourPocket(List<Pocket> list_pockets) {
		this.setList_pockets(list_pockets);
	}

	public static List<Pocket> getList_pockets() {
		return list_pockets;
	}

	public static void setList_pockets(List<Pocket> list_pockets) {
		DataYourPocket.list_pockets = list_pockets;
	}
	
}
