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
import com.pocket.patit.activities.MainPocketsActivity;
import com.pocket.patit.activities.RandomPocketsActivity;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeRandomPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataRandomPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.fragments.SponsoredPocketsFragments.CallbackChargingSponsoredData;
import com.pocket.patit.views.GridPocketsAdapter;

/**
 * {@link RandomPocketsFragments}
 * 
 * Fragmento que mustra un Grid o cuadrícula con los bolsillos aleatorios que el servidor nos
 * sirva. Este grid se caracteriza por elementos de color rojo.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class RandomPocketsFragments extends Fragment implements OnItemClickListener{
	
	private GridView gridview;							//grid con los bolsillos aleatorios
	private List<Pocket> list_pocket;					//lista con bolsillos
	private AdapterView.AdapterContextMenuInfo info;	//info fe la barra superior
	private Activity act;								//actividad
	private ProgressDialog dialog;						//diálogo
	private CallbackChargingSponsoredData mCallbackChargingData;	
	private Button refresh;								//botón de refrescar
	private int selected_pocket_position;				//bolsillo seleccionado
	private String actionElem;							//acción que se realiza

	/**
	 * Constructor parametrizado
	 * @param mainPocketsActivity
	 */
	public RandomPocketsFragments(MainPocketsActivity mainPocketsActivity) {
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
		updateInfoGRID(list_pocket);
		registerForContextMenu(gridview);
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
				Toast.makeText(getActivity(), getString(R.string.NO_USER_PREF),Toast.LENGTH_LONG).show();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
	    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    }
		
	}
	
	/**
	 * Método para definir los parámetros de la conexión para obtener los bolsillos
	 * aleatorios
	 */
	private void getRandomPocketFromServer() {
		
		setActionElem(Constants.FEATURED);
		
		try{
			
			
			//se comprueba que hay conexión
			if(ConnectionTest.haveInternet(getActivity())){
				// se definen 	los parámetros para la conexión
				ConnectionServerRandomPocket con2 = new ConnectionServerRandomPocket(
						getActivity(), Constants.GET);
		
				con2.defineURL(Constants.API_GET_RANDOM_POCKET );
				con2.execute();
			}
			else{ //si no hay conexión, se avisa al usuario
	    		
		    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
		    }
		}
		catch(Exception e){
			Log.e("FRAGMENT RANDOM", "error change activity");
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
		case R.id.a_item: // details

			// se crea la actividad para mostrar los detalles del bolsillo
			Intent spon_intent = new Intent(this.getActivity(),
					DescriptionPocketActivity.class);
			spon_intent.putExtra(Constants.POCKET_TO_DESCRIPTION,
					DataRandomPocket.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(spon_intent,
					Constants.DESCRIPTION_POCKET_OK);
			return true;

		}
		return super.onContextItemSelected(item);
	}
	

	/**
	 * Servirá para seleccionar varios bolsillos. No usada ahora mismo
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
		Intent random_intent = new Intent(this.getActivity(),
				RandomPocketsActivity.class);
		random_intent.putExtra(Constants.MAIN_RANDOM_TO_SINGLE_RANDOM, arg2);
		this.getActivity().startActivityForResult(random_intent,
				Constants.RANDOM_POCKET_ACTIVITY_OK);

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
						getRandomPocketFromServer();
					}
					else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				
			}
			mCallbackChargingData.onButtonChargingData(false);
		}

	}

	class ConnectionServerRandomPocket extends ConnectionServer {

		String URLMethod;

		public ConnectionServerRandomPocket(Activity act, String URLMethod) {
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
					ChargeRandomPocketFromJSON charge_pockets = new ChargeRandomPocketFromJSON(
							json0);

					// se cargan la lista de bolsillos nuevos
					List<Pocket> new_pockets = charge_pockets.getPockets();

					if (new_pockets != null) {
						// se almacena en la clase de los datos
						DataRandomPocket.setList_pockets(new_pockets);
						// se actualizan las vistas
						GridPocketsAdapter adapter = new GridPocketsAdapter(
								getActivity(), DataRandomPocket.getList_pockets(),
								getActivity(), 1);
						
						if (gridview != null)
							gridview.setAdapter(adapter);
					}

				}
				
				// se para la imagen de carga
				mCallbackChargingData.onButtonChargingData(false);

			} 

		}

	}

	/**
	 * Interfaz para mandar información a la actividad padre de los fragmentos
	 * en este caso se usa para actualizar el icono de carga en la action bar
	 * 
	 * @author juanma
	 * 
	 */
	public interface CallbackChargingRandomData {
		public void onButtonChargingData(boolean estate);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallbackChargingData = (CallbackChargingSponsoredData) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement "
					+ CallbackChargingRandomData.class.getName());
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
				getActivity(), 1);
			if (gridview != null) gridview.setAdapter(adapter);
		}
	}


	/**
	 * @return the refresh
	 */
	public Button getRefresh() {
		return refresh;
	}


	/**
	 * @param refresh the refresh to set
	 */
	public void setRefresh(Button refresh) {
		this.refresh = refresh;
	}


	/**
	 * @return the selected_pocket_position
	 */
	public int getSelected_pocket_position() {
		return selected_pocket_position;
	}


	/**
	 * @param selected_pocket_position the selected_pocket_position to set
	 */
	public void setSelected_pocket_position(int selected_pocket_position) {
		this.selected_pocket_position = selected_pocket_position;
	}


	/**
	 * @return the actionElem
	 */
	public String getActionElem() {
		return actionElem;
	}


	/**
	 * @param actionElem the actionElem to set
	 */
	public void setActionElem(String actionElem) {
		this.actionElem = actionElem;
	}


	/**
	 * @return the dialog
	 */
	public ProgressDialog getDialog() {
		return dialog;
	}


	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}
}
