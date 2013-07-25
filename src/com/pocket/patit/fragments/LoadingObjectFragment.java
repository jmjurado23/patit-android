package com.pocket.patit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pocket.patit.R;

/**
 * {@link LoadingObjectFragment}
 * 
 * Clase Fragmento que muestra un progreso de diálogo. Se puede usar para mostrar un barra
 * de diálogo sobre la pantalla del dispositivo
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class LoadingObjectFragment extends Fragment {

	
	
	private ProgressBar progressLoading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_loadging_object, container,
				false);
		
        progressLoading = (ProgressBar) view.findViewById(R.id.progressBarViewObject);
		progressLoading.setMax(100);
		
		return view;
	}
	
	public void setProgress(int progress){
		progressLoading.setProgress(progress);
	}


}