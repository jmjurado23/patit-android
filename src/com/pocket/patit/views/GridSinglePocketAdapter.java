package com.pocket.patit.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;

/**
 * {@link Grid de objetos}
 * 
 * Adaptador del grid de los bolsillos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class GridSinglePocketAdapter extends BaseAdapter {

	public List<Object> list_objects;
	private Context mContext;


	// Gets the context so it can be used later
	public GridSinglePocketAdapter(Context c, List<Object> list) {
		mContext = c;
		list_objects = list;
	}

	// Require for structure, not really used in my code.
	public Object getItem(int position) {
		return list_objects.get(position);
	}

	// Require for structure, not really used in my code. Can
	// be used to get the id of an item in the adapter for
	// manual control.
	public long getItemId(int position) {
		return position;
	}

	 
	 
	public View getView(int position, View convertView, ViewGroup parent) {
	  
      View v;
      if(convertView==null)
      {
          LayoutInflater li = LayoutInflater.from(mContext);
          v = li.inflate(R.layout.element_objects_grid, null);
      }else{
          v = convertView;
      }
      
      ViewWrapperPocketsGridRow wrapper = new ViewWrapperPocketsGridRow(v);
 
      
      ImageView iv = wrapper.getImage();
      if (list_objects.get(position).getType().equals(Constants.IMAGE))
    	  iv.setImageResource(R.drawable.imagegrid);
      else if (list_objects.get(position).getType().equals(Constants.MUSIC))
    	  iv.setImageResource(R.drawable.musicgrid);
      else if (list_objects.get(position).getType().equals(Constants.TEXT))
    	  iv.setImageResource(R.drawable.textgrid);
      else if (list_objects.get(position).getType().equals(Constants.URL))
    	  iv.setImageResource(R.drawable.urlgrid);
      
    //se define el título, el número de elementos y el usuario
      TextView tv = wrapper.getTitle();
      tv.setSelected(true);
	  tv.setText(list_objects.get(position).getName());
      TextView uv = wrapper.getUser();
      uv.setSelected(true);
      uv.setText(list_objects.get(position).getDescript());
      TextView nev = wrapper.getNElements();
      
      return v;
	}

	public int getCount() {
		if(list_objects == null)
			return 0;
		else
			return list_objects.size();
	}
	
	
}
