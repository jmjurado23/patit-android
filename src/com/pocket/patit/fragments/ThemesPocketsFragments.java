package com.pocket.patit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pocket.patit.R;
import com.pocket.patit.classes.PocketTheme;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.views.ArrayAdapterThemePockets;

/**
 * {@link ThemesPocketsFragments}
 * 
 * Fragmento que muestra una lista con los temas de los bolsillos disponibles en 
 * la aplicación
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ThemesPocketsFragments extends Fragment implements OnItemClickListener{
	
	ListView list;						//lista
	List<PocketTheme> list_themes;		//lista con los temas
	ArrayAdapterThemePockets adapter;	//adaptador
	int selected_item = 0;				//elemento seleccionado
	CallbackThemePocket mCallback;		//callback
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_themes_new_pocket, container, false);
    	list = (ListView) view.findViewById(R.id.listViewThemesPockets);
    
    	
    	list_themes = new ArrayList<PocketTheme>();
    	chargeThemes();
    	
    	 adapter = new ArrayAdapterThemePockets(getActivity().getApplicationContext(), 
                R.layout.row_theme_pocket, list_themes);
    	
    	
    	View header = (View) getActivity().getLayoutInflater().inflate(R.layout.row_theme_pocket_header, null);
    	list.addHeaderView(header);
        
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        
        // Inflate the layout for this fragment
        return view;
    }
    
    /**
     * Método para cargar los temas
     */
    private void chargeThemes() {
    	PocketThemeData data = new PocketThemeData(getActivity());
    	list_themes = data.getThemes();
	}

    public interface CallbackThemePocket {
        public void onButtonBClicked(int number);
    }
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		mCallback.onButtonBClicked((int) arg3);	
		
	}
	
	@Override
	public void onAttach(Activity activity) {

	    super.onAttach(activity);
	    try {
	        mCallback = (CallbackThemePocket) activity;
	    }
	    catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement " + CallbackThemePocket.class.getName());
	    }
	}
	  
		public int getSelected_item() {
			return selected_item;
		}
		public void setSelected_item(int selected_item) {
			this.selected_item = selected_item;
		}
    

}