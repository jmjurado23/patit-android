package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocket.patit.classes.Comment;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.classes.Object;

/**
 * {@link ChargeFeaturedPocketFromJSON}
 * 
 * Clase que pasa la información de los bolsillos destacados desde un elemento JSON
 * a elementos de clase, para después pasarlos a instancias de clases. 
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ChargeFeaturedPocketFromJSON {
	public List<Pocket> getPockets() {
		return pockets; 
	}

	public void setPockets(List<Pocket> pockets) {
		this.pockets = pockets;
	}

	List<Pocket> pockets;					//lista con los bolsillos destacados
	List<String> featured_featured_ids;		//ids con los bolsillos destacados
	JSONObject json0;						//json
	boolean correct;						//si se ha pasado correctamente
	
	public ChargeFeaturedPocketFromJSON(JSONObject json0) {
		pockets = new ArrayList<Pocket>();
		featured_featured_ids = new ArrayList<String>();
		this.json0 = json0;
		this.correct = false;
		
		//se almacena 
		chargePockets();
	}

	/**
	 * Método para cargar los bolsillos a partir del json
	 */
	private void chargePockets() {
		JSONArray commentsArray;
		JSONArray objectsArray;
		JSONArray featuredArray;
		JSONObject featured;
		JSONObject pocket;
		JSONObject comment;
		JSONObject object;
		JSONObject user;
		
		System.out.println(json0.toString());
		
		try {
			
			featuredArray = json0.getJSONArray(Constants.FEATURED);
			
			correct = true;
			
			//variables auxiliares para los bolsillos
			String name_p = "";
			String create_date_p = "";
			String description_p = "";
			String id_p = "";
			String user_p = "";
			String last_mod_p = "";
			String type_p = "";
			int pos_votes_p = -1;
			int neg_votes_p = -1;
			
			//variables auxiliares para los comentarios
			String create_date_c = "";
			int id_c = -1;
			String pocket_c = "";
			String text_c = "";
			String user_c = "";
			
			//variables auxiliares para los objetos
			String name_o = "";
			String id_o = "";
			String description_o = "";
			String url_o = "";
			int n_mod_o = -1;
			String type_o = "";
			String creation_date_o = "";
			String las_mod_o = "";
			
			
			for (int i = 0; i < featuredArray.length(); i++) {
				
				
				
				//obtenemos los bolsillos del JSON
				featured = featuredArray.getJSONObject(i);
				
				featured_featured_ids.add(featured.getString(Constants.ID));
				pocket = featured.getJSONObject(Constants.POCKET);
				
				//se obtienen los datos del bolsillo
				name_p = pocket.getString(Constants.POCKET_NAME);
				create_date_p = pocket.getString(Constants.POCKET_CREATE_DATE);
				description_p = pocket.getString(Constants.POCKET_DESCRIPTION);
				id_p = pocket.getString(Constants.POCKET_ID);
				last_mod_p = pocket.getString(Constants.POCKET_LAST_MOD);
				type_p = pocket.getString(Constants.POCKET_TYPE);
				pos_votes_p = pocket.getInt(Constants.POS_VOTES);
				neg_votes_p = pocket.getInt(Constants.NEG_VOTES);
				
				//se obtiene los datos del dueño del bolsillo
				user = pocket.getJSONObject(Constants.USER);
				user_p = user.getString(Constants.NICK);
				
				//se añaden los bolsillo
				pockets.add(new Pocket(id_p, name_p, type_p, user_p, description_p
						,create_date_p,last_mod_p,0, true,featured_featured_ids.get(i),
						pos_votes_p,neg_votes_p));
				

				//para cada bolsillo obtenemos sus comentarios
				commentsArray = pocket.getJSONArray(Constants.COMMENTS);
				
				for(int j=commentsArray.length()-1;j>=0;j--){
					
					comment = commentsArray.getJSONObject(j);
					
					create_date_c = comment.getString(Constants.COMMENT_CREATE_DATE);
					id_c = comment.getInt(Constants.COMMENT_ID);
					pocket_c = comment.getString(Constants.COMMENT_POCKET);
					text_c = comment.getString(Constants.COMMENT_TEXT);
					user_c =  comment.getString(Constants.COMMENT_USER_NICK);;
					//creamos el comentario y lo añadimos al bolsillo
					Comment com = new Comment(user_c, text_c,create_date_c,id_c);
					pockets.get(i).getComments().add(com);
					
				}
				
				
				//para cada bolsillo obtenemos sus objetos
				objectsArray = pocket.getJSONArray(Constants.OBJECTS);
				
				for(int j=0;j< objectsArray.length();j++){
					
					
					object = objectsArray.getJSONObject(j);
	
					name_o = object.getString(Constants.NAME_OBJECT);
					id_o = object.getString(Constants.ID_OBJECT);
					n_mod_o = object.getInt(Constants.N_MOD_OBJECT);
					type_o = object.getString(Constants.TYPE_OBJECT);
					description_o = object.getString(Constants.DESCRIPTION_OBJECT);
					url_o = object.getString(Constants.URL_OBJECT);
					creation_date_o = object.getString(Constants.CREATION_DATE_OBJECT);
					las_mod_o = object.getString(Constants.LAST_MOD_OBJECT);
					
					//se crea el objeto y se añade a la lista
					pockets.get(i).getObjects().add(new Object(
							id_o,
							type_o, 
							name_o, 
							description_o, 
							"", 
							creation_date_o, 
							las_mod_o, 
							url_o, 
							pockets.get(i).getId_pocket(), 
							n_mod_o,
							""));
				}
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public List<String> getIdFeaturedPockets() {
		
		return featured_featured_ids;
	}



	
	
}
