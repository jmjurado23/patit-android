package com.pocket.patit.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.fragments.FeaturedPocketsFragments;
import com.pocket.patit.fragments.RandomPocketsFragments;
import com.pocket.patit.fragments.RandomPocketsFragments.CallbackChargingRandomData;
import com.pocket.patit.fragments.SearchPocketsFragments;
import com.pocket.patit.fragments.SearchPocketsFragments.CallbackChargingSearchData;
import com.pocket.patit.fragments.SponsoredPocketsFragments;
import com.pocket.patit.fragments.YourPocketsFragments;
import com.pocket.patit.fragments.YourPocketsFragments.CallbackChargingYourData;
import com.pocket.patit.fragments.FeaturedPocketsFragments.CallbackChargingFeaturedData;
import com.pocket.patit.fragments.SponsoredPocketsFragments.CallbackChargingSponsoredData;

/**
 * {@link MainPocketsActivity}
 * 
 * Actividad encargada de mostrar todos los fragmentos del segundo nivel de la aplicación.
 * En esta actividad se cambia de fragmento para mostrar los fragmentos de Tus bolsillos, 
 * destacados, aleatorios, esponsorizados y el fragmento de buscar.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 *
 */
public class MainPocketsActivity extends FragmentActivity implements ActionBar.OnNavigationListener, 
CallbackChargingYourData, CallbackChargingFeaturedData, CallbackChargingSponsoredData,
CallbackChargingRandomData,CallbackChargingSearchData {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private YourPocketsFragments yourPocket;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //cargamos la barra de carga
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Se define el action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Se definen las opciones del navegador del action bar
        actionBar.setListNavigationCallbacks(
                // Se especifica un adaptador para los elementos del Spiner
                new ArrayAdapter<Object>(
                        actionBar.getThemedContext(),
                        R.layout.row_title_simple_list_item,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_fragment_yourpockets),
                                getString(R.string.title_fragment_randompockets),
                                getString(R.string.title_fragment_featuredpockets),
                                getString(R.string.title_fragment_sponsoredpockets),
                                getString(R.string.title_fragment_searchpockets),
                        }),
                this);
        
        //seleccionar el elemento de la action bar que hemos seleccionado en la 
        //actividad anterior. 
        
        int position;
        
        try{
        	position = getIntent().getExtras().getInt(Constants.HOME_TO_MAIN_FRAGMENT);
       	 
            if(position == Constants.YOUR_POCKETS_LIST_POSITION ||
        			position == Constants.RANDOM_POCKETS_LIST_POSITION ||
        			position == Constants.FEATURED_POCKETS_LIST_POSITION ||
        			position == Constants.SPONSORED_POCKETS_LIST_POSITION ||
        			position == Constants.SEARCH_POCKETS_LIST_POSITION){
            	
            	actionBar.setSelectedNavigationItem(position);
            }
        }catch(Exception e){
        	System.out.println("Error al volver de la actividad anterior");
        };
       
        
         
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                
            case R.id.newpocketiconbar:
            	Intent your_intent = new Intent(this, NewPocketActivity.class);
        		this.startActivityForResult(your_intent, Constants.NEW_POCKET_ACTIVITY_OK);
                finish();
            	
                return true;
            
            case R.id.refreshicon:
            	//si pulsan el botón de refrescar, hay que ver qué página está seleccionada            	
            	//TODO terminar para las otras páginas
            	
            	if (getActionBar().getSelectedNavigationIndex()== 0 ) //YOUR POCKETS
            	{
            		//recargamos la vista 
            		showFragment(0);
            		return true;
            	}
            	else if (getActionBar().getSelectedNavigationIndex()== 1 ) //RAMDOM
            	{
            		//recargamos la vista 
            		showFragment(1);
            		return true;
            	}
            	if (getActionBar().getSelectedNavigationIndex()== 2 ) //FEATURED
            	{
            		//recargamos la vista 
            		showFragment(2);
            		return true;
            	}
            	if (getActionBar().getSelectedNavigationIndex()== 3 ) //SPONSORED
            	{
            		//recargamos la vista 
            		showFragment(3);
            		return true;
            	}
            	if (getActionBar().getSelectedNavigationIndex()== 4 ) //SPONSORED
            	{
            		//recargamos la vista 
            		showFragment(4);
            		return true;
            	}
            	return true;
            case R.id.menu_settings:
            	Intent Intent= new Intent(this, PreferencesActivity.class);
    			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    /**
     * Se define el comportamiento al pulsar en un elemento del spiner
     * de la barra de navegación superior
     */
    public boolean onNavigationItemSelected(int position, long id) {
    	
    	if(position == Constants.YOUR_POCKETS_LIST_POSITION ||
    			position == Constants.RANDOM_POCKETS_LIST_POSITION ||
    			position == Constants.FEATURED_POCKETS_LIST_POSITION ||
    			position == Constants.SPONSORED_POCKETS_LIST_POSITION ||
    			position == Constants.SEARCH_POCKETS_LIST_POSITION)
    	{
    		showFragment(position);
	    	return true;
    	}
		return false;
    }
    
    
    /**
     * Método que muestra un fragmento según el índice que se le indique como
     * parámero. Si el índice coincide, se carga el fragmento
     * @param fragment
     */
    public void showFragment(int fragment){
    	
    	if(fragment == Constants.YOUR_POCKETS_LIST_POSITION) 
    	{
  
    		yourPocket = new YourPocketsFragments(this);
	    	Bundle args = new Bundle();
	    	args.putInt("number", fragment + 1);
	    	yourPocket.setArguments(args);
	    	getSupportFragmentManager().beginTransaction()
	    	.replace(R.id.container, yourPocket)
	    	.commit();
    	}
	    else if(fragment == Constants.RANDOM_POCKETS_LIST_POSITION )
	    {
	    	RandomPocketsFragments randompockets =new RandomPocketsFragments(this);
	    	Bundle args = new Bundle();
	    	args.putInt("number", fragment + 1);
	    	randompockets.setArguments(args);
	    	getSupportFragmentManager().beginTransaction()
	    	.replace(R.id.container, randompockets)
	    	.commit();
	    }
	    else if(fragment == Constants.FEATURED_POCKETS_LIST_POSITION)
	    {
	    	FeaturedPocketsFragments featuredpockets =new FeaturedPocketsFragments(this);
	    	Bundle args = new Bundle();
	    	args.putInt("number", fragment + 1);
	    	featuredpockets.setArguments(args);
	    	getSupportFragmentManager().beginTransaction()
	    	.replace(R.id.container, featuredpockets)
	    	.commit();
	    }
	    else if(fragment == Constants.SPONSORED_POCKETS_LIST_POSITION)
	    {
	    	SponsoredPocketsFragments sponsoredpockets =new SponsoredPocketsFragments(this);
	    	Bundle args = new Bundle();
	    	args.putInt("number", fragment + 1);
	    	sponsoredpockets.setArguments(args);
	    	getSupportFragmentManager().beginTransaction()
	    	.replace(R.id.container, sponsoredpockets)
	    	.commit();
	    }
	    else if(fragment == Constants.SEARCH_POCKETS_LIST_POSITION)
	    {
	    	SearchPocketsFragments searchpockets =new SearchPocketsFragments(this);
	    	Bundle args = new Bundle();
	    	args.putInt("number", fragment + 1);
	    	searchpockets.setArguments(args);
	    	getSupportFragmentManager().beginTransaction()
	    	.replace(R.id.container, searchpockets)
	    	.commit();
	    }
    }
    
    /**
     * Método para gestionar lo que devuelve la activity superior
     */
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
    	
        if (requestCode == Constants.YOUR_POCKET_ACTIVITY_OK) {
        	showFragment(0);
        }
        if (resultCode == Constants.RANDOM_POCKET_ACTIVITY_OK) {
        	showFragment(1);
        }
        if (requestCode == Constants.FEATURED_POCKET_ACTIVITY_OK) {
        	showFragment(2);
        }
        if (requestCode == Constants.SPONSORED_POCKET_ACTIVITY_OK) {
        	showFragment(3);
        }
        if (requestCode == Constants.SEARCH_POCKET_ACTIVITY_OK) {
        	showFragment(4);
        }
        if (requestCode == Constants.UPDATE_POCKET_OK) {
        	yourPocket.getInfoFromServer();
        }
    }
    

	public void onButtonChargingData(boolean estate) {
		setProgressBarIndeterminateVisibility(estate);
	}

   
}
