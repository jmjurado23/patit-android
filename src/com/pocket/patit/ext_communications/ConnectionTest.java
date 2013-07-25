package com.pocket.patit.ext_communications;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * Clase con métodos estáticos para comprobar los estados de las conexiones del
 * dispositivo. Indica si hay internet y también dice el tipo de conexión que tiene  
 * el dispositivo, tanto móvil como por wifi.
 * 
 * @author Juan Manuel Jurado
 *
 */
public class ConnectionTest {

	/**
	 * Método para comprobar si hay internet en la aplicación
	 * @param ctx
	 * @return si está conectado o no
	 */
	public static boolean haveInternet(Context ctx) {
		
		try{
		    NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
		            .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	
		    if (info == null || !info.isConnected()) {
		        return false;
		    }
		    return true;
		}catch(Exception e){
			return false;
		}
	    
	}
	
	/**
	 * Método para indicar si hay conexión móbil y está activada
	 * @param ctx
	 * @return
	 */
	public static boolean isMobileConexion(Context ctx) {
		try{
			ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	
			State mobile = conMan.getNetworkInfo(0).getState();
			
			if (haveInternet(ctx) && mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) 
			    return true;
			
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Método para indicar si hay conexión wifi y si ésta está activada
	 * @param ctx
	 * @return
	 */
	public static boolean isWifiConexion(Context ctx) {
		
		ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		State wifi = conMan.getNetworkInfo(1).getState();
		
		if (haveInternet(ctx) && wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) 
		    return true;
	
		return false;
	}
	
}
