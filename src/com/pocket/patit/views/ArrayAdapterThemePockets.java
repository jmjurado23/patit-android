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
import com.pocket.patit.classes.PocketTheme;


/**
 * {@link ArrayAdapterThemePockets}
 * 
 * Adaptador de los themas de los bolsillos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ArrayAdapterThemePockets extends ArrayAdapter<PocketTheme>{


	    Context context;
	    LayoutInflater inflater; 
	    int layoutResourceId;    
	    List<PocketTheme> data = null;

	    public ArrayAdapterThemePockets(Context context, int layoutResourceId, List<PocketTheme> list_themes) {
	        super(context, layoutResourceId, list_themes);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = list_themes;
	        this.inflater =   LayoutInflater.from(context);
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        WeatherHolder holder = null;
	        
	        if(row == null)
	        {
	        	row = inflater.inflate(R.layout.row_theme_pocket, null);  
	            
	            holder = new WeatherHolder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.TextViewThemeNewPocket);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (WeatherHolder)row.getTag();
	        }
	        
	        PocketTheme theme = data.get(position);
	        holder.txtTitle.setText(theme.getName());
	        holder.txtTitle.setBackgroundResource(theme.getImage_back());
//	        
	        
			return row;
	    }
	    
	    static class WeatherHolder
	    {
	        ImageView imgIcon;
	        TextView txtTitle;
	    }

	
}
