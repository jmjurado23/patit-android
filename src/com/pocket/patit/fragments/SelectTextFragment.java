package com.pocket.patit.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pocket.patit.R;

/**
 * {@link SelectTextFragment}
 * 
 * Fragmento que mustra un cuadro con un sitio para escribir un texto para almacenarlo en 
 * el bolsillo. Este se podrá enviar, o eliminar el fragmento
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SelectTextFragment extends Fragment implements OnClickListener{
	
	EditText text;			//texto
	Button ok_button;		//botón de aceptar
	Button cancel_button;	//botón de cancelar
	CallbackText mCallback;	//callback del fragmento

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
		//cargar los elementos a partir del layout
		View view = inflater.inflate(R.layout.fragment_select_text, container, false);
		text = (EditText) view.findViewById(R.id.textNewElement);
		ok_button = (Button) view.findViewById(R.id.acceptButtonNewElement);
		cancel_button = (Button) view.findViewById(R.id.cancelButtonNewElement);
		
		ok_button.setOnClickListener(this);
		cancel_button.setOnClickListener(this);
		
		return view;
	}
	
	
	public interface CallbackText {
        public void onButtonTextClicked(String dir);
    }
	
	
	
	@Override
	public void onAttach(Activity activity) {

	    super.onAttach(activity);
	    try {
	        mCallback = (CallbackText) activity;
	    }
	    catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement " + CallbackText.class.getName());
	    }
	}	
	
	/**
	 * Método que se llama al hacer click en un elemento del fragmento.
	 */
	public void onClick(View arg0) {
		if(arg0.equals(ok_button)){
			
			writeFile("text.html",text.getText().toString());
			
			mCallback.onButtonTextClicked(Environment.getExternalStorageDirectory() + "/text.html");
		}
		if(arg0.equals(cancel_button)){
			mCallback.onButtonTextClicked(null);
		} 
		
	}	
	
	public static boolean isExternalStorageReadOnly() {  
		 String extStorageState = Environment.getExternalStorageState();  
		 if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {  
		       return true;  
		 }  
		 return false;  
	}  
		   
	public static boolean isExternalStorageAvailable() {  
		 String extStorageState = Environment.getExternalStorageState();  
		 if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {  
		     return true;  
		 }  
		 return false;  
	} 
	
	/**
	 * Método que guarda el texto en un fichero para después subirlo a la red. El texto es un html
	 * @param filename
	 * @param textfile
	 */
	public void writeFile(String filename, String textfile){
		try {
		  if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) { 
		   File file = new File(Environment.getExternalStorageDirectory(), filename );
		   OutputStreamWriter outw = new OutputStreamWriter(new FileOutputStream(file));
		   outw.write(textfile);
		   outw.close();
		  }
		} catch (Exception e) {}  
	}
	
}
