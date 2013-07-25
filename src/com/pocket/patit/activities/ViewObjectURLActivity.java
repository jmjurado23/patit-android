package com.pocket.patit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.pocket.patit.classes.Object;


/**
 * {@link ViewObjectURLActivity}
 * 
 * Clase que muestra un objeto de tipo url. Esta clase tiene sus particularidades
 * por lo que se decidió no hacer una herencia con los otros tipos de objetos. Ésta realiza
 * una conexión para cargar el html del url
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewObjectURLActivity extends Activity implements OnClickListener{

	private Object obj;						//objeto
	private CheckedTextView Name;			//nombre
	private TextView URL;					//url
	private WebView webView;				//webview 
	private Handler handlerInfo;			//manejador del loading
	private LinearLayout info;				//layout de la info de obj
	private RelativeLayout webViewRela;		//layout de loading
	private TextView text;					//texto
	private int parent;						//padre
	private int n_pocket_parent;			//número del bolsillos del obj
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_object_url);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	//se obtiene el objeto de la actividad anterior
        obj = (Object) this.getIntent().getExtras().getSerializable(Constants.SINGLE_TO_VIEW_OBJECT);
        parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_PARENT, Constants.SINGLE_YOUR);
        n_pocket_parent = (int) this.getIntent().getExtras().getInt(Constants.SINGLE_TO_VIEW_OBJECT_N_PARENT,0);
    
        //se cargan los elementos a partir del layout
        Name = (CheckedTextView) findViewById(R.id.textViewTitlePocketGrid);
        URL = (TextView) findViewById(R.id.textViewUrlObjectGrid);
        setInfo((LinearLayout) findViewById(R.id.layoutViewObjectData));
        webView = (WebView) findViewById(R.id.webView1);
        text = (TextView) findViewById(R.id.textViewObjURL);
        webViewRela = (RelativeLayout) findViewById(R.id.RelativeLayout02);
        

        setHandlerInfo(new Handler());
        
       
        if(obj!=null){ //si el objeto existe
        	
        	Name.setText(obj.getName());
        	Name.setSelected(true);
        	URL.setText(obj.getDescript());
        	URL.setSelected(true);
        	
        	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        	
        	//se comprueba si se usa un navegador interno
        	if(preferences.getBoolean(Constants.PREF_KEY_EXT_NAV, false)){
        		String url = obj.getUrl();
        		
        		if(!url.startsWith("http://") && !url.startsWith("https://"))
        			url = Constants.HTTP + obj.getUrl();
        		
        		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    			startActivity(browserIntent);
    			webView.setVisibility(View.GONE);
    			text.setText(obj.getUrl());
        	}
        	else{
        		text.setVisibility(View.GONE);
	        	webView.setWebChromeClient(new WebChromeClient() {
	                public void onProgressChanged(WebView view, int progress)   
	                {}
	        	});
	        	WebSettings webSettings = webView.getSettings();
	        	webSettings.setEnableSmoothTransition(true);
	        	webSettings.setBuiltInZoomControls(true);
	        	webSettings.setMinimumFontSize(14);
	        	
	        	webView.loadUrl(obj.getUrl());	
        	}
    		
        }
        webViewRela.setOnClickListener(this);
    }

        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_object_url, menu);
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

		case R.id.descicon: //descripción del objeto
			
			// se crea la actividad para mostrar los detalles del bolsillo
			Intent intent = new Intent(this,DescriptionObjectActivity.class);
			intent.putExtra(Constants.OBJECT_TO_DESCRIPTION, obj);
			startActivityForResult(intent,Constants.DESCRIPTION_OBJECT_OK);
			
			return true;

		case R.id.menu_settings: //menú de preferencias
        	Intent Intent= new Intent(this, PreferencesActivity.class);
			startActivityForResult(Intent, Constants.PREFERENCES_ACTIVITY_OK);  
            return true;

		}
		return super.onOptionsItemSelected(item);
	}


	
	

	public void onClick(View arg0) {
		if(arg0.equals(webViewRela)){
			
		}
		
	}

	/**
	 * @return the handlerInfo
	 */
	public Handler getHandlerInfo() {
		return handlerInfo;
	}


	/**
	 * @param handlerInfo the handlerInfo to set
	 */
	public void setHandlerInfo(Handler handlerInfo) {
		this.handlerInfo = handlerInfo;
	}


	/**
	 * @return the info
	 */
	public LinearLayout getInfo() {
		return info;
	}


	/**
	 * @param info the info to set
	 */
	public void setInfo(LinearLayout info) {
		this.info = info;
	}

}
