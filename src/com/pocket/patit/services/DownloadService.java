package com.pocket.patit.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;

/**
 * {@link DownloadService}
 * 
 * Fragmento que se encarga de descargar los elementos de forma asíncrona como un
 * servicio de android. Se encarga de actualizar la bara de progreso en la notificación
 * que crea.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DownloadService extends IntentService {
	
      private long totalSize=0;		//tamaño total
      private int count=0;			//contador
      private File input;			//fichero para almacenar el fichero
      private InputStream is;		
      private boolean UPLOADFILE = false;
      private NotificationManager notificationManager; //notificación
      private Notification notification;			   //notificación

	  public DownloadService() {
	    super("UPLOAD");
	  }

	  @SuppressWarnings("deprecation")
	@Override
	  protected void onHandleIntent(Intent intent) {
		  
		  final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

          // configure the notification
          notification = new Notification(R.drawable.downloadicon, getString(R.string.dowloading), System.currentTimeMillis());
          notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
          notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.download_notification);
          notification.contentIntent = pendingIntent;
          notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.downloadicon);
          notification.contentView.setTextViewText(R.id.status_text,"Patit "+ getString(R.string.downloading));
          notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);
          getApplicationContext();
          notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
          notificationManager.notify(43, notification);
	        
	        //se obtiene la información
	        //de la actividad, tanto el messenger como los datos del fichero
	        Bundle extras = intent.getExtras();
	        if (extras != null) {
	        	
	        
	        try {
	            String destination = (String) extras.get(Constants.DESTINATION);
	            URL url = new URL((String)extras.get(Constants.URL));
	            System.out.println("DONWLOADING FILE");
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setDoOutput(true);
	            urlConnection.connect();
	            
	            // this will be useful so that you can show a typical 0-100% progress bar
	            int fileLength = urlConnection.getContentLength();

	            // download the file
	            InputStream input = new BufferedInputStream(url.openStream());
	            OutputStream output = new FileOutputStream(destination);

	            byte data[] = new byte[512];
	            long total = 0;
	            int count;
	            Thread act = new Thread(runnable);
	            act.start();
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                notification.contentView.setProgressBar(R.id.status_progress, 100, (int) (total * 100 / fileLength), true);
	                output.write(data,0, count);
	            }
	           
	            //se cancela la notificación
		        notificationManager.cancelAll();
	            try{
	            	act.interrupt();
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            System.out.println("DESTINATION "+ destination);
	            output.flush();
	            output.close();
	            input.close();
	            
		        
		        final Notification notification2 = new Notification(R.drawable.downloadicon, getString(R.string.downloaded), System
		                .currentTimeMillis());
		       
		        notification2.flags = notification2.flags | Notification.FLAG_AUTO_CANCEL;
		        notification2.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.simple_notification);
		        notification2.contentIntent = pendingIntent;
		        notification2.contentView.setImageViewResource(R.id.status_icon, R.drawable.downloadicon);
		        notification2.contentView.setTextViewText(R.id.status_text,getString(R.string.completedownload));
		        notification2.contentView.setTextViewText(R.id.status_text2,destination);
		        
		        getApplicationContext();
				final NotificationManager notificationManager2 = (NotificationManager) getApplicationContext().getSystemService(
		                Context.NOTIFICATION_SERVICE);
		        notificationManager2.notify(44, notification2);
		        
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	System.out.println("ERROR");
	        	notificationManager.cancel(43);
	        }
	     }
	  } 
	  /**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(File input) {
		this.input = input;
	}
	/**
	 * @return the is
	 */
	public InputStream getIs() {
		return is;
	}

	/**
	 * @param is the is to set
	 */
	public void setIs(InputStream is) {
		this.is = is;
	}
	/**
	 * @return the uPLOADFILE
	 */
	public boolean isUPLOADFILE() {
		return UPLOADFILE;
	}

	/**
	 * @param uPLOADFILE the uPLOADFILE to set
	 */
	public void setUPLOADFILE(boolean uPLOADFILE) {
		UPLOADFILE = uPLOADFILE;
	}
	private Runnable runnable = new Runnable() {
		public void run() {
			while(true){
				notificationManager.notify(43, notification);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
} 