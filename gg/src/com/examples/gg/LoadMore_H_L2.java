package com.examples.gg;


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
		setRetryButtonListener(new LoadMore_H_L2(mAPI));

	}
	
	@Override
	public void refreshFragment(){
		currentFragment = new LoadMore_H_L2(mAPI);
	}
}
