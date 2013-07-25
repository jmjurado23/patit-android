package com.pocket.patit.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.activities.DescriptionPocketActivity;
import com.pocket.patit.activities.MainPocketsActivity;
import com.pocket.patit.activities.SearchPocketsActivity;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeSearchPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataSearchPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.views.GridPocketsAdapter;

/**
 * {@link SearchPocketsFragments}
 * 
 * Fragmento que mustra un Grid o cuadrícula con los bolsillos buscados. Se caracteriza por el color
 * gris y negro. 
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SearchPocketsFragments  extends Fragment implements OnItemClickListener, OnClickListener{
	
	private GridView gridview;									//grid con los bolsillos
	private List<Pocket> list_pocket;							//lista con los bolsillos
	private AdapterView.AdapterContextMenuInfo info;			//info del menu superior
	private Activity act;										//actividad
	private CallbackChargingSearchData mCallbackChargingData;	//callback del fragmento
	private int selected_pocket_position;						//posición seleccionada del fragmento
	private String actionElem;									//acción seleccionada
	private Button buttonUser;				//botón de búsqueda por usuario
	private Button buttonPocket;			//botón de búsqueda por bolsillo
	private TextView textTitle;				//título de la búsqueda
	private EditText text;					//texto de la búsqueda
	private ImageButton buttonSearch;		//botón de buscar
	private String searchType;				//tipo de búsqueda
	private String searchValue;				//valor de la búsqueda

	
	public SearchPocketsFragments(MainPocketsActivity mainPocketsActivity) {
		super();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//se obtiene la vista del fragment y se infla
		View view = inflater.inflate(R.layout.fragment_grid_search_pockets, container,
				false);
		gridview = (GridView) view.findViewById(R.id.gridViewMainPocket);
		buttonUser = (Button) view.findViewById(R.id.buttonUser);
		buttonPocket = (Button) view.findViewById(R.id.buttonPocket);
		textTitle = (TextView) view.findViewById(R.id.textViewSearch);
		text = (EditText) view.findViewById(R.id.editTextSearch);
		buttonSearch = (ImageButton) view.findViewById(R.id.imageButtonSearch);
		
		
		list_pocket = new ArrayList<Pocket>();
		act = getActivity();

		

		// se crea el adaptador para mostrar los bolsillos
		updateInfoGRID(list_pocket);
		registerForContextMenu(gridview);
		
		setViewToUser();
		ConnectionTest con = new ConnectionTest();
		
		buttonPocket.setOnClickListener(this);
		buttonSearch.setOnClickListener(this);
		buttonUser.setOnClickListener(this);
		gridview.setOnItemClickListener(this);

		return view;
	}

	/**
	 * Método para obtener la información del servidor. Bolsillos, destacados, información
	 * del usuario. Es necesario llamarla la primera y después llamar a las demás
	 */
	private void getInfoFromServer() {

		  //se comprueba que hay conexión
		if(ConnectionTest.haveInternet(getActivity())){
			
		
			SharedPreferences preferences = getActivity().getSharedPreferences(
					Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);
	
			String nick = preferences.getString(Constants.PREFERENCES_LOGIN_NICK,
					"");
	
			if (nick != "") {
				
				// se definen los parámetros para la conexión
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						getActivity(), Constants.GET);
				con.defineURL(Constants.API_GET_POCKET_USER + nick + Constants.API_FORMAT_JSON);
				con.execute();
			} else
				Toast.makeText(getActivity(), "Register an user first",Toast.LENGTH_LONG).show();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
	    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    }
		
	}
	
	private void getSearchPocketFromServer() {
		
		actionElem = Constants.SEARCH;
		//se comprueba que hay conexión
		try{
			if(ConnectionTest.haveInternet(getActivity())){
				// se definen los parámetros para la conexión
				ConnectionServerSearchPocket con2 = new ConnectionServerSearchPocket(
						getActivity(), Constants.GET);
				
				if(searchType.equals(Constants.SEARCH_TYPE_POCKET))
					con2.defineURL(Constants.API_GET_SEARCH_POCKET + convertSpaces(text.getText().toString()));
				else 
					con2.defineURL(Constants.API_GET_SEARCH_USER + convertSpaces(text.getText().toString()));
				
				searchValue =  convertSpaces(text.getText().toString());
				System.out.println(con2.getURL());
				con2.execute();
			}
			else{ //si no hay conexión, se avisa al usuario
		    		
		    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e){
			Log.e("FRAGMENT SEARCH", "error change activity");
		}
	}

	private String convertSpaces(String string) {
		return string.replaceAll(" ", "%20");
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, R.id.a_item, Menu.NONE, R.string.details);
		info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.a_item: // details

			// se crea la actividad para mostrar los detalles del bolsillo
			Intent search_intent = new Intent(this.getActivity(),
					DescriptionPocketActivity.class);
			search_intent.putExtra(Constants.POCKET_TO_DESCRIPTION,
					DataSearchPocket.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(search_intent,
					Constants.DESCRIPTION_POCKET_OK);
			return true;

		case R.id.b_item: // edit
			System.out.println(info.position);
			return true;

		}
		return super.onContextItemSelected(item);
	}
	

	/**
	 * No usada ahora mismo. se usará para seleccionar varios elementos de la cuadrícula
	 * 
	 * @author juanma
	 * 
	 */
	public class MultiChoiceModeListener implements
			GridView.MultiChoiceModeListener {

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Select Items");
			mode.setSubtitle("One item selected");
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return true;
		}

		public void onDestroyActionMode(ActionMode mode) {
		}

		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			int selectCount = gridview.getCheckedItemCount();
			switch (selectCount) {
			case 1:
				mode.setSubtitle("One item selected");
				break;
			default:
				mode.setSubtitle("" + selectCount + " items selected");
				break;
			}
		}

	}

	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent search2_intent = new Intent(this.getActivity(),
				SearchPocketsActivity.class);
		search2_intent.putExtra(Constants.MAIN_SEARCH_TO_SINGLE_SEARCH, arg2);

		SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SEARCH,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Constants.SEARCH_TYPE,searchType);
		editor.putString(Constants.SEARCH_VALUE,searchValue);
		editor.commit();
		this.getActivity().startActivityForResult(search2_intent,
				Constants.SEARCH_POCKET_ACTIVITY_OK);

	}

	/**
	 * Clase para obtener la información del usuario
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
			mCallbackChargingData.onButtonChargingData(true);
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
						DataYourPocket.setList_pockets(new_pockets);
	
						// se almacena en la memoria de preferencias
						DataToInternalMemory memory = new DataToInternalMemory(act);
						memory.saveJSONDataIntoSharedPreferences(json0);
						
						//se piden los bolsillos sponsorizados
						getSearchPocketFromServer();
					}
					else
						Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
					// se para la imagen de carga
					mCallbackChargingData.onButtonChargingData(false);
			}
		}

	}

	class ConnectionServerSearchPocket extends ConnectionServer {

		String URLMethod;

		public ConnectionServerSearchPocket(Activity act, String URLMethod) {
			super(act, URLMethod);
			this.URLMethod = URLMethod;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mCallbackChargingData.onButtonChargingData(true);
		}

		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			if (URLMethod.equals(Constants.GET)) {

				JSONObject json0 = result;
				
				if (result != null) {
					// cargamos la info obtenida del server en la memoria del
					// teléfono
					
					ChargeSearchPocketFromJSON charge_pockets = null;
					if(searchType.equals(Constants.SEARCH_TYPE_POCKET))
						charge_pockets = new ChargeSearchPocketFromJSON(json0,1);
					else 
						charge_pockets = new ChargeSearchPocketFromJSON(json0,0);
					// se cargan la lista de bolsillos nuevos
					List<Pocket> new_pockets = charge_pockets.getPockets();

					if (new_pockets != null && !new_pockets.isEmpty()) {
						// se almacena en la clase de los datos
						DataSearchPocket.setList_pockets(new_pockets);
					}
					else{
						DataSearchPocket.setList_pockets(new ArrayList<Pocket>());
						Toast.makeText(act, act.getString(R.string.no_result_search), Toast.LENGTH_LONG).show();
					}
					// se actualizan las vistas
					updateInfoGRID(DataSearchPocket.getList_pockets());
				}
				else{
					DataSearchPocket.setList_pockets(new ArrayList<Pocket>());
					Toast.makeText(act, act.getString(R.string.no_result_search), Toast.LENGTH_LONG).show();
				}
				

			} 
			mCallbackChargingData.onButtonChargingData(false);

		}

	}

	/**
	 * Interfaz para mandar información a la actividad padre de los fragmentos
	 * en este caso se usa para actualizar el icono de carga en la action bar
	 * 
	 * @author juanma
	 * 
	 */
	public interface CallbackChargingSearchData {
		public void onButtonChargingData(boolean estate);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallbackChargingData = (CallbackChargingSearchData) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement "
					+ CallbackChargingSearchData.class.getName());
		}
	}
	
	/**
	 * Método para actualizar el grid de los elementos con la información de 
	 * una lista de bolsillos
	 * @param list
	 */
	private void updateInfoGRID(List<Pocket> list){
		if(list!=null){
			GridPocketsAdapter adapter = new GridPocketsAdapter(
				getActivity(), list,
				getActivity(), 4);
			if (gridview != null) gridview.setAdapter(adapter);
		}
	}


	public void onClick(View v) {
		if(v.equals(buttonUser)){
			setViewToUser();
		}
		else if(v.equals(buttonPocket)){
			setViewToPocket();
		}
		else if(v.equals(buttonSearch)){
			getInfoFromServer();
		}
		
	}

	/**
	 * Método que cambia la interfaz para buscar bolsillos
	 */
	private void setViewToPocket() {
		buttonUser.setBackgroundResource(R.drawable.darkgreybutton);
		buttonUser.setTextColor(Color.GRAY);
		buttonPocket.setBackgroundResource(R.drawable.bluebutton);
		buttonPocket.setTextColor(Color.BLACK);
		textTitle.setText(R.string.pocket_name);
		searchType = Constants.SEARCH_TYPE_POCKET;
	}

	/**
	 * Método que cambia la interfaz para buscar usuarios
	 */
	private void setViewToUser() {
		
		buttonPocket.setBackgroundResource(R.drawable.darkgreybutton);
		buttonPocket.setTextColor(Color.GRAY);
		buttonUser.setBackgroundResource(R.drawable.bluebutton);
		buttonUser.setTextColor(Color.BLACK);
		textTitle.setText(R.string.usernameDescObject);
		searchType = Constants.SEARCH_TYPE_USER;
	}


	
}
