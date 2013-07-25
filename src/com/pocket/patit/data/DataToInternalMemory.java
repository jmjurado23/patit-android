package com.pocket.patit.data;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.pocket.patit.classes.Constants;

/**
 * {@link DataToInternalMemory}
 * 
 * Clase que alamacena en memoria de la aplicación los datos de los bolsillos de usuario
 * así como información de los bolsillos destacados. Esto se utiliza para poder ser accedida 
 * cuando no hay conexión para utilizar la aplicación.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class DataToInternalMemory {

	private static SharedPreferences preferences;		//preferencias de la aplicación
	private static SharedPreferences.Editor editor;		//editor para editar las preferencias
	private Context context;							//contexto de la aplicación
	
	/**
	 * Constructor parametrizado
	 * @param context
	 */
	public DataToInternalMemory(Context context) {
		super();
		//se cargan las preferencias y el editor
		preferences = context.getSharedPreferences(Constants.SHAREDPREFERENCES_DATASTORAGE, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	/**
	 * Método para obtener un json con los bolsillos a partir de las preferencias internas de la
	 * aplicación. Con esto después de pasarlo con un parser se puede sacar la información
	 * y mantenerla entre sesiones de la aplicación
	 * @return json con la información de las preferencias
	 */
	public JSONObject getDataJSONFromSharedPreferences() {
		
		JSONObject json = null;
		String str = "";
		str = preferences.getString(Constants.DATAJSON, "");
		
		if (!str.equals("")) {
			try {
				json = new JSONObject(str);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;

	}

	/**
	 * Método que almacena un json en las preferencias del sistema
	 * @param json
	 */
	public static void saveJSONDataIntoSharedPreferences(JSONObject json) {
		
		editor.putString(Constants.DATAJSON, json.toString());
		editor.commit();
		
	}

	/**
	 * Método para almacenar un json en la memoria del sistema en forma de archivo.
	 * @param json	json
	 * @param category	your,spon,featu,spo,search,etc
	 * @return resultado de l aoperación
	 */
	public boolean JSONDataToMemory(JSONObject json, int category) {

		// dependiendo de la categoría, almacenamos en uno de los directorios
		switch (category) {
		case 0: // YOUR_POCKETS category

			FileOutputStream fout = null;
			try {

				fout = context.openFileOutput(Constants.DIR_IN_MEM_YOURPOCKETS,
						Context.MODE_PRIVATE);
				OutputStreamWriter ows = new OutputStreamWriter(fout);

				ows.write(json.toString()); // Escribe en el buffer la cadena de
											// texto
				ows.flush(); // Volca lo que hay en el buffer al archivo
				ows.close(); // Cierra el archivo de texto

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Método para obtener la información proveniente de un archivo a un tipo json
	 * @param category
	 * @return	
	 */
	public JSONObject DataMemoryToJSON(int category) {

		JSONObject json = null;

		switch (category) {
		case 0:
			try {

				// Se lee el archivo de texto indicado
				FileInputStream fin = context
						.openFileInput(Constants.DIR_IN_MEM_YOURPOCKETS);
				InputStreamReader isr = new InputStreamReader(fin);

				char[] inputBuffer = new char[100];
				String str = "";

				// Se lee el archivo de texto mientras no se llegue al final de
				// él
				int charRead;
				while ((charRead = isr.read(inputBuffer)) != 0) {
					String strRead = String.copyValueOf(inputBuffer, 0,
							charRead);
					str += strRead;

					inputBuffer = new char[100];
				}
				// se cierra el flujo
				isr.close();

				// se pasa la cadena a JSON
				json = new JSONObject(str);

			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		return json;
	}
}
