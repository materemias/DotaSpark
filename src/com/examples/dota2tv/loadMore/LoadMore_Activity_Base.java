package com.examples.dota2tv.loadMore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.dota2tv.R;
import com.examples.dota2tv.adapters.VideoArrayAdapter;
import com.examples.dota2tv.data.MyAsyncTask;
import com.examples.dota2tv.data.Video;
import com.examples.dota2tv.feedManagers.FeedManager_Base;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class LoadMore_Activity_Base extends AppCompatActivity {

	protected static String FRGMNT_TAG = "subfragment";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		MyFragment fragment = new MyFragment();
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, FRGMNT_TAG).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		if (item.getItemId() == 0) {
			refreshActivity();
		}

		return super.onOptionsItemSelected(item);
	}

	// Clear fragment back stack
	public void refreshActivity() {
		redoRequest(recentAPI, new FeedManager_Base());
	}

	public void redoRequest(String api, FeedManager_Base fb) {
		// Clean the API array, titiles, videos, and videlist
		API = new ArrayList<>();
		titles = new ArrayList<>();
		videos = new ArrayList<>();
		videolist = new ArrayList<>();

		// Set feed manager
		feedManager = fb;

		// Add playlist API
		API.add(api);

		// Reset isMoreVideos
		if (API.isEmpty())
			isMoreVideos = false;
		else if (API.get(0) != null)
			isMoreVideos = true;

		// Getting playlists from Youtube
		((MyFragment)getSupportFragmentManager().findFragmentByTag(FRGMNT_TAG)).setListView();
	}

	public void Initializing() {}

	protected ArrayList<String> titles;
	protected ArrayList<String> videos;
	protected ArrayList<Video> videolist;
	protected ArrayList<String> API;
	protected boolean isMoreVideos;
	protected FeedManager_Base feedManager;
	protected String recentAPI;
	protected String playlistAPI;
	protected ActionBar ab;
	protected int currentPosition = 0;
	protected String abTitle;
	protected int section = 0;
	protected boolean hasHeader = true;

	public class MyFragment extends ListFragment {
		protected LoadMoreListView myLoadMoreListView;

		protected Fragment nextFragment;
		protected Fragment FragmentAll;
		protected Fragment FragmentUploader;
		protected View view;
		protected LayoutInflater mInflater;
		protected VideoArrayAdapter vaa;
		protected ArrayList<LoadMoreTask> mLoadMoreTasks = new ArrayList<>();
		protected Button mRetryButton;
		protected View mRetryView;
		protected boolean needFilter;
		protected FragmentManager fm;
		protected View fullscreenLoadingView;
		protected boolean hasRefresh;
		protected boolean hasDropDown = false;
		protected Fragment currentFragment;
		public boolean isBusy = false;
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		protected boolean firstTime = true;
		protected String title;
		protected String thumbnailUrl;
		private DisplayImageOptions options;
		private View mRootView;
		private LoadMore_Activity_Base mBaseActivity;

		@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			mRootView = inflater.inflate(R.layout.loadmore_list, container, false);
			// Get loading view
			fullscreenLoadingView = mRootView.findViewById(R.id.fullscreen_loading_indicator);
			setListView();
			return mRootView;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			mBaseActivity = ((LoadMore_Activity_Base)getActivity());
			// default no filter for videos

			Intent intent = getActivity().getIntent();
			((LoadMore_Activity_Base)getActivity()).recentAPI = intent.getStringExtra("API");
			playlistAPI = intent.getStringExtra("PLAYLIST_API");
			title = intent.getStringExtra("title");
			thumbnailUrl = intent.getStringExtra("thumbnail");

			if (!this.imageLoader.isInited()) {
				this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			}
			// imageLoader=new ImageLoader(context.getApplicationContext());

			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.loading)
					.showImageForEmptyUri(R.drawable.loading)
					.showImageOnFail(R.drawable.loading).cacheInMemory(true)
					.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
					.build();

			// Get Retry view
			mRetryView = mRootView.findViewById(R.id.mRetry);

			// get action bar
			ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

			// Initilizing the empty arrays
			mBaseActivity.titles = new ArrayList<>();
			mBaseActivity.videos = new ArrayList<>();
			mBaseActivity.videolist = new ArrayList<>();
			// thumbList = new ArrayList<String>();

			// set adapter
			// vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist,
			// this);

			mBaseActivity.API = new ArrayList<>();

			// Initializing important variables
			mBaseActivity.API.add(mBaseActivity.recentAPI);
			// Set action bar title
			// System.out.println("My title: "+title);

			ab.setTitle("");

			mBaseActivity.feedManager = new FeedManager_Base();

			Initializing();
			// check whether there are more videos in the playlist
			if (mBaseActivity.API.isEmpty())
				mBaseActivity.isMoreVideos = false;
			else if (API.get(0) != null)
				mBaseActivity.isMoreVideos = true;

			// set the adapter
			// setListAdapter(vaa);

			ab.setHomeButtonEnabled(true);
			ab.setDisplayHomeAsUpEnabled(true);


		}

		public void setOptionMenu(boolean hasRefresh, boolean hasDropDown) {
			this.hasRefresh = hasRefresh;
			this.hasDropDown = hasDropDown;
		}

		public void setListView() {
			myLoadMoreListView = (LoadMoreListView) this.getListView();
			myLoadMoreListView.setDivider(null);

			if (myLoadMoreListView.getHeaderViewsCount() == 0 && hasHeader) {
				View header = getActivity().getLayoutInflater().inflate(
						R.layout.titleview, null);
				myLoadMoreListView.addHeaderView(header, null, false);

				ImageView channelImage = (ImageView) mRootView.findViewById(R.id.thumbnail);
				TextView channelName = (TextView) mRootView.findViewById(R.id.name);

				imageLoader.displayImage(thumbnailUrl, channelImage, options, null);
				channelName.setText(title);

			}

			vaa = new VideoArrayAdapter(getActivity(), titles, videolist, imageLoader);
			setListAdapter(vaa);

			forceSet();
			if (mBaseActivity.isMoreVideos) {

				// there are more videos in the list
				// set the listener for loading need
				myLoadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
					public void onLoadMore() {

						if (mBaseActivity.isMoreVideos) {
							// new LoadMoreTask().execute(API.get(0));
							LoadMoreTask newTask = new LoadMoreTask(
									LoadMoreTask.LOADMORETASK, myLoadMoreListView,
									fullscreenLoadingView, mRetryView);
							newTask.execute(API.get(API.size() - 1));
							mLoadMoreTasks.add(newTask);
						}

					}
				});

			} else {
				myLoadMoreListView.setOnLoadMoreListener(null);
			}

			// sending Initial Get Request to Youtube
			if (!mBaseActivity.API.isEmpty()) {
				// show loading screen
				// DisplayView(fullscreenLoadingView, myLoadMoreListView,
				// mRetryView) ;
				doRequest();
			}

		}

		// Used to force set isMoreVideos variable
		protected void forceSet() {

		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

			menu.add(0, 0, 0, "Refresh")
					.setIcon(R.drawable.ic_refresh)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

			Intent i = new Intent(getActivity(), YoutubeActionBarActivity.class);
			i.putExtra("video", mBaseActivity.videolist.get(position - 1));
			startActivity(i);

		}

		class LoadMoreTask extends MyAsyncTask {

			public LoadMoreTask(int type, View contentView, View loadingView,
								View retryView) {
				super(type, contentView, loadingView, retryView);
			}

			@Override
			public void handleCancelView() {
				myLoadMoreListView.onLoadMoreComplete();

				if (isException) {

					DisplayView(retryView, contentView, loadingView);
				}

			}

			@Override
			public void setRetryListener(final int type) {
				mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);

				mRetryButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						LoadMoreTask newTask = new LoadMoreTask(
								type, contentView, loadingView, retryView);
						newTask.DisplayView(loadingView, contentView, retryView);
						newTask.execute(mBaseActivity.API.get(mBaseActivity.API.size() - 1));
						mLoadMoreTasks.add(newTask);

					}
				});

			}

			@Override
			protected void onPostExecute(String result) {
				// Do anything with response..
				// System.out.println(result);

				// Log.d("AsyncDebug", "Into onPostExecute!");

				if (!taskCancel && result != null) {
					// Do anything with response..
					try {
						mBaseActivity.feedManager.setmJSON(result);

						List<Video> newVideos = mBaseActivity.feedManager.getVideoPlaylist();

						// adding new loaded videos to our current video list
						for (Video v : newVideos) {
							// System.out.println("new id: " + v.getVideoId());
							if (needFilter) {
								filtering(v);
								// System.out.println("need filter!");
							} else {
								titles.add(v.getTitle());
								videos.add(v.getVideoId());
								videolist.add(v);
							}
						}

						// put the next API in the first place of the array
						mBaseActivity.API.add(mBaseActivity.feedManager.getNextApi(mBaseActivity.API.get(0)));
						// nextAPI = feedManager.getNextApi();
						if (mBaseActivity.API.get(mBaseActivity.API.size() - 1) == null) {
							// No more videos left
							mBaseActivity.isMoreVideos = false;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					vaa.notifyDataSetChanged();

					// Call onLoadMoreComplete when the LoadMore task, has
					// finished
					myLoadMoreListView.onLoadMoreComplete();

					// loading done
					DisplayView(contentView, retryView, loadingView);
					if (!mBaseActivity.isMoreVideos) {
						myLoadMoreListView.onNoMoreItems();

						myLoadMoreListView
								.setOnLoadMoreListener(null);
					}

				} else {
					handleCancelView();
				}

			}

		}

		// sending the http request
		protected void doRequest() {
			// TODO Auto-generated method stub
			for (String s : mBaseActivity.API) {
				LoadMoreTask newTask = new LoadMoreTask(LoadMoreTask.INITTASK,
						myLoadMoreListView, fullscreenLoadingView, mRetryView);
				mLoadMoreTasks.add(newTask);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
				} else {
					newTask.execute(s);
				}
			}
		}

		@Override
		public void onDestroy() {
			super.onDestroy();

			// Log.d("UniversalImageLoader", "It's task root!");
			imageLoader.clearDiscCache();
			imageLoader.clearMemoryCache();

			// check the state of the task
			cancelAllTask();
//		hideAllViews();

		}

		public void cancelAllTask() {

			for (LoadMoreTask mTask : mLoadMoreTasks) {
				if (mTask != null && mTask.getStatus() == Status.RUNNING) {
					mTask.cancel(true);

					// Log.d("AsyncDebug", "Task cancelled!!!!!!!!");
				}
				// else
				// Log.d("AsyncDebug", "Task cancellation failed!!!!");
			}

		}

		protected void filtering(Video v) {
			// TODO Auto-generated method stub

		}

		public void hideAllViews() {
			if (fullscreenLoadingView != null)
				fullscreenLoadingView.setVisibility(View.GONE);
			if (myLoadMoreListView != null)
				myLoadMoreListView.setVisibility(View.GONE);
			if (mRetryView != null)
				mRetryView.setVisibility(View.GONE);
		}
	}
}
