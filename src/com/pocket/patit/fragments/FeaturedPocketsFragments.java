package com.pocket.patit.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pocket.patit.R;
import com.pocket.patit.activities.DescriptionPocketActivity;
import com.pocket.patit.activities.FeaturedPocketsActivity;
import com.pocket.patit.activities.MainPocketsActivity;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.views.GridPocketsAdapter;

/**
 * {@link FeaturedPocketsFragments}
 * 
 * Fragmento que mustra un Grid o cuadrícula con los bolsillos que el usuario
 * tiene destacados. Este grid se caracteriza por elementos de color amarillo.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class FeaturedPocketsFragments extends Fragment implements
		OnItemClickListener {

	GridView gridview;							//grid con los bolsillos
	List<Pocket> list_pocket;					//lista con los bolsillos destacados
	AdapterView.AdapterContextMenuInfo info;	//adaptador con información de navegación
	Activity act;								//actividad
	Context context;							//contexto de la act
	ProgressDialog dialog;						//diálogo
	Button refresh;								//botón de refrescar
	int selected_pocket_position;				//elemento seleccionado
	String actionElem;							//accion
	CallbackChargingFeaturedData mCallbackChargingData;

	
	public FeaturedPocketsFragments(MainPocketsActivity mainPocketsActivity) {
		super();
		this.act = mainPocketsActivity;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//se obtiene la vista del fragment y se infla
		View view = inflater.inflate(R.layout.fragment_grid_you_ran_fea_spo_pockets, container,
				false);
		gridview = (GridView) view.findViewById(R.id.gridViewMainPocket);
		list_pocket = new ArrayList<Pocket>();
		act = getActivity();

		// se cargan los contenidos de los bolsillos
		getInfoFromServer();

		// se crea el adaptador para mostrar los bolsillos
		updateInfoGRID(DataFeaturedPocket.getList_pockets());
				
		registerForContextMenu(gridview);
		gridview.setOnItemClickListener(this);

		return view;
	}

	/**
	 * Método para obtener la información del servidor
	 */
	private void getInfoFromServer() {
		
		try{

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
					Toast.makeText(getActivity(),getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG).show();
				
			}
			else{ //si no hay conexión, se avisa al usuario
	    		
	    		Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    	}
		}
		catch(Exception e){
			Log.e("FRAGMENT FEATURED", "error change activity");
		}
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
		
		case R.id.a_item: // Detalles

			// se crea la actividad para mostrar los detalles del bolsillo
			Intent featured_intent = new Intent(this.getActivity(),
					DescriptionPocketActivity.class);
			featured_intent.putExtra(Constants.POCKET_TO_DESCRIPTION,
					DataFeaturedPocket.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(featured_intent,
					Constants.DESCRIPTION_POCKET_OK);
			return true;

		case R.id.b_item: // Editar
			System.out.println(info.position);
			return true;

		}
		return super.onContextItemSelected(item);
	}
	

	/**
	 * Clase para seleccionar varios elementos del grid. Se usará en futuras versiones
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

	/**
	 * ;Método listener que es lanzado al presionar un elemento del grid
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent featured_intent = new Intent(this.getActivity(),
				FeaturedPocketsActivity.class);
		featured_intent.putExtra(Constants.MAIN_FEATURED_TO_SINGLE_FEATURED, arg2);
		this.getActivity().startActivityForResult(featured_intent,
				Constants.FEATURED_POCKET_ACTIVITY_OK);

	}

	/**
	 * Método para realizar un conexión al servidor y actualizar los datos de los bolsillos
	 * 
	 * @author Juan Manuel Jurado
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
					//cargamos bolsillos Destacados
					ChargeFeaturedPocketFromJSON charge_pockets_fea = new ChargeFeaturedPocketFromJSON(json0);
					
					if(charge_pockets_fea.isCorrect()){
						DataFeaturedPocket.setList_pockets(charge_pockets_fea.getPockets());
						DataFeaturedPocket.setId_featured_pockets(charge_pockets_fea.getIdFeaturedPockets());
						
						// cargamos la info obtenida del server en la memoria 
						ChargeUserPocketFromJSON charge_pockets = new ChargeUserPocketFromJSON(
								json0);
	
						// se cargan la lista de bolsillos nuevos
						List<Pocket> new_pockets = charge_pockets.getPockets();
						boolean correctParsing = charge_pockets.isCorrect();
						
						
						//si se han recibido bolsillos y no son nulos
						if(new_pockets != null && correctParsing){
							DataYourPocket.setList_pockets(new_pockets);
	
							// se almacena en la memoria de preferencias
							DataToInternalMemory memory = new DataToInternalMemory(act);
							
							memory.saveJSONDataIntoSharedPreferences(json0);
							
						}
						
						// se crea el adaptador para mostrar los bolsillos
						updateInfoGRID(DataFeaturedPocket.getList_pockets());
					}
					else
							Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
	
			}
			else if (URLMethod.equals(Constants.DELETE)) { // ELIMINAR

				if (actionElem.equals(Constants.FEATURED))
					Toast.makeText(act, act.getString(R.string.unfeatured),
							Toast.LENGTH_LONG).show();

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
	public interface CallbackChargingFeaturedData {
		public void onButtonChargingData(boolean estate);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallbackChargingData = (CallbackChargingFeaturedData) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement "
					+ CallbackChargingFeaturedData.class.getName());
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
				getActivity(), 2);
			if (gridview != null) gridview.setAdapter(adapter);
		}
	}
}
