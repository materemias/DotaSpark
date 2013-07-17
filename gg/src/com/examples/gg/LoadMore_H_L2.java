package com.examples.gg;

import android.view.View;

public class LoadMore_H_L2 extends LoadMore_Base {
	private String mAPI;

	public LoadMore_H_L2(String url) {
		this.mAPI = url;
	}

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Highlights";
		
		// Set action bar title
		ab.setTitle(abTitle);

		// API can be get from the previous fragment
		API.add(mAPI);

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
		FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Base();

		// Show menu
		setHasOptionsMenu(false);
		
		// Set retry button listener
		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Continue to check network status
				networkHandler(new LoadMore_H_L2(mAPI));

			}
		});

	}
}
