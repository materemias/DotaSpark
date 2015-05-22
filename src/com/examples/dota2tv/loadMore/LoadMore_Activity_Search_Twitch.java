package com.examples.dota2tv.loadMore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ListView;

import com.examples.dota2tv.feedManagers.FeedManager_Twitch;
import com.examples.dota2tv.settings.FlashInstallerActivity;
import com.examples.dota2tv.twitchplayers.TwitchPlayer;
import com.examples.dota2tv.twitchplayers.VideoBuffer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class LoadMore_Activity_Search_Twitch extends LoadMore_Activity_Search
		implements SearchView.OnQueryTextListener {
	private SharedPreferences prefs;

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Search Lives";

		ab.setTitle(abTitle);

		// Get the query
		Intent i = getIntent();
		mQuery = i.getStringExtra("query");


		// encoding the query
		try {
			recentAPI = "https://api.twitch.tv/kraken/search/streams?q=" + URLEncoder.encode(mQuery, "UTF-8") + "&limit=10";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		// Give API URLs
		API.clear();
		API.add(recentAPI);


		// set a feed manager
		feedManager = new FeedManager_Twitch();

		// No header view in the list
		hasHeader = false;

		// set text in search field
		queryHint = "Search streams";

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}


	@Override
	public boolean onQueryTextSubmit(String query) {

		// encoding the query
		try {
			recentAPI = "https://api.twitch.tv/kraken/search/streams?q=" + URLEncoder.encode(query, "UTF-8") + "&limit=10";
			refreshActivity();

		} catch (UnsupportedEncodingException e) {

		}
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshActivity() {
		redoRequest(recentAPI, new FeedManager_Twitch());
	}

	class MyFragment extends LoadMore_Activity_Base.MyFragment {

		@Override
		protected void forceSet() {
			isMoreVideos = false;
		}

		@Override
		public void onListItemClick(ListView l, View v, final int position, long id) {

			// Getting the preferred player
			String preferredPlayer = prefs.getString("preferredPlayer", "-1");
	//		Log.i("debug prefs", preferredPlayer);
			final Context mContext = getActivity();
			if (preferredPlayer.equals("-1")) {
				// No preference
				final CharSequence[] colors_radio = {
						"New Player(No flash needed)", "Old Player(Flash needed)"};

				new AlertDialog.Builder(getActivity())
						.setSingleChoiceItems(colors_radio, 0, null)
						.setPositiveButton("Just once",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int whichButton) {
										dialog.dismiss();
										int selectedPosition = ((AlertDialog) dialog)
												.getListView()
												.getCheckedItemPosition();
										// Do something useful withe the position of
										// the selected radio button
										openPlayer(selectedPosition, mContext,
												position, false);
									}
								})
						.setNegativeButton("Remember my selection",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int whichButton) {
										dialog.dismiss();
										int selectedPosition = ((AlertDialog) dialog)
												.getListView()
												.getCheckedItemPosition();
										// Do something useful withe the position of
										// the selected radio button
										openPlayer(selectedPosition, mContext,
												position, true);

									}
								}).show();
			} else {
				// Got preferred player
				openPlayer(Integer.parseInt(preferredPlayer), mContext, position, false);
			}
		}
	}
	
	private void openPlayer(int selectedPosition, Context mContext,
			int videoPostion, boolean isSave) {
		switch (selectedPosition) {
		case 0:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "0").commit();
			}
			// Using new video player
			Intent i = new Intent(mContext, VideoBuffer.class);
			i.putExtra("video", videolist.get(videoPostion).getVideoId());
			startActivity(i);
			break;

		case 1:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "1").commit();
			}

			// Using old player
			if (check()) {
				Intent intent1 = new Intent(mContext, TwitchPlayer.class);
				intent1.putExtra("video", videolist.get(videoPostion)
						.getVideoId());
				startActivity(intent1);

			} else {
				Intent intent2 = new Intent(mContext,
						FlashInstallerActivity.class);
				startActivity(intent2);
			}
			break;
		}
	}
	
	private boolean check() {
		PackageManager pm = this.getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}
}
