package com.pocket.patit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;

/**
 * {@link SelectURLFragment}
 * 
 * Fragmento que mustra un elemento donde escribir una dirección web para almacenarla
 * en el bolsillo.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SelectURLFragment extends Fragment implements OnClickListener{
	
	EditText text;			//texto URL
	Button ok_button;		//botón de aceptar
	Button cancel_button;	//botón de cancelar
	CallbackURL mCallback;	//callback

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_select_url, container, false);
    	
		//cargar los elementos a partir del layout
		text = (EditText) view.findViewById(R.id.urlNewElement);
		ok_button = (Button) view.findViewById(R.id.acceptButtonNewURLElement);
		cancel_button = (Button) view.findViewById(R.id.cancelButtonNewURLElement);
		
		ok_button.setOnClickListener(this);
		cancel_button.setOnClickListener(this);
		
		return view;
	}
	
	
	public interface CallbackURL {
        public void onButtonURLClicked(String dir);
    }
	
	
	
	@Override
	public void onAttach(Activity activity) {

	    super.onAttach(activity);
	    try {
	        mCallback = (CallbackURL) activity;
	    }
	    catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement " + CallbackURL.class.getName());
	    }
	}	
	
	
	public void onClick(View arg0) {
		if(arg0.equals(ok_button)){
			
			if(!text.getText().toString().startsWith(Constants.HTTP))
				mCallback.onButtonURLClicked(Constants.HTTP + text.getText().toString());
			else 
				mCallback.onButtonURLClicked(text.getText().toString());
		}
		if(arg0.equals(cancel_button)){
			mCallback.onButtonURLClicked(null);
		} 
		
	}	
	
}
