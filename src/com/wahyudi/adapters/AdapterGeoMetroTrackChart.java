package com.wahyudi.adapters;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahyudi.QueryTest;
import edu.gvsu.cis.toptracks.R;
import com.wahyudi.data.DataGeoMetroTrackChart;
import com.wahyudi.tasks.ConvertImageGeoMetroTrackChart;

/**
 * A custom adapter to fill the Track list.
 */
public class AdapterGeoMetroTrackChart extends BaseAdapter implements OnClickListener {
	
	private static final String debugTag = "TrackDataAdapter";
	private QueryTest activity;
	private ConvertImageGeoMetroTrackChart imgFetcher;
	private LayoutInflater layoutInflater;
	private ArrayList<DataGeoMetroTrackChart> tracks;
	
	
    public AdapterGeoMetroTrackChart(QueryTest a, ConvertImageGeoMetroTrackChart i, LayoutInflater l, ArrayList<DataGeoMetroTrackChart> data)
    {
    	this.activity = a;
    	this.imgFetcher = i;
    	this.layoutInflater = l;
    	this.tracks = data;
    }
    
    @Override
    public int getCount() {
        return this.tracks.size();
    }

    @Override
    public boolean areAllItemsEnabled () 
    {
    	return true;
    }
    
    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        QueryTest.MyViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate (R.layout.trackrow, parent, false);
            holder = new QueryTest.MyViewHolder();
            holder.trackName = (TextView) convertView.findViewById(R.id.track_name);
            holder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.album_icon);
            holder.trackButton = (Button) convertView.findViewById(R.id.track_button);
            holder.trackButton.setTag(holder);
            convertView.setTag(holder);
        }
        else {
            holder = (QueryTest.MyViewHolder) convertView.getTag();
        }
        
   		convertView.setOnClickListener(this);
   		
   		DataGeoMetroTrackChart track = tracks.get(pos);
   		holder.track = track;
   		holder.trackName.setText(track.getName());
   		holder.artistName.setText(track.getArtist());
   		holder.trackButton.setOnClickListener(this);
   		if(track.getImageUrl() != null) {
   			holder.icon.setTag(track.getImageUrl());
   			Drawable dr = imgFetcher.loadImage(this, holder.icon);
   			if(dr != null) {
   				holder.icon.setImageDrawable(dr);
   			}
   		} else {
   			holder.icon.setImageResource(R.drawable.filler_icon);
   		}
   		
        return convertView;
    }
    
    @Override
	public void onClick(View v) {
		QueryTest.MyViewHolder holder = (QueryTest.MyViewHolder) v.getTag();
		if (v instanceof Button) {
			
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(holder.track.getArtistUrl()));
				this.activity.startActivity(intent);

		} else if (v instanceof View) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(holder.track.getTrackUrl()));
			this.activity.startActivity(intent);
		}
   		Log.d(debugTag,"OnClick pressed.");

	}
    
}