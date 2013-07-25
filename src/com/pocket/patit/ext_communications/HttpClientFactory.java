package com.pocket.patit.ext_communications;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import com.pocket.patit.data.TutorialContent;

/**
 * {@link HttpClientFactory}
 * 
 * Clase para realizar las conexiones con el servidor. Crea un cliente http
 * por defecto.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class HttpClientFactory {

    private static DefaultHttpClient client;

    public synchronized static DefaultHttpClient getThreadSafeClient() {
  
        if (client != null)
            return client;
         
        client = new DefaultHttpClient();
        
        ClientConnectionManager mgr = client.getConnectionManager();
        
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(
        new ThreadSafeClientConnManager(params,
            mgr.getSchemeRegistry()), params);
  
        return client;
    } 
}