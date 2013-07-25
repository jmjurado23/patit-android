package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * {@link ComparePocketsChanges}
 * 
 * Clase encargado de comparar dos listas de bolsillos para indicar
 * los cambios que se han producido en ellos. Compara el número de comentarios
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ComparePocketsChanges {
	
	private int n_coment=0;			
	private int change_pocket=0;

	public int getN_coment() {
		return n_coment;
	}

	public int getChange_pocket() {
		return change_pocket;
	}

	public void setN_coment(int n_coment) {
		this.n_coment = n_coment;
	}

	public void setChange_pocket(int change_pocket) {
		this.change_pocket = change_pocket;
	}

	public static List<Pocket> getChangesOnPockets(List<Pocket> old_p, List<Pocket> new_p){
		
		List<Pocket> result = new ArrayList<Pocket>();

		//ponewmos todos los elementos de los nuevos bolsillos a New
		for(int i=0;i<new_p.size();i++){
			
			result.add(new_p.get(i));
			result.get(i).setIs_new_pocket(true);
			
			int stop=0;
			for(int j=0;j<old_p.size() && stop==0;j++){
				//se comprueba si es nuevo
				if(i < old_p.size() && new_p.get(i).getId_pocket().equals(old_p.get(i).getId_pocket()))
				{
					//si existía se comprueba si tiene mensajes nuevos
					result.get(i).setIs_new_pocket(false);
					
					//si el número de comentarios es distinto
					if(new_p.get(i).getComments().size() != old_p.get(i).getComments().size())
					{
						int new_com = new_p.get(i).getComments().size() -old_p.get(i).getComments().size();
						result.get(i).setNew_com(new_com);
					}
					stop = 1;
				}
			}
		}
		
		return result;
	}
}
