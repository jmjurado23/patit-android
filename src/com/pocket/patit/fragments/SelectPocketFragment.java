package com.pocket.patit.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pocket.patit.R;
import com.pocket.patit.data.DataYourPocket;

/**
 * {@link SelectPocketFragment}
 * 
 * Fragmento que mustra una lista con una lista de bolsillos. Este fragmento se
 * usa cuando quieres modificar un objeto de un bolsillo, para poder moverlo de
 * un bolsillo a otro.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class SelectPocketFragment extends Fragment implements
		OnItemClickListener {

	ListView list; // lista con los bolsillo
	List<String> list_pocket; // lista con los nombre
	int selected_item = 0; // elementos seleccionado
	CallbackPocketEditObject mCallback; // respuesta del fragmento


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_pocket_edit_object,
				container, false);
		list = (ListView) view.findViewById(R.id.listViewPocketEditObject);

		list_pocket = new ArrayList<String>();

		for (int i = 0; i < DataYourPocket.getList_pockets().size(); i++)
			list_pocket.add(DataYourPocket.getList_pockets().get(i).getName());

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < list_pocket.size(); i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("pocket", list_pocket.get(i));
			aList.add(hm);
		}

		String[] from = { "pocket" };

		int[] to = { R.id.TextViewPocketName };
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList,
				R.layout.row_pocket_object, from, to);

		list.setAdapter(adapter);
		list.setOnItemClickListener(this);

		return view;
	}

	public interface CallbackPocketEditObject {
		public void onButtonBClicked(int idpocket);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		mCallback.onButtonBClicked((int) arg3);

	}

	public int getSelected_item() {
		return selected_item;
	}

	public void setSelected_item(int selected_item) {
		this.selected_item = selected_item;
	}
	
	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			mCallback = (CallbackPocketEditObject) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement "
					+ CallbackPocketEditObject.class.getName());
		}
	}

}