package com.bignerdranch.android.crimeintentrev.fragment;

import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bignerdranch.android.crimeintentrev.R;
import com.bignerdranch.android.crimeintentrev.activity.CrimePagerActivity;
import com.bignerdranch.android.crimeintentrev.domain.Crime;
import com.bignerdranch.android.crimeintentrev.domain.CrimeListAdapter;
import com.bignerdranch.android.crimeintentrev.utils.CrimeLab;

public class CrimeListFragment extends ListFragment{
	
	private static final String TAG = "CrimeListFragment";
	
	private static final String IS_SHOW_SUBTITLE_KEY = "isShowSubtitleKey";
	
	private boolean isShowSubtitle;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
				
		List<Crime> crimeList = CrimeLab.getInstance(getActivity()).getCrimeList();
		
		CrimeListAdapter crimeListAdapter = new CrimeListAdapter(
				getActivity(), R.layout.list_item_crime, crimeList);
		setListAdapter(crimeListAdapter);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  super.onCreateView(inflater, container, savedInstanceState);

		if(savedInstanceState != null){
			isShowSubtitle = savedInstanceState.getBoolean(IS_SHOW_SUBTITLE_KEY,false);
		}else{
			isShowSubtitle = false;
		}
		
		if(isShowSubtitle){
			showSubtitle();
		}
		
		ListView listView = (ListView)view.findViewById(android.R.id.list);
		
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			registerForContextMenu(listView);
		}else{
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater menuInflater = mode.getMenuInflater();
					menuInflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						
						CrimeListAdapter adapter = 
							(CrimeListAdapter) CrimeListFragment.this.getListAdapter();
						CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
						
						int length = adapter.getCount();
						for(int i=length-1;i>=0;i--){
							boolean isItemChecked = 
									CrimeListFragment.this.getListView()
									.isItemChecked(i);
							if(isItemChecked){
								Crime checkedCrime = adapter.getItem(i);
								crimeLab.deleteCrimeById(checkedCrime.getCrimeId());
							}
						}
						
						adapter.notifyDataSetChanged();
						mode.finish();
						return true;

					default:
						return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		ArrayAdapter<Crime> adapter = (ArrayAdapter<Crime>) l.getAdapter();
		Crime crime = adapter.getItem(position);
		Log.d(TAG, "you choose crime item :"+crime.getCrimeTitle());
		
		UUID crimeId = crime.getCrimeId();
		
		Intent intent = new Intent(getActivity(),CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crimeId);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		ArrayAdapter<Crime> adapter = (ArrayAdapter<Crime>) getListAdapter();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo contextMenuInfo =  
				(AdapterContextMenuInfo) item.getMenuInfo();
		int position = contextMenuInfo.position;
		CrimeListAdapter adapter = (CrimeListAdapter) getListAdapter();
		
		Crime selectedCrime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			
			CrimeLab.getInstance(getActivity()).deleteCrimeById(selectedCrime.getCrimeId());
			adapter.notifyDataSetChanged();
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
		if(menuItem!=null && isShowSubtitle){
			menuItem.setTitle(R.string.string_menu_item_hide_subtitle);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_item_add_crime:
			
			Crime crime = new Crime();
			CrimeLab.getInstance(getActivity()).addCrime(crime);
			
			Intent intent = new Intent(getActivity(),CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getCrimeId());
			startActivity(intent);
			
			return true;
			
		case R.id.menu_item_show_subtitle:
			
			if(isShowSubtitle){

				hideSubtitle();
				item.setTitle(R.string.string_menu_item_show_subtitle);
				isShowSubtitle = false;
			}else{

				showSubtitle();
				item.setTitle(R.string.string_menu_item_hide_subtitle);
				isShowSubtitle = true;
			}
			
			return true;
			
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(IS_SHOW_SUBTITLE_KEY, isShowSubtitle);
	}
	
	@TargetApi(11)
	public void showSubtitle(){
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setSubtitle(R.string.subtitle_record_crime);
		}
	}
	
	@TargetApi(11)
	public void hideSubtitle(){
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setSubtitle(null);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.getInstance(getActivity()).saveCrimeList();
	}
	
}
