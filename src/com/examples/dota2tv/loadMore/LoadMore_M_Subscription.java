package com.examples.dota2tv.loadMore;

import com.examples.dota2tv.feedManagers.FeedManager_Subscription;


public class LoadMore_M_Subscription extends LoadMore_Base {

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Matches";

		// Give API URLs
		//API.add("https://gdata.youtube.com/feeds/api/users/GJoABYYxwoGsl6TuP0DGnw/newsubscriptionvideos?max-results=10&alt=json");
		API.add(String.format("http://pipes.yahoo.com/pipes/pipe.run?_id=92e52e810623a96d40d72ca0f317a2c3&_render=json&limit=%d&from=0", noOfElementsToLoadATime));

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_M_Subscription();

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);
		
		// Set retry button listener
		currentPosition = 0;

	}
	
	
}
