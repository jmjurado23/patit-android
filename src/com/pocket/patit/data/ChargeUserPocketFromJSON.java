package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.pocket.patit.classes.Comment;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;
import com.pocket.patit.classes.Pocket;

/**
 * {@link ChargeUserPocketFromJSON}
 * 
 * Clase que pasa la información del usuario a partir de un
 * elemento JSON. Después se puede usar para acceder a esta información las variables
 * de esta clase. 
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ChargeUserPocketFromJSON {
	public List<Pocket> getPockets() {
		return pockets;
	}

	public void setPockets(List<Pocket> pockets) {
		this.pockets = pockets;
	}

	List<Pocket> pockets;	//bolsillos del usuario
	JSONObject json0;		//json	
	boolean correct;		//elemento que dice si se ha completado correctamente el parsing
	
	public ChargeUserPocketFromJSON(JSONObject json0) {
		
		this.pockets = new ArrayList<Pocket>();
		this.json0 = json0;
		this.correct = false;
		
		//se almacena 
		chargePockets();
	}

	public boolean isCorrect() {
		return correct;
	}

	private void chargePockets() {
		JSONArray pocketsArray;
		JSONArray commentsArray;
		JSONArray featuredArray;
		JSONArray objectsArray;
		JSONObject object;
		JSONObject pocket;
		JSONObject comment;
		try {
			
			pocketsArray = json0.getJSONArray(Constants.POCKETS);
			featuredArray = json0.getJSONArray(Constants.FEATURED);
			String user_p = json0.getString(Constants.USER_NICK);
			
			correct = true;
			//variables auxiliares para los bolsillos
			String name_p = "";
			String create_date_p = "";
			String description_p = "";
			String id_p = "";
			String last_mod_p = "";
			String type_p = "";
			int pos_votes_p = -1;
			int neg_votes_p = -1;
			boolean featured_p = false;
			String featured_id_p = "";
			
			//variables auxiliares para los comentarios
			String create_date_c = "";
			String pocket_c = "";
			String text_c = "";
			String user_c = "";
			int id_c = -1;
			
			//variables auxiliares para los objetos
			String name_o = "";
			String id_o = "";
			String description_o = "";
			String url_o = "";
			int n_mod_o = -1;
			String type_o = "";
			String creation_date_o = "";
			String las_mod_o = "";
			
			for (int i = 0; i < pocketsArray.length(); i++) {
				//obtenemos los bolsillos del JSON
				pocket = pocketsArray.getJSONObject(i);
				
				name_p = pocket.getString(Constants.POCKET_NAME);
				create_date_p = pocket.getString(Constants.POCKET_CREATE_DATE);
				description_p = pocket.getString(Constants.POCKET_DESCRIPTION);
				id_p = pocket.getString(Constants.POCKET_ID);
				last_mod_p = pocket.getString(Constants.POCKET_LAST_MOD);
				type_p = pocket.getString(Constants.POCKET_TYPE);
				pos_votes_p = pocket.getInt(Constants.POS_VOTES);
				neg_votes_p = pocket.getInt(Constants.NEG_VOTES);
				int pos = isFeatured(id_p);
				System.out.println("POS "+pos);
				featured_p = false;
				if(pos!= -1)
					pockets.add(new Pocket(id_p, name_p, type_p, user_p, 
							description_p,create_date_p,last_mod_p,0, true,
							DataFeaturedPocket.getId_featured_pockets().get(pos),
							pos_votes_p,neg_votes_p));
				else
					pockets.add(new Pocket(id_p, name_p, type_p, user_p, 
							description_p,create_date_p,last_mod_p,0, false,
							pos_votes_p,neg_votes_p));
				
				
				//para cada bolsillo obtenemos sus comentarios
				commentsArray = pocket.getJSONArray(Constants.COMMENTS);
				
				for(int j=commentsArray.length()-1;j>=0;j--){
					System.out.println("Creando comentario "+j+" del bolsillo "+i);
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

	/**
	 * Método que define si un bolsillo es destacado o no, a la hora de presentarlo
	 * @param id_p
	 * @return entero con ls posición del featured en la lista
	 */
	private int isFeatured(String id_p) {
		for(int i=0;i<DataFeaturedPocket.getList_pockets().size();i++){
			if(DataFeaturedPocket.getList_pockets().get(i).getId_pocket().equals(id_p))
				return i;
			
		}
		return -1;
	}

}
