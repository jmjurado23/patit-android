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
 * {@link ArrayAdapterThemeInfoUser}
 * 
 * Adaptador de los themas de los bolsillos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ArrayAdapterThemeInfoUser extends ArrayAdapter<PocketTheme>{


	    Context context;
	    LayoutInflater inflater; 
	    int layoutResourceId;    
	    List<PocketTheme> data = null;
	    List<Integer> dataNumber = null;
	    
	    public ArrayAdapterThemeInfoUser(Context context, int layoutResourceId, List<PocketTheme> list_themes) {
	        super(context, layoutResourceId, list_themes);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = list_themes;
	        this.inflater =   LayoutInflater.from(context);
	        System.out.println("SE CREA");
	    }

	    public void setListNumber(List<Integer> number){
	    	this.dataNumber = number;
	    }
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        Holder holder = null;
	        
	        if(row == null)
	        {
	        	row = inflater.inflate(R.layout.row_theme_info_user, null);  
	            
	            holder = new Holder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.TextViewThemeNewPocket);
	            holder.txtNumber = (TextView) row.findViewById(R.id.TextViewRowThemeInfoUser);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (Holder)row.getTag();
	        }
	        PocketTheme theme = data.get(position);
	        holder.txtTitle.setText(theme.getName());
	        if(dataNumber.get(position)==1)
	        	holder.txtNumber.setText(Integer.toString(dataNumber.get(position))+ " " +context.getString(R.string.spocket));
	        else
	        	holder.txtNumber.setText(Integer.toString(dataNumber.get(position))+ " " +context.getString(R.string.pockets));
	        holder.txtTitle.setBackgroundResource(theme.getImage_back());
//	        
	        
			return row;
	    }
	    
	    static class Holder
	    {
	        ImageView imgIcon;
	        TextView txtTitle;
	        TextView txtNumber;
	    }

	
}
