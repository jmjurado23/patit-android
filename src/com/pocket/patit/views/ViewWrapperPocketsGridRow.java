package com.pocket.patit.views;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.patit.R;

/**
 * {@link ViewWrapperPocketsGridRow}
 * 
 * Wrapper para rellenar una lista
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewWrapperPocketsGridRow {
	View base;
	TextView title = null;
	ImageView image = null;
	TextView user = null;
	TextView nelements = null;
	TextView new_comment = null;
	ImageView imageFeatured = null;

	public TextView getNew_comment() {
		if (new_comment == null) {
			new_comment = (TextView) base.findViewById(R.id.textYourGridNewComment);
		}
		return new_comment;
	}


	public ViewWrapperPocketsGridRow(View base) {
		this.base = base;
	}

	public ImageView getImgFeaTitle() {
		if (imageFeatured == null) {
			imageFeatured = (ImageView) base.findViewById(R.id.imageViewFeaturedGrid);
		}
		return (imageFeatured);
	}
	public TextView getTitle() {
		if (title == null) {
			title = (TextView) base.findViewById(R.id.textViewTitlePocketGrid);
		}
		return (title);
	}
	public TextView getUser() {
		if (user == null) {
			user = (TextView) base.findViewById(R.id.textViewUserPocketGrid);
		}
		return (user);
	}
	public TextView getNElements() {
		if (nelements == null) {
			nelements = (TextView) base.findViewById(R.id.textViewNElementsPocketGrid);
		}
		return (nelements);
	}

	public ImageView getImage() {
		if (image == null) {
			image = (ImageView) base.findViewById(R.id.imageViewPocketGrid);
		}
		return (image);
	}
	
	

}
