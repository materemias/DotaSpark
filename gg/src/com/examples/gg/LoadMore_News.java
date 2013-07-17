package com.examples.gg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

public class LoadMore_News extends LoadMore_Base {

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private ViewGroup group;
	private ArrayList<String> matches = new ArrayList();
	private Elements links;
	boolean isPagerSet = false;

	@Override
	public void Initializing() {
		// Inflating view
		view = mInflater.inflate(R.layout.whatsnew, null);

		// Give a title for the action bar
		abTitle = "News";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/cpGJHANGum7tFm0kg6fh7g/newsubscriptionvideos?max-results=10&alt=json");

		// initialize the fragments in the Menu
		// FragmentAll = new LoadMore_H_Subscription();
		// FragmentUploader = new LoadMore_H_Uploader();
		// FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(false);
		
		// Set retry button listener
		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Continue to check network status
				networkHandler(new LoadMore_News());

			}
		});

	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//
//		myLoadMoreListView = (LoadMoreListView) this.getListView();
//		myLoadMoreListView.setDivider(null);
//
//		if (ic.checkConnection(sfa)) {
//			if (isMoreVideos) {
//				// there are more videos in the list
//				// set the listener for loading need
//				myLoadMoreListView
//						.setOnLoadMoreListener(new OnLoadMoreListener() {
//							public void onLoadMore() {
//								// Do the work to load more items at the end of
//								// list
//								// hereru
//
//								// checking network
//								if (ic.checkConnection(sfa)) {
//
//									// network ok
//									if (isMoreVideos == true) {
//										new LoadMoreTask_News().execute(API
//												.get(0));
//									}
//								} else {
//									//ic.networkToast(sfa);
//									((LoadMoreListView) getListView())
//											.onLoadMoreComplete();
//								}
//
//							}
//						});
//
//			} else{
//				myLoadMoreListView.setOnLoadMoreListener(null);
//			}
//			
//			// show loading screen
//			// sending Initial Get Request to Youtube
//			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
//					View.VISIBLE);
//
//			if (!API.isEmpty())
//				doRequest();
//
//		}
//
//
//
//	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		advPager = (ViewPager) sfa.findViewById(R.id.adv_pager);
		group = (ViewGroup) sfa.findViewById(R.id.viewGroup);

		List<View> advPics = new ArrayList<View>();
		FrameLayout flayout = new FrameLayout(sfa);
		LinearLayout lLayout = new LinearLayout(sfa);
		lLayout.setOrientation(LinearLayout.VERTICAL);
		TextView title = new TextView(sfa);
		title.setTextColor(Color.WHITE);
		title.setTextSize(24);
		title.setText("Today's matches");
		LinearLayout.LayoutParams titleLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		titleLayout.setMargins(20, 10, 0, 0);
		LinearLayout.LayoutParams matchLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		matchLayout.setMargins(20, 20, 0, 0);
		
		TextView matchtable = new TextView(sfa);
		matchtable.setSingleLine(false);

		String Matchtext = "";
		String[] matcharray = matches.toArray(new String[matches.size()]);

		for (int i = 0; i < 3; i++) {
			Matchtext = Matchtext + matcharray[i] + "\n";
		}
		matchtable.setText(Matchtext);
		matchtable.setTextColor(Color.WHITE);
		matchtable.setTextSize(18);
		ImageView img1 = new ImageView(sfa);
		img1.setBackgroundResource(R.drawable.bountyhunter);

		flayout.addView(img1, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER));
		
		lLayout.addView(title, titleLayout);
		lLayout.addView(matchtable, matchLayout);
		flayout.addView(lLayout, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		advPics.add(flayout);

		ImageView img2 = new ImageView(sfa);
		img2.setBackgroundResource(R.drawable.centaurwarlord);
		advPics.add(img2);

		ImageView img3 = new ImageView(sfa);
		img3.setBackgroundResource(R.drawable.razor);
		advPics.add(img3);

		ImageView img4 = new ImageView(sfa);
		img4.setBackgroundResource(R.drawable.snk);
		advPics.add(img4);

		imageViews = new ImageView[advPics.size()];
		for (int i = 0; i < advPics.size(); i++) {
			imageView = new ImageView(sfa);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(5, 5, 5, 5);
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.selected);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.unselected);
			}
			group.addView(imageViews[i]);
		}

		advPager.setAdapter(new AdvAdapter(advPics));
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		advPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(what.get());
						whatOption();
					}
				}
			}

		}).start();
	}

	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > imageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			advPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.selected);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.unselected);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	class LoadMoreTask_News extends LoadMoreTask {
		@Override
		protected String doInBackground(String... uri) {

			super.doInBackground(uri[0]);

			if (!isPagerSet) {
				String url = "http://www.gosugamers.net/dota2/gosubet";
				Document doc;
				try {
					doc = Jsoup
							.connect(url)
							.header("User-Agent",
									"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
							.get();

					links = doc.select("tr:has(td.opp)");

					// for (String match: matches){
					// System.out.println(match);
					// }

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			if(ic.checkConnection(sfa)){
				if (!taskCancel && result != null) {
					if (!isPagerSet) {
						for (Element link : links) {
	
							String match;
	
							match = link.select("span").first().text().trim()
									+ " vs "
									+ link.select("span").get(2).text().trim()
									+ " ";
							if (link.getElementsByClass("results").isEmpty())
								match += link.select("td").get(3).text().trim();
							else
								match += link.select("span.hidden").first().text()
										.trim();
	
							matches.add(match);
						}
	
						initViewPager();
	
						isPagerSet = true;
					}
	
					super.onPostExecute(result);
				}
			}else{
				// No internet
				// Cancel the load more animation
				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();
				
				if(sfa.findViewById(R.id.fullscreen_loading_indicator).getVisibility() == View.VISIBLE){
					// Internet lost during fullscree loading
					
					ic.setNetworkError(InternetConnection.fullscreenLoadingError);
				}else{
					// Internet lost during loading more
					ic.setNetworkError(InternetConnection.loadingMoreError);
				}
			}
		}

	}

	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API)
			new LoadMoreTask_News().execute(s);
	}

}
