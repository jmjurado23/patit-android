package com.pocket.patit.ext_communications;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.pocket.patit.classes.Constants;

/**
 * {@link JsonServerConnection}
 * 
 * Clase que define y realiza las conexiones con internet. Esta clase se
 * especializa en el tratado de las conexiones que se realizan pidiendo un texto
 * en formato JSON como respuesta. Tiene métodos para realizar conexiones,
 * GET,POST,PUT,DELETE e incluso para subir ficheros multipart, pero siempre con
 * un JSON como respuesta.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class JsonServerConnection {

	private HttpResponse response;

	/**
	 * Método que obtiene un objeto JSON a partir de una respuesta del server
	 * http
	 * 
	 * @param response
	 * @return objeto JSON
	 */
	public JSONObject getJSONObjectFromHttpResult(HttpResponse response) {

		JSONObject jsonO = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));

			String buffer, json = null;
			json = "";

			while ((buffer = reader.readLine()) != null) {
				json = json + buffer;
			}
			System.out.println(json);
			jsonO = new JSONObject(json);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonO;
	}

	/**
	 * Método que obtiene un objeto JSON a partir de una petición tipo GET
	 * 
	 * @param URL
	 * @return
	 */
	public JSONObject getJSONObjectGET(String URL) {

		// se crea el JSON para recibir
		JSONObject json = null;

		// se crea el cliente HTTP
		HttpClient client = HttpClientFactory.getThreadSafeClient();

		// se crea la petición GET
		HttpGet requestGET = new HttpGet();

		try {
			// se define la URI de la dirección
			requestGET.setURI(new URI(URL));

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// se ejecuta la respuesta
		try {
			HttpResponse response = client.execute(requestGET);
			json = getJSONObjectFromHttpResult(response); // se pasa la
															// respuesta
															// a json

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Método que obtiene un objeto JSON a partir de una petición tipo GET
	 * 
	 * @param URL
	 * @param POSTParameters
	 * @param key
	 * @param user_id
	 * @return
	 */
	public JSONObject getJSONObejctPOST(String URL,
			List<NameValuePair> POSTParameters, String key, String user_id) {

		// se crea el JSON para recibir
		JSONObject json = null;

		// se crea el cliente HTTP
		HttpClient client = HttpClientFactory.getThreadSafeClient();

		// se crea la petición GET
		HttpPost requestPOST = new HttpPost(URL);

		try {
			// se crea la respuesta
			requestPOST.setHeader("Content-type", "application/json");
			requestPOST.setHeader("Accept", "application/json");
			requestPOST.setHeader(Constants.HEADER_USER_ID, user_id);
			requestPOST.addHeader(Constants.HEADER_API_KEY, key);

			JSONObject obj = new JSONObject();
			// se meten todos los parámetros
			for (int i = 0; i < POSTParameters.size(); i++)
				obj.put(POSTParameters.get(i).getName(), POSTParameters.get(i)
						.getValue());

			// JSONObject obj = new JSONObject();
			// try {
			//
			//
			// //obtenemos la clave secreta de shared preferences
			//
			// SecretKeySpec keySpec = new SecretKeySpec(
			// key.getBytes(),
			// "HmacSHA1");
			//
			// Mac mac;
			// byte[] result = null;
			// try {
			// mac = Mac.getInstance("HmacSHA1");
			// mac.init(keySpec);
			// System.out.println("RES= "+obj.toString()+key);
			// System.out.println("RES Byte= "+
			// (obj.toString()+key).getBytes());
			// result = mac.doFinal( (obj.toString()+key).getBytes());
			//
			// } catch (NoSuchAlgorithmException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InvalidKeyException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			//
			// System.out.println(Base64.encode(result, 0));
			// //se mete un resumen con la clave privada y el json formado
			//
			// obj.put(Constants.API_KEY,Base64.encode(result, 0));
			//
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			System.out.println("JSON+ " + obj.toString());
			requestPOST.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			// requestPOST.setEntity(new UrlEncodedFormEntity(POSTParameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("Obtenemos la respuesta del servidor");
			HttpResponse response = client.execute(requestPOST);

			json = getJSONObjectFromHttpResult(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Método que obtiene un objeto JSON a partir de una petición tipo DELETE
	 * 
	 * @param url
	 * @param params
	 * @param key
	 * @param user_id
	 * @return
	 */
	public JSONObject getJSONObjectDELETE(String url,
			List<NameValuePair> params, String key, String user_id) {
		// se crea el JSON para recibir
		JSONObject json = null;

		// se crea el cliente HTTP
		HttpClient client = HttpClientFactory.getThreadSafeClient();

		// se crea la petición DELETE
		HttpDelete requestDELETE = new HttpDelete();
		requestDELETE.setHeader("Content-type", "application/json");
		requestDELETE.setHeader("Accept", "application/json");
		requestDELETE.setHeader(Constants.HEADER_USER_ID, user_id);
		requestDELETE.addHeader(Constants.HEADER_API_KEY, key);

		try {
			// se define la URI de la dirección
			requestDELETE.setURI(new URI(url));

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// se ejecuta la respuesta
		try {
			HttpResponse response = client.execute(requestDELETE);
			System.out.println("Respuesta: " + response.toString());
			json = getJSONObjectFromHttpResult(response); // se pasa la
															// respuesta
															// a json

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Método para subir un fichero al servidor. La conexión es de tipo
	 * multipart
	 * 
	 * @param url
	 *            del servidor
	 * @param file
	 *            Fichero a subir al servidor
	 * @return
	 */
	public JSONObject getJSONObejctPOSTFILE(String url, String file) {

		JSONObject json = null;

		try {
			HttpClient client = HttpClientFactory.getThreadSafeClient();
			HttpPost requestPOSTFILE = new HttpPost(url);

			MultipartEntity mpEntity = new MultipartEntity();
			mpEntity.addPart("file", new FileBody(new File(file), "image/jpeg"));
			requestPOSTFILE.setEntity(mpEntity);
			requestPOSTFILE.setHeader("Accept", "application/json");

			response = client.execute(requestPOSTFILE);

			json = getJSONObjectFromHttpResult(response);

		} catch (Exception e) {

		}
		return json;
	}
	
	/**
	 * Método que obtiene un objeto JSON a partir de una petición tipo PUT
	 * @param URL
	 * @param POSTParameters
	 * @param key
	 * @param user_id
	 * @return
	 */
	public JSONObject getJSONObejctPUT(String URL,
			List<NameValuePair> POSTParameters, String key, String user_id) {

		// se crea el JSON para recibir
		JSONObject json = null;

		// se crea el cliente HTTP
		HttpClient client = HttpClientFactory.getThreadSafeClient();

		// se crea la petición GET
		HttpPut requestPUT = new HttpPut(URL);

		try {
			// se crea la respuesta
			requestPUT.setHeader("Content-type", "application/json");
			requestPUT.setHeader("Accept", "application/json");
			requestPUT.setHeader(Constants.HEADER_USER_ID, user_id);
			requestPUT.addHeader(Constants.HEADER_API_KEY, key);

			JSONObject obj = new JSONObject();
			try {

				// se meten todos los parámetros
				for (int i = 0; i < POSTParameters.size(); i++)
					obj.put(POSTParameters.get(i).getName(), POSTParameters
							.get(i).getValue());

				// obtenemos la clave secreta de shared preferences

				SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),
						"HmacSHA1");

				Mac mac;
				byte[] result = null;
				try {
					mac = Mac.getInstance("HmacSHA1");
					mac.init(keySpec);
					System.out.println("RES Byte= "
							+ (obj.toString() + key).getBytes());
					result = mac.doFinal((obj.toString() + key).getBytes());

				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				}

				obj.put(Constants.API_KEY, Base64.encode(result, 0));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			requestPUT.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			HttpResponse response = client.execute(requestPUT);

			json = getJSONObjectFromHttpResult(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

}
