package com.pocket.patit.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;

/**
 * {@link PreferenceFragment}
 * 
 * Clase Fragmento que muestra las preferencias a partir de un archivo XML. Todas las preferencias
 * se cargand desde las vistas.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class PreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceClickListener{

	private Preference storePref;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.preferences);
        
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sp = pm.getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
        
        //esto se ha creado porque no detecta las preferencias simples
        storePref = (Preference) this.findPreference(Constants.PREF_KEY_STORE);
        storePref.setOnPreferenceClickListener(this);
      
        
    }

	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {

	}
	
	/**
	 * Cuadro de diálogo con una respuesta de si o no cuando se decide eliminar a un usuario
	 * @return
	 */
	public AlertDialog getDialogYesNo(){
		return new AlertDialog.Builder(this.getActivity())
        .setIconAttribute(android.R.attr.alertDialogIcon)
        .setTitle(R.string.deleteInfoTitle)
        .setMessage(R.string.deleteInfoQuestion)
        .setPositiveButton(R.string.OkDialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            
            	SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFERENCES_LOGIN,Context.MODE_PRIVATE);
            	SharedPreferences.Editor editor = preferences.edit();
    			editor.putString(Constants.PREFERENCES_FIRST_USE, Constants.TRUE);
    			editor.putString(Constants.PREFERENCES_LOGIN_EMAIL, "");
    			editor.putString(Constants.PREFERENCES_LOGIN_NICK, "");
    			editor.putString(Constants.PREFERENCES_LOGIN_PASS, "");
    			editor.commit();
            }
        })
        .setNegativeButton(R.string.CancelDialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            }
        })
        .create();
	}

	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().equals(Constants.PREF_KEY_STORE)){
			AlertDialog dia = getDialogYesNo();
			dia.show();
		}
			
		return false;
	}
}