package com.pocket.patit.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.ext_communications.ConnectionTest;

/**
 * {@link RegisterActivity}
 * 
 * Ativity para registrar a los usuarios en la aplicación Patit. Además de registrar hace
 * conexiones a la red para comprobar si el nick del usuarios está disponible
 * 
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class RegisterActivity extends Activity implements OnClickListener{

	Activity act;				//actividad
	Button registerButton;		//botón para registar
	Button nextButton;			//botón para continuar
	EditText user;				//nick de usuario
	EditText email;				//email de usuario
	EditText pass1;				//constraseña 1
	EditText pass2;				//contraseña 2
	ImageButton checkButton;	//botón para comprobar el nombre de usuario
	String action;				//acción
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        onButtonChargingData(false);
        
        act = this;
        //se cargan los elementos desde el layout
        registerButton = (Button) findViewById(R.id.cfgBtnRegister);
        user = (EditText) findViewById(R.id.editTextRegisterUser);
        email = (EditText) findViewById(R.id.editTextRegisterEmail);
        pass1 = (EditText) findViewById(R.id.editTextRegisterPass1);
        pass2 = (EditText) findViewById(R.id.editTextRegisterPass2);
        checkButton = (ImageButton) findViewById(R.id.buttonCheck);
        
        registerButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);
		
    }

    /**
     * Método para establecer el icono de carga de la parte superior
     * @param estate
     */
    public void onButtonChargingData(boolean estate) {
		setProgressBarIndeterminateVisibility(estate);

	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Clase para rellenar los datos de conexión con el servidor. En esta ocasión los datos 
     * serán para hacer un registro de los datos del usuario o comprobar
     * el nombre del usaurio.
     * 
     * @author Juan Manuel Jurado
     *
     */
    class ConnectionServerRegister extends ConnectionServer{
    	
		public ConnectionServerRegister(Activity act,String URLMethod) {
			super(act, URLMethod);
			
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			//se oculta el diálogo de carga
			onButtonChargingData(false);
			
			try {
				if(action.equals(Constants.REGISTER)){ 
					if(result.get(Constants.RESULT).equals(Constants.OK)){
						
						//si se ha producido el registro correctamente se pasa a almacenar
						//en las preferencias dichos datos
						SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
								Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString(Constants.PREFERENCES_LOGIN_NICK, user.getText()
								.toString());
						editor.putString(Constants.PREFERENCES_LOGIN_EMAIL, email.getText()
								.toString());
						editor.putString(Constants.PREFERENCES_LOGIN_PASS, pass1.getText().toString());
						editor.putString(Constants.PREFERENCES_LOGIN_REGDATE, result.get(Constants.REG_DATE).toString());
						editor.putString(Constants.PREFERENCES_ID_USER, result.get(Constants.ID).toString());
						editor.putString(Constants.PREFERENCES_LOGIN_APIKEY, result.get(Constants.API_KEY).toString());
						editor.commit();
						
						Toast.makeText(act, act.getString(R.string.json_status_user_create_OK), Toast.LENGTH_LONG).show();
						
						//lanzamos el menu de la aplicación
						Intent Intent= new Intent(act, HomeActivity.class);
						startActivityForResult(Intent, Constants.HOME_ACTIVITY_OK);
			            finish();
			            
						act.setResult(Constants.RESULT_OK);
						act.finish();
					}
					else{
						Toast.makeText(act,act.getString(R.string.json_status_user_create_KO), Toast.LENGTH_SHORT).show();
					}
				}
				else if(action.equals(Constants.CHECK_USER)){ //comprobar el nick
					if(result!=null){
						if(result.get(Constants.NICK).equals(user.getText().toString())){
							checkButton.setImageResource(R.drawable.removeicon_color);
							Toast.makeText(act, getString(R.string.check_user_KO), Toast.LENGTH_SHORT).show();}
					}
					else{
						checkButton.setImageResource(R.drawable.accecpt_nav_color);
						Toast.makeText(act, getString(R.string.check_user_OK), Toast.LENGTH_SHORT).show();
					}
						
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
	}

    /**
     * Método encargado de gestionar las pulsaciones en los botones
     */
	public void onClick(View arg0) {
		if(arg0.equals(registerButton)){
			
				register();	
		}
		else if(arg0.equals(checkButton)){
			checkUser();
		}
		
	}
	
	/**
	 * Método que comprueba los campos de user u de email, para indicar si son aptos o no para 
	 * el registro del usuario
	 * @return
	 */
	private boolean testUserPassEditText() {
		if((user.getText().toString()=="")){
			Toast.makeText(act, "Empty user nick", Toast.LENGTH_LONG).show();
		}
		else if((email.getText().toString() == "")){
			Toast.makeText(act, "Empty user email", Toast.LENGTH_LONG).show();
		}
		else if((pass1.getText().toString() == "") || (pass2.getText().toString() == "")){
			Toast.makeText(act, "Empty user email", Toast.LENGTH_LONG).show();
		}
		else if(!(pass1.getText().toString().equals(pass2.getText().toString()))){
			Toast.makeText(act,	"differents passwords",Toast.LENGTH_LONG).show();	
		}
		else if(pass1.getText().length() <= 5){
			Toast.makeText(act,	"Short password",Toast.LENGTH_LONG).show();	
		}
		else return true;
		
		return false;
	}

	/**
	 * Método encargado de definir los parámetros de la conexión y del registro
	 * Este método si no tiene internet, se encarga de mostrar un texto indicando que 
	 * no hay conexión
	 */
	private void register(){
		
		if(ConnectionTest.haveInternet(this)){
			
			action = Constants.REGISTER;
			ConnectionServerRegister con = new ConnectionServerRegister(this,Constants.POST);
			//se definen los parámetros para la conexión
			con.defineURL(Constants.API_REGISTER);
			con.addParam(Constants.NICK, user.getText().toString());
			con.addParam(Constants.PASS, pass1.getText().toString());
			con.addParam(Constants.EMAIL, email.getText().toString());
			
			con.execute();
			onButtonChargingData(true);
			
    	}
    	else{ //si no hay conexión, se avisa al usuario
    		
    		Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
    	}
		
	}
	
	private void checkUser(){
		
		if(ConnectionTest.haveInternet(this)){
			
			action = Constants.CHECK_USER;
			ConnectionServerRegister con = new ConnectionServerRegister(this,Constants.GET);
			//se definen los parámetros para la conexión
			con.defineURL(Constants.API_CHECKUSER+user.getText().toString());
			
			con.execute();
			onButtonChargingData(true);
			
    	}
    	else{ //si no hay conexión, se avisa al usuario
    		
    		Toast.makeText(this, getString(R.string.NO_INTERNET), Toast.LENGTH_LONG).show();
    	}
		
	}

}
