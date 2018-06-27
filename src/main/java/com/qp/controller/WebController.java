package com.qp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.qp.model.VideoItem;
import com.qp.model.YtRequest;
import com.qp.model.YtSearchItem;
import com.qp.service.QueueService;
import com.qp.service.YtDownloader;
import com.qp.service.YtSearcherSelenium;


@Controller
public class WebController {
	@Autowired
	YtDownloader ytDownloader;
	
	@Autowired
	QueueService queueService;
	
	@Autowired
	YtSearcherSelenium ytSearchSelenium;
	
	@RequestMapping(value={"/player"}, method = RequestMethod.GET)
	public ModelAndView player(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("player");
		return modelAndView;
	}
	
	@RequestMapping(value={"/", "/remote"}, method = RequestMethod.GET)
	public ModelAndView remote(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("remote");
		return modelAndView;
	}
	
	@RequestMapping(value={ "/addVideo"}, method = RequestMethod.GET)
	public ModelAndView addVideo(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("addition");
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value={"/getNext"}, method = RequestMethod.GET)
	public ResponseEntity<VideoItem> getNext(){		
		return new ResponseEntity<VideoItem>(queueService.getNext(), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/listVideos"}, method = RequestMethod.GET)
	public ResponseEntity<List<VideoItem>> listVideos(){		
		return new ResponseEntity<List<VideoItem>>(queueService.getAllSongs(), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/scan"}, method = RequestMethod.GET)
	public ResponseEntity<List<VideoItem>> scanVideos(){
		return new ResponseEntity<List<VideoItem>>(queueService.scanForVideos(), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/enqueue"}, method = RequestMethod.POST)
	public ResponseEntity<VideoItem> enqueue(@RequestBody VideoItem videoItem){
		videoItem = queueService.enqueue(videoItem);
		if (videoItem != null) {
			return new ResponseEntity<VideoItem>(videoItem, HttpStatus.OK);
		}
		return new ResponseEntity<VideoItem>(videoItem, HttpStatus.BAD_REQUEST);
	}
	
	@ResponseBody
	@RequestMapping(value={"/getVideo"}, method = RequestMethod.POST)
	public ResponseEntity<String> getVideo(@RequestBody YtRequest ytRequest){
		String response = ytDownloader.downloadYTbyYTDL(ytRequest);
		if (response != null) {
			return new ResponseEntity<String>(response, HttpStatus.OK);			
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseBody
	@RequestMapping(value={"/searchYt"}, method = RequestMethod.GET)
	public ResponseEntity<List<YtSearchItem>> searchYt(@RequestParam("str") String request){
		List<YtSearchItem> response = ytSearchSelenium.search(request);
		if (response != null) {
			return new ResponseEntity<List<YtSearchItem>>(response, HttpStatus.OK);			
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
