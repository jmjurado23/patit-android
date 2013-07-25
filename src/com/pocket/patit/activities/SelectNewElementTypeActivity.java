package com.pocket.patit.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;
import com.pocket.patit.data.DataObjectToNet;
import com.pocket.patit.fragments.NewElementPocketFragments;
import com.pocket.patit.fragments.NewElementPocketFragments.CallbackElementType;
import com.pocket.patit.fragments.NewObjectFragment;
import com.pocket.patit.fragments.NewObjectFragment.CallbackNewObjectData;
import com.pocket.patit.fragments.SelectMusicFragment;
import com.pocket.patit.fragments.SelectMusicFragment.CallbackMusic;
import com.pocket.patit.fragments.SelectTextFragment;
import com.pocket.patit.fragments.SelectTextFragment.CallbackText;
import com.pocket.patit.fragments.SelectURLFragment;
import com.pocket.patit.fragments.SelectURLFragment.CallbackURL;

/**
 * {@link SelectNewElementTypeActivity}
 * 
 * Actividad que muestra los tipos de objetos que se pueden crear. Para mostrarlo utiliza un fragmento con una lista
 * con los tipos de objetos que puedes guardar
 * 
 * @author Juan Manuel Jurado Ruiz	
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 * 
 */
public class SelectNewElementTypeActivity extends Activity implements
		CallbackElementType, CallbackMusic, CallbackText, CallbackURL, CallbackNewObjectData {

	private NewElementPocketFragments menuNewitemFragment; 	//fragmento con el nuevo tipo
	private SelectMusicFragment newFragmentMusic;			//nuevo fragmento de música
	private SelectTextFragment newFragmentText;				//nuevo fragmento de texto
	private SelectURLFragment newFragmentURL;				//nuevo fragmento de url
	private NewObjectFragment objectFragment;				//nuevo fragmento de tipo de objeto
	private String id_pocket;								//id de bolsillo
	private String type;									//tipo de objeto
	private String element;									//elemento
	private String data;									//dirección del elemento
	private int n_pocket;									//número de bolsillo

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_new_element_type);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		menuNewitemFragment = new NewElementPocketFragments();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, menuNewitemFragment).commit();

		// se obtiene el bolsillo para el que vamos a crear el elemento
		setId_pocket(this.getIntent().getExtras()
				.getString(Constants.POCKET_ID));
		n_pocket = this.getIntent().getExtras()
				.getInt(Constants.SINGLE_TO_NEW_ITEM_N_POCKET, 0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater()
				.inflate(R.menu.activity_select_new_element_type, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // si se pulsa la tecla home se asciende
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback devuelto por el fragmento de nuevo elemento. Dice el elemento que se quiere
	 * introducir
	 */
	public void onButtonElementClicked(int number) {

		if (number == 0) { // nueva Imagen para el bolsillo
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, Constants.GALLERYITEM_RESULT);

		}
		if (number == 1) { // nueva música para el bolsillo

			newFragmentMusic = new SelectMusicFragment();
			FragmentTransaction transaction;
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, newFragmentMusic);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

		}
		if (number == 2) { // nuevo texto para el bolsillo

			newFragmentText = new SelectTextFragment();
			FragmentTransaction transaction;
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, newFragmentText);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

		}
		if (number == 3) { // nueva dir URL para el bolsillo

			newFragmentURL = new SelectURLFragment();
			FragmentTransaction transaction;
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, newFragmentURL);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

	}

	/**
	 * Método redefinido para obtener el resultado de las actividades que son
	 * llamadas desde la actividad actual
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		
		case Constants.GALLERYITEM_RESULT:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
				//Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
				
				//almacenamos el elemento en data
				this.data= filePath;
				//se crea el elemento
				createNewElement(Constants.IMAGE,filePath);
				
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	
	    	System.out.println("POCKET " +n_pocket);
	    	Intent upIntent = new Intent(this, YourPocketsActivity.class);
			upIntent.putExtra(Constants.ACTIVITY_TO_SINGLE, n_pocket);
			NavUtils.navigateUpTo(this, upIntent);
	   
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

	/**
	 * Callback con la canción elegida del sistema
	 */
	public void onButtonMusicClicked(String dir) {
		
		if(dir!=null){
			//se crea el elemento
			//almacenamos el elemento en data
			this.data= dir;
			createNewElement(Constants.MUSIC,dir);
		}
		else{
			getFragmentManager().beginTransaction()
			.replace(R.id.container, menuNewitemFragment).commit();
		}
		
	}

	/**
	 * callback con el texto elegido 
	 */
	public void onButtonTextClicked(String dir) {
		
		if(dir!=null){
			//almacenamos el elemento en data
			this.data= dir;
			//se crea el elemento
			createNewElement(Constants.TEXT,dir);
		}
		else{
			getFragmentManager().beginTransaction()
			.replace(R.id.container, menuNewitemFragment).commit();
		}
			
	}

	/**
	 * callback con la dirección elegida
	 */
	public void onButtonURLClicked(String dir) {
		
		if(dir!=null){
			//almacenamos el elemento en data
			this.data= dir;
			
			//se crea el elemento
			createNewElement(Constants.URL,dir);
		}
		else{
			getFragmentManager().beginTransaction()
			.replace(R.id.container, menuNewitemFragment).commit();
		}

	}

	/**
	 * Método que se encarga de recibir lso parámetros del nuevo objeto y 
	 * de crearlos en la clase pública estática para subirlos al servidor.
	 * También se encargará de cerrar la actividad indicando si ha habido éxito 
	 * o no al crear un objeto
	 * @param oBJECT_TYPE_IMG
	 * @param filePath
	 */
	private void createNewElement(String object_type, String filePath) {
		
		setElement(filePath);
		setType(object_type);
		
		objectFragment = new NewObjectFragment();
		objectFragment.setType(object_type);
		objectFragment.setTextprev(filePath);
		
		FragmentTransaction transaction;
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, objectFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
		
		
		
	}
	
	
//	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
//
//		// Decode image size
//		BitmapFactory.Options o = new BitmapFactory.Options();
//		o.inJustDecodeBounds = true;
//		BitmapFactory.decodeStream(
//				getContentResolver().openInputStream(selectedImage), null, o);
//
//		// The new size we want to scale to
//		final int REQUIRED_SIZE = 140;
//
//		// Find the correct scale value. It should be the power of 2.
//		int width_tmp = o.outWidth, height_tmp = o.outHeight;
//		int scale = 1;
//		while (true) {
//			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
//				break;
//			}
//			width_tmp /= 2;
//			height_tmp /= 2;
//			scale *= 2;
//		}
//
//		// Decode with inSampleSize
//		BitmapFactory.Options o2 = new BitmapFactory.Options();
//		o2.inSampleSize = scale;
//		return BitmapFactory.decodeStream(
//				getContentResolver().openInputStream(selectedImage), null, o2);
//
//	}

	public String getId_pocket() {
		return id_pocket;
	}

	public void setId_pocket(String id_pocket) {
		this.id_pocket = id_pocket;
	}


	/**
	 * Método que rellena la lista de objeto a subir y finaliza, para que la actividad {@link YourPocketsActivity} se encargue de hacer
	 * la conexión
	 */
	public void onButtonSendObjectClicked(String nameObject,
			String descriptionObject, String type) {
		
				//si se pulsa el botón de enviar, se crea el elemento en la lista de objetos para subir
				//y se envía un mensaje a la actividad anterior de que debe lanzar un hilo para subir
				if(DataObjectToNet.getList_objects()==null){
					List<Object> list = new ArrayList<Object>();
					list.add(new Object(type,nameObject,descriptionObject,id_pocket,data));
					DataObjectToNet.setList_objects(list);
				}
				else{
					DataObjectToNet.getList_objects().add(new Object(type,nameObject,descriptionObject,id_pocket,data));

				}
				
				//se da como resultado el elemento de la lista que hay que subir a la red
				setResult( DataObjectToNet.getList_objects().size() - 1 );
				finish();
		
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the element
	 */
	public String getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(String element) {
		this.element = element;
	}

}
