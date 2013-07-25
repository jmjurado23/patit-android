package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;
import com.pocket.patit.classes.Object;

/**
 * {@link DataObjectToNet}
 * Esta clase contendrá los objetos credos mientras se suben al servidor
 * Será manejada por un hilo independiente que irá subiendo los objetos mientras se
 * realizan otras acciones en la aplicación.
 * 
 * @author juanma
 *
 */
public class DataObjectToNet {
	private static List<Object> list_objects; //lista con los objetos para la red

	public DataObjectToNet(List<Object> list_objects) {
		DataObjectToNet.setList_objects(list_objects);
	}

	public static List<Object> getList_objects() {
		return list_objects;
	}

	public static void setList_objects(List<Object> list_objects) {
		DataObjectToNet.list_objects = list_objects;
	}
	
}
