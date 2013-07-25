package com.pocket.patit.views;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.ElementType;

/**
 * {@link ArrayAdapterElementType}
 * 
 * Adaptador de los tipos de elementos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ArrayAdapterElementType extends ArrayAdapter<ElementType>{


	    Context context;				//contexto
	    LayoutInflater inflater; 		//inflador
	    int layoutResourceId;    		//resource id
	    List<ElementType> data = null;	//data

	    public ArrayAdapterElementType(Context context, int layoutResourceId, List<ElementType> elements) {
	        super(context, layoutResourceId, elements);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = elements;
	        this.inflater =   LayoutInflater.from(context);
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        WeatherHolder holder = null;
	        
	        if(row == null)
	        {
	        	row = inflater.inflate(R.layout.row_new_element_pocket, null);  
	            
	            holder = new WeatherHolder();
	            holder.txtTitle = (TextView) row.findViewById(R.id.TextViewNewElement);
	            holder.imgIcon = (ImageView) row.findViewById(R.id.imageViewNewElement);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (WeatherHolder)row.getTag();
	        }
	        
	        ElementType item = data.get(position);
	        holder.txtTitle.setText(item.getName());
	        holder.imgIcon.setImageResource(item.getImg());
	        
			return row;
	    }
	    
	    static class WeatherHolder
	    {
	        ImageView imgIcon;
	        TextView txtTitle;
	    }

	
}
