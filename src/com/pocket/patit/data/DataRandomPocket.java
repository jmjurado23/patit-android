package com.pocket.patit.data;

import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link DataRandomPocket}
 * 
 * Clase que alamacena en memoria los elementos aleatorios. Se almacenan de forma pública 
 * y estática para acceder a ellos desde cualquier parte del código
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataRandomPocket {
	private static List<Pocket> list_pockets; //lista con los bolsillos aleatorios

	/**
	 * Constructor parametrizado
	 * @param list_pockets
	 */
	public DataRandomPocket(List<Pocket> list_pockets) {
		this.setList_pockets(list_pockets);
	}

	public static List<Pocket> getList_pockets() {
		return list_pockets;
	}

	public static void setList_pockets(List<Pocket> list_pockets) {
		DataRandomPocket.list_pockets = list_pockets;
	}
	
	/**
	 * Método para reemplazar un bolsillo dentro de la lista
	 * @param pocket
	 * @return si se ha reemplazado, si falla -1
	 */
	public static int replacePocket(Pocket pocket){
		for(int i=0;i<list_pockets.size();i++){
			
			if(list_pockets.get(i).getId_pocket() == pocket.getId_pocket())
				DataRandomPocket.list_pockets.get(i).setComments( pocket.getComments());
				DataRandomPocket.list_pockets.get(i).setCreate_date(pocket.getCreate_date());
				DataRandomPocket.list_pockets.get(i).setDescription(pocket.getDescription());
				DataRandomPocket.list_pockets.get(i).setFeatured(pocket.isFeatured());
				DataRandomPocket.list_pockets.get(i).setFeatured_id(pocket.getFeatured_id());
				DataRandomPocket.list_pockets.get(i).setIs_new_pocket(pocket.getIs_new_pocket());
				DataRandomPocket.list_pockets.get(i).setLast_mod(pocket.getLast_mod());
				DataRandomPocket.list_pockets.get(i).setName(pocket.getName());
				DataRandomPocket.list_pockets.get(i).setPos_votes(pocket.getPos_votes());
				DataRandomPocket.list_pockets.get(i).setNeg_votes(pocket.getNeg_votes());
				DataRandomPocket.list_pockets.get(i).setType(pocket.getType());
				DataRandomPocket.list_pockets.get(i).setUser(pocket.getUser());
	
				return i;
		}
		return -1;
	}
	
}
