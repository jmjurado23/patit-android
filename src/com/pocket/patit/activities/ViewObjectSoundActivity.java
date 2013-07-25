package com.pocket.patit.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.FormatValidation;
import com.pocket.patit.classes.Object;
import com.pocket.patit.services.DownloadService;

/**
 * {@link ViewObjectMusicActivity}
 * 
 * Clase que muestra un objeto de tipo musica. Esta clase tiene sus particularidades
 * por lo que se decidió no hacer una herencia con los otros tipos de objetos. Ésta realiza
 * una conexión para cargar el sonido desde la red
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewObjectSoundActivity extends Activity implements OnClickListener{

	private Object obj;				//objeto
	private CheckedTextView Name;	//nombre del objeto
	private TextView URL;			//dir del objeto
	private ImageButton playButton;	//botón de reproducir
	private int parent;				//padre
	private int n_pocket_parent;	//número del bolsillo padre
	private MediaPlayer media;		//media para reproducir sonidos
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_object_sound);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	//se obtiene el objeto de la actividad anterior
        obj = (Object) this.getIntent().getExtras().getSerializable(Constants.SINGLE_TO_VIEW_OBJECT);
        parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_PARENT, Constants.SINGLE_YOUR);
        n_pocket_parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_N_PARENT,0);
        playButton = (ImageButton) this.findViewById(R.id.buttonPlay);
        Name = (CheckedTextView) findViewById(R.id.textViewTitlePocketGrid);
        URL = (TextView) findViewById(R.id.textViewUrlObjectGrid);
        
		
        if(obj!=null){ //si el objeto existe
        	
        	Name.setText(obj.getName());
        	Name.setSelected(true);
        	URL.setText(obj.getDescript());
        	URL.setSelected(true);
        	
        	//set up MediaPlayer    
            media = new MediaPlayer();
         
            try {
                media.setDataSource(obj.getUrl());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                media.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            playButton.setOnClickListener(this);
    		
        }
       
    }


        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_object, menu);
        MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		actionProvider.setShareIntent(createShareIntent());

		//Menu
		MenuItem overflowItem = menu
				.findItem(R.id.menu_item_share_action_provider_overflow);
		ShareActionProvider overflowProvider = (ShareActionProvider) overflowItem
				.getActionProvider();
		overflowProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		overflowProvider.setShareIntent(createShareIntent());
        return true;
    }

    /**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
    private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		
		try{
			shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.whatobject)+ " "+Constants.API_WEB_OBJECT + obj.getId_objet()+ " - @patit_web" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return shareIntent;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home: //botón de navegación superior
			Intent upIntent = null;
			if(parent == Constants.SINGLE_YOUR){
				upIntent = new Intent(this, YourPocketsActivity.class);
				upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket_parent);
			}
			else if(parent == Constants.SINGLE_RANDOM){
				upIntent = new Intent(this, RandomPocketsActivity.class);
				upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket_parent);
			}
			else if(parent == Constants.SINGLE_FEATURED){
				upIntent = new Intent(this, FeaturedPocketsActivity.class);
				upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket_parent);
			}
			else if(parent == Constants.SINGLE_SPONSORED){
				upIntent = new Intent(this, SponsoredPocketsActivity.class);
				upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket_parent);
			}
			else if(parent == Constants.SINGLE_SEARCH){
				upIntent = new Intent(this, SearchPocketsActivity.class);
				upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket_parent);
			}
			
			NavUtils.navigateUpTo(this, upIntent);
			return true;

		case R.id.saveicon: //botón para guardar el archivo

			Intent intent = new Intent(this, DownloadService.class);
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String routeFile = preferences.getString(Constants.PREF_KEY_FILE, Constants.DEFAULT_LOCATION);

	    	intent.putExtra(Constants.URL, obj.getUrl());
	    	
	    	if(FormatValidation.isValidDestination(routeFile))
	    		intent.putExtra(Constants.DESTINATION, 
	    			Constants.SDCARD +routeFile+"/" + obj.getName() + obj.getUrl().substring(obj.getUrl().length()-4));
	    	else
	    		intent.putExtra(Constants.DESTINATION, 
		    			Constants.SDCARD +obj.getName()+obj.getUrl().substring(obj.getUrl().length()-4));
	    		
	    	startService(intent);
			return true;
			
		case R.id.descicon://botón de descripción del objeto
			// se crea la actividad para mostrar los detalles del bolsillo
			Intent intent2 = new Intent(this,DescriptionObjectActivity.class);
			intent2.putExtra(Constants.OBJECT_TO_DESCRIPTION, obj);
			startActivityForResult(intent2,Constants.DESCRIPTION_OBJECT_OK);
			return true;
			
		case R.id.menu_settings: //botón de preferencias
        	Intent Intent= new Intent(this, PreferencesActivity.class);
			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
            return true;

		

		}
		return super.onOptionsItemSelected(item);
	}

	

	public void onClick(View arg0) {
		if(arg0.equals(playButton)){
			
			if(media.isPlaying()){
				media.pause();
				playButton.setImageResource(R.drawable.playicon);
			}
			else{
				media.start();
				playButton.setImageResource(R.drawable.pauseicon);
			}
		}
	} 
}
