package com.pocket.patit.activities;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocket.patit.activities.ItemListActivity;
import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.ComparePocketsChanges;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.views.ViewWrapperHomeRow;

/**
 * {@link HomeActivity}
 * 
 * Clase que muestra el menú principal. Este menú es la primera actividad que se muestra
 * una vez que se ha hecho un login. La clase tiene llamadas a las demás activities del siguiente
 * nivel
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class HomeActivity extends Activity {

	private ListView list;                            	//lista con los elementos del menu
	private ArrayAdapter<ItemMenuHome> listAdapter; 	//adaptador de la lista de los elem menu
	private ArrayList<ItemMenuHome> ItemMenuList;		//Elementos del menu
	private static Activity instance;					//instancia de la actividad 
	private DataToInternalMemory memory;				//memoria de los datos almacenados
	private static int new_comment_your_p;				//nuevo comentario
	private static int changes_pocket_your_p; 			//cambio de bolsillo
	private SharedPreferences preferences;				//preferencias de la aplicación
	private Context context;							//conexto de la aplicación
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_home);
        
        onButtonChargingData(false);
        
        context = this;
        
        //inicializamos las variables
        instance = this;
        changes_pocket_your_p = 0;
        new_comment_your_p = 0;
        preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
				Context.MODE_PRIVATE);
        
        //se genera la información para la lista
        list = (ListView) findViewById(R.id.listViewHome);
        generateMenuInfo();
        
        
        //se inicializan los datos
        List<Pocket> list_aux = new ArrayList<Pocket>();  
        DataFeaturedPocket.setList_pockets(list_aux);
        DataYourPocket.setList_pockets(list_aux);
        //se cargan los bolsillos de la memoria si existen
        
        //se intenta cargar la última sesión
        //ChargeUserInfo();
        
        //se obtiene la información de memoria
        memory = new DataToInternalMemory(this);
        JSONObject json = memory.getDataJSONFromSharedPreferences();
        
        chargeUserInfoFromJON(json);
        
        // se realiza la conexión con el servidor, para actualizar los datos
        getInfoFromServer();
        
        //se carga la lista con los fragmentos en el menu
    	listAdapter = new ItemMenuArrayAdapter(this.getBaseContext(),
				 android.R.layout.simple_list_item_1,ItemMenuList);
    	list.setAdapter(listAdapter);
    	
    }

    /**
     * Método que se encarga de sacar la información de usuario a partir de un JSON
     * que se le pasa como parámetro. Carga (Your pockets y featured así como user)
     * @param json
     */
    private void chargeUserInfoFromJON(JSONObject json){
    	
    	if(json != null) //si existen datos almacenados
        {
        	ChargeUserPocketFromJSON charge_pockets = new ChargeUserPocketFromJSON(json);
			DataYourPocket.setList_pockets(charge_pockets.getPockets());
        }
    }
    
    /**
     * Método encargado de realizar una conexión con el servidor para obtener los
     * datos del usuario. Featured, your pockets y usuario
     */
    private void ChargeUserInfo(){
    	
    	// se comprueba si hay internet
    	if(ConnectionTest.haveInternet(this)){
    	
	    	ConnectionServerLogin con = new ConnectionServerLogin(this, Constants.POST);
			//se definen los parámetros para la conexión
			con.defineURL(Constants.API_LOGIN);
	    	con.addParam(Constants.NICK, preferences.getString(Constants.PREFERENCES_LOGIN_NICK,""));
			con.addParam(Constants.PASS, preferences.getString(Constants.PREFERENCES_LOGIN_PASS,""));
			con.execute();
    	}
		else{ //si no hay conexión, se avisa al usuario
    		
    		Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
    	}
		
    }
    
    class ConnectionServerLogin extends ConnectionServer{

		public ConnectionServerLogin(Activity act,String URLMethod) {
			super(act,URLMethod);
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			JSONObject json0 = result;
			
			//se almacenan los datos del usuario
			try {
				
				
				if (result.get("result").equals("OK")) {
					
					//se almacenan las preferencias
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(Constants.PREFERENCES_LOGIN_NICK, result.get(Constants.NICK).toString());
					editor.putString(Constants.PREFERENCES_LOGIN_EMAIL, result.get(Constants.EMAIL).toString());
					//editor.putString(Constants.PREFERENCES_LOGIN_PASS, result.get(Constants.PASSWORD).toString());
					editor.putString(Constants.PREFERENCES_ID_USER, result.get(Constants.ID).toString());
					editor.putString(Constants.PREFERENCES_FIRST_USE, Constants.FALSE);
					editor.putString(Constants.PREFERENCES_LOGIN_REGDATE, result.get(Constants.REG_DATE).toString());
					editor.putString(Constants.PREFERENCES_LOGIN_APIKEY, result.get(Constants.API_KEY).toString());
					editor.commit();
					
				}
				else
					Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(Constants.PREFERENCES_LOGIN_NICK, "");
					editor.putString(Constants.PREFERENCES_LOGIN_EMAIL, "");
					editor.putString(Constants.PREFERENCES_LOGIN_PASS, "");
					editor.putString(Constants.PREFERENCES_ID_USER, "");
					editor.putString(Constants.PREFERENCES_FIRST_USE, Constants.TRUE);
					editor.putString(Constants.PREFERENCES_LOGIN_REGDATE, "");
					editor.putString(Constants.PREFERENCES_LOGIN_APIKEY, "");
					editor.commit();
					
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
    
    /**
     * Método encargado de hacer la conexión con el servidor para obtener los datos de 
     * usuario, bolsillos destacados y bolsillos del usuario
     */
    public void getInfoFromServer(){
   
    	// se comprueba si hay internet
    	if(ConnectionTest.haveInternet(this)){
    		
    		ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket();
	        
	        //se cargan los datos desde las preferencias de usuario
	    	SharedPreferences preferences = this.getSharedPreferences(Constants.PREFERENCES_LOGIN,Context.MODE_PRIVATE);
	    	
	        String nick = preferences.getString(Constants.PREFERENCES_LOGIN_NICK,"");
	   
	        if(nick != ""){
	    		con.defineURL(Constants.API_GET_POCKET_USER+nick+Constants.API_FORMAT_JSON);
	    		con.execute();
	        }
	        else
	        	Toast.makeText(this,getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG).show();
    	}
    	else{ //si no hay conexión, se avisa al usuario
    		Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
    	}
    }
    
    
    /**
     * Método para generar la información del menú con todos los nombres
     * de los fragmentos que maneja la aplicación
     */
    private void generateMenuInfo() {
    	
		ItemMenuList = new ArrayList<ItemMenuHome>();
		ItemMenuList.add(new ItemMenuHome(getString(R.string.yourpocketshome),
				getString(R.string.yourpocketshome),
				R.drawable.yourpocket));
		ItemMenuList.add(new ItemMenuHome(getString(R.string.randompocketshome),
				getString(R.string.randompocketssubhome),
				R.drawable.otherspockets));
		ItemMenuList.add(new ItemMenuHome(getString(R.string.featuredpocketshome),
				getString(R.string.featuredpocketssubhome),
				R.drawable.featuredpockets));
		ItemMenuList.add(new ItemMenuHome(getString(R.string.sponsoredpocketshome),
				getString(R.string.sponsoredpocketssubhome),
				R.drawable.sponsorpockets));
		ItemMenuList.add(new ItemMenuHome(getString(R.string.searchpocketshome),
				getString(R.string.searchpocketssubhome),
				R.drawable.searchpockets));
		
	}

	private static class ItemMenuHome{
		public String title;
		public String subtitle;
		public int image;

		
		public ItemMenuHome(String title, String subtitle, int image){
			this.title= title;
			this.subtitle= subtitle;
			this.image = image;
		}

		public String getTitle() {
			return title;
		}

		public String getSubtitle() {
			return subtitle;
		}
		
	}
	
	/**
	 * Métido para inflar el menu de Action bar con un layout
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    /**
     * Método que realiza una acción en función del botón del action bar menu pulsado
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_settings:
            	Intent Intent= new Intent(this, PreferencesActivity.class);
    			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
                return true;
            case R.id.usericonbarhome:
            	Intent Intent2= new Intent(this, InfoUserActivity.class);
    			startActivityForResult(Intent2, Constants.INFOUSER_ACTIVITY_OK);
            	return true;
            case R.id.helpiconbar:
            	Intent Intent3= new Intent(this, ItemListActivity.class);
    			startActivityForResult(Intent3, Constants.INFOUSER_ACTIVITY_OK);
            	return true;
            	
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Clase encargada de cargar los nombres en el menu desplegable del action bar
     * de la aplicación. Es un Adaptador del menu superior
     * @author Juan Manuel Jurado
     *
     */
    private static class ItemMenuArrayAdapter extends ArrayAdapter<ItemMenuHome> {

		private LayoutInflater inflater;

		public ItemMenuArrayAdapter(Context context,int textViewResourceId,
				List<ItemMenuHome> itemmenuList) {
			
			super(context, textViewResourceId, itemmenuList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			TextView textviewtitle; 
			TextView textviewsubtitle;
			ImageView image ;
			LinearLayout color;
			
			ItemMenuHome item = (ItemMenuHome) this.getItem(position); //mensaje
			
			if (convertView == null) { //si no está definido se define
				
				convertView = inflater.inflate(R.layout.row_home, null);
				
				//se crea el wrapper para dar los elementos de la vista
				ViewWrapperHomeRow wrapper = new ViewWrapperHomeRow(
						convertView);
				
				textviewtitle = wrapper.getTitle();
				textviewtitle.setSelected(true);
				textviewsubtitle = wrapper.getSubTitle();
				textviewsubtitle.setSelected(true);
				image = wrapper.getImage();
				color = wrapper.getColor();
				
				//segçun la posición se carga una imagen
				if(position == Constants.YOUR_POCKETS_LIST_POSITION)
					color.setBackgroundResource(R.drawable.bluebordergrid);
				else if(position == Constants.RANDOM_POCKETS_LIST_POSITION)
					color.setBackgroundResource(R.drawable.redbordergrid);
				else if(position == Constants.FEATURED_POCKETS_LIST_POSITION)
					color.setBackgroundResource(R.drawable.yellowbordergrid);				
				else if(position == Constants.SPONSORED_POCKETS_LIST_POSITION)
					color.setBackgroundResource(R.drawable.greenbordergrid);
				else if(position == Constants.SEARCH_POCKETS_LIST_POSITION)
					color.setBackgroundResource(R.drawable.greybordergrid);

				
				//se definene las etiquetas
				convertView.setTag(new ItemMenuViewHolder(textviewtitle,
						textviewsubtitle, image,color));
				

			} else {  // sino, se aprovecha para no tener que definir (acelera )

				ItemMenuViewHolder viewHolder = (ItemMenuViewHolder) convertView
						.getTag();
				textviewtitle = viewHolder.getTextTitle();
				textviewsubtitle = viewHolder.getTextSubtitle();
				image = viewHolder.getImage();
			}

			//se definen los mensajes
			
			if(position == 0 && changes_pocket_your_p != 0 && new_comment_your_p !=0) //si está en YOUR POCKET, se comprueban act
				textviewsubtitle.setText(item.getSubtitle()+ " np: "+changes_pocket_your_p+" nc : "+new_comment_your_p);
			else
				textviewsubtitle.setText(item.getSubtitle());
			textviewtitle.setText(item.getTitle());
			image.setImageResource(item.image);

			//se definen los parámetros del intent que se va a crear
			final Intent main_intent = new Intent(instance, MainPocketsActivity.class);
			
			if(position == Constants.YOUR_POCKETS_LIST_POSITION){
				main_intent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.YOUR_POCKETS_LIST_POSITION);
			}
			else if(position == Constants.RANDOM_POCKETS_LIST_POSITION){
				main_intent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.RANDOM_POCKETS_LIST_POSITION);
			}
			else if(position == Constants.FEATURED_POCKETS_LIST_POSITION){
				main_intent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.FEATURED_POCKETS_LIST_POSITION);
			}
			else if(position == Constants.SPONSORED_POCKETS_LIST_POSITION){
				main_intent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.SPONSORED_POCKETS_LIST_POSITION);
			}
			else if(position == Constants.SEARCH_POCKETS_LIST_POSITION){
				main_intent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.SEARCH_POCKETS_LIST_POSITION);
			}
			
			convertView.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) {
					instance.startActivityForResult(main_intent, Constants.MAIN_ACTIVITY_OK);
		            
				}
			});
				
			//se devuelve la vista convertida al formato
			return convertView;
		}
	}
	
    /**
     * Clase que define los parámetros de los elementos del menu desplegable del action bar
     * @author Juan Manuel Jurado
     *
     */
	private static class ItemMenuViewHolder {  
	    private TextView textTitle;  
	    private TextView textSubtitle;
	    private ImageView image;
	    private LinearLayout color;
	    
	    public ItemMenuViewHolder(TextView textTitleView, TextView textSubtitle, ImageView image, LinearLayout color ) {  
	      this.textTitle = textTitleView;
	      this.textSubtitle = textSubtitle;
	      this.image = image;
	      this.color = color;
	    }
		
		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}

		public TextView getTextTitle() {
			return textTitle;
		}
		@SuppressWarnings("unused")
		public void setTextTitle(TextView textTitle) {
			this.textTitle = textTitle;
		}
		public TextView getTextSubtitle() {
			return textSubtitle;
		}
		@SuppressWarnings("unused")
		public void setTextSubtitle(TextView textSubtitle) {
			this.textSubtitle = textSubtitle;
		}

		public LinearLayout getColor() {
			return color;
		}

		public void setColor(LinearLayout color) {
			this.color = color;
		}
		
	}
	
	
	/**
	* Clase que hereda de la clase ConnectionServer y que modifica algunos métodos
	* añadiendo funcionalidad a algunos métodos a partir de la clase HomeActivity
	* @author Juan Manuel Jurado
	*/
	class ConnectionServerUpdatePocket extends ConnectionServer{
		
		public ConnectionServerUpdatePocket() {
			super(instance, Constants.GET);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onButtonChargingData(true);
		}
		
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			
					JSONObject json0 = result;
					
					if(result != null)
					{
						//Cargamos los bolsillos Destacados
						ChargeFeaturedPocketFromJSON charge_pockets_fea = new ChargeFeaturedPocketFromJSON(json0);
						
						if(charge_pockets_fea.isCorrect()){
							
							DataFeaturedPocket.setList_pockets(charge_pockets_fea.getPockets());
							DataFeaturedPocket.setId_featured_pockets(charge_pockets_fea.getIdFeaturedPockets());
							
							//cargamos la info obtenida del server en la memoria del teléfono
							ChargeUserPocketFromJSON charge_pockets = new ChargeUserPocketFromJSON(json0);
							
							//se cargan la lista de bolsillos nuevos
							List<Pocket> new_pockets = charge_pockets.getPockets();
							boolean correctParsing = charge_pockets.isCorrect();
							
							
							//si se han recibido bolsillos y no son nulos
							if(new_pockets != null && correctParsing){
								
								//si había datos almacenados se comparan para resaltar los cambios
								if(DataYourPocket.getList_pockets()!=null){
									
									//se crea un comparador para ver los cambios que se han producido
									ComparePocketsChanges comp =  new ComparePocketsChanges();
									
									//se comprueba que DataPocket
									List<Pocket> list_pocket_update = comp.getChangesOnPockets(DataYourPocket.getList_pockets(), new_pockets);
									new_comment_your_p = comp.getN_coment();
									changes_pocket_your_p = comp.getChange_pocket();
									
									//se almacena en la clase de los datos
									DataYourPocket.setList_pockets(list_pocket_update);
									
									//se redibuja la lista
									listAdapter = new ItemMenuArrayAdapter(instance.getBaseContext(),
											 android.R.layout.simple_list_item_1,ItemMenuList);
							    	list.setAdapter(listAdapter);
									
								}
								else{ //si no había datos almacenados, se almacenan como datos nuevos :)
									//se almacena en la clase de los datos
									DataYourPocket.setList_pockets(new_pockets);
								}
								
								//almacenamos en la memoria el json
								memory.saveJSONDataIntoSharedPreferences(json0);
							}
							else
								Toast.makeText(instance, instance.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
						}
					
					}
					else
						Toast.makeText(instance, instance.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
					onButtonChargingData(false);
		}
		
	}
	public void onButtonChargingData(boolean estate) {
	setProgressBarIndeterminateVisibility(estate);
	
	}
	
}
