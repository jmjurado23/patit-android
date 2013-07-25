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
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.pocket.patit.R;
import com.pocket.patit.activities.DescriptionPocketActivity;
import com.pocket.patit.activities.MainPocketsActivity;
import com.pocket.patit.activities.SponsoredPocketsActivity;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeSponsoredPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataSponsoredPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.views.GridPocketsAdapter;

/**
 * {@link SponsoredPocketsFragments}
 * 
 * Fragmento que mustra un Grid o cuadrícula con los bolsillos esponsorizados que el servidor nos
 * sirva. Este grid se caracteriza por elementos de color vere.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SponsoredPocketsFragments  extends Fragment implements OnItemClickListener{
	
	private GridView gridview;							//grid
	private List<Pocket> list_pocket;					//lista de bolsillos
	private AdapterView.AdapterContextMenuInfo info;	//información superior
	private Activity act;								//actividad
	private Context context;							//contexto
	private ProgressDialog dialog;						//diálogo
	private MainPocketsActivity acti;					//actividad
	private CallbackChargingSponsoredData mCallbackChargingData;	
	private Button refresh;								//refrescar
	private int selected_pocket_position;				//posición
	private String actionElem;							//acción

	
	public SponsoredPocketsFragments(MainPocketsActivity mainPocketsActivity) {
		super();
		this.acti = mainPocketsActivity;
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
				Toast.makeText(getActivity(), "Register an user first",Toast.LENGTH_LONG).show();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
	    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    }
		
	}
	
	private void getSponsoredPocketFromServer() {
		
		actionElem = Constants.FEATURED;
		//se comprueba que hay conexión
		
		if(ConnectionTest.haveInternet(getActivity())){
			// se definen los parámetros para la conexión
			ConnectionServerSponsoredPocket con2 = new ConnectionServerSponsoredPocket(
					getActivity(), Constants.GET);
	
			con2.defineURL(Constants.API_GET_SPONSORED_POCKET );
			con2.execute();
		}
		else{ //si no hay conexión, se avisa al usuario
	    		
	    	Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
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
					DataSponsoredPocket.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(spon_intent,
					Constants.DESCRIPTION_POCKET_OK);
			return true;

		case R.id.b_item: // edit
			System.out.println(info.position);
			return true;

		}
		return super.onContextItemSelected(item);
	}
	

	/**
	 * No usada ahora mismo
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
		Intent sponsored_intent = new Intent(this.getActivity(),
				SponsoredPocketsActivity.class);
		sponsored_intent.putExtra(Constants.MAIN_SPONSORED_TO_SINGLE_SPONSORED, arg2);
		this.getActivity().startActivityForResult(sponsored_intent,
				Constants.FEATURED_POCKET_ACTIVITY_OK);

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
						getSponsoredPocketFromServer();
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

	class ConnectionServerSponsoredPocket extends ConnectionServer {

		String URLMethod;

		public ConnectionServerSponsoredPocket(Activity act, String URLMethod) {
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
					ChargeSponsoredPocketFromJSON charge_pockets = new ChargeSponsoredPocketFromJSON(
							json0);

					// se cargan la lista de bolsillos nuevos
					List<Pocket> new_pockets = charge_pockets.getPockets();

					if (new_pockets != null) {
						// se almacena en la clase de los datos
						DataSponsoredPocket.setList_pockets(new_pockets);
					}

					
				}
				// se actualizan las vistas
				updateInfoGRID(DataSponsoredPocket.getList_pockets());

			} else if (URLMethod.equals(Constants.DELETE)) { // ELIMINAR

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
	public interface CallbackChargingSponsoredData {
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
					+ CallbackChargingSponsoredData.class.getName());
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
				getActivity(), 3);
			if (gridview != null) gridview.setAdapter(adapter);
		}
	}
}
