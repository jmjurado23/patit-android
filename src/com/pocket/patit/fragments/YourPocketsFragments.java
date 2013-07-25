package com.pocket.patit.fragments;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.activities.DescriptionPocketActivity;
import com.pocket.patit.activities.MainPocketsActivity;
import com.pocket.patit.activities.UpdatePocketActivity;
import com.pocket.patit.activities.YourPocketsActivity;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.ChargeFeaturedPocketFromJSON;
import com.pocket.patit.data.ChargeUserPocketFromJSON;
import com.pocket.patit.data.ComparePocketsChanges;
import com.pocket.patit.data.DataFeaturedPocket;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.data.DataToInternalMemory;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.views.GridPocketsAdapter;

/**
 * {@link YourPocketsFragments}
 * 
 * Fragmento que mustra un Grid o cuadrícula con los bolsillos del usuario
 * . Este grid se caracteriza por elementos de color azul
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class YourPocketsFragments extends Fragment implements
		OnItemClickListener {

	GridView gridview;							//grid
	AdapterView.AdapterContextMenuInfo info;	//información del menu
	Activity act;								//actividad
	ProgressDialog dialog;						//diálogo
	MainPocketsActivity acti;					//actividad
	CallbackChargingYourData mCallbackChargingData;
	int selected_pocket_position;				//elemento seleccionado
	

	public YourPocketsFragments(MainPocketsActivity mainPocketsActivity) {
		super();
		this.acti = mainPocketsActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.fragment_grid_you_ran_fea_spo_pockets, container,
				false);
		gridview = (GridView) view.findViewById(R.id.gridViewMainPocket);
		act = getActivity();

		// se cargan los contenidos de los bolsillos
		getInfoFromServer();

		// se crea el adaptador para mostrar los bolsillos
		updateInfoGRID(DataYourPocket.getList_pockets());
		
		registerForContextMenu(gridview);
		gridview.setOnItemClickListener(this);

		return view;
	}

	/**
	 * Método encargado de definir los parámetros para realizar una conexión con
	 * el servidor.
	 */
	public void getInfoFromServer() {
		try{
			//se comprueba que hay conexión
			if(ConnectionTest.haveInternet(getActivity())){
			
				ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
						getActivity(), Constants.GET);
		
				SharedPreferences preferences = getActivity().getSharedPreferences(
						Constants.PREFERENCES_LOGIN, Context.MODE_PRIVATE);
				
				String nick = preferences.getString(Constants.PREFERENCES_LOGIN_NICK,
						"");
		
				if (nick != "") {
					// se definen los parámetros para la conexión
					con.defineURL(Constants.API_GET_POCKET_USER +nick+ Constants.API_FORMAT_JSON);
					con.execute();
				} else
					Toast.makeText(getActivity(),getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG).show();
			}
			else{ //si no hay conexión, se avisa al usuario
	    		
	    		Toast.makeText(getActivity(), getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
	    	}
		}
		catch(Exception e){
			Log.e("FRAGMENT YOUR", "error change activity");
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, R.id.a_item, Menu.NONE, R.string.details);
		menu.add(Menu.NONE, R.id.b_item, Menu.NONE, R.string.edit);
		menu.add(Menu.NONE, R.id.c_item, Menu.NONE, R.string.delete);
		info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.a_item: //Detalles
			
			// se crea la actividad para mostrar los detalles del bolsillo
			Intent your_intent = new Intent(this.getActivity(),
					DescriptionPocketActivity.class);
			your_intent.putExtra(Constants.POCKET_TO_DESCRIPTION, DataYourPocket
					.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(your_intent,
					Constants.DESCRIPTION_POCKET_OK);
			
			return true;

		case R.id.b_item: //Editar
			
			Intent update_intent = new Intent(this.getActivity(),
					UpdatePocketActivity.class);
			update_intent.putExtra(Constants.POCKET_TO_UPDATE, DataYourPocket
					.getList_pockets().get(info.position));
			this.getActivity().startActivityForResult(update_intent,
					Constants.UPDATE_POCKET_OK);
			return true;

		case R.id.c_item: //Eliminar
			
			this.selected_pocket_position = info.position;
			getDialogYesNo().show();
			
			return true;
		}
		return super.onContextItemSelected(item);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("EOO" +requestCode+ " "+resultCode);
		if(resultCode == Constants.UPDATE_POCKET_OK)
			getInfoFromServer();
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

	public AlertDialog getDialogYesNo(){
		return new AlertDialog.Builder(this.getActivity())
        .setIconAttribute(android.R.attr.alertDialogIcon)
        .setTitle(R.string.deletepocketTitle)
        .setMessage(R.string.deletepocketQuestion)
        .setPositiveButton(R.string.OkDialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //si se selecciona si, se procede a eliminar el bolsillo
   
            	ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
        				getActivity(), Constants.DELETE);
            	
            	con.defineURL(Constants.API_DELETEPOCKET + DataYourPocket.getList_pockets().get(selected_pocket_position).getId_pocket().toString()
    					+ Constants.API_FORMAT_JSON);
            	System.out.println(con.getURL());
        		con.execute();
        		
        		//actualizamos la vista de los bolsillos con la info
        		getInfoFromServer();
        		
            }
        })
        .setNegativeButton(R.string.CancelDialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked Cancel so do some stuff */
            }
        })
        .create();
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent your_intent = new Intent(this.getActivity(),
				YourPocketsActivity.class);
		your_intent.putExtra(Constants.MAIN_YOUR_TO_SINGLE_YOUR, arg2);
		your_intent.putExtra(Constants.NEW_POCKET_TO_SINGLE_YOUR, "false"); 
		this.getActivity().startActivityForResult(your_intent,
				Constants.YOUR_POCKET_ACTIVITY_OK);

	}
	
	/**
	 * Clase que se encarga de enviar los nuevos datos de un bolsillo al servidor
	 * para almacenarlos en el servidor.
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

			if(URLMethod.equals(Constants.GET)){
				
				JSONObject json0 = result;
				
				if (result != null) {
					
					//Cargamos los bolsillos Destacados
					ChargeFeaturedPocketFromJSON charge_pockets_fea = new ChargeFeaturedPocketFromJSON(json0);
					System.out.println(charge_pockets_fea.isCorrect());
					if(charge_pockets_fea.isCorrect()){
						DataFeaturedPocket.setList_pockets(charge_pockets_fea.getPockets());
						DataFeaturedPocket.setId_featured_pockets(charge_pockets_fea.getIdFeaturedPockets());
						
						// cargamos la info obtenida del server en la memoria del
						// teléfono
						ChargeUserPocketFromJSON charge_pockets = new ChargeUserPocketFromJSON(
								json0);
		
						// se cargan la lista de bolsillos nuevos
						List<Pocket> new_pockets = charge_pockets.getPockets();
						boolean correctParsing = charge_pockets.isCorrect();
						
						
						//si se han recibido bolsillos y no son nulos
						if(new_pockets != null && correctParsing){
							// si había datos almacenados se comparan para resaltar los
							// cambios
							if (DataYourPocket.getList_pockets() != null) {
			
								// se crea un comparador para ver los cambios que se han
								// producido
								
								// se comprueba que DataPocket
								List<Pocket> list_pocket_update = ComparePocketsChanges.getChangesOnPockets(
										DataYourPocket.getList_pockets(), new_pockets);
			
								// se almacena en la clase de los datos
								DataYourPocket.setList_pockets(list_pocket_update);
			
							} else {
								// se almacena en la clase de los datos
								DataYourPocket.setList_pockets(new_pockets);
							}
			
							// se almacena en la memoria de preferencias
							DataToInternalMemory memory = new DataToInternalMemory(act);
							memory.saveJSONDataIntoSharedPreferences(json0);
			
							// se actualizan las vistas
							updateInfoGRID(DataYourPocket.getList_pockets());
						}
					}
					else
						Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(act, act.getString(R.string.SERVER_ERROR), Toast.LENGTH_LONG).show();
			}
			else if(URLMethod.equals(Constants.DELETE)){
				Toast.makeText(act, act.getString(R.string.deletepocket), Toast.LENGTH_LONG).show();
				
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
	public interface CallbackChargingYourData {
		public void onButtonChargingData(boolean estate);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallbackChargingData = (CallbackChargingYourData) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement " + CallbackChargingYourData.class.getName());
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
				getActivity(), 0);
			if (gridview != null) gridview.setAdapter(adapter);
		}
	}

}