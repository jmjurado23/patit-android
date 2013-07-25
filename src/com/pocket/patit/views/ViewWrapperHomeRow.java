package com.pocket.patit.views;


import com.pocket.patit.R;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * {@link ViewWrapperHomeRow}
 * 
 * Wrapper de los elementos de la lista que se muestra en home
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewWrapperHomeRow {
	View base;
	TextView title = null;
	TextView subtitle = null;
	ImageView image = null;
	LinearLayout color = null;

	public ViewWrapperHomeRow(View base) {
		this.base = base;
	}

	public TextView getTitle() {
		if (title == null) {
			title = (TextView) base.findViewById(R.id.homerowtitle);
		}
		return (title);
	}

	public TextView getSubTitle() {
		if (subtitle == null) {
			subtitle = (TextView) base.findViewById(R.id.homerowsubtitle);
		}
		return (subtitle);
	}
	
	public ImageView getImage() {
		if (image == null) {
			image = (ImageView) base.findViewById(R.id.imageView23);
		}
		return (image);
	}
	public LinearLayout getColor() {
		if (color == null) {
			color = (LinearLayout) base.findViewById(R.id.homelayoutcolor);
		}
		return (color);
	}


}
