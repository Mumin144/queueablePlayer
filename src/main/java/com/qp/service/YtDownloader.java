package com.qp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qp.model.VideoItem;
import com.qp.model.YtRequest;
import com.qp.model.YtSearchItem;
import com.qp.repository.VideoItemRepository;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
@Configurable
public class YtDownloader {
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	VideoItemRepository videoItemRepository;
	
	@Autowired
	QueueService queueService;
	
	@Value("${filePath}")
	private String folderPath;
    //private OkHttpClient client = new OkHttpClient();
	
	public List<YtSearchItem> search(String requestString){
		OkHttpClient.Builder builder = new Builder();
		OkHttpClient client = builder.readTimeout(30, TimeUnit.SECONDS).build();
		String siteUrl = "https://www.youtube.com/results?search_query="
						+requestString.replace(' ', '+');
		final Request.Builder requestBuilder = new Request.Builder()
                .method("GET", null).url(siteUrl);
		Request request = requestBuilder.build();
	    Response response ;
	    try {
			response = client.newCall(request).execute();
			String body = response.body().string();			
			Document doc = Jsoup.parse(body);
			Element div = doc.getElementById("results");
			Elements ol = div.getElementsByClass("yt-lockup-thumbnail");	
			List<YtSearchItem> list = new ArrayList<>();
			for (Element el : ol) {
				YtSearchItem item = new YtSearchItem();
				item.setId("https://www.youtube.com"+el.getElementsByTag("a").attr("href"));
				item.setThumbnail(el.getElementsByTag("img").attr("src"));
				item.setName(getVideoName(item.getId()));
				list.add(item);
			}
			
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public String downloadYT(YtRequest ytRequest) {		
  	    OkHttpClient.Builder builder = new Builder();
		OkHttpClient client = builder.readTimeout(30, TimeUnit.SECONDS).build();
		String siteUrl = getVideoLink(ytRequest.getUrl());
		String fileName = ytRequest.getName();
		if (ytRequest.getName() == null) {
			fileName = getVideoName(ytRequest.getUrl());
		}
		final Request.Builder requestBuilder = new Request.Builder()
                .method("GET", null).url(siteUrl);
		Request request = requestBuilder.build();
	    Response response;
		try {
			response = client.newCall(request).execute();
			ResponseBody body = response.body();
			InputStream stream = body.byteStream();
			fileStorageService.saveFileFromStream(stream, fileName);
			VideoItem vid = new VideoItem();
			vid.setName(fileName);
			vid = videoItemRepository.saveAndFlush(vid);
			if(ytRequest.getAddAtEnd()) {
				queueService.enqueue(vid);
			}
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public String downloadYTbyYTDL(YtRequest ytRequest) {		
		String fileName = ytRequest.getName();
		if (fileName == null) {
			fileName = getVideoName(ytRequest.getUrl());
		}
		String path = folderPath + fileName + ".mp4";
		try {
			Process p;
			p = Runtime.getRuntime().exec(new String[] {"youtube-dl",
					"-f", "22",
					"-o", path,
					ytRequest.getUrl()});
			p.waitFor();
			if (p.exitValue()!= 0) { // if no 720p get next best
				path = path.substring(0, path.indexOf('.'));
				p = Runtime.getRuntime().exec(new String[] {"youtube-dl",
						"--recode-video", "mp4",
						"-o", path,
						ytRequest.getUrl()});
				p.waitFor();
				if(p.exitValue()!=0) {
					p.destroy();
					return null;
				}
			}
            p.destroy();
			VideoItem vid = new VideoItem();
			vid.setName(fileName);
			vid = videoItemRepository.saveAndFlush(vid);
			if(ytRequest.getAddAtEnd()) {
				queueService.enqueue(vid);
			}
			return fileName;
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return null;
		}
		 
	}
	
	private String getVideoLink(String url) {
		StringBuffer output = new StringBuffer();		
		Process p;
		
		try {
			p = Runtime.getRuntime().exec("youtube-dl -g -f 22 "+ url);
			p.waitFor();
			BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line); //check if video in 720p is available
			}
			line = output.toString();
			if (line.isEmpty()) {// if no 720p then get next best
				line = "";
				p = Runtime.getRuntime().exec("youtube-dl -g "+ url);
				p.waitFor();
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            	output.append(reader.readLine());
				/*while ((line = reader.readLine())!= null) {
					output.append(line);
				}*/
            	p.destroy();
			}
			if(output.toString().length()==0) {
				return null;
			}
			return output.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getVideoName(String url) {
		StringBuffer output = new StringBuffer();		
		Process p;		
		try {
			p = Runtime.getRuntime().exec("youtube-dl -e "+ url);
			p.waitFor();
			BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line); 
			}
			line = output.toString();
			p.destroy();
			if(output.toString().length()==0) {
				return "failed to get a name";
			}
			return output.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
