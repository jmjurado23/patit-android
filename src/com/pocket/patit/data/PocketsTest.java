package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.List;

import com.pocket.patit.classes.Pocket;

/**
 * Clase para almacenar los bolsillos de todos los tipos. Es una clase auxiliar que se usa para
 * comprobar algunos aspectos de la aplicación.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 *
 */
public class PocketsTest {
	
	List<Pocket> your_pockets;
	List<Pocket> random_pockets;
	List<Pocket> featured_pockets;
	List<Pocket> sponsored_pockets;
	

	public PocketsTest() {
		super();
		your_pockets = new ArrayList<Pocket>();
		
		random_pockets = new ArrayList<Pocket>();
		
		featured_pockets = new ArrayList<Pocket>();
		
		sponsored_pockets = new ArrayList<Pocket>();
	}
	
	
	public List<Pocket> getYour_pockets() {
		return your_pockets;
	}
	public List<Pocket> getRandom_pockets() {
		return random_pockets;
	}
	public List<Pocket> getFeatured_pocket() {
		return featured_pockets;
	}
	public List<Pocket> getSponsored_pockets() {
		return sponsored_pockets;
	}
	
	public int getCountYourPocket(){
		if(your_pockets!=null)
			return your_pockets.size();
		else
			return -1;
	}
	public int getCountRandomPocket(){
		if(random_pockets!=null)
			return random_pockets.size();
		else
			return -1;
	}

	public int getCountFeaturedPocket(){
		if(featured_pockets!=null)
			return featured_pockets.size();
		else
			return -1;
	}

	public int getCountSponsoredPocket(){
		if(sponsored_pockets!=null)
			return sponsored_pockets.size();
		else
			return -1;
	}


	
}
