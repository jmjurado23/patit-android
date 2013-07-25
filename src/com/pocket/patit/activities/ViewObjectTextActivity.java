package com.pocket.patit.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.FormatValidation;
import com.pocket.patit.classes.Object;
import com.pocket.patit.fragments.LoadingObjectFragment;
import com.pocket.patit.services.DownloadService;

/**
 * {@link ViewObjectTextActivity}
 * 
 * Clase que muestra un objeto de tipo texto. Esta clase tiene sus particularidades
 * por lo que se decidió no hacer una herencia con los otros tipos de objetos. Ésta realiza
 * una conexión para cargar el html del texto
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewObjectTextActivity extends Activity implements OnTouchListener,OnClickListener{

	private Object obj;							//objeto
	private CheckedTextView Name;				//nombre
	private TextView URL;						//dirección
	private WebView webView;					//visor web
	private Handler handlerInfo;				//manejador de la información
	private LinearLayout info;					//información 
	private RelativeLayout webViewRela;			//layout de carga
	private LoadingObjectFragment fragment;		//fragmento de carga
	private int parent;							//padre
	private int n_pocket_parent;				//número de bolsillo del objeto
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_object_txt);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	//se obtiene el objeto de la actividad anterior
        obj = (Object) this.getIntent().getExtras().getSerializable(Constants.SINGLE_TO_VIEW_OBJECT);
        parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_PARENT, Constants.SINGLE_YOUR);
        n_pocket_parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_N_PARENT,0);
    
        //se cargan los elementos desde el layout	
        Name = (CheckedTextView) findViewById(R.id.textViewTitlePocketGrid);
        URL = (TextView) findViewById(R.id.textViewUrlObjectGrid);
        info = (LinearLayout) findViewById(R.id.layoutViewObjectData);
        webView = (WebView) findViewById(R.id.webView1);
        webViewRela = (RelativeLayout) findViewById(R.id.RelativeLayout02);
        
        //manejador del loading de la imagen y carga del fragmento de cargando...
        handlerInfo = new Handler();
    
        fragment= new LoadingObjectFragment();
        FragmentTransaction transaction;
		transaction= getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
		
        if(obj!=null){ //si el objeto existe
        	
        	Name.setText(obj.getName());
        	Name.setSelected(true);
        	URL.setText(obj.getDescript());
        	URL.setSelected(true);
        	
        	webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)   
                {
                fragment.setProgress(progress);
                
                 if(progress == 100)
                	removeFragment();
                  }
        	});
				
        	WebSettings webSettings = webView.getSettings();
        	webSettings.setEnableSmoothTransition(true);
        	webSettings.setJavaScriptEnabled(false);
        	webSettings.setBuiltInZoomControls(false);
        	webSettings.setMinimumFontSize(14);
        	
        	webView.loadUrl(obj.getUrl());
        }
        webViewRela.setOnClickListener(this);
    }

    /**
     * Método para dejar de mostrar el fragmento de carga
     */
    private void removeFragment() {
    	try{
    		
	    	FragmentTransaction transaction;
			transaction= getFragmentManager().beginTransaction();
			transaction.remove(fragment);
			transaction.commit();
			
    	}catch(Exception e){
    		e.printStackTrace();
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

		//Menu para compartir elementos
		MenuItem overflowItem = menu
				.findItem(R.id.menu_item_share_action_provider_overflow);
		ShareActionProvider overflowProvider = (ShareActionProvider) overflowItem
				.getActionProvider();
		overflowProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		overflowProvider.setShareIntent(createShareIntent());
        return true;
    }

    /**
	 * Se crea un intent para compartir elementos {@link Intent}.
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

		case R.id.saveicon: //botón de almacenar objeto

			Intent intent = new Intent(this, DownloadService.class);
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			
			String routeFile = preferences.getString(Constants.PREF_KEY_FILE, Constants.DEFAULT_LOCATION);
	    	
			// Create a new Messenger for the communication back
	    	intent.putExtra(Constants.URL, obj.getUrl());
	    	
	    	if(FormatValidation.isValidDestination(routeFile))
	    		intent.putExtra(Constants.DESTINATION, 
	    			Constants.SDCARD +routeFile+"/" + obj.getName() + obj.getUrl().substring(obj.getUrl().length()-4));
	    	else
	    		intent.putExtra(Constants.DESTINATION, 
		    			Constants.SDCARD +obj.getName()+obj.getUrl().substring(obj.getUrl().length()-4));
	    		
	    	startService(intent);
	    	
	    	
			return true;
			
		case R.id.descicon: //botón de descripción
			
			// se crea la actividad para mostrar los detalles del bolsillo
			Intent intent2 = new Intent(this,DescriptionObjectActivity.class);
			intent2.putExtra(Constants.OBJECT_TO_DESCRIPTION, obj);
			startActivityForResult(intent2,Constants.DESCRIPTION_OBJECT_OK);
			
			return true;

		case R.id.menu_settings: //menú de preferencias
        	Intent Intent= new Intent(this, PreferencesActivity.class);
			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
            return true;
		

		}
		return super.onOptionsItemSelected(item);
	}


	
	/**
	 * Método para hacer visible la información
	 * @param value
	 */
	public void setVisibleInfo(boolean value){
		if(value) 
			info.setVisibility(View.VISIBLE);
		else
			info.setVisibility(View.INVISIBLE);
	}

	/**
	 * Hilo para hacer visible la carga
	 */
	final Runnable threadInfo = new Runnable() {
		   public void run() {
		
					setVisibleInfo(true);
		   }
		};

	public boolean onTouch(View arg0, MotionEvent arg1) {
		
		return false;
	}

	public void onClick(View arg0) {
		if(arg0.equals(webViewRela)){
			setVisible(false);
			handlerInfo.post(threadInfo);
		}
	}
}
