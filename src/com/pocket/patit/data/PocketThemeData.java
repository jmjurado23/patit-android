package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.pocket.patit.R;
import com.pocket.patit.classes.PocketTheme;

/**
 * {@link PocketThemeData}
 * 
 * Clase que alamacena en memoria los datos de los temas de los bolsillos de la aplicación.
 * Se cargan los nombres, enteros de las imágenes en R.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class PocketThemeData {
	
	public static List<String> theme_name_new_pocket;
	public static List<Integer> theme_img_resource_new_pocket;
	public static List<Integer> theme_img_resource_main_pocket;
	public static List<Integer> theme_img_resource_main_trans_pocket;
	public static List<PocketTheme> themes;
	Context c;	//contexto de la aplicación
	
	/**
	 * Constructor parametrizado de la clase
	 * @param context
	 */
	public PocketThemeData(Context context) {
		super();
		theme_img_resource_new_pocket = new ArrayList<Integer>();
		theme_img_resource_main_pocket = new ArrayList<Integer>();
		theme_img_resource_main_trans_pocket = new ArrayList<Integer>();
		theme_name_new_pocket = new ArrayList<String>();
		themes = new ArrayList<PocketTheme>();
		c = context;
		if(context != null)
			loadData();
	}

	public int getThemeImgResourceMainPocket(int pos){
		return theme_img_resource_main_pocket.get(pos);
	}
	
	public static int getThemeImgResourceMainPocket(String type){
		for(int i = 0; i<theme_name_new_pocket.size();i++)
			if(theme_name_new_pocket.get(i).equals(type))
				return theme_img_resource_main_pocket.get(i);
		return theme_img_resource_main_pocket.get(0);
	}
	
	public int getFeaturedImgResource(boolean state){
		if(state)
			return R.drawable.featured_icon;
		else
			return R.drawable.unfeatured_icon;
	}
	
	public List<String> getTheme_name_new_pocket() {
		return theme_name_new_pocket;
	}

	public void setTheme_name_new_pocket(List<String> theme_name_new_pocket) {
		this.theme_name_new_pocket = theme_name_new_pocket;
	}

	public List<Integer> getTheme_img_resource_new_pocket() {
		return theme_img_resource_new_pocket;
	}

	public void setTheme_img_resource_new_pocket(
			List<Integer> theme_img_resource_new_pocket) {
		this.theme_img_resource_new_pocket = theme_img_resource_new_pocket;
	}

	public List<Integer> getTheme_img_resource_main_pocket() {
		return theme_img_resource_main_pocket;
	}

	public void setTheme_img_resource_main_pocket(
			List<Integer> theme_img_resource_main_pocket) {
		this.theme_img_resource_main_pocket = theme_img_resource_main_pocket;
	}

	public List<PocketTheme> getThemes() {
		return themes;
	}

	public void setThemes(List<PocketTheme> themes) {
		this.themes = themes;
	}

	public int count_themes(){
		return theme_name_new_pocket.size();
	}
	
	/**
	 * Método con la información de todos los datos de los temas de los bolsillos
	 */
	private void loadData() {
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_misc));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_music));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_sport));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_cinema));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_book));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_photo));
		theme_name_new_pocket.add(c.getString(R.string.pocket_theme_cooking));
		
		theme_img_resource_new_pocket.add(R.drawable.miscback);
		theme_img_resource_new_pocket.add(R.drawable.musicback);
		theme_img_resource_new_pocket.add(R.drawable.sportback);
		theme_img_resource_new_pocket.add(R.drawable.cinemaback);
		theme_img_resource_new_pocket.add(R.drawable.bookback);
		theme_img_resource_new_pocket.add(R.drawable.photoback);
		theme_img_resource_new_pocket.add(R.drawable.cookingback);
		
		theme_img_resource_main_pocket.add(R.drawable.miscpocket);
		theme_img_resource_main_pocket.add(R.drawable.musicpocket);
		theme_img_resource_main_pocket.add(R.drawable.sportpocket);
		theme_img_resource_main_pocket.add(R.drawable.cinepocket);
		theme_img_resource_main_pocket.add(R.drawable.bookpocket);
		theme_img_resource_main_pocket.add(R.drawable.photopocket);
		theme_img_resource_main_pocket.add(R.drawable.cookingpocket);
		
		theme_img_resource_main_trans_pocket.add(R.drawable.miscpocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.musicpocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.sportpocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.cinepocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.bookpocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.photopocket2);
		theme_img_resource_main_trans_pocket.add(R.drawable.cookingpocket2);
		
		themes.add(new PocketTheme(theme_name_new_pocket.get(0),theme_img_resource_new_pocket.get(0)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(1),theme_img_resource_new_pocket.get(1)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(2),theme_img_resource_new_pocket.get(2)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(3),theme_img_resource_new_pocket.get(3)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(4),theme_img_resource_new_pocket.get(4)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(5),theme_img_resource_new_pocket.get(5)));
		themes.add(new PocketTheme(theme_name_new_pocket.get(6),theme_img_resource_new_pocket.get(6)));
	}

	public CharSequence getTheme_name_new_pocket(int pos) {
		
		return theme_name_new_pocket.get(pos);
	}

	public int getTheme_img_resource_new_pocket(int pos) {
	
		return theme_img_resource_new_pocket.get(pos);
	}
	

	public int getTheme_img_resource_new_pocket_from_string(String type) {
	
		for(int i=0;i<theme_name_new_pocket.size();i++){
			if(theme_name_new_pocket.get(i).equals(type))
				return theme_img_resource_new_pocket.get(i);
		}
		return theme_img_resource_new_pocket.get(0);
	}

	public int getThemeImgResourceMainPocketTrans(String type) {
		for(int i = 0; i<theme_name_new_pocket.size();i++)
			if(theme_name_new_pocket.get(i).equals(type))
				return theme_img_resource_main_trans_pocket.get(i);
		return theme_img_resource_main_trans_pocket.get(0);
	}

}
