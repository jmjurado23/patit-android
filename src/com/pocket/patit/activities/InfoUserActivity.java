package com.pocket.patit.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.PocketTheme;
import com.pocket.patit.data.DataYourPocket;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.views.ArrayAdapterThemeInfoUser;

/**
 * {@link InfoUserActivity}
 * 
 * Actividad encargada de mostrar la información de un usuario de Patit.
 * Esta información se obtiene a partir de la información proporcionada en la activity
 * anterior. Esta no tiene por tanto que realizar conexiones a la red.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class InfoUserActivity extends FragmentActivity implements OnItemClickListener {

	private TextView userNick;					//nick del usuario
	private TextView userEmail;					//email del usuario
	private TextView userRegDate;				//fecha de registro del usuario
	private TextView userNPockets;				//número de bolsillos de usuario
	private ImageView img;						//imagen para mostrar un icono
	private ListView list;						//lista con los temas de bolsillos
	private List<PocketTheme> list_themes;		//lista con los temas
	private List<Integer> list_number;			//lista con los números de comentarios de cada bolsillo
	private ArrayAdapterThemeInfoUser adapter;	//adaptador de la lista
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //cargar los elementos a partir del layout
        userNick = (TextView) findViewById(R.id.textViewNickInfo);
        userEmail = (TextView) findViewById(R.id.textViewEmailInfo);
        userRegDate = (TextView) findViewById(R.id.textViewRegDateInfo);
        userNPockets = (TextView) findViewById(R.id.TextViewNPocketInfo);
        list = (ListView) findViewById(R.id.listViewInfoUser);
        
        //preferencias del sistema
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
				Context.MODE_PRIVATE);
        userNick.setText(preferences.getString( 
         		Constants.PREFERENCES_LOGIN_NICK,""));
        userEmail.setText(preferences.getString(
         		Constants.PREFERENCES_LOGIN_EMAIL,""));
        String  date = preferences.getString(
         		Constants.PREFERENCES_LOGIN_REGDATE,"").replace( 'T', ' ' );
        String subDate=date.substring(0, 10);
        userRegDate.setText(subDate);
        userNPockets.setText(Integer.toString(DataYourPocket.getList_pockets().size()));
        
        //crear la lista que tendrá el número de bolsillos de cada tipo que tiene el usuario
        list_themes = new ArrayList<PocketTheme>();
        PocketThemeData data = new PocketThemeData(this);
    	list_themes = data.getThemes();
    	
    	list_number = new ArrayList<Integer>();
    	
    	for(int i=0;i<list_themes.size();i++)
    		list_number.add(0);
    	
    	for(int i=0;i<DataYourPocket.getList_pockets().size();i++)
    	{
    		//se cuentan los tipos de bolsillo de cada tipo
    		for(int j=0;j<list_themes.size();j++){
    			if(list_themes.get(j).getName().equals(DataYourPocket.getList_pockets().get(i).getType())){
    				list_number.set(j,list_number.get(j)+1);
    			}
    		}
    	}
    	
    	
        adapter = new ArrayAdapterThemeInfoUser(this, 
                R.layout.row_theme_info_user, list_themes);
        adapter.setListNumber(list_number);
    	
        //se seleccionan los textos para que funcionen las marquesinas
        userNick.setSelected(true);
        userEmail.setSelected(true);
        userRegDate.setSelected(true);
    	
        //esto define una cabecera para la lista
    	View header = (View) this.getLayoutInflater().inflate(R.layout.row_theme_info_user_header, null);
    	list.addHeaderView(header);
    	
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	finish();
                return true;
            	
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_description_pocket, menu);
        return true;
    }
    
    /**
     * método para definir el comportamiento al hacer click en un elemento de la 
     * lista
     */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		
	}

	/**
	 * @return la imagen
	 */
	public ImageView getImg() {
		return img;
	}

	/**
	 * @param img la imagen para mostrar
	 */
	public void setImg(ImageView img) {
		this.img = img;
	}

    
}
