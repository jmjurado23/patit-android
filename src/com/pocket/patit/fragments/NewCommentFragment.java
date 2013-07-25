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

/**
 * {@link NewCommentFragment}
 * 
 * Clase Fragmento que muestra un cuadro de diálogo para escribir un comentario en 
 * la aplicación
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class NewCommentFragment extends Fragment implements OnClickListener {

	
	CallbackNewComment mCallback;	//respuesta del fragmento con el texto
	EditText text;					//texto
	Button sendComment;				//botón de enviar comentario
	Button cancelComment;			//botón de cancelar 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_comment, container,
				false);
		text = (EditText) view.findViewById(R.id.editTextNewComment);
		sendComment = (Button) view.findViewById(R.id.cfgBtnNewComment);
		cancelComment = (Button) view.findViewById(R.id.cfgBtnCancelComment);
		
		
		sendComment.setOnClickListener(this);
		cancelComment.setOnClickListener(this);
		// Inflate the layout for this fragment
		return view;
	}

	public interface CallbackNewComment {
		public void onButtonSendCommentClicked(String text);
		public void OnButtonCancelCommentClicked();
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallback = (CallbackNewComment) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement " + CallbackNewComment.class.getName());
		}
	}

	public void onClick(View arg0) {
		if (arg0.equals(sendComment)) {
			if (!text.getText().toString().equals("")) {
				mCallback.onButtonSendCommentClicked(text.getText().toString());

			}
		}
		if (arg0.equals(cancelComment)) {
			
			mCallback.OnButtonCancelCommentClicked();

		}

	}

}