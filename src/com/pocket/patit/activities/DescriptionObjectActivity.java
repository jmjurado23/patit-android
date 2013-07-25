package com.pocket.patit.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.DateToString;
import com.pocket.patit.classes.Object;

/**
 * DescriptionObjectActivity.java
 * 
 * Actividad que se encarda de mostrar los datos de un objeto. Esta clase recibe 
 * la instancia del objeto y se encarga de rellenar los datos del layout.
 *  
 * @author Juan Manuel Jurado Ruiz	
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 * 
 */


public class DescriptionObjectActivity extends FragmentActivity {

	private TextView objectName; //nombre del objeto
	private TextView objectDesc; //descripción del objeto
	private TextView objectCreateDate; //fecha de creación del objeto
	private TextView objectLastMod; //fecha de la última modificación del objeto
	private TextView objectURL; //dirección web del objeto
	private ImageView imgTheme; //tipo de objeto
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_object);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //cargar los elementos del layout en variables
        objectName = (TextView) findViewById(R.id.textViewNameDesc);
        objectDesc = (TextView) findViewById(R.id.textViewDescDesc);
        objectCreateDate = (TextView) findViewById(R.id.textViewCreateDate);
        objectLastMod = (TextView) findViewById(R.id.textViewLastDate);
        objectURL = (TextView) findViewById(R.id.textViewurlDesc);
        imgTheme = (ImageView) findViewById(R.id.imageViewDescObject);
        
        //se comprueba cuál es el bolsillo a mostrar
        Object object = (Object) this.getIntent().getExtras().get(Constants.OBJECT_TO_DESCRIPTION);
        
        //se asignan los valores del objeto a los elementos del layout
        objectName.setText(object.getName());
        objectDesc.setText(object.getDescript());
        String date = object.getC_date();
        date = DateToString.getDay(date) +"/"+DateToString.getMonth(date) +"/"+
        		DateToString.getYear(date) +"-"+ DateToString.getHour(date) +"/"+DateToString.getMinute(date);
        objectCreateDate.setText(date);
        date = object.getLast_mod();
        date = DateToString.getDay(date) +"/"+DateToString.getMonth(date) +"/"+
        		DateToString.getYear(date) +"-"+ DateToString.getHour(date) +"/"+DateToString.getMinute(date);
        objectLastMod.setText(date);
        objectURL.setText(object.getUrl());
        
        //se ponen como seleccionados para que las marquesinas funcionen
        objectName.setSelected(true);
        objectDesc.setSelected(true);
        objectCreateDate.setSelected(true);
        objectLastMod.setSelected(true);
        objectURL.setSelected(true);
        
        //según el tipo, se usa una imagen
        if(object.getType().equals(Constants.IMAGE))
        	imgTheme.setBackgroundResource(R.drawable.imagegrid);
        else if(object.getType().equals(Constants.MUSIC))
        	imgTheme.setBackgroundResource(R.drawable.musicgrid);
        else if(object.getType().equals(Constants.TEXT))
        	imgTheme.setBackgroundResource(R.drawable.textgrid);
        else if(object.getType().equals(Constants.URL))
        	imgTheme.setBackgroundResource(R.drawable.urlgrid);
            
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //se define el comportamiento de presionar home
            	finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//se infla el menu superior con el layout de su menu
        getMenuInflater().inflate(R.menu.activity_description_pocket, menu);
        return true;
    }

    
}
