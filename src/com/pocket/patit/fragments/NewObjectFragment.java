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
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.fragments.NewCommentFragment.CallbackNewComment;

/**
 * {@link NewObjectFragment}
 * 
 * Clase Fragmento que muestra un menú con el nombre, la descripción y el objeto elegido 
 * previamente para definir los parámetros del mismo antes de subirlo al servidor.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class NewObjectFragment extends Fragment implements OnClickListener {

	CallbackNewObjectData mCallback;	//respuesta del fragmento
	EditText textName;					//nombr del objeto
	EditText textDescription;			//descripción del objeto
	TextView preview;					//vista previa del objeto
	Button imgPreview;					//imagen del tipo de objeto
	Button sendObject;					//botón para enviar el objeto
	String type;						//tipo del objeto
	String textprev;					//texto previo del objeto

	public String getType() {
		return type;
	}

	public String getTextprev() {
		return textprev;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTextprev(String textprev) {
		this.textprev = textprev;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_object, container,
				false);
		textName = (EditText) view.findViewById(R.id.editTextNewNameObject);
		textDescription = (EditText) view.findViewById(R.id.editTextDescriptionObject);
		sendObject = (Button) view.findViewById(R.id.cfgBtnNewObject);
		preview = (TextView) view.findViewById(R.id.textViewPreviewNewObject);
		imgPreview = (Button) view.findViewById(R.id.buttonNewObjectType);
		
		
		
		//definimos el preview de los elementos
		preview.setText(textprev);
		if (type.equals(Constants.IMAGE)){
			imgPreview.setBackgroundResource(R.drawable.pictureicon);
		}
		else if(type.equals(Constants.MUSIC)){
			imgPreview.setBackgroundResource(R.drawable.musicicon);
		}
		else if(type.equals(Constants.TEXT)){
			imgPreview.setBackgroundResource(R.drawable.editicon);
		}
		else if(type.equals(Constants.URL)){
			imgPreview.setBackgroundResource(R.drawable.urlicon);
		}
		

		sendObject.setOnClickListener(this);
		// Inflate the layout for this fragment
		return view;
	}

	public interface CallbackNewObjectData {
		public void onButtonSendObjectClicked(String nameObject, String descriptionObject, String type);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallback = (CallbackNewObjectData) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement " + CallbackNewComment.class.getName());
		}
	}

	public void onClick(View arg0) {
		if (arg0.equals(sendObject)) {
			if (!textName.getText().toString().equals("")) {
				mCallback.onButtonSendObjectClicked(textName.getText().toString(),
						textDescription.getText().toString(),getType());

			}
		}

	}

}