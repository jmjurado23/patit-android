package com.pocket.patit.activities;


import com.pocket.patit.fragments.PreferencesFragment;

import android.os.Bundle;
import android.app.Activity;

/**
 * {@link PreferencesActivity}
 * 
 * Actividad que carga el fragmento de las preferencias. Sólo carga al fragmento
 * Propio de la versión 3.2 + de android
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class PreferencesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PreferencesFragment()).commit();
    } 
}
