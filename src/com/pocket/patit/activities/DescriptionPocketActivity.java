package com.pocket.patit.activities;

/**
 * DescriptionPocketActivity.java
 * 
 * Actividad que se encarda de mostrar los datos de un bolsillo. Esta clase recibe 
 * la instancia del objeto y se encarga de rellenar los datos del layout.
 *  
 * @author Juan Manuel Jurado Ruiz	
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 * 
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.DateToString;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.PocketThemeData;

public class DescriptionPocketActivity extends FragmentActivity {

	private TextView pocketName; //nombre del bolsillo
	private TextView pocketUser; //nombre del usuario del bolsillo
	private TextView pocketCreate; //fecha de creación
	private TextView pocketTheme; //tema del bolsillo
	private TextView pocketnObject; //Número de objetos del bolsillo
	private TextView pocketnComment; //número de comentarios del bolsillo 
	private TextView pocketGoodVotes; //Número de votos positivos
	private TextView pocketBadVotes; //Número de votos negativos
	private ImageView imgTheme; //tema del bolsillo en imagen
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_pocket);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //se cargan los elementos del layout en variables
        pocketName = (TextView) findViewById(R.id.textViewNameDesc);
        pocketUser = (TextView) findViewById(R.id.textViewUserDesc);
        pocketCreate = (TextView) findViewById(R.id.textViewCreateDate);
        pocketTheme = (TextView) findViewById(R.id.textViewThemeDesc);
        pocketnObject = (TextView) findViewById(R.id.TextViewNumberObjectDesc);
        pocketnComment = (TextView) findViewById(R.id.TextViewNumberCommentDesc);
        imgTheme = (ImageView) findViewById(R.id.imageViewDescrPocket);
        pocketGoodVotes = (TextView) findViewById(R.id.textViewPosVotesDesc);
        pocketBadVotes = (TextView) findViewById(R.id.textViewNegVotesDesc);
        
        //se comprueba cuál es el bolsillo a mostrar
        Pocket pocket = (Pocket) this.getIntent().getExtras().get(Constants.POCKET_TO_DESCRIPTION);
        
        //se rellenan los elementos con variables
        pocketName.setText(pocket.getName());
        pocketUser.setText(pocket.getUser());
        String date = pocket.getCreate_date();
        date = DateToString.getDay(date) +"/"+DateToString.getMonth(date) +"/"+
        		DateToString.getYear(date) +"-"+ DateToString.getHour(date) +"/"+DateToString.getMinute(date);
        pocketCreate.setText(date);
        pocketTheme.setText(pocket.getType());
        pocketnObject.setText(Integer.toString(pocket.getNObjects()));
        pocketnComment.setText(Integer.toString(pocket.getNComments()));
        pocketGoodVotes.setText(Integer.toString(pocket.getPos_votes()));
        pocketBadVotes.setText(Integer.toString(pocket.getNeg_votes()));
        
        //se poenen los elementos como seleccionados para que las marquesinas funcionen
        pocketName.setSelected(true);
        pocketUser.setSelected(true);
        pocketCreate.setSelected(true);
        pocketTheme.setSelected(true);
        pocketnObject.setSelected(true);
        pocketnComment.setSelected(true);
      
        //se cargan las imágenes de los temas según el tipo de bolsillo
        PocketThemeData theme = new PocketThemeData(this);
        imgTheme.setBackgroundResource(theme.getThemeImgResourceMainPocketTrans(pocket.getType()));

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //se define el comportamiento del home
            	finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//se carga el menu con la descripción del bolsillo
        getMenuInflater().inflate(R.menu.activity_description_pocket, menu);
        return true;
    }

    
}
