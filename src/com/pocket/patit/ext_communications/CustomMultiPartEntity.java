package com.pocket.patit.ext_communications;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;


/**
 * {@link CustomMultiPartEntity}
 * 
 * Clase abstracta que define el comportamiento para enviar ficheros de forma multipart.
 * Gracias a esta clase se pueden usar listeners para saber los bytes que se
 * han subido en cada momento y poder realziar barras de progreso de las subidas.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class CustomMultiPartEntity extends MultipartEntity
{
 
	private final ProgressListener listener;
 
	public CustomMultiPartEntity(final ProgressListener listener)
	{
		super();
		this.listener = listener;
	}
 
	public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener)
	{
		super(mode);
		this.listener = listener;
	}
 
	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener)
	{
		super(mode, boundary, charset);
		this.listener = listener;
	}
 

	@Override
	public void writeTo(final OutputStream outstream) throws IOException
	{
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}
 
	public static interface ProgressListener
	{
		void transferred(long num);
	}
 
	public static class CountingOutputStream extends FilterOutputStream
	{
 
		private final ProgressListener listener;
		private long transferred;
 
		public CountingOutputStream(final OutputStream out, final ProgressListener listener)
		{
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}
 
		public void write(byte[] b, int off, int len) throws IOException
		{
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}
 
		public void write(int b) throws IOException
		{
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}