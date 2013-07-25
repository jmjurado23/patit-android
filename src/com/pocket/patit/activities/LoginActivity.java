package com.pocket.patit.activities;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;

/**
 * {@link LoginActivity}
 * 
 * Clase encargada de mostrar la información así como de realizar las acciones
 * para enviar la información de LOGIN de los usuarios en el servidor PATIT
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class LoginActivity extends Activity implements OnClickListener{

	private Button login;					//botón para hacer login
	private Button register;				//botón para registrar
	private EditText nick;					//elemento para introducir el nick
	private EditText pass;					//elemento para introducir la pass
	private Activity act;					//actividad 
	private SharedPreferences preferences;	//prefrencias de la aplicación
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
        onButtonChargingData(false);
        
        //se cargan los elementos desde el layout
        login = (Button) findViewById(R.id.cfgBtnLogin);
        register = (Button) findViewById(R.id.idBtnToRegisterAct);
        nick = (EditText) findViewById(R.id.editTextLoginNick);
        pass = (EditText) findViewById(R.id.editTextLoginPass);
        
        act =this;
        
        preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
				Context.MODE_PRIVATE);
        
        //Esto es para cargar por primera vez esta clase estática
        @SuppressWarnings("unused")
		PocketThemeData data = new PocketThemeData(this);
        
        //Comprobar si es la PRIMERA vez que se entra a la aplicación
        if(preferences.getString(Constants.PREFERENCES_FIRST_USE,Constants.TRUE).equals(Constants.FALSE)){
        	Intent Intent= new Intent(act, HomeActivity.class);
			startActivityForResult(Intent, Constants.HOME_ACTIVITY_OK);
            finish();
        }
        
        //si no es el primer uso se cargan los Nicks y pass por defecto en caso que existan
        if(!preferences.getString(Constants.PREFERENCES_LOGIN_NICK,"").equals("")){
        	nick.setText(preferences.getString( 
             		Constants.PREFERENCES_LOGIN_NICK,""));
            pass.setText(preferences.getString(
             		Constants.PREFERENCES_LOGIN_PASS,""));
        }
        
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    
    
    /**
     * Método sobrescrito para definir el menu de la actividad
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    
    
    /**
     * Método que se encarga de realizar el login con el servidor, definiendo los
     * parámetros para enviar al servidor
     */
    public void login(){
		
    	// se comprueba si hay internet
    	if(ConnectionTest.haveInternet(this)){
    		onButtonChargingData(true);
    		ConnectionServerLogin con = new ConnectionServerLogin(act, Constants.POST);
			//se definen los parámetros para la conexión
			con.defineURL(Constants.API_LOGIN);
			con.addParam(Constants.NICK, nick.getText().toString());
			con.addParam(Constants.PASS, pass.getText().toString());
			
			con.execute();
			
    	}
    	else{ //si no hay conexión, se avisa al usuario
    		
    		Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
    	}
		
    }
    
    
    
    /**
     * Método implementado para definir las acciones al pulsar los botones de la aplicación
     */
	public void onClick(View arg0) {
		
		if(arg0.equals(login)){
			//LOGIN
			login();
		}
		if(arg0.equals(register)){
			
			//REGISTRO
			Intent registerIntent= new Intent(this, RegisterActivity.class);
			startActivityForResult(registerIntent, Constants.REGISTER_ACTIVITY_OK);
            
		}	
	}
	
	public void onButtonChargingData(boolean estate) {
		setProgressBarIndeterminateVisibility(estate);
		
		}
    
	/**
	 * Clase que hereda de la clase ConnectionServer y que modifica algunos métodos
	 * añadiendo funcionalidad a algunos métodos a partir de la clase LoginActivity
	 * @author juanma
	 *
	 */
	class ConnectionServerLogin extends ConnectionServer{

		public ConnectionServerLogin(Activity act,String URLMethod) {
			super(act,URLMethod);
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			//se almacenan los datos del usuario
			try {
				
				
				if (result.get("result").equals("OK")) {
					
					//se almacenan las preferencias
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(Constants.PREFERENCES_LOGIN_NICK, result.get(Constants.NICK).toString());
					editor.putString(Constants.PREFERENCES_LOGIN_EMAIL, result.get(Constants.EMAIL).toString());
					editor.putString(Constants.PREFERENCES_LOGIN_PASS, result.get(Constants.PASSWORD).toString());
					editor.putString(Constants.PREFERENCES_ID_USER, result.get(Constants.ID).toString());
					editor.putString(Constants.PREFERENCES_FIRST_USE, Constants.FALSE);
					editor.putString(Constants.PREFERENCES_LOGIN_REGDATE, result.get(Constants.REG_DATE).toString());
					editor.putString(Constants.PREFERENCES_LOGIN_APIKEY, result.get(Constants.API_KEY).toString());
					editor.commit();
					
					onButtonChargingData(false);
					Intent Intent= new Intent(act, HomeActivity.class);
					startActivityForResult(Intent, Constants.HOME_ACTIVITY_OK);
		            finish();
					
				}
				else
					Toast.makeText(act, getString(R.string.no_correct_credentials), Toast.LENGTH_LONG).show();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Método que recoge los resultados de la actividad que finaliza.
	 */
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == Constants.REGISTER_ACTIVITY_OK) {
        	System.out.println("Código de respuesta");
            if (resultCode == Constants.RESULT_OK) {
            	System.out.println("No entramos :(");
                 	nick.setText(preferences.getString( 
                     		Constants.PREFERENCES_LOGIN_NICK,""));
                    pass.setText(preferences.getString(
                     		Constants.PREFERENCES_LOGIN_PASS,""));
                     //finish();
                 
            }
        }
    }
}
