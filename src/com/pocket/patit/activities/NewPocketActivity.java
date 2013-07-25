package com.pocket.patit.activities;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.fragments.ThemesPocketsFragments;
import com.pocket.patit.fragments.ThemesPocketsFragments.CallbackThemePocket;

/**
 * {@link NewPocketActivity}
 * 
 * Clase que permite crear un bolsillo nuevo. Esta clase es una actividad y muestra también
 * un fragmento para mostrar los tipos de bolsillos que se pueden elegir
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class NewPocketActivity extends Activity implements OnClickListener,CallbackThemePocket{


	private ImageButton edit_theme;					//Imagen para hacer click y editar el tema
	private ThemesPocketsFragments newFragment;		//Fragmento para mostrar los temas
	private TextView txt;							//texto de nuevo bolsillo
	private int pos;								//posición de la lista
	private Activity act;							//actividad
	private EditText pocket_name;					//nombre del bolsillo
	private EditText pocket_description;			//descripción del bolsillo
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pocket);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        pos=0;
        //cargar los elementos desde el layout
        edit_theme = (ImageButton) findViewById(R.id.buttonEditTheme);
        txt = (TextView) findViewById(R.id.TextViewThemeNewPocket);
        pocket_name = (EditText) findViewById(R.id.editTextNewPocket);
        pocket_description = (EditText) findViewById(R.id.editTextNewPocketDesc);
        act = this;
        
        //fragment para mostrar los tipos de fragmentos de bolsillo
        newFragment = new ThemesPocketsFragments();
        
        //listener del botón para seleccionar el tema
        edit_theme.setOnClickListener(this);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_pocket, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.nextbarnewpocket:
            	
            	//se cargan las preferencias
            	SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
						Context.MODE_PRIVATE);
            	//se carga la clase con la info de los temas de los bolsillos
            	PocketThemeData datapocket = new PocketThemeData(this);
    			txt.setText(datapocket.getTheme_name_new_pocket(pos));
    			
    			
            	ConnectionServerNewPocket con = new ConnectionServerNewPocket(this,Constants.POST);
            	
				//se definen los parámetros para la conexión
				con.defineURL(Constants.API_NEWPOCKET);
				con.addParam(Constants.NICK, preferences.getString(Constants.PREFERENCES_LOGIN_NICK,""));
				con.addParam(Constants.NAME_POCKET, pocket_name.getText().toString());
				con.addParam(Constants.DESC_POCKET, pocket_description.getText().toString());
				con.addParam(Constants.THEME_POCKET, datapocket.getTheme_name_new_pocket(pos).toString());
				
				con.execute();
				
            	
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
	public void onClick(View v) {
		if(v.equals(edit_theme)){
			
			//lanzar el fragmento
			FragmentTransaction transaction;
			transaction= getFragmentManager().beginTransaction();
			transaction.replace(R.id.container_newpocket, newFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
		
	}
	

	/**
	 * Callback del fragmento de seleccionar el bolsillo
	 * @param number número del elemento seleccionado
	 */
	public void onButtonBClicked(int number) {
		if(number>=0){ //se carga el tema y los datos
			
			pos = number;
			PocketThemeData datapocket = new PocketThemeData(this);
			txt.setText(datapocket.getTheme_name_new_pocket(pos));
			txt.setBackgroundResource(datapocket.getTheme_img_resource_new_pocket(pos));
		}
		FragmentTransaction transaction;
		transaction= getFragmentManager().beginTransaction();
		transaction.remove(newFragment);
		transaction.commit();
		
		
	}
	
	/**
	* Clase que hereda de la clase ConnectionServer y que se encarga de rellenar los pará
	* metros para crear el nuevo bolsillo
	* @author Juan Manuel Jurado
	*/
	class ConnectionServerNewPocket extends ConnectionServer{
    	
    	public ConnectionServerNewPocket(Activity act,String URLMethod) {
			super(act,URLMethod);
			this.dialog = new ProgressDialog(act);
			this.dialog.setTitle(getString(R.string.uploading));
			this.dialog.setMessage(getString(R.string.dowloadingtext));
		}
		protected ProgressDialog dialog;
    
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try {
				if(result.get("result").equals("OK")){ 
					
					
					Toast.makeText(act,act.getString(R.string.json_status_pocket_create_OK),
							Toast.LENGTH_LONG).show();
					Intent yourpocketIntent= new Intent(act, MainPocketsActivity.class);
					yourpocketIntent.putExtra(Constants.HOME_TO_MAIN_FRAGMENT, Constants.YOUR_POCKETS_LIST_POSITION);
	    			startActivity(yourpocketIntent);

					finish();
				}
				else{
					Toast.makeText(act,act.getString(R.string.json_status_pocket_create_KO),
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
		}
		
	}	
}
