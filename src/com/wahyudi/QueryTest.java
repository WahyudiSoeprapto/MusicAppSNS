package com.wahyudi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.gvsu.cis.toptracks.R;
import com.wahyudi.adapters.AdapterGeoMetroTrackChart;
import com.wahyudi.data.DataGeoMetroTrackChart;
import com.wahyudi.tasks.ConvertImageGeoMetroTrackChart;
import com.wahyudi.tasks.ParsingGeoMetroTrackChart;

/**
 * Find/display top tracks in a given metro area.
 */
public class QueryTest extends Activity {
	
	private ArrayList<DataGeoMetroTrackChart> tracks;
	private ListView trackList;
	private LayoutInflater layoutInflator;
	private Button tracksButton;
	private Spinner metroSpinner;
    private InputMethodManager inMgr;
    private ConvertImageGeoMetroTrackChart imgFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        this.inMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.trackList = (ListView) findViewById(R.id.track_list);
        this.imgFetcher = new ConvertImageGeoMetroTrackChart(this);
        this.layoutInflator = LayoutInflater.from(this);
        this.metroSpinner = (Spinner) this.findViewById(R.id.metro);
        this.tracksButton = (Button)this.findViewById(R.id.track_button);

        
        this.tracksButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				inMgr.hideSoftInputFromWindow(tracksButton.getWindowToken(), 0);
		        ParsingGeoMetroTrackChart lfmTask = new ParsingGeoMetroTrackChart(QueryTest.this);
		        try {
		        	TextView txtView  =  (TextView)metroSpinner.getSelectedView();
		        	String metroTxt = txtView.getText().toString();
		            lfmTask.execute(metroTxt);
		        }
		        catch (Exception e)
		        {
		            lfmTask.cancel(true);
		            alert (getResources().getString(R.string.no_tracks));
		        }
		        
				
			}
		});  
        

        // Restore any already fetched data on orientation change. 
        final Object[] data = (Object[]) getLastNonConfigurationInstance();
        if(data != null) {
        	this.tracks = (ArrayList<DataGeoMetroTrackChart>) data[0];
        	this.imgFetcher = (ConvertImageGeoMetroTrackChart)data[1];
         	trackList.setAdapter(new AdapterGeoMetroTrackChart(this,this.imgFetcher,this.layoutInflator, this.tracks));
        }
        	
    }
    


    /**
     * Handy dandy alerter.
     * @param msg the message to toast.
     */
    public void alert (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Save any fetched track data for orientation changes. 
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
    	Object[] myStuff = new Object[2];
    	myStuff[0] = this.tracks;
    	myStuff[1] = this.imgFetcher;
    	return myStuff;
    }

    
    /**
     * Bundle to hold refs to row items views.
     *
     */
    public static class MyViewHolder {
        public TextView trackName, artistName;
        public Button trackButton;
        public ImageView icon;
        public DataGeoMetroTrackChart track;
    }


	public void setTracks(ArrayList<DataGeoMetroTrackChart> tracks) {
		this.tracks = tracks;
		this.trackList.setAdapter(new AdapterGeoMetroTrackChart(this,this.imgFetcher,this.layoutInflator, this.tracks));
	}

    
}