package com.examples.dota2tv.feedManagers;

import com.examples.dota2tv.data.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedManager_Subscription extends FeedManager_Base {

	@Override
	public ArrayList<Video> getVideoPlaylist() {
		processJSON(mJSON);
		ArrayList<Video> videos = new ArrayList<Video>();

		try {
			// System.out.println(plTitle);
			// get the playlist
			//JSONArray playlist = feed.getJSONArray("entry");
			JSONArray playlist = feed.getJSONArray("items");
			// System.out.println("Length: "+ playlist.length());

			for (int i = 0; i < playlist.length(); i++) {
				// get a video in the playlist
				JSONObject oneVideo = playlist.getJSONObject(i);
				// get the title of this video
				String videoTitle = oneVideo.getString("title");
				String videoLink = null;
				String videoId = null;

				videoLink = oneVideo.getString("link");
				videoId = oneVideo.getString("id").substring(10);

				// System.out.println("Working 2: "+videoLink);
				String videoDesc = oneVideo.getJSONObject("media:group").getString("media:description");
				String thumbUrl = oneVideo.getJSONObject("media:group").getJSONObject("media:thumbnail").getString("url");
				String updateTime = oneVideo.getString("published");
				// System.out.println("Working 4");
				String author = oneVideo.getJSONObject("author").getString("name");
				String vCount = oneVideo.getJSONObject("media:group").getJSONObject("media:community").getJSONObject("media:statistics").getString("views") + " views";
//				String inSecs = oneVideo.getJSONObject("media$group").getJSONObject("yt$duration").getString("seconds");
				String inSecs = "0";
				String convertedDuration = formatSecondsAsTime(inSecs) + " HD";

				// System.out.println("date: " +
				// updateTime.substring(0,updateTime.indexOf("T")));
				// System.out.println("time: " +
				// updateTime.substring(updateTime.indexOf("T")+1,
				// updateTime.indexOf(".")));
				updateTime = handleDate(updateTime);

				Video video = new Video();

				// System.out.println("converted duration: " +
				// convertedDuration);
				// System.out.println(videoDesc);
				// store title and link

				video.setTitle(videoTitle);
				video.setVideoId(videoId);
				video.setThumbnailUrl(thumbUrl);
				video.setVideoDesc(videoDesc);
				video.setUpdateTime(updateTime);
				video.setAuthor(author);
				video.setViewCount(vCount);
				video.setDuration(convertedDuration);
				// System.out.println(video.getTitle());
				// push it to the list
				videos.add(video);
				// System.out.println(videoTitle+"***"+videoLink);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return videos;

	}

}
