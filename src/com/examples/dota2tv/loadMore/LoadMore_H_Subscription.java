package com.examples.dota2tv.loadMore;

import com.examples.dota2tv.feedManagers.FeedManager_Subscription;


public class LoadMore_H_Subscription extends LoadMore_Base {

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Highlights";

		// Give API URLs
//		API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json");
		API.add(String.format("http://pipes.yahoo.com/pipes/pipe.run?_id=cbdfe3fdaeb0672a56819f798a50f33d&_render=json&limit=%d&from=0", noOfElementsToLoadATime));

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
//		FragmentPlaylist = new LoadMore_H_New_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu		
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		currentPosition = 0;
		// Set retry button listener
		

	}

}
