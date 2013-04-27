package com.pda_sru;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Home extends Activity implements OnClickListener {

	/**
	 * Called when Activity is created. Should initiate all layout object here.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
	}

	/**
	 * Initiates the menu for this Activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	/**
	 * Event Handling for Individual menu items
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch(id) {
		case R.id.action_change_group:
			
			return true;
		case R.id.action_create_group:
			
			return true;
		case R.id.action_edit_profile:
			
			return true;
		case R.id.action_join_group:
			
			return true;
		case R.id.action_view_groups:
			
			return true;
		case R.id.action_view_members:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	/**
	 * This Activity onClick method should implement onClickListeners
	 * for all components that should respond to a click
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id) {
		
		}
		
	}

}
