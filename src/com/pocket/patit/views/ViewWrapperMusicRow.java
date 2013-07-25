package com.pocket.patit.views;


import com.pocket.patit.R;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ViewWrapperMusicRow}
 * 
 * Wrapper de los elementos que se muestran en el selector de música
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewWrapperMusicRow {
	View base;
	TextView label = null;
	TextView label2 = null;
	ImageView icon = null;

	public ViewWrapperMusicRow(View base) {
		this.base = base;
	}

	public TextView getTitle() {
		if (label == null) {
			label = (TextView) base.findViewById(R.id.TextViewMusicTitle);
		}
		return (label);
	}

	public TextView getSubTitle() {
		if (label2 == null) {
			label2 = (TextView) base.findViewById(R.id.TextViewMusicSubtitle);
		}
		return (label2);
	}


}
