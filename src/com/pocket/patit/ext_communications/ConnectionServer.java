package com.pocket.patit.ext_communications;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.pocket.patit.classes.Constants;
import com.pocket.patit.data.TutorialContent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


/**
 * {@link TutorialContent}
 * 
 * Clase asíncrona que se encarga de realizar la conexión con el servidor, definiendo
 * los parámetros de la conexión, las acciones antes y después de completar la conexión. 
 * Esta clase puede heredarse para definir estos comportamientos.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ConnectionServer extends AsyncTask<ArrayList<String>, Void, JSONObject>{
		
		private JSONObject JSONObjectResult = null;	//Json con el resultado
		private Activity activity;					//actividad
		private List<NameValuePair> params;			//parámetros para la conexión
		private String URL;							//dirección URL
		private JsonServerConnection getJSON = new JsonServerConnection();
		private String URLMethod;					//GET,POST,PUT,DELETE, POSTFILE
		private String file;						//fichero


		/**
		 * Constructor de la clase
		 * @param act
		 */
		public ConnectionServer(Activity act,String URLMethod){
			this.activity = act;
			this.params = new ArrayList<NameValuePair>();
			this.URL = "";
			this.URLMethod=URLMethod;
		}
		
		@Override
		protected void onPreExecute() {
			
		}
		
		public String getURL(){
			return URL;
		}

		/**
		 * Método para añadir pares al método POST
		 * @param key
		 * @param value
		 */
		public void addParam(String key, String value){
			params.add(new BasicNameValuePair(key, value));
		}
		
		/**
		 * Método para definir la URL de la conexión 
		 * @param URL
		 */
		public void defineURL(String URL){
			this.URL = URL;
		}
		
		/**
		 * Método que hace la conexión en segundo plano
		 * @return 
		 */
		@Override
		protected JSONObject doInBackground(ArrayList<String>... params) {
			
			System.out.println("Mandamos el JSON con la info");
			SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCES_LOGIN,
					Context.MODE_PRIVATE);
			String key = preferences.getString(
             		Constants.PREFERENCES_LOGIN_APIKEY,"");
			String user_id = preferences.getString(
             		Constants.PREFERENCES_LOGIN_ID,"");
			
			if(URLMethod.equals(Constants.GET))
				JSONObjectResult = this.getJSON.getJSONObjectGET(this.URL);
			else if(URLMethod.equals(Constants.POST))
				JSONObjectResult = this.getJSON.getJSONObejctPOST(this.URL,this.params,key,user_id);
			else if(URLMethod.equals(Constants.DELETE))
				JSONObjectResult = this.getJSON.getJSONObjectDELETE(this.URL,this.params,key,user_id);
			else if(URLMethod.equals(Constants.POSTFILE))
				JSONObjectResult = this.getJSON.getJSONObejctPOSTFILE(this.URL,this.file);
			else if(URLMethod.equals(Constants.PUT))
				JSONObjectResult = this.getJSON.getJSONObejctPUT(this.URL,this.params,key,user_id);
		
			return JSONObjectResult;
		}
		

		@Override
		protected void onPostExecute(JSONObject result) {
			
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}
	
}
