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
import com.pocket.patit.classes.ElementType;
import com.pocket.patit.views.ArrayAdapterElementType;

/**
 * {@link NewElementPocketFragments}
 * 
 * Clase Fragmento que muestra un menú con los tipos de objeto que se puede crear en la aplicación.
 * Lo que se muestra es una lista simple. Esto se puede modificar para crear otros diseños
 * a la hora de presentar los elementos que se pueden crear.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class NewElementPocketFragments extends Fragment implements OnItemClickListener{
	
	ListView list;						//lista para la vista
	List<ElementType> elements; 		//lista con los elementos
	ArrayAdapterElementType adapter;	//adaptador de la lista
	int selected_item = 0;				//objeto seleccionado
	CallbackElementType mCallback;		//respuesta del fragmento
	
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_new_element_pocket, container, false);
    	list = (ListView) view.findViewById(R.id.listViewNewElement);
    
    	
    	elements = new ArrayList<ElementType>();
    	chargeElements();
    	
    	adapter = new ArrayAdapterElementType(getActivity().getApplicationContext(), 
                R.layout.row_theme_pocket, elements);
    	
    	
    	View header = (View) getActivity().getLayoutInflater().inflate(R.layout.row_new_element_pocket_header, null);
    	list.addHeaderView(header);
        
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        
        // Inflate the layout for this fragment
        return view;
    }
    
	public int getSelected_item() {
		return selected_item;
	}
	public void setSelected_item(int selected_item) {
		this.selected_item = selected_item;
	}
    
    /**
     * Método para cargar los elementos de la lista con los tipos de objeto
     */
    private void chargeElements() {
    	elements.add(new ElementType(getActivity().getString(R.string.img_type)
    			, R.drawable.imageicon));
    	elements.add(new ElementType(getActivity().getString(R.string.msc_type)
    			, R.drawable.soundicon));
    	elements.add(new ElementType(getActivity().getString(R.string.txt_type)
    			, R.drawable.texticon));
    	elements.add(new ElementType(getActivity().getString(R.string.url_type)
    			, R.drawable.wwwicon));
    	
	}

    /**
     * Respuesta con lo que se ha pulsado en el fragmento
     * @author Juan Manuel Jurado
     *
     */
    public interface CallbackElementType {
        public void onButtonElementClicked(int number);
    }
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		System.out.println((int) arg3);
		mCallback.onButtonElementClicked((int) arg3);	
		
		
	}
	
	@Override
	public void onAttach(Activity activity) {

	    super.onAttach(activity);
	    try {
	        mCallback = (CallbackElementType) activity;
	    }
	    catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement " + CallbackElementType.class.getName());
	    }
	}
    

}