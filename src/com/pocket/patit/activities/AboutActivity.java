package com.pocket.patit.activities;
import com.pocket.patit.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * {@link AboutActivity}
 * 
 * Actividad encargada de mostrar un texto con los datos acerca de Patit
 * @author Juan Manuel Jurado Ruiz	
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 * 
 */
public class AboutActivity extends Activity implements OnClickListener{
	
	private TextView texto; //Texto donde se mostrarán los datos del desarrollador
	
	/**
     * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
     * y de llamar a los Listeners.
     * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
     * @return No devuelve ningun valor
     */
	protected void onCreate(Bundle savedInstanceState) {
        //guardamos el estado de la actividad anterior
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //cargamos el layout
        setContentView(R.layout.about_activity);
        
        
        //editamos el texto con un código html
        texto=(TextView) findViewById(R.id.about);
        texto.setText(Html.fromHtml("<p><b>Patit</b></p><p>Juan Manuel Jurado Ruiz " +
        		"</p><p><i>jmj23elviso@gmail.com</i><p><p>Aplicación de " +
        		getString(R.string.aboutText)),BufferType.SPANNABLE);
        texto.setOnClickListener(this);
    }

	/**
     * Método que se activará cuando pulsemos un botón de nuestra aplicación
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
	public void onClick(View v) 
	{
		//si pulsamos cualquier cosa, salimos de la actividad
		this.finish();
	}
}


