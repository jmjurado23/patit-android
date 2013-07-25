package com.pocket.patit.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.pocket.patit.classes.Comment;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;
import com.pocket.patit.classes.Pocket;

/**
 * {@link ChargePocketFromJSON}
 * 
 * Clase que pasa la información de un bolsillo a partir de una cadena en formato JSON
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ChargePocketFromJSON {
	public Pocket getPocket() {
		return pocket;
	}

	public void setPocket(Pocket pocket) {
		this.pocket = pocket;
	}

	private Pocket pocket;		//bolsillo
	private JSONObject json0;	//json

	public ChargePocketFromJSON(JSONObject json0) {
		pocket = new Pocket();
		this.json0 = json0;

		// se almacena
		chargePocket();
	}

	private void chargePocket() {

		JSONArray commentsArray;
		JSONArray objectsArray;
		JSONObject userOb;
		JSONObject pocketOb;
		JSONObject commentOb;
		JSONObject objectOb;
		
		try {

			pocketOb = json0;
			userOb = json0.getJSONObject(Constants.USER);
			
			//variables auxiliares para los bolsillos
			String name_p = "";
			String create_date_p = "";
			String description_p = "";
			String id_p = "";
			String last_mod_p = "";
			String type_p = "";
			String user_p = "";
			int id_c = -1;
			int pos_votes_p = -1;
			int neg_votes_p = -1;
			boolean featured_p = false;
			
			//variables auxiliares para los comentarios
			String create_date_c = "";
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

			user_p = userOb.getString(Constants.USER_NICK);
			name_p = pocketOb.getString(Constants.POCKET_NAME);
			create_date_p = pocketOb.getString(Constants.POCKET_CREATE_DATE);
			description_p = pocketOb.getString(Constants.POCKET_DESCRIPTION);
			id_p = pocketOb.getString(Constants.POCKET_ID);
			last_mod_p = pocketOb.getString(Constants.POCKET_LAST_MOD);
			type_p = pocketOb.getString(Constants.POCKET_TYPE);
			pos_votes_p = pocketOb.getInt(Constants.POS_VOTES);
			neg_votes_p = pocketOb.getInt(Constants.NEG_VOTES);

			int pos = isFeatured(id_p);
			featured_p = false;
			
			if (pos != -1)
				pocket = new Pocket(id_p, name_p, type_p, user_p,
						description_p, create_date_p, last_mod_p, 0, true,
						DataFeaturedPocket.getId_featured_pockets().get(pos),
						pos_votes_p, neg_votes_p);
			else
				pocket = new Pocket(id_p, name_p, type_p, user_p,
						description_p, create_date_p, last_mod_p, 0, false,
						pos_votes_p, neg_votes_p);

			// para cada bolsillo obtenemos sus comentarios
			commentsArray = pocketOb.getJSONArray(Constants.COMMENTS);

			for(int j=commentsArray.length()-1;j>=0;j--){
				
				commentOb = commentsArray.getJSONObject(j);

				create_date_c = commentOb
						.getString(Constants.COMMENT_CREATE_DATE);
				id_c = commentOb.getInt(Constants.COMMENT_ID);
				pocket_c = commentOb.getString(Constants.COMMENT_POCKET);
				text_c = commentOb.getString(Constants.COMMENT_TEXT);
				user_c = commentOb.getString(Constants.COMMENT_USER_NICK);
				
				
				// creamos el comentario y lo añadimos al bolsillo
				Comment com = new Comment(user_c, text_c, create_date_c, id_c);
				pocket.getComments().add(com);

			}
			
			//para cada bolsillo obtenemos sus objetos
			objectsArray = pocketOb.getJSONArray(Constants.OBJECTS);
			
			for(int j=0;j< objectsArray.length();j++){
				
				
				objectOb = objectsArray.getJSONObject(j);

				name_o = objectOb.getString(Constants.NAME_OBJECT);
				id_o = objectOb.getString(Constants.ID_OBJECT);
				n_mod_o = objectOb.getInt(Constants.N_MOD_OBJECT);
				type_o = objectOb.getString(Constants.TYPE_OBJECT);
				description_o = objectOb.getString(Constants.DESCRIPTION_OBJECT);
				url_o = objectOb.getString(Constants.URL_OBJECT);
				creation_date_o = objectOb.getString(Constants.CREATION_DATE_OBJECT);
				las_mod_o = objectOb.getString(Constants.LAST_MOD_OBJECT);
				
				//se crea el objeto y se añade a la lista
				pocket.getObjects().add(new Object(
						id_o,
						type_o, 
						name_o, 
						description_o, 
						"", 
						creation_date_o, 
						las_mod_o, 
						url_o, 
						pocket.getId_pocket(), 
						n_mod_o,
						""));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int isFeatured(String id_p) {
		for (int i = 0; i < DataFeaturedPocket.getList_pockets().size(); i++) {
			if (DataFeaturedPocket.getList_pockets().get(i).getId_pocket()
					.equals(id_p))
				return i;

		}
		return -1;
	}


}
