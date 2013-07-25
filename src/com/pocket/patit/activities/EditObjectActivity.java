package com.pocket.patit.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;
import com.pocket.patit.fragments.SelectPocketFragment;
import com.pocket.patit.fragments.SelectPocketFragment.CallbackPocketEditObject;

/**
 * {@link EditObjectActivity}
 * 
 * Clase encargada de modificar los datos de un objeto. Los datos que se modifican son el nombre, 
 * la descripción y el bolsillo en el que se encuentran. La clase se encarga de hacer una conexión 
 * asíncrona con el servidor.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class EditObjectActivity extends Activity implements OnClickListener, CallbackPocketEditObject  {


	EditText textName; 			//nombre del objeto
	EditText textDescription; 	//descripción del objeto
	TextView preview; 			//texto con una prewien del bolsillo usado
	TextView textpocket;		//texto con el bolsillo que guarda al objeto
	ImageButton editNpocket;	//Imagen de editar el bolsillo
	Button imgPreview; 			//botón de previsualización del bolsillo que guarda al objeto
	Button sendObject; 			//botón para enviar los cambios al servidor
	String type; 				//tipo de bolsillo
	Object obj; 				//objeto que se desea modificar
	Pocket pocket; 				//bolsillos que contiene al objeto
	String textprev; 			//preview del texto del bolsillo
 	String idpocket; 			//identificador del bolsillo 
	String actionElem; 			//elemento auxiliar para enviar el objeto
	Context context; 			//contexto de la aplicación
	SelectPocketFragment selectPocketFragment; 	//fragmento para selecionar bolsillo



	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.activity_edit_object);
	        
	    onButtonChargingData(false);
	    
	    //se cargan los elemetos desde el layout
		
		textDescription = (EditText) findViewById(R.id.editTextDescriptionObject);
		textpocket = (TextView) findViewById(R.id.textViewPocketEditObject);
		preview = (TextView) findViewById(R.id.textViewPreviewNewObject);
		editNpocket = (ImageButton) findViewById(R.id.buttonEditPocket);
		imgPreview = (Button) findViewById(R.id.buttonEditObjectType);
		textName = (EditText) findViewById(R.id.editTextNameObject);
		sendObject = (Button) findViewById(R.id.cfgBtnNewObject);
		
		//se almacena el contexto
		context = this;
		//se obtiene el objeto desde el bundle de la aplicación
		obj = (Object) this.getIntent().getExtras().get(Constants.OBJECT_TO_EDIT);
		
		//si el objeto existe
		if(obj!=null){
			
			//se rellenan los campos con los valores que tenía el objeto
			textName.setText(obj.getName());
			textDescription.setText(obj.getDescript());
			type = obj.getType();
			textprev = obj.getUrl();
			preview.setText(obj.getUrl());
			
			//se obtiene la lista con los bolsillos del usuario para mostralos en una lista
			for(int i=0;i<DataYourPocket.getList_pockets().size();i++)
				if(DataYourPocket.getList_pockets().get(i).getId_pocket().equals(obj.getId_pocket()))
					{
						pocket = DataYourPocket.getList_pockets().get(i);
						textpocket.setText(DataYourPocket.getList_pockets().get(i).getName());
					}
		
			//se obtiene el bolsillo para sacar los datos necesarios
			idpocket = pocket.getId_pocket(); 
			
			//se selecciona una imagen con el tipo de objeto
			if (type.equals(Constants.IMAGE)){
				imgPreview.setBackgroundResource(R.drawable.pictureicon);
			}
			else if(type.equals(Constants.MUSIC)){
				imgPreview.setBackgroundResource(R.drawable.musicicon);
			}
			else if(type.equals(Constants.TEXT)){
				imgPreview.setBackgroundResource(R.drawable.editicon);
			}
			else if(type.equals(Constants.URL)){
				imgPreview.setBackgroundResource(R.drawable.urlicon);
			}
			
			//se crean los listener de los botones
			editNpocket.setOnClickListener(this);
			sendObject.setOnClickListener(this);
		}
		
	}
	 
	 /**
	  * Método encargada de mostrar u ocultar el dibujo de carga de la página
	  * @param estate
	  */
	public void onButtonChargingData(boolean estate) {
			setProgressBarIndeterminateVisibility(estate);
	}

	/**
	 * Método que define los comportamientos de los comportamientos de los click en
	 * los botones
	 */
	public void onClick(View arg0) {
		if (arg0.equals(sendObject)) { //si se clicka en enviar objeto
			if (!textName.getText().toString().equals("")) { //si se ha escrito el nombre
				
				// se crea la conexión de tipo PUT con los datos modificados
				if(ConnectionTest.haveInternet(this)){
					
						actionElem = Constants.OBJECT;
						ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(
								this, Constants.PUT);
				
						//se define el tipo de la conexión
						con.defineURL(Constants.API_UPDATEOBJECT
								+ obj.getId_objet()
								+ Constants.API_FORMAT_JSON);
						
						//se definen los parámetros de la conexión
						con.addParam(Constants.OBJECT_ID_OBJECT, obj.getId_objet());
						con.addParam(Constants.OBJECT_ID_POCKET, pocket.getId_pocket());
						con.addParam(Constants.OBJECT_NAME,textName.getText().toString());
						con.addParam(Constants.OBJECT_DESCRIPTION,textDescription.getText().toString() );
						SharedPreferences pref = getSharedPreferences(Constants.PREFERENCES_LOGIN,
								Context.MODE_PRIVATE);
						con.addParam(Constants.ID_USER, pref.getString(Constants.PREFERENCES_ID_USER, ""));
						
						//se ejecuta la conexión
						con.execute();
						
					} else
						Toast.makeText(this,getString(R.string.NO_USER_PREF), Toast.LENGTH_LONG)
								.show();

			}
		}
		if (arg0.equals(editNpocket)){ //si se pulsa editar el bolsillo al que pertenece el objeto
			
			//se define el fragmento a mostrar con los bolsillos a los que se puede mover
			selectPocketFragment = new SelectPocketFragment();
			FragmentTransaction transaction;
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, selectPocketFragment);

			// Se ejecuta la transición para mostrar el fragmento
			transaction.commit();
		}

	}
	
	/**
	 * Método que recoge la acción del callback del fragmento SelectPocket
	 */
	public void onButtonBClicked(int pospocket) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.remove(selectPocketFragment);
	    transaction.commit();
	    
	    //se definen los parámetros que ha devuelto el fragmento
	    textpocket.setText(DataYourPocket.getList_pockets().get(pospocket).getName());
	    pocket = DataYourPocket.getList_pockets().get(pospocket);
	}
	
	/**
	 * Clase usada para enviar una petición de modificación de los parámetros de un 
	 * objeto de Patit. Esta clase hereda de ConnectionServer para usar las propiedades 
	 * asíncronas de esta. 
	 * @author Juan Manuel Jurado
	 *
	 */
	class ConnectionServerUpdatePocket extends ConnectionServer {

		String URLMethod; //Método POST,GET,PUT,REMOVE

		/**
		 * Constructor parametrizado de la clase
		 * @param act contexto de la actividad para realizar acciones
		 * @param URLMethod Método POST,GET,PUT,REMOVE
		 */
		public ConnectionServerUpdatePocket(Activity act, String URLMethod) {
			super(act, URLMethod);
			this.URLMethod = URLMethod;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onButtonChargingData(true);
		}

		/**
		 * Método sobreescrito que define el comportamiento al recibir la respuesta del servidor
		 */
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			/**
			 * Si se ha mandado una petición con PUT
			 */
			if (URLMethod.equals(Constants.PUT)) {
				
				onButtonChargingData(false); //se para la carga de la página
				
				try {
					if (result != null) { //si el resultado no es nulo se comprueba qué ha devuelto el server
						
						if (result.get(Constants.RESULT).equals(Constants.OK)) { //si el resultado es correcto
							Toast.makeText(context, getString(R.string.json_status_edit_object_OK), Toast.LENGTH_LONG).show();
							finish(); //se termina la actividad
						}
						else{ //si no es correcto, se informa del mal funcionamiento del servidor o la red
							Toast.makeText(context, getString(R.string.json_status_edit_object_KO), Toast.LENGTH_LONG).show();
						}
					}
				}catch(JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				onButtonChargingData(false);
			}
		}
	}
}