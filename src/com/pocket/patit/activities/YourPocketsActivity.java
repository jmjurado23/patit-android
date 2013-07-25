package com.pocket.patit.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.ComparePocketsChanges;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataObjectToNet;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.fragments.NewCommentFragment;
import com.pocket.patit.fragments.NewCommentFragment.CallbackNewComment;
import com.pocket.patit.fragments.SingleYourPocketFragments;
import com.pocket.patit.fragments.SingleYourPocketFragments.CallbackNewItem;
import com.pocket.patit.services.UploadService;

/**
 * {@link YourPocketsActivity}
 * 
 * Clase que muestra los Fragment de los bolsillos del usuario. Esta actividad permite
 * crear un comentario, guardar objetos, eliminar los bolsillos o midificar los
 * bolsillos del usuario. Para mostar los bolsillos se ayuda de un fragment, que se 
 * muestra con un visor de páginas.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class YourPocketsActivity extends FragmentActivity implements
		CallbackNewItem, CallbackNewComment{

	private SectionsPagerAdapter mSectionsPagerAdapter; //adaptador de página
	private NewCommentFragment newFragment;				//nuevo fragmento
	private PageListener pageListener;					//listener de cambio en página
	private static int currentPage;						//página actual
	private ViewPager mViewPager;						//visor de página
	private int pag_pre_update;							//página antes de actualizar
	private String actionElem;							//accion
	private Activity act;								//actividad
	private int n_pag;									//número de página actual
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_your_pockets);
		onButtonChargingData(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		//se comprueba si hay unos bolsillos cargados, sino, se sale para que 
		//se realice una conexión
		try{
			if(DataYourPocket.getList_pockets() == null){
				finish();
			}
		}catch(Exception e){
			finish();
		}
		
		
		// se inicializan los fragmentos
		newFragment = new NewCommentFragment();

		//se carga el adaptador de las páginas
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		this.act = this;


		// Se cargan las páginas con los adaptadores
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		pageListener = new PageListener();
		mViewPager.setOnPageChangeListener(pageListener);

		// cargamos el bolsillo seleccionado en la actividad anterior, o uno
		// nuevo creado (al final)
		int pag = 0;

		try {

			if (this.getIntent().getExtras() //nuevo bolsillo
					.getString(Constants.NEW_POCKET_TO_SINGLE_YOUR,"false")
					.equals("true")) 
			{

				getInfoFromServer();
				pag = 0;
				
			} else if(this.getIntent().getExtras()
						.getInt(Constants.MAIN_YOUR_TO_SINGLE_YOUR,-1) != -1)
			{
				pag = this.getIntent().getExtras()
						.getInt(Constants.MAIN_YOUR_TO_SINGLE_YOUR);
			}
			else if(this.getIntent().getExtras()
					.getInt(Constants.ACTIVITY_TO_SINGLE,-1) != -1){
				pag = this.getIntent().getExtras()
						.getInt(Constants.ACTIVITY_TO_SINGLE);
			}
			
			System.out.println("PAGE "+ pag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		

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
		getMenuInflater().inflate(R.menu.activity_your_pockets, menu);
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

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		try{
			shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.whatpocket)+ " "+Constants.API_WEB_POCKET + DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getId_pocket()+ " - @patit_web" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return shareIntent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home: //botón de navegación superior
			
			Intent upIntent = new Intent(this, MainPocketsActivity.class);
			upIntent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, 0);
			NavUtils.navigateUpTo(this, upIntent);
			return true;

		case R.id.refreshicon: //botón de refrescar con el server
			getInfoFromServer();

			return true;

		case R.id.removeicon: //botón de eliminar bolsillo
			getDialogYesNoDeletePocket().show();
			return true;
		
		case R.id.editicon: //botón de editar bolsillo
			
			Intent update_intent = new Intent(this,
					UpdatePocketActivity.class);
			update_intent.putExtra(Constants.POCKET_TO_UPDATE, DataYourPocket
					.getList_pockets()
					.get(mViewPager
							.getCurrentItem()));
			startActivityForResult(update_intent,
					Constants.UPDATE_POCKET_OK);
			
			return true;
			
		case R.id.nextbarlogin: //nuevo bolsillo
        	Intent your_intent = new Intent(this, NewPocketActivity.class);
    		this.startActivityForResult(your_intent, Constants.NEW_POCKET_ACTIVITY_OK);
            this.finish();
        	finish();
            return true;
            
		case R.id.menu_settings: //menu de preferencias
        	Intent Intent= new Intent(this, PreferencesActivity.class);
			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
            return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	
	    	Intent upIntent = new Intent(this, MainPocketsActivity.class);
			upIntent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, 0);
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
			SingleYourPocketFragments frag = new SingleYourPocketFragments(
					DataYourPocket.getList_pockets().get(i));
			return frag;
		}

		@Override
		public int getCount() {
			if(DataYourPocket.getList_pockets() != null)
				return DataYourPocket.getList_pockets().size();
			else
				act.finish();
				return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position < DataYourPocket.getList_pockets().size())
				return DataYourPocket.getList_pockets().get(position).getName();
			return "";

		}
		
		/**
		 * Método para obtener el objeto de la posición de la página que se está visualizando
		 */
		public int getItemPosition(Object item) {
			SingleYourPocketFragments fragment = (SingleYourPocketFragments) item;
			Pocket pocket = fragment.getPocket();
			int position = DataYourPocket.getList_pockets().indexOf(pocket);

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
			n_pag = DataYourPocket.getList_pockets().size();
			pag_pre_update = mViewPager.getCurrentItem();
	
			ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
					this, Constants.GET);
	
			SharedPreferences preferences = getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);
	
			String nick = preferences.getString(Constants.PREFERENCES_LOGIN_NICK,
					"");
	
			if (nick != "") {
				// se definen los parámetros para la conexión
				con.defineURL(Constants.API_GET_POCKET_USER + nick
						+ Constants.API_FORMAT_JSON);
				con.execute();
			} else
				Toast.makeText(this,getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG)
						.show();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
		    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Cuadro de diálogo para avisar de que se va a eliminar el bolsillo
	 * @return
	 */
	public AlertDialog getDialogYesNoDeletePocket() {
		return new AlertDialog.Builder(act)
				.setIconAttribute(android.R.attr.alertDialogIcon)
				.setTitle(R.string.deletepocketTitle)
				.setMessage(R.string.deletepocketQuestion)
				.setPositiveButton(R.string.OkDialog,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								// si se selecciona si, se procede a eliminar el
								// bolsillo
								actionElem = Constants.POCKET;
								ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
										act, Constants.DELETE);

								con.defineURL(Constants.API_DELETEPOCKET
										+ DataYourPocket
												.getList_pockets()
												.get(mViewPager
														.getCurrentItem())
												.getId_pocket().toString()
										+ Constants.API_FORMAT_JSON);
								System.out.println(con.getURL());
								con.execute();

								// actualizamos la vista de los bolsillos con la
								// info
								getInfoFromServer();

							}
						})
				.setNegativeButton(R.string.CancelDialog,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked Cancel so do some stuff */
							}
						}).create();
	}

	/**
	 * Clase para conectar con el server
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
	
						mSectionsPagerAdapter.notifyDataSetChanged();
	
						onButtonChargingData(false);
	
						// mostramos la página que se estaba mostrando antes de
						// actualizar
						mViewPager.setCurrentItem((DataYourPocket.getList_pockets()
								.size() - n_pag) + pag_pre_update);
					}
				
					else
						Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
					// se para la imagen de carga
					onButtonChargingData(false);
				
				
			} else if (URLMethod.equals(Constants.DELETE)) { //ELIMINAR
				
				if(actionElem.equals(Constants.POCKET))
					Toast.makeText(act, act.getString(R.string.deletepocket),
						Toast.LENGTH_LONG).show();
				else if(actionElem.equals(Constants.FEATURED))
					Toast.makeText(act, act.getString(R.string.unfeatured),
							Toast.LENGTH_LONG).show();
				else if(actionElem.equals(Constants.OBJECT))
					Toast.makeText(act, act.getString(R.string.deleteobject),
							Toast.LENGTH_LONG).show();
				
				//actualizar la info de los bolsillos
				getInfoFromServer();
				
				//se para el indicador de carga
				onButtonChargingData(false);
				
			} else if (URLMethod.equals(Constants.POST)) { // si el método es
															// POST, es que
															// hemos creado algo
				onButtonChargingData(false);
				try {
						
					if (result != null) {
						if (result.get("result").equals("OK")) {
	
							if(actionElem.equals(Constants.COMMENT))
								Toast.makeText(
									act,
									act.getString(R.string.json_status_comment_create_OK),
									Toast.LENGTH_LONG).show();
							else if(actionElem.equals(Constants.FEATURED))
								Toast.makeText(
										act,
										act.getString(R.string.featured_OK),
										Toast.LENGTH_LONG).show();
	
						} else {
							if(actionElem.equals(Constants.COMMENT))
								Toast.makeText(
									act,
									act.getString(R.string.json_status_comment_create_KO),
									Toast.LENGTH_LONG).show();
							else if(actionElem.equals(Constants.FEATURED))
								Toast.makeText(
										act,
										act.getString(R.string.featured_KO),
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
			} else if (URLMethod.equals(Constants.PUT)) {
				onButtonChargingData(false);
				try {
						
					if (result != null) {
						if (result.get(Constants.RESULT).equals(Constants.OK)) {
							Toast.makeText(act, getString(R.string.json_status_edit_object_OK), Toast.LENGTH_LONG).show();
							getInfoFromServer();
						}
						else
							Toast.makeText(act, getString(R.string.json_status_edit_object_KO), Toast.LENGTH_LONG).show();
					}
				}catch(JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public void onButtonChargingData(boolean estate) {
		setProgressBarIndeterminateVisibility(estate);

	}

	/**
	 * Método encargado de recoger del fragmento la pulsación de crear un nuevo
	 * elemento para el bolsillo
	 */
	public void onButtonNewItemClicked() {

		actionElem = Constants.ITEM; 
		
		// se crea el intent para introducir los elementos
		Intent selectIntent = new Intent(this,
				SelectNewElementTypeActivity.class);
		String id_pocket = DataYourPocket.getList_pockets()
				.get(mViewPager.getCurrentItem()).getId_pocket();
		selectIntent.putExtra(Constants.POCKET_ID, id_pocket);
		selectIntent.putExtra(Constants.SINGLE_TO_NEW_ITEM_N_POCKET, mViewPager.getCurrentItem());
		startActivityForResult(selectIntent,
				Constants.SELECT_NEW_ELE_ACTIVITY_OK);

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
		transaction.replace(R.id.containerYourPocketsBig, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
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
	public void OnButtonCancelCommentClicked() {

		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.remove(newFragment);
		transaction.commit();

	}

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
			con.addParam(Constants.NICK,
					preferences.getString(Constants.PREFERENCES_LOGIN_NICK, ""));
			con.addParam(Constants.POCKET_ID,
					DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem())
							.getId_pocket());
			con.addParam(Constants.COMMENT_TEXT, text);

			con.execute();

			// se actualizan los bolsillos
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
		// se envía el comentario
		// se cargan las preferencias
		

		// en primer lugar se comprueba si el bolsillo es destacado o no
		if (!DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem())
				.isFeatured()) {
			setFeaturedToNet(true);
			
		} else { 
			setFeaturedToNet(false);
		}

		// se actualizan los bolsillos
		getInfoFromServer();

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
						DataYourPocket.getList_pockets()
								.get(mViewPager.getCurrentItem()).getId_pocket());
				con.execute();
			}
			else{
				// se crea la conexion
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						this, Constants.DELETE);

				// se definen los parámetros para la conexión
				System.out.println(Constants.API_DELETEFEATURED +DataYourPocket.getList_pockets()
						.get(mViewPager.getCurrentItem()).getFeatured_id()
						+Constants.API_FORMAT_JSON);
				con.defineURL(Constants.API_DELETEFEATURED +DataYourPocket.getList_pockets()
						.get(mViewPager.getCurrentItem()).getFeatured_id()
						+Constants.API_FORMAT_JSON);
				con.execute();
			}
		}
		else{ //si no hay conexión, se avisa al usuario
    		
	    	Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case (Constants.SELECT_NEW_ELE_ACTIVITY_OK):
			
			Toast.makeText(this, getString(R.string.uploading), Toast.LENGTH_LONG).show();
			Intent intent2 = new Intent(this, UploadService.class);
			
	    	// Create a new Messenger for the communication back
	    	Messenger messenger = new Messenger(handler);
	    	intent2.putExtra(Constants.MESSENGER, messenger);
	    	intent2.putExtra(Constants.FILE, DataObjectToNet.getList_objects().get(resultCode));
	    	startService(intent2);
	    
			break;
			
		case (Constants.UPDATE_POCKET_OK):
			getInfoFromServer();
			break;
			
		case (Constants.NEW_POCKET_ACTIVITY_OK):
			getInfoFromServer();
			break;
		case (Constants.EDIT_POCKET_ACTIVITY_OK):
			getInfoFromServer();
			break;
		}	
	}

	
	/**
	 * Manejador para subir elementos a la red. Este se encargará de recibir los
	 * mensajes que llegand desde la actividad que se encarga de subir los objetos
	 */
	private Handler handler = new Handler() {
	    public void handleMessage(Message message) {
	    	System.out.println("MENSAJE "+message.arg1);
	    	System.out.println(message.arg1+" "+Constants.MSG_OK);
	    	System.out.println(message.arg1==Constants.MSG_OK);
	      if (message.arg1 == Constants.MSG_OK) {
	    	  //TODO hay que quitar los Toast
	        Toast.makeText(act,
	            "upload OK", Toast.LENGTH_LONG)
	            .show();
	        getInfoFromServer();
	      } else {
	        Toast.makeText(act, "upload FAIL",
	            Toast.LENGTH_LONG).show();
	      }

	    };
	  };


	 /**
	 * Método que se es llamado cuando se quiere realizar una acción sobre uno de 
	 * los elementos del GRID. 
	 * @param object entero con el número del elemento en el grid
	 */
	public void onButtonGridObjectClicked(int object) {
		
		Intent view_obj_intent = null;
		
		if(DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.IMAGE)){
			view_obj_intent = new Intent(this,ViewObjectImageActivity.class);
		}
		else if(DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.MUSIC)){
			view_obj_intent = new Intent(this,ViewObjectSoundActivity.class);
		}
		else if(DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.TEXT)){
			view_obj_intent = new Intent(this,ViewObjectTextActivity.class);
		}
		else if(DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object).getType().equals(Constants.URL)){
			view_obj_intent = new Intent(this,ViewObjectURLActivity.class);;
		}
	
		if(view_obj_intent != null)
		{
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT, DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object));
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT_PARENT, Constants.SINGLE_YOUR);
			view_obj_intent.putExtra(Constants.SINGLE_TO_VIEW_OBJECT_N_PARENT,mViewPager.getCurrentItem());
			this.startActivityForResult(view_obj_intent,
				Constants.VIEW_OBJECT_ACTIVITY_OK);
			
		}
		else
			Toast.makeText(this, getString(R.string.brokenobject), Toast.LENGTH_LONG).show();
	}

	public void onButtonDeleteObject(int object) {
		// TODO Auto-generated method stub
		if(ConnectionTest.haveInternet(this)){
		actionElem = Constants.OBJECT;
			ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
					act, Constants.DELETE);
	
			con.defineURL(Constants.API_DELETEOBJECT
					+ DataYourPocket
							.getList_pockets()
							.get(mViewPager
									.getCurrentItem()).getObjects().get(object).getId_objet()
					+ Constants.API_FORMAT_JSON);
			System.out.println(con.getURL());
			con.execute();
		} else
			Toast.makeText(this,getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG)
					.show();
	}

	
	public void onButtonEditObject(int object) {
		
		Intent intent2 = new Intent(this, EditObjectActivity.class);
		
    	// Create a new Messenger for the communication back
    	intent2.putExtra(Constants.OBJECT_TO_EDIT, DataYourPocket.getList_pockets().get(mViewPager.getCurrentItem()).getObjects().get(object));
    	
    	startActivityForResult(intent2, Constants.EDIT_POCKET_ACTIVITY_OK);

		
	}

	
	
}
