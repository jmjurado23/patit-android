package com.pocket.patit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pocket.patit.R;
import com.pocket.patit.views.ViewWrapperMusicRow;


/**
 * {@link SelectMusicFragment}
 * 
 * Fragmento que mustra una lista con las canciones del sistema y permite seleccionar
 * una de la lista que se muestra. Esto se devuelve mediante un callback
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SelectMusicFragment extends Fragment {

	private ListView list; // lista donde se colocarán las vistas de las
	private String[] music; // cadena con las músicas
	private ArrayAdapter<Music> listAdapter; // adaptador de canciones a lalista
												
	private static ArrayList<Music> musicList; // array con las músicas
	private static CallbackMusic mCallback;
	private Cursor cursor = null;

	// se crea una proyección para la consulta en la BBDD
	private String[] proyection = { MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION };
	private String sel = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_select_music, container,
				false);
		list = (ListView) view.findViewById(R.id.listViewSelectMusic);

		// se rellenen las listas con la música
		music2List();

		// se crea un adaptador para listas de los msgs
		listAdapter = new MusicArrayAdapter(getActivity(), musicList);

		list.setAdapter(listAdapter);

		return view;
	}

	/**
	 * Clase con los datos de las canciones del sistema
	 * 
	 * @author juanma
	 */
	private static class Music {
		public String music; // cadena con el título
		public String album; // cadena con el álbum
		public String dir; // cadena con la dirección

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		public Music(String music, String album, String dir) {
			this.music = music;
			this.album = album;
			this.dir = dir;
		}

		public String getMusic() {
			return music;
		}

		public void setMusic(String music) {
			this.music = music;
		}

		public String getAlbum() {
			return album;
		}

		public void setAlbum(String album) {
			this.album = album;
		}

	}

	/**
	 * Método para rellenar el array de Music con las canciones
	 */
	public void music2List() {

		// se genera una lista con las preferencias
		musicList = new ArrayList<Music>();

		// se crea el cursor con la consulta
		cursor = getActivity().managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proyection, sel,
				null, null);
		cursor.moveToFirst();

		// se recorre el cursor para obtener los datos
		if (cursor != null && cursor.getCount() != 0) {
			// System.out.println(cursor.getString(1));
			for (int i = 0; i < cursor.getCount(); i++) {
				musicList.add(new Music(cursor.getString(2) , cursor.getString(3),
						cursor.getString(1)));
				cursor.moveToNext();
			}
		}

	}

	/**
	 * Adaptador personalizado para los elementos de una lista de mensajes
	 * 
	 * @author juanma
	 * 
	 */
	private static class MusicArrayAdapter extends ArrayAdapter<Music> {

		private LayoutInflater inflater;

		public MusicArrayAdapter(Context context, List<Music> musicList) {
			super(context, R.layout.row_select_music, musicList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final TextView textviewtitle;
			TextView textviewtitle2;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_select_music, null);

				ViewWrapperMusicRow wrapper = new ViewWrapperMusicRow(
						convertView);
				textviewtitle = wrapper.getTitle();
				textviewtitle2 = wrapper.getSubTitle();

				convertView.setTag(new ChatViewHolder(textviewtitle,
						textviewtitle2));

			} else {

				ChatViewHolder viewHolder = (ChatViewHolder) convertView
						.getTag();
				textviewtitle = viewHolder.getTextTitle();
				textviewtitle2 = viewHolder.getTextSubtitle();
			}

			final Music msg1 = (Music) this.getItem(position);
			textviewtitle.setText(msg1.getMusic());
			textviewtitle2.setText(msg1.getAlbum());

			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					mCallback.onButtonMusicClicked(musicList.get(position).getDir());

				}

			});
			return convertView;
		}

	}

	/**
	 * Clase para manejar los nombres de las caciones
	 * 
	 * @author juanma
	 */
	private static class ChatViewHolder {
		private TextView textTitle;
		private TextView textSubtitle;

		public ChatViewHolder(TextView textTitleView, TextView textSubtitle) {
			this.textTitle = textTitleView;
			this.textSubtitle = textSubtitle;
		}

		public TextView getTextTitle() {
			return textTitle;
		}

		@SuppressWarnings("unused")
		public void setTextTitle(TextView textTitle) {
			this.textTitle = textTitle;
		}

		public TextView getTextSubtitle() {
			return textSubtitle;
		}

		@SuppressWarnings("unused")
		public void setTextSubtitle(TextView textSubtitle) {
			this.textSubtitle = textSubtitle;
		}
	}

	public ListView getViewList() {

		return list;
	}

	public interface CallbackMusic {
		public void onButtonMusicClicked(String dir);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallback = (CallbackMusic) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement " + CallbackMusic.class.getName());
		}
	}

	/**
	 * @return the music
	 */
	public String[] getMusic() {
		return music;
	}

	/**
	 * @param music the music to set
	 */
	public void setMusic(String[] music) {
		this.music = music;
	}

}
