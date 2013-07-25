package com.pocket.patit.services;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.RemoteViews;

import com.pocket.patit.R;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Object;
import com.pocket.patit.ext_communications.CustomMultiPartEntity;
import com.pocket.patit.ext_communications.CustomMultiPartEntity.ProgressListener;
import com.pocket.patit.ext_communications.HttpClientFactory;
import com.pocket.patit.ext_communications.JsonServerConnection;

/**
 * {@link UploadService}
 * 
 * Fragmento que se encarga de subir los elementos de forma asíncrona como un
 * servicio de android. Se encarga de actualizar la bara de progreso en la notificación
 * que crea.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class UploadService extends IntentService {

	  private int result = Activity.RESULT_CANCELED;
	  private int progress ;
	  private JSONObject JSONObjectResultFile;
	  private JSONObject JSONObjectResultObject;
	  private Messenger messenger;
	  private int postObject;
	  //variables para la barra de progreso de subida
      long totalSize=0;
      int count=0;
      File input;
      InputStream is;
      byte data[] = new byte[1024];//1024
      boolean UPLOADFILE = false;
      

	  public UploadService() {
	    super("UPLOAD");
	  }

	  // Will be called asynchronously be Android
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  
	       final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

	        // configure the notification
	        final Notification notification = new Notification(R.drawable.uploadicon, getString(R.string.uploading), System
	                .currentTimeMillis());
	        
	        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
	        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.upload_notification);
	        notification.contentIntent = pendingIntent;
	        notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.uploadicon);
	        notification.contentView.setTextViewText(R.id.status_text,"Patit "+ getString(R.string.uploading));
	        notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);

	        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
	                getApplicationContext().NOTIFICATION_SERVICE);
	        notificationManager.notify(42, notification);
	        
	        //se obtiene la información de la actividad, tanto el messenger como los datos del fichero
	        Bundle extras = intent.getExtras();
	        
	        if (extras != null) {
	        	
	        	Object fileobject = (Object) extras.get(Constants.FILE);
	        	
	        	JsonServerConnection getJSON = new JsonServerConnection();
		        
		        JSONObject JSONObjectResultFile = null;
		        
		        HttpResponse response;
		        
		        if(!fileobject.getType().equals(Constants.URL)) //si no es una URL, se sube el fichero
		        {	
			    	try {
			    		
			    		//se crea un custom multipart para actualizar la barra de notificaciones con un progress bar
			    		CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new ProgressListener()
						{
							public void transferred(long num)
							{
								notification.contentView.setProgressBar(R.id.status_progress,
										100, (int) ((num / (float) totalSize) * 100),false);
							}
						});
			    		
			    	   HttpClient client = HttpClientFactory.getThreadSafeClient();
			    	   HttpPost requestPOSTFILE = new HttpPost(Constants.API_UPLOAD_FILE);
			    	    
			    	   totalSize = multipartContent.getContentLength();
			           input=new File(fileobject.getData());
			           
			           FileBody fb = null;
			           System.out.println("TYPE " +fileobject.getType());
			           System.out.println("DATA "+fileobject.getData());
			           
			           //si es una imagen, musica o texto, se ejecuta el fichero
			           if(fileobject.getType().equals(Constants.IMAGE))
			        	   fb=new FileBody(input,Constants.TYPE_IMAGE);
			           else if(fileobject.getType().equals(Constants.MUSIC))
			        	   fb=new FileBody(input,Constants.TYPE_MUSIC);
			           else if(fileobject.getType().equals(Constants.TEXT))
			        	   fb=new FileBody(input,Constants.TYPE_TEXT);
			           
			           //se crea un multipart y se envía
			           multipartContent.addPart("file", fb);
			    	   requestPOSTFILE.setEntity(multipartContent);
			    	   requestPOSTFILE.setHeader("Accept", "application/json");
			    	   response = client.execute(requestPOSTFILE); 
			    	   JSONObjectResultFile = getJSON.getJSONObjectFromHttpResult(response); // se pasa la respuesta
	
			    	            
			    	 } catch (Exception e) {
			    	     Log.e("UPLOAD","ERROR FILE");
			    	}
			        
			        System.out.println(JSONObjectResultFile.toString());
			        System.out.println("TODO OK");
			        
			        //se comprueba el JSON que nos ha devuelto y si es correcto se crea el objeto
			        try {
						if( JSONObjectResultFile.getString(Constants.RESULT).equals(Constants.OK)){
							
							UPLOADFILE=true;
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        else
		        	UPLOADFILE = true; //si es una URL, no hace falta subirla al servidor
		        
		        if(UPLOADFILE){
		        	
		        	
					try {
						SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_LOGIN,
								Context.MODE_PRIVATE);
						String key = preferences.getString(
			             		Constants.PREFERENCES_LOGIN_APIKEY,"");
						String user_id = preferences.getString(
			             		Constants.PREFERENCES_LOGIN_ID,"");
						
						String URL = "";
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						
						//se cargan los parámetros
						
						params.add(new BasicNameValuePair(Constants.NAME_OBJECT,fileobject.getName()));
						params.add(new BasicNameValuePair(Constants.TYPE_OBJECT,fileobject.getType()));
						params.add(new BasicNameValuePair(Constants.TYPE_DESCRIPTION_OBJECT,fileobject.getDescript()));
						params.add(new BasicNameValuePair(Constants.POCKET_ID_OBJECT,fileobject.getId_pocket()));
						
						if(!fileobject.getType().equals(Constants.URL)) //si no es una URL, se pasa la dir del archivo
							params.add(new BasicNameValuePair(Constants.URL_OBJECT, JSONObjectResultFile.getString(Constants.ROUTE_OBJECT)));
						else
							params.add(new BasicNameValuePair(Constants.URL_OBJECT, fileobject.getData()));
						
						
						params.add(new BasicNameValuePair(Constants.NICK,preferences.getString(Constants.PREFERENCES_LOGIN_NICK,"")));
						
						URL = Constants.API_NEWOBJECT;
						
						//se realiza la segunda conexión
						JSONObjectResultObject = getJSON.getJSONObejctPOST(URL,params,key,user_id);
						
						if( JSONObjectResultObject.getString(Constants.RESULT).equals(Constants.OK)){
							
							messenger = (Messenger) extras.get(Constants.MESSENGER);
						    Message msg = Message.obtain();
						    msg.arg1 = Constants.MSG_OK;
						 
						    try {
						        messenger.send(msg);
						      } catch (android.os.RemoteException e1) {
						        Log.w(getClass().getName(), "Exception sending message", e1);
						      }
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		        }
		        
	        }
	        
	        
	        
	        //se cancela la notificación
	        notificationManager.cancel(42);

	  }

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * @return the jSONObjectResultFile
	 */
	public JSONObject getJSONObjectResultFile() {
		return JSONObjectResultFile;
	}

	/**
	 * @param jSONObjectResultFile the jSONObjectResultFile to set
	 */
	public void setJSONObjectResultFile(JSONObject jSONObjectResultFile) {
		JSONObjectResultFile = jSONObjectResultFile;
	}

	/**
	 * @return the postObject
	 */
	public int getPostObject() {
		return postObject;
	}

	/**
	 * @param postObject the postObject to set
	 */
	public void setPostObject(int postObject) {
		this.postObject = postObject;
	}
	  
	 
	  
	 
} 