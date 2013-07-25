package com.pocket.patit;

/**
 * ItemDetailsFragment.java
 * 
 * Fragmento empleado para mostrar cada página individual del tutorial. Este fragmento
 * utiliza el layout fragment_item_detail es usado de distinta forma según la 
 * resolución del dispositivo.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
import com.pocket.patit.R;
import com.pocket.patit.data.TutorialContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    
    //se cargan lso elementos del tutorial
    TutorialContent.TutorialItem mItem;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = TutorialContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	//se define cada elemento del layout con unos valores predefinidos.
    	//los valores se cargan del fichero TutorialContent en el paquete data
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        if (mItem != null) {
        	//Título de la página del tutorial
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.content);
            //Primer párrafo del tutorial
            ((TextView) rootView.findViewById(R.id.item_detail_content)).setText(mItem.content2);
            //Segundo párrafo del tutorial
            ((TextView) rootView.findViewById(R.id.item_detail_content_two)).setText(mItem.content3);
            //Tipo de imagen del margen blue,red,yellow,etc...
            ((LinearLayout) rootView.findViewById(R.id.tutotype)).setBackgroundResource(mItem.image_type);
            //imagen del tutorial explicativa
            ((ImageView) rootView.findViewById(R.id.imageViewTuto1)).setBackgroundResource(mItem.image_tuto);
            
        }
        return rootView;
    }
}
