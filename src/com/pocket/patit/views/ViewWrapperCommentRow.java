package com.pocket.patit.views;


import com.pocket.patit.R;

import android.view.View;
import android.widget.TextView;

/**
 * {@link ViewWrapperCommentRow}
 * 
 * Wrapper de comentarios
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ViewWrapperCommentRow {
	View base;
	TextView title = null;
	TextView subtitle = null;
	TextView date = null;

	public ViewWrapperCommentRow(View base) {
		this.base = base;
	}

	public TextView getTitle() {
		if (title == null) {
			title = (TextView) base.findViewById(R.id.commentowtitle);
		}
		return (title);
	}

	public TextView getSubTitle() {
		if (subtitle == null) {
			subtitle = (TextView) base.findViewById(R.id.commentrowsubtitle);
		}
		return (subtitle);
	}
	public TextView getDate() {
		if (date == null) {
			date = (TextView) base.findViewById(R.id.commentrowdate);
		}
		return (date);
	
	}
	


}
