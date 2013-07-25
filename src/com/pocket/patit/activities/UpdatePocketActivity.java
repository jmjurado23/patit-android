package com.pocket.patit.activities;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.ext_communications.ConnectionServer;
import com.pocket.patit.fragments.ThemesPocketsFragments;
import com.pocket.patit.fragments.ThemesPocketsFragments.CallbackThemePocket;

/**
 * {@link UpdatePocketActivity}
 * 
 * Clase que actualiza los datos de un bolsillo. Se pueden modificar el nombre, la descripción y el tema del
 * bolsillo. 
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class UpdatePocketActivity extends Activity implements OnClickListener,CallbackThemePocket{


	private ImageButton edit_theme;					//Botón para editar el tema
	private ThemesPocketsFragments newFragment;		//fragmento con los temas
	private TextView txt;							//texto del tema
	private int pos;								//posición de la lista e temas
	private Pocket pocket;							//bolsillo
	private Activity act;							//actividad
	private EditText pocket_name;					//nombre del bolsillo
	private EditText pocket_description;			//descripción del bolsillo
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pocket);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        pos=0;
        edit_theme = (ImageButton) findViewById(R.id.buttonEditTheme);
        txt = (TextView) findViewById(R.id.TextViewThemeNewPocket);
        pocket_name = (EditText) findViewById(R.id.editTextNewPocket);
        pocket_description = (EditText) findViewById(R.id.editTextNewPocketDesc);
        act = this;
        
        //se comprueba cuál es el bolsillo a modificar
        pocket = (Pocket) this.getIntent().getExtras().get(Constants.POCKET_TO_UPDATE);
        
        pocket_name.setText(pocket.getName());
        pocket_description.setText(pocket.getDescription());
        
        PocketThemeData datapocket = new PocketThemeData(this);
		txt.setText(pocket.getType());
		txt.setBackgroundResource(datapocket.getTheme_img_resource_new_pocket_from_string(pocket.getType()));
        
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
        
            case android.R.id.home: //botón de navegación de la barra superior
                NavUtils.navigateUpFromSameTask(this);
                return true;
                
            case R.id.nextbarnewpocket:
            	
            	//se cargan las preferencias
            	SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
						Context.MODE_PRIVATE);
            	//se carga la clase con la info de los temas de los bolsillos
            	PocketThemeData datapocket = new PocketThemeData(this);
    			txt.setText(datapocket.getTheme_name_new_pocket(pos));
    			
    			
            	ConnectionServerUpdatePocket con = new ConnectionServerUpdatePocket(this,Constants.PUT);
            	
				//se definen los parámetros para la conexión
				con.defineURL(Constants.API_PUT_POCKET+pocket.getId_pocket()+Constants.API_FORMAT_JSON);

				con.addParam(Constants.ID_USER, preferences.getString(Constants.PREFERENCES_LOGIN_ID,""));
				con.addParam(Constants.NAME_POCKET, pocket_name.getText().toString());
				con.addParam(Constants.DESC_POCKET, pocket_description.getText().toString());
				con.addParam(Constants.THEME_POCKET, datapocket.getTheme_name_new_pocket(pos).toString());
				con.addParam(Constants.ID_POCKET, pocket.getId_pocket());
				
				con.execute();
				
            	
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Método que se activa al hacer click
     */
	public void onClick(View v) {
		if(v.equals(edit_theme)){
			
			//nueva transición
			FragmentTransaction transaction;
			
			transaction= getFragmentManager().beginTransaction();
			transaction.replace(R.id.container_newpocket, newFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
		
	}
	
	/**
	 * Callback del tema del bolsillo seleccionado
	 */
	public void onButtonBClicked(int number) {
		if(number>=0){
			
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
	 * Clase que se encarga de realizar las acciones de los resultados de la conexión
	 * cuando se actualizan los bolsillos
	 * @author Juan Manuel Jurado
	 *
	 */
	class ConnectionServerUpdatePocket extends ConnectionServer{
    	
    	public ConnectionServerUpdatePocket(Activity act,String URLMethod) {
			super(act,URLMethod);
			this.dialog = new ProgressDialog(act);
			this.dialog.setTitle("Downloading");
			this.dialog.setMessage("Connecting, please wait");
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
					
					
					Toast.makeText(act,act.getString(R.string.json_status_pocket_update_OK),
							Toast.LENGTH_LONG).show();
					
					act.setResult(Constants.UPDATE_POCKET_OK);
					act.finishActivity(Constants.UPDATE_POCKET_OK);
					act.finish();
				}
				else{
					Toast.makeText(act,act.getString(R.string.json_status_pocket_update_KO),
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
	}	
}
