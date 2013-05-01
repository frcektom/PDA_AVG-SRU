package com.pda_sru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Playlist extends Activity implements OnClickListener {
	
	TextView playlist_group_name;
	ListView playlist_listview;
	Button playlist_btn_add;
	
	/**
	 * Called when Activity is created. Should initiate all layout object here.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_playlist);
		
		//Initialize layout components
		playlist_group_name = (TextView) findViewById(R.id.playlist_group_name);
		playlist_listview = (ListView) findViewById(R.id.playlist_listview);
		playlist_btn_add = (Button) findViewById(R.id.playlist_btn_add);
		
		//Set onClickListeners
		playlist_btn_add.setOnClickListener(this);
		
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
		case R.id.playlist_btn_add:
			
			//Show search dialog
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.layout_search_dialog);
			dialog.setTitle("Search Youtube");
			
			//Initialize dialog content
			final EditText searchdialog_edittext_search = (EditText) dialog.findViewById(R.id.searchdialog_edittext_search);
			ListView searchdialog_listview = (ListView) dialog.findViewById(R.id.searchdialog_listview);
			Button searchdialog_btn_search = (Button) dialog.findViewById(R.id.searchdialog_btn_search);
			Button searchdialog_btn_ok = (Button) dialog.findViewById(R.id.searchdialog_btn_ok);
			
			searchdialog_btn_search.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Send request to YouTube web API
					String search_string = searchdialog_edittext_search.getText().toString();
					if (search_string.length() == 0) {
						Toast.makeText(getApplicationContext(), "Enter what you are looking for", Toast.LENGTH_LONG).show();
						return;
					}
					String[] search_tokens = search_string.split(" ");
					if (!networkIsAvailable()) {
						Toast.makeText(getApplicationContext(), "Internet connection unavailable", Toast.LENGTH_LONG).show();
						return;
					}
					else {
						Comm comm = new Comm();
						comm.execute(search_tokens);
					}
															
				}
			});
			
			searchdialog_btn_ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Save current selection, close dialog and save to playlist			
					dialog.dismiss();
				}
			});
			
			dialog.show();
			
			return;
		}
		
	}
	
	/**
	 * Check Internet connectivity
	 * @return true if connected, false otherwise
	 */
	public boolean networkIsAvailable() {
    	ConnectivityManager cm = (ConnectivityManager)
    	getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = cm.getActiveNetworkInfo();
    	return networkInfo != null && networkInfo.isConnected();    
    }
	
	public class Comm extends AsyncTask<String[], Void, String[][]> {
		
		//1st argument - data the AsyncTask gets
		//2nd argument - data the AsyncTask provides during computation
		//3rd argument - data the AsyncTask returns
    	
    	public Comm() {
    		super();    		
    	}

    	@Override
    	protected String[][] doInBackground(String[]... data) {
    		 
    		HttpClient client = new DefaultHttpClient();
        	HttpGet request = new HttpGet("https://jep.staging.jochen-schweizer.de/rest/voucher/validate/" + data[0]);
        		
        	request.setHeader("Accept", "application/json");
        	request.setHeader("X-AuthToken","3ac85fb5156b0f173e385ef9c7109976e2860b81");
        	
        	HttpResponse response;
    		try {
    			response = client.execute(request);
    		} catch (ClientProtocolException e) {
    			Log.e("ClientProtocolException", e.toString());
    			e.printStackTrace();
    			return {"-1" "cpe"};
    		} catch (IOException e) {
    			Log.e("IOException", e.toString());
    			e.printStackTrace();
    			return "ioe";
    		}    	
        	StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            StringBuilder builder = new StringBuilder();
            
            switch(statusCode) {
            case 200:
            	//OK
            	HttpEntity entity = response.getEntity();
                InputStream content;
    			try {
    				content = entity.getContent();
    			} catch (IllegalStateException e) {
    				Log.e("IllegalStateException", e.toString());
    				e.printStackTrace();
    				return "ise";
    			} catch (IOException e) {
    				Log.e("IOException", e.toString());
    				e.printStackTrace();
    				return "ioe";
    			}
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                try {
    				while ((line = reader.readLine()) != null) {
    				  builder.append(line);
    				}
    			} catch (IOException e) {
    				Log.e("IOException", e.toString());
    				e.printStackTrace();
    				return "ioe";
    			}
            	break;
            case 404:
            	//Code not found
            	return "notfound";            	
            case 403:
            	//Voucher expired
            	return "expired";            	
            }
            
            return builder.toString();
    	}

    	protected void onPostExecute(String[][] result) {
    		if (result.equals((String) "cpe")) {
    			//ClientProtocolException
    			textValid.setText("There has been a problem with network communication: ClientProtocolException");	
    			textValid.setVisibility(View.VISIBLE);
    			return;
    		}
    		if (result.equals((String) "ioe")) {
    			//IOException
    			textValid.setText("There has been a problem with network communication: IOException");	
    			textValid.setVisibility(View.VISIBLE);
    			return;
    		}
    		if (result.equals((String) "ise")) {
    			//IllegalStateException
    			textValid.setText("There has been a problem with network communication: IllegalStateException");	
    			textValid.setVisibility(View.VISIBLE);
    			return;
    		}
    		if (result.equals((String) "notfound")) {
    			//Not found
    			imageValid.setImageBitmap(invalid);
				imageValid.setVisibility(View.VISIBLE);
    			textValid.setText("VOUCHER NOT FOUND");	
    			textValid.setVisibility(View.VISIBLE);  
    			return;
    		}
    		if (result.equals((String) "expired")) {
    			//Expired
    			imageValid.setImageBitmap(invalid);
				imageValid.setVisibility(View.VISIBLE);
    			textValid.setText("VOUCHER EXPIRED");	
    			textValid.setVisibility(View.VISIBLE);
    			return;
    		}
    		imageValid.setImageBitmap(valid);
			imageValid.setVisibility(View.VISIBLE);
			//textValid.setText("VOUCHER VALID");
			//textValid.setVisibility(View.VISIBLE);
			try {
				//Log.v("raw data", result);
				JSONObject jObject = new JSONObject(result);
				JSONObject voucher = jObject.getJSONObject("voucher");
				JSONObject event = voucher.getJSONObject("event_type");
				String start = voucher.getString("valid_start_date");
				start = start.replace('T', ' ');
				String end = voucher.getString("valid_end_date");
				end = end.replace('T', ' ');
				String name = event.getString("name");
				
				textStart.setText(start);
				textEnd.setText(end);
				textName.setText(name);
				infoLayout.setVisibility(View.VISIBLE);
				//Log.v("start", start);
				//Log.v("end", end);
				//Log.v("name", name);
				//Log.v("json length","" + jObject.length());
				//Log.v("voucher start date",voucher.getString("valid_start_date"));
				//Log.v("start date","" + jObject.getString("valid_start_date"));
				//Log.v("end date","" + jObject.getString("valid_end_date"));
			} catch (JSONException e) {
				textValid.setText("There has been a problem with network communication: JSONException");	
    			textValid.setVisibility(View.VISIBLE);
    			Log.e("JSONException", e.toString());
				e.printStackTrace();
			}
			
    	}
    	
    }
	
}