package com.pocket.patit.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeSomePocketsFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.ComparePocketsChanges;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataSearchPocket;
import com.pocket.patit.data.DataSponsoredPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.fragments.NewCommentFragment;
import com.pocket.patit.fragments.NewCommentFragment.CallbackNewComment;
import com.pocket.patit.fragments.SingleSearchPocketFragments;
import com.pocket.patit.fragments.SingleSearchPocketFragments.CallbackSearchFragment;

/**
 * {@link SearchPocketsActivity}
 * 
 * Actividad encargada de mostrar los bolsillos buscados por el usuario. Debe recibir
 * los datos de los bolsillos que se han obtenido en la búsqueda del fragmento que llama a
 * esta actividad. Esta sólo muestra los bolsillos pasados como parámetro y carga el layout correspondiente.
 * 
 * @author Juan Manuel Jurado Ruiz	
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 * 
 */
public class SearchPocketsActivity extends FragmentActivity implements CallbackSearchFragment,CallbackNewComment
{

    
	private SectionsPagerAdapter mSectionsPagerAdapter; //adaptador de la página
	private ViewPager mViewPager;						//visor de las páginas
	private static int currentPage;						//página actual
	private PageListener pageListener;					//listener de la página
	private Activity act;								//actividad 
	private NewCommentFragment newFragment;				//fragmento para crear un nuevo comentario
	private String actionElem;							//acción del elemento
	private String searchType;							//tipo de búsqueda
	private String searchValue;							//valor de la búsqueda
	SharedPreferences preferences;						//preferencias 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		//cargar los valores a partir del layout
		setContentView(R.layout.activity_search_pockets);
		onButtonChargingData(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		
		try{
			if(DataSearchPocket.getList_pockets() == null){
				finish();
			}
		}catch(Exception e){
				finish();
		}
		// fragment del comentario
		newFragment = new NewCommentFragment();
		//adaptador
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		this.act = this;

		// Se cargan las páginas con los adaptadores
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		pageListener = new PageListener();
		mViewPager.setOnPageChangeListener(pageListener);

		int pag=0;
		
		getInfoFromServer();

		try {
			//En este sitio se intenta cargar el bolsillo correspondiente
			//que se ha pulsado en el grid de la actividad anterior
			if (this.getIntent().getExtras() //nuevo bolsillo
					.getString(Constants.NEW_POCKET_TO_SINGLE_YOUR,"false")
					.equals("true")) 
			{

				getInfoFromServer();
				pag = 0;
				
			} else if(this.getIntent().getExtras()
						.getInt(Constants.MAIN_SEARCH_TO_SINGLE_SEARCH,-1) != -1)
			{
				pag = this.getIntent().getExtras()
						.getInt(Constants.MAIN_SEARCH_TO_SINGLE_SEARCH);
			}
			else if(this.getIntent().getExtras()
					.getInt(Constants.ACTIVITY_TO_SINGLE,-1) != -1){
				pag = this.getIntent().getExtras()
						.getInt(Constants.ACTIVITY_TO_SINGLE);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		
		//valores desde búsqueda
        preferences = getSharedPreferences(Constants.SEARCH,Context.MODE_PRIVATE);
		setSearchValue(preferences.getString(Constants.SEARCH_VALUE, ""));
		setSearchType(preferences.getString(Constants.SEARCH_TYPE, ""));
		
		// seleccionamos la página elegida
		mViewPager.setCurrentItem(pag);
	}


	/**
	 * Clase para crear el adaptador de las páginas
	 * 
	 * @author juanma
	 * 
	 */
	private static class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {
			currentPage = position;
		}
	}

	public int getCurrentPage() {

		return currentPage;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//se carga el layout del menu 
		getMenuInflater().inflate(R.menu.activity_search_pockets, menu);
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		actionProvider.setShareIntent(createShareIntent());

		//Menu
		MenuItem overflowItem = menu
				.findItem(R.id.menu_item_share_action_provider_overflow);
		ShareActionProvider overflowProvider = (ShareActionProvider) overflowItem
				.getActionProvider();
		overflowProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		overflowProvider.setShareIntent(createShareIntent());
		
		return true;
	}

	/**
	 * Método encargado de generar el intent de compartir el elemento
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home: //botón superior de navegación
			
			Intent upIntent = new Intent(this, MainPocketsActivity.class);
			upIntent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, 4);
			NavUtils.navigateUpTo(this, upIntent);
			
			return true;

		case R.id.refreshicon: //botón de refrescar
			getInfoFromServer();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		//sobrescribir la acción al pulsar el botón de retroceso
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	
	    	Intent upIntent = new Intent(this, MainPocketsActivity.class);
			upIntent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, 4);
			NavUtils.navigateUpTo(this, upIntent);
			
	   
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/**
	 * Un {@link FragmentPagerAdapter} que retorna el fragmento correspondiente
	 * al elemento de la página que se está viendo
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			SingleSearchPocketFragments frag = new SingleSearchPocketFragments(
					DataSearchPocket.getList_pockets().get(i));
			return frag;
		}

		@Override
		public int getCount() {
			if(DataSearchPocket.getList_pockets() != null)
				return DataSearchPocket.getList_pockets().size();
			else
				act.finish();
				return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			if (position < DataSearchPocket.getList_pockets().size())
				return DataSearchPocket.getList_pockets().get(position).getName();
			return "";

		}
		/**
		 * Método para obtener el objeto de la posición de la página que se está visualizando
		 */
		public int getItemPosition(Object item) {
			SingleSearchPocketFragments fragment = (SingleSearchPocketFragments) item;
			Pocket pocket = fragment.getPocket();
			int position = DataSearchPocket.getList_pockets().indexOf(pocket);

			if (position >= 0) {
				return position;
			} else {
				return POSITION_NONE;
			}
		}
	}

	/**
	 * Método para obtener la información del servidor. Bolsillos, destacados, información
	 * del usuario. Es necesario llamarla la primera y después llamar a las demás
	 */
	private void getInfoFromServer() {

		
		 //se comprueba que hay conexión
		if(ConnectionTest.haveInternet(this)){
			SharedPreferences preferences = getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);
	
			String nick = preferences.getString(Constants.PREFERENCES_LOGIN_NICK,
					"");
	
			if (nick != "") {
				
				// se definen los parámetros para la conexión
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						this, Constants.GET);
				con.defineURL(Constants.API_GET_POCKET_USER + nick + Constants.API_FORMAT_JSON);
				con.execute();
			} else
				Toast.makeText(this, getString(R.string.NO_USER_PREF),Toast.LENGTH_LONG).show();
		}
		else{ //si no hay conexión, se avisa al usuario
    		
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Método que rellena los parámetros para realizar la conexión de los bolsillos solicitados
	 */
	private void getSearchPocketFromServer() {
		
		actionElem = Constants.FEATURED;

		if(ConnectionTest.haveInternet(this)){
			
			// se definen los parámetros para la conexión
			ConnectionServerSearchPocket con2 = new ConnectionServerSearchPocket(
					this, Constants.GET);
			
			String pockets_act = "";
			for(int i=0;i<DataSearchPocket.getList_pockets().size();i++)
				if(i==0)
					pockets_act = DataSearchPocket.getList_pockets().get(i).getId_pocket();
				else	
					pockets_act += ";" + DataSearchPocket.getList_pockets().get(i).getId_pocket();
			
			con2.defineURL(Constants.API_GET_POCKET_SET +pockets_act+Constants.API_FORMAT_JSON );

			con2.execute();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    }
	}


	/**
	 * Clase para conectar con el server y actualizar la información de los datos
	 * 
	 * @author juanma
	 * 
	 */
	class ConnectionServerUpdatePocket extends ConnectionServer {

		String URLMethod;

		public ConnectionServerUpdatePocket(Activity act, String URLMethod) {
			super(act, URLMethod);
			this.URLMethod = URLMethod;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onButtonChargingData(true);
		}

		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			if (URLMethod.equals(Constants.GET)) {

				JSONObject json0 = result;

				if (result != null) {
					
					//Cargamos los bolsillos Destacados
					ChargeFeaturedPocketFromJSON charge_pockets_fea = new ChargeFeaturedPocketFromJSON(json0);
					if(charge_pockets_fea.isCorrect()){
					
						DataFeaturedPocket.setList_pockets(charge_pockets_fea.getPockets());
						DataFeaturedPocket.setId_featured_pockets(charge_pockets_fea.getIdFeaturedPockets());
						
						// cargamos la info obtenida del server en la memoria del
						// teléfono
						ChargeUserPocketFromJSON charge_pockets = new ChargeUserPocketFromJSON(
								json0);
	
						// se cargan la lista de bolsillos nuevos
						List<Pocket> new_pockets = charge_pockets.getPockets();
	
						// si había datos almacenados se comparan para resaltar los
						// cambios
						if (DataYourPocket.getList_pockets() != null) {
	
							// se crea un comparador para ver los cambios que se han
							// producido
							ComparePocketsChanges comp = new ComparePocketsChanges();
	
							// se comprueba que DataPocket
							List<Pocket> list_pocket_update = comp
									.getChangesOnPockets(
											DataYourPocket.getList_pockets(),
											new_pockets);
	
							// se almacena en la clase de los datos
							DataYourPocket.setList_pockets(list_pocket_update);
	
						} else {
							// se almacena en la clase de los datos
							DataYourPocket.setList_pockets(new_pockets);
						}
	
						// se almacena en la memoria de preferencias
						DataToInternalMemory memory = new DataToInternalMemory(act);
						memory.saveJSONDataIntoSharedPreferences(json0);
	
						//se hace la conexión para recibir los datos 
						getSearchPocketFromServer();
						
						onButtonChargingData(false);
					}
					else
						Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				// se para la imagen de carga
				onButtonChargingData(false);
				
				
			} else if (URLMethod.equals(Constants.DELETE)) { //ELIMINAR
				
				if(actionElem.equals(Constants.FEATURED))
					Toast.makeText(act, act.getString(R.string.unfeatured),
							Toast.LENGTH_LONG).show();
				
				//se para el indicador de carga
				onButtonChargingData(false);
				
			} else if (URLMethod.equals(Constants.POST)) { // si el método es
															// POST, es que
															// hemos creado algo
				onButtonChargingData(false);
				try {
					if (result!=null){
						if (result.get("result").equals(Constants.API_RESULT_OK)) {
							if(actionElem.equals(Constants.FEATURED))
							{
								Toast.makeText(
										act,
										act.getString(R.string.featured_OK),
										Toast.LENGTH_LONG).show();
							}
							else if(actionElem.equals(Constants.COMMENT))
							{
								Toast.makeText(
									act,
									act.getString(R.string.json_status_comment_create_OK),
									Toast.LENGTH_LONG).show();
							}
							else if(actionElem.equals(Constants.VOTE))
							{
								Toast.makeText(
									act,
									act.getString(R.string.json_status_send_vote_OK),
									Toast.LENGTH_LONG).show();
							}
						} 
						else if(result.get("result").equals(Constants.API_RESULT_KO))
						{
							if(actionElem.equals(Constants.FEATURED))
							{
								Toast.makeText(
										act,
										act.getString(R.string.featured_KO),
										Toast.LENGTH_LONG).show();
							}
							else if(actionElem.equals(Constants.COMMENT))
							{
								Toast.makeText(
									act,
									act.getString(R.string.json_status_comment_create_KO),
									Toast.LENGTH_LONG).show();
							}
							else if(actionElem.equals(Constants.VOTE))
							{
								Toast.makeText(
									act,
									act.getString(R.string.json_status_send_vote_KO),
									Toast.LENGTH_LONG).show();
							}
						}
						else if(result.get("result").equals(Constants.API_RESULT_OK_CHANGED_VOTE))
						{
							Toast.makeText(
									act,
									act.getString(R.string.json_status_send_vote_changed_OK),
									Toast.LENGTH_LONG).show();
						}
						else if(result.get("result").equals(Constants.API_KO_VOTE_PREVIOUSLY))
						{
							Toast.makeText(
									act,
									act.getString(R.string.json_status_send_vote_changed_KO),
									Toast.LENGTH_LONG).show();
						}
					
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				// se para la imagen de carga
				onButtonChargingData(false);
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * Clase para realizar la conexión y carga de los valores de los bolsillos obtenidos en la 
	 * búsqueda de usuario y bolsillos.
	 * 
	 * @author Juan Manuel Jurado
	 *
	 */
	class ConnectionServerSearchPocket extends ConnectionServer {

		String URLMethod;

		public ConnectionServerSearchPocket(Activity act, String URLMethod) {
			super(act, URLMethod);
			this.URLMethod = URLMethod;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onButtonChargingData(true);
		}

		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			if (URLMethod.equals(Constants.GET)) {
				JSONObject json0 = result;
				
				if (result != null) {
					
					//se cargan algunos bolsillos con este cargador
					ChargeSomePocketsFromJSON charge_pockets = new ChargeSomePocketsFromJSON(json0);
					
	
					List<Pocket> new_pockets = charge_pockets.getPockets();

					if(new_pockets!=null){
						DataSearchPocket.setList_pockets(new_pockets);
					}

					mSectionsPagerAdapter.notifyDataSetChanged();

					onButtonChargingData(false);
				}
				// se para la imagen de carga
				onButtonChargingData(false);
				
			} 

		}

	}


	/**
	 * Método para definir el estado del indicador de trabajando de la parte superior de 
	 * la actividad
	 * @param estate
	 */
	public void onButtonChargingData(boolean estate) {
		setProgressBarIndeterminateVisibility(estate);
	}



	/**
	 * Método que implementa la función de nuevo comentario, para mostrar el
	 * fragmento del nuevo comentario donde se escribirá el texto del mismo.
	 */
	public void onButtonNewCommentClicked() {

		actionElem = Constants.COMMENT; 
		
		// se genera el fragmento
		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.containerSearchPocketsBig, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}
	public void OnButtonCancelCommentClicked() {

		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.remove(newFragment);
		transaction.commit();

	}

	/**
	 * Método implementado del fragmento de nuevo comentario, que lee el texto y
	 * se encarga de enviar el comentario al servidor
	 */
	public void onButtonSendCommentClicked(String text) {

		actionElem = Constants.COMMENT; 
		
		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.remove(newFragment);
		transaction.commit();

		setCommentToNet(text);

	}

	/**
	 * Método para definir el comentario que se quiere subir a la red
	 * @param text
	 */
	public void setCommentToNet(String text){
		if(ConnectionTest.haveInternet(this)){
			
			// se cargan las preferencias
			SharedPreferences preferences = getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);

			// se crea la conexion
			ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
					this, Constants.POST);

			// se definen los parámetros para la conexión
			con.defineURL(Constants.API_NEWCOMMENT);
			con.addParam(Constants.NICK,preferences.getString(Constants.PREFERENCES_LOGIN_NICK, ""));
			con.addParam(Constants.POCKET_ID,DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem())
							.getId_pocket());
			con.addParam(Constants.COMMENT_TEXT, text);

			con.execute();

			getInfoFromServer();
			
		}
		else{ //si no hay conexión, se avisa al usuario
    		
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Método encargado de destacar un bolsillo o en su contra, de hacerlo no
	 * destacado
	 */
	public void onButtonFeaturedClicked() {
		
		actionElem = Constants.FEATURED;
		
		// en primer lugar se comprueba si el bolsillo es destacado o no
		if (!DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem())
				.isFeatured()) {
			setFeaturedToNet(true);
			
		} else { 
			setFeaturedToNet(false);
		}
	}
	
	public void onGoodVoteButtonClicked() {
		setGoodVote();
	}

	public void onBadVoteButtonClicked() {
		setBadVote();
	}
	
	
	
	/**
	 * Método que comprueba si hay conexión a internet, y en caso afirmativo, 
	 * genera la información necesaria para enviar al servidor del bolsillo
	 * que se quiere marcar como destacado o desmarcar
	 * @param activate 
	 */
	public void setFeaturedToNet(boolean activate){
		
		if(ConnectionTest.haveInternet(this)){
			if(activate){
				SharedPreferences preferences = getSharedPreferences(
						Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);
				// se crea la conexion
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						this, Constants.POST);
		
				// se definen los parámetros para la conexión
				con.defineURL(Constants.API_NEWFEATURED);
				con.addParam(Constants.NICK,
						preferences.getString(Constants.PREFERENCES_LOGIN_NICK, ""));
				con.addParam(
						Constants.FEATURED_ID_POCKET,
						DataSearchPocket.getList_pockets()
								.get(mViewPager.getCurrentItem()).getId_pocket());
				con.execute();
				getInfoFromServer();
			}
			else{
				// se crea la conexion
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(this, Constants.DELETE);

				// se definen los parámetros para la conexión
				System.out.println(Constants.API_DELETEFEATURED +DataSearchPocket.getList_pockets()
						.get(mViewPager.getCurrentItem()).getFeatured_id()
						+Constants.API_FORMAT_JSON);
				con.defineURL(Constants.API_DELETEFEATURED +DataSearchPocket.getList_pockets()
						.get(mViewPager.getCurrentItem()).getFeatured_id()
						+Constants.API_FORMAT_JSON);
				con.execute();
				getInfoFromServer();
			}
		}
		else{ //si no hay conexión, se avisa al usuario
    		
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Método encargado de crear los parámetros de la conexión cuado se pulsa el botón de voto 
	 * positivo.
	 */
	public void setGoodVote(){
		if(ConnectionTest.haveInternet(this)){
			actionElem = Constants.VOTE;
			
			SharedPreferences preferences = getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);

			
			// se crea la conexion
			ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						this, Constants.POST);

			// se definen los parámetros para la conexión
			con.defineURL(Constants.API_NEWVOTE);
			con.addParam(Constants.ID_USER,preferences.getString(Constants.PREFERENCES_LOGIN_ID, ""));
			con.addParam(Constants.FEATURED_ID_POCKET,DataSearchPocket.getList_pockets()
								.get(mViewPager.getCurrentItem()).getId_pocket());
			con.addParam(Constants.VOTE,Integer.toString(1));
			con.execute();
			
			getInfoFromServer();
			
		}
		else{ //si no hay conexión, se avisa al usuario
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
			}
	}

	/**
	 * Método encargado de definir la conexión cuando se pulsa el botón de voto negativo
	 */
	public void setBadVote(){
		
		if(ConnectionTest.haveInternet(this)){
			actionElem = Constants.VOTE;
			
			SharedPreferences preferences = getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);

			
			// se crea la conexion
			ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						this, Constants.POST);

			// se definen los parámetros para la conexión
			con.defineURL(Constants.API_NEWVOTE);
			con.addParam(Constants.ID_USER,preferences.getString(Constants.PREFERENCES_LOGIN_ID, ""));
			con.addParam(Constants.FEATURED_ID_POCKET,DataSearchPocket.getList_pockets()
								.get(mViewPager.getCurrentItem()).getId_pocket());
			con.addParam(Constants.VOTE,Integer.toString(-1));
			con.execute();
			
			getInfoFromServer();
	
		}
		else{ //si no hay conexión, se avisa al usuario
			Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Método que se es llamado cuando se quiere realizar una acción sobre uno de 
	 * los elementos del GRID. 
	 * @param object entero con el número del elemento en el grid
	 */
	public void onButtonGridObjectClicked(int object) {
		
		Intent view_obj_intent = null;
		
		if(DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.IMAGE)){
			view_obj_intent = new Intent(this,ViewObjectImageActivity.class);
		}
		else if(DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.MUSIC)){
			view_obj_intent = new Intent(this,ViewObjectSoundActivity.class);
		}
		else if(DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.TEXT)){
			view_obj_intent = new Intent(this,ViewObjectTextActivity.class);
		}
		else if(DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.URL)){
			view_obj_intent = new Intent(this,ViewObjectURLActivity.class);;
		}
	
		if(view_obj_intent != null)
		{
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT, DataSearchPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object));
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT_PARENT, Constants.SINGLE_SEARCH);
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT_N_PARENT,mViewPager.getCurrentItem());
			this.startActivityForResult(view_obj_intent,
				Constants.VIEW_OBJECT_ACTIVITY_OK);
			
		}
		else
			Toast.makeText(this, getString(R.string.brokenobject), Toast.LENGTH_LONG).show();
	}

	/**
	 * @return el searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType el searchType 
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return el searchValue
	 */
	public String getSearchValue() {
		return searchValue;
	}

	/**
	 * @param searchValue el searchValue 
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

}