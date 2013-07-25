package com.pocket.patit.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.classes.Comment;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.DateToString;

/**
 * {@link ArrayAdapterComments}
 * 
 * Adaptador de los comentarios a las listas de comentarios
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ArrayAdapterComments extends ArrayAdapter<Comment>{


	    Context context;				//contexto
	    LayoutInflater inflater; 		//inflador
	    int layoutResourceId;    		//id
	    List<Comment> data = null;		//data
	    SimpleDateFormat format = null;	//formato fecha
        Date date=null; 				//fecha
        Calendar cal = null;			//calendario

	    public ArrayAdapterComments(Context context, int layoutResourceId, List<Comment> elements) {
	        super(context, layoutResourceId, elements);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = elements;
	        this.inflater =   LayoutInflater.from(context);
	        this.format = new SimpleDateFormat(Constants.DATEFORMAT); //formato en que viene tu fecha
	        this.cal = Calendar.getInstance();
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ViewWrapperCommentRow holder = null;
	        
	        if(row == null)
	        {
	        	row = inflater.inflate(R.layout.row_comment_pocket, null);  
	            
	            holder = new ViewWrapperCommentRow(row);
	            holder.subtitle = (TextView) row.findViewById(R.id.commentowtitle);
	            holder.title = (TextView) row.findViewById(R.id.commentrowsubtitle);
	            holder.date = (TextView) row.findViewById(R.id.commentrowdate);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ViewWrapperCommentRow)row.getTag();
	        }
	        
	        Comment item = data.get(position);
	        holder.title.setText(item.getUser());
	        holder.subtitle.setText(item.getComments());
	        
	        
	        String date = item.getDate();
	        date = DateToString.getDay(date) +"/"+DateToString.getMonth(date) +"/"+
	        		DateToString.getYear(date) +"-"+ DateToString.getHour(date) +":"+DateToString.getMinute(date);
	        
	        holder.date.setText(date);        

			return row;
	    }
	    
	    

	
}
