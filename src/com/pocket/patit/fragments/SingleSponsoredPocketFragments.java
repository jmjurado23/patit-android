package com.pocket.patit.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.pocket.patit.R;
import com.pocket.patit.activities.DescriptionObjectActivity;
import com.pocket.patit.classes.Comment;
import com.pocket.patit.classes.Constants;
import com.pocket.patit.classes.Pocket;
import com.pocket.patit.classes.Object;
import com.pocket.patit.data.DataSponsoredPocket;
import com.pocket.patit.data.PocketThemeData;
import com.pocket.patit.fragments.SingleRandomPocketFragments.CallbackRandomFragment;
import com.pocket.patit.views.ArrayAdapterComments;
import com.pocket.patit.views.GridSinglePocketAdapter;

/**
 * {@link SingleSponsoredPocketFragments}
 * 
 * Fragmento que mustra un bolsillo esponsorizado. Este es de color verde y permite
 * votar al bolsillo, quitarlos de destacado, o comentar. Muestra un bolsillo con una
 * cremallera para ver los objetos del bolsillo.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SingleSponsoredPocketFragments extends Fragment implements SeekBar.OnSeekBarChangeListener, 
	SlidingDrawer.OnDrawerOpenListener, SlidingDrawer.OnDrawerCloseListener,
	SlidingDrawer.OnDrawerScrollListener, OnClickListener{
	
	public Pocket getPocket() {
		return pocket;
	}

	public void setPocket(Pocket pocket) {
		this.pocket = pocket;
	}

	private GridView gridview;							//grid
	private List<Pocket> list_pocket;					//lista de bolsillo
	AdapterView.AdapterContextMenuInfo info;			//información
	private SeekBar zip;								//cremallera
	private SlidingDrawer handle_slider;				//manejador de la cremallera
	private TextView pocketName;						//nombre del bolsillo
	private ImageView pocketImg;						//imagen del tema
	private int zip_position;							//posición de la cremallera
	private MediaPlayer zipsound;						//sonido de la cremallera
	private CallbackSponsoredFragment mCallbackSponsored;	    //callback del fragmento
	private Pocket pocket;								//bolsillo
	private PocketThemeData dataPockets;				//Temas de los bolsillos
	private List<Comment> list_comments;				//listas con los comentarios
	private List<Object> list_objects;					//listas con los objetos
	private ArrayAdapter<Comment> listAdapter;			//adaptador de la lista
	private ListView listComment;						//lista de comentarios
	private TextView textDescription;					//texto descriptivo
	private Button AddCommentButton;					//botón de añadir comentarios
	private ImageButton featuredImage;					//imagen de destacado
	private Button goodVoteButton;						//voto positivo, botón
	private Button badVoteButton;						//voto negativo, botón
	private TextView textUserPocket;					//nick de usuario
	private SharedPreferences preferences;				//preferencias
	private Vibrator v;									//vibrador
	private long[] pattern = { 0, 100, 100 };			//patrón vibrador
	private boolean bMenu;								//menú
	private TextView im;								//n_imágenes
	private TextView mu;								//n_música
	private TextView tx;								//n_textos;
	private TextView ur;								//n_www

	public SingleSponsoredPocketFragments() {
		super();
	}

	public SingleSponsoredPocketFragments(Pocket pocket2) {
		super();
		this.pocket = pocket2;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_single_sponsored_pocket,
				container, false);

		zip = (SeekBar) view.findViewById(R.id.zipslider);
		gridview = (GridView) view.findViewById(R.id.gridViewSinglePocket);
		handle_slider = (SlidingDrawer) view
				.findViewById(R.id.slidingDrawerPocket);
		zipsound = MediaPlayer.create(getActivity(), R.raw.zipclip);
		listComment = (ListView) view.findViewById(R.id.listViewComments);
		pocketName = (TextView) view.findViewById(R.id.textViewSinglePocket);
		pocketImg = (ImageView) view.findViewById(R.id.imageViewSinglePocket);
		listComment = (ListView) view.findViewById(R.id.listViewComments);
		AddCommentButton = (Button) view.findViewById(R.id.buttonCommentPocket);
		featuredImage = (ImageButton) view.findViewById(R.id.buttonFeaturedPocketYour);
		goodVoteButton = (Button) view.findViewById(R.id.buttonGoodPocket);
		badVoteButton = (Button) view.findViewById(R.id.buttonBadPocket);
		textUserPocket = (TextView) view.findViewById(R.id.textViewSinglePocketUser);
		im = (TextView) view.findViewById(R.id.n_im);
		mu = (TextView) view.findViewById(R.id.n_mu);
		tx = (TextView) view.findViewById(R.id.n_tx);
		ur = (TextView) view.findViewById(R.id.n_ur);

		// se cargan los comentarios para ese bolsillo
		View header = (View) this.getActivity().getLayoutInflater()
				.inflate(R.layout.pocket_info_header, null);
		textDescription = (TextView) header
				.findViewById(R.id.textViewDescriptionPocket);
		textDescription.setText(pocket.getDescription());

		// se genera la lista de comentarios
		listComment.addHeaderView(header);
		list_comments = pocket.getComments();
		
		listAdapter = new ArrayAdapterComments(this.getActivity(),
				android.R.layout.simple_list_item_1, list_comments);
		
		listComment.setAdapter(listAdapter);

		// cargar información de los bolsillos
		dataPockets = new PocketThemeData(getActivity());
		chargePockets();
		pocketName.setText(pocket.getName());
		goodVoteButton.setText(Integer.toString(pocket.getPos_votes()));
		badVoteButton.setText(Integer.toString(pocket.getNeg_votes()));
		textUserPocket.setText(pocket.getUser());
		
		//se cuentan los elementos de cada tipo
		int ima=0,mus=0,tex=0,url=0;
		for(int i=0;i < pocket.getObjects().size();i++){
			if(pocket.getObjects().get(i).getType().equals(Constants.IMAGE))
				ima++;
			else if(pocket.getObjects().get(i).getType().equals(Constants.MUSIC))
				mus++;
			else if(pocket.getObjects().get(i).getType().equals(Constants.TEXT))
				tex++;
			else if(pocket.getObjects().get(i).getType().equals(Constants.URL))
				url++;
		}
		im.setText(Integer.toString(ima));
		mu.setText(Integer.toString(mus));
		tx.setText(Integer.toString(tex));
		ur.setText(Integer.toString(url));
		
		pocketImg.setBackgroundResource(dataPockets
				.getThemeImgResourceMainPocketTrans(pocket.getType()));
		featuredImage.setImageResource(dataPockets
				.getFeaturedImgResource(pocket.isFeatured()));

		//se carga el GRID con los objectos del bolsillo
    	list_objects = pocket.getObjects();
    	GridSinglePocketAdapter adapter = new GridSinglePocketAdapter(getActivity(), list_objects);
    	
		if (gridview != null)
			gridview.setAdapter(adapter);

		// se abre el bolsillo
		handle_slider.open();
		zip.setMax(100);
		zip.setProgress(100);
		zip_position = 100;
		v = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

		zip.setOnSeekBarChangeListener(this);
		AddCommentButton.setOnClickListener(this);
		handle_slider.setOnDrawerOpenListener(this);
		handle_slider.setOnDrawerCloseListener(this);
		handle_slider.setOnDrawerScrollListener(this);
		featuredImage.setOnClickListener(this);
		goodVoteButton.setOnClickListener(this);
		badVoteButton.setOnClickListener(this);
		
		//se cargan las preferencias
    	preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());


    	gridview.setOnItemClickListener(new OnItemClickListener() {
    		
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				mCallbackSponsored.onButtonGridObjectClicked(position);
				
				
				}
			});
    	
    	registerForContextMenu(gridview);
		// Inflate the layout for this fragment
		return view;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		MenuInflater inflater = getActivity().getMenuInflater();
		
		menu.add(Menu.NONE, R.id.a_item, Menu.NONE, R.string.details);
		
	    inflater.inflate(R.menu.empty_menu, menu);
	    bMenu =true;
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (bMenu) {
	       	bMenu=false;
		
	       	switch (item.getItemId()) {
		
	       	case R.id.a_item: //Detalles
			
				// se crea la actividad para mostrar los detalles del bolsillo
				Intent intent = new Intent(this.getActivity(),
						DescriptionObjectActivity.class);
				intent.putExtra(Constants.OBJECT_TO_DESCRIPTION, list_objects.get(info.position));
				this.getActivity().startActivityForResult(intent,
						Constants.DESCRIPTION_OBJECT_OK);
				
				return true;
				
			
			}
			return super.onContextItemSelected(item);
		} else {
			        return super.onContextItemSelected(item);
	    }
	}
	
	private void checkZip() {
		if (zip_position == 100) {
			zip.setProgress(100);
			handle_slider.open();
		} else {
			zip.setProgress(0);
			handle_slider.close();
		}
	}

	private void saveZip() {
		if (handle_slider.isOpened()) {
			zip_position = 100;
			zip.setProgress(100);
		} else {
			zip_position = 0;
			zip.setProgress(0);
		}

	}

	public void onResume() {
		super.onResume();
		checkZip();
	}

	public void onPause() {
		super.onPause();
	}

	private void chargePockets() {
		// TODO
		setList_pocket(DataSponsoredPocket.getList_pockets());
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		zip_position = progress;
		
		if(preferences.getBoolean(Constants.PREF_KEY_VIBRATE, false) && (progress%2==0))
			v.vibrate(pattern, -1);
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		if(preferences.getBoolean(Constants.PREF_KEY_SOUND, false))
			zipsound.start();
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if (zip_position < 10) {
			handle_slider.close();
			zipsound.pause();
		} else {
			handle_slider.open();
			new Thread() {
				public void run() {
					for (int i = zip_position; i < 100; i++) {
						zip.setProgress(i);
					}
				}
			}.start();
		}
	}

	public void onDrawerOpened() {
		saveZip();
	}

	public void onDrawerClosed() {
		saveZip();

	}

	public void onScrollEnded() {
		zipsound.pause();

	}

	public void onScrollStarted() {
		if(preferences.getBoolean(Constants.PREF_KEY_SOUND, false))
			zipsound.start();

	}

	public void onClick(View v) {
		if (v.equals(AddCommentButton)) {
			mCallbackSponsored.onButtonNewCommentClicked();
		}
		if (v.equals(featuredImage)) {
			mCallbackSponsored.onButtonFeaturedClicked();
		}
		if(v.equals(goodVoteButton)){
			mCallbackSponsored.onGoodVoteButtonClicked();
		}
		if(v.equals(badVoteButton)){
			mCallbackSponsored.onBadVoteButtonClicked();
		}
	}

	public interface CallbackSponsoredFragment {

		public void onButtonNewCommentClicked();

		public void onButtonFeaturedClicked();
		
		public void onGoodVoteButtonClicked();
		
		public void onBadVoteButtonClicked();
		
		public void onButtonGridObjectClicked(int object);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallbackSponsored = (CallbackSponsoredFragment) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement " + CallbackSponsoredFragment.class.getName());
		}
	}

	/**
	 * @return the list_pocket
	 */
	public List<Pocket> getList_pocket() {
		return list_pocket;
	}

	/**
	 * @param list_pocket the list_pocket to set
	 */
	public void setList_pocket(List<Pocket> list_pocket) {
		this.list_pocket = list_pocket;
	}

}