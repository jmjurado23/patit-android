package com.pocket.patit.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.data.PocketThemeData;

/**
 * {@link GridPocketsAdapter}
 * 
 * Adaptador del grid de los bolsillos
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class GridPocketsAdapter extends BaseAdapter {

	public List<Pocket> list_pocket;
	private Context mContext;
	private int last_click;
	private int pocketsSource;
	

	// Gets the context so it can be used later
	public GridPocketsAdapter(Context c, List<Pocket> list, Activity instance,int pocketSource) {
		mContext = c;
		list_pocket = list;
		this.last_click = -1;
		this.pocketsSource = pocketSource;
		PocketThemeData theme = new PocketThemeData(c);
	}

	// Require for structure, not really used in my code.
	public Object getItem(int position) {
		return list_pocket.get(position);
	}

	// Require for structure, not really used in my code. Can
	// be used to get the id of an item in the adapter for
	// manual control.
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	  
      View v;
      final CheckableLayout l;
      final int pos = position;
      
      if(convertView==null)
      {
          LayoutInflater li = LayoutInflater.from(mContext);
          switch(pocketsSource){
          	case 0:
          		v = li.inflate(R.layout.element_your_pockets_grid, null);
          		break;
          	case 1:
          		v = li.inflate(R.layout.element_random_pockets_grid, null);
          		break;
          	case 2:
          		v = li.inflate(R.layout.element_featured_pockets_grid, null);
          		break;
          	case 3:
          		v = li.inflate(R.layout.element_sponsored_pockets_grid, null);
          		break;
          	case 4:
          		v = li.inflate(R.layout.element_search_pockets_grid, null);
          		break;
          	default:
          		v = li.inflate(R.layout.element_your_pockets_grid, null);
          		break;
          }
          
          l = new CheckableLayout(mContext);
          l.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
                  GridView.LayoutParams.WRAP_CONTENT));
          l.addView(v);
      }else{
    	  l = (CheckableLayout) convertView;
          v = convertView;
      }
      
      ViewWrapperPocketsGridRow wrapper = new ViewWrapperPocketsGridRow(v);
      
      TextView new_c = wrapper.getNew_comment();
      ImageView imgFea = wrapper.getImgFeaTitle();
      
      //si es el bolsillo
      if(list_pocket.get(position).getNew_com() != 0){
    	  //si hay comentarios, se muestran 
    	  new_c.setText(Integer.toString(list_pocket.get(position).getNew_com()));
    	  new_c.setBackgroundColor(stringToColor("CCCC0000"));
    	  new_c.setTextColor(stringToColor("FFFFFFFF"));
      }
      else{ //si no hay comentarios, se ponen lso elementos transparentes
    	  new_c.setTextColor(stringToColor("00FFFFFF"));
    	  new_c.setBackgroundColor(stringToColor("00CC0000"));
      }
      if(list_pocket.get(position).isFeatured())
    	  imgFea.setVisibility(View.VISIBLE);
      else
    	  imgFea.setVisibility(View.GONE);
    	  
      
      //se define el título, el número de elementos y el usuario
      TextView tv = wrapper.getTitle();
      tv.setSelected(true);
	  tv.setText(list_pocket.get(position).getName());
      TextView uv = wrapper.getUser();
      uv.setSelected(true);
      uv.setText(list_pocket.get(position).getUser());
      TextView nev = wrapper.getNElements();
      nev.setText(Integer.toString(list_pocket.get(position).getNObjects()) +" "+ mContext.getString(R.string.elements));
      
      ImageView iv = wrapper.getImage();
      iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
      
      //definimos el tema de la imagen
      iv.setBackgroundResource(PocketThemeData.getThemeImgResourceMainPocket(list_pocket.get(position).getType() ));
     
      return l;
	}
	
	private int stringToColor(String paramValue)
	{
		int alpha;
		int red;
		int green;
		int blue;

		alpha = (Integer.decode("0x" + paramValue.substring(0,2))).intValue();
		red = (Integer.decode("0x" + paramValue.substring(2,4))).intValue();
		green = (Integer.decode("0x" + paramValue.substring(4,6))).intValue();
		blue = (Integer.decode("0x" + paramValue.substring(6,8))).intValue();
		
    return Color.argb(alpha, red, green, blue);
  }
	
	public  int getLastClick(){
		return last_click;
	}
	
	public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackgroundDrawable(checked ?
                    getResources().getDrawable(R.drawable.selectedpocket)
                    : null);
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }
	public int getCount() {
		if(list_pocket!=null)
			return list_pocket.size();
		else
			return 0;
	}
}
