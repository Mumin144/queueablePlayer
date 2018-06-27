package com.qp.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.qp.model.QueueItem;
import com.qp.model.VideoItem;
import com.qp.repository.QueueItemRepository;
import com.qp.repository.VideoItemRepository;

@Service
@Configurable
public class QueueService {
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	VideoItemRepository videoItemRepository;
	
	@Autowired
	QueueItemRepository queueItemRepository;
	
		
	public List<VideoItem> scanForVideos() {		
		List<VideoItem> itemsAdded= new ArrayList<VideoItem>();
		List<Path> songList = fileStorageService.getFileList();
		songList.forEach(song ->{
			if(videoItemRepository.findByfilePath(song.toAbsolutePath().toString())==null) {
				VideoItem videoItem = new VideoItem();
				videoItem.setFilePath(song.toAbsolutePath().toString());
				String name = song.getFileName().toString();
				/*if (name.indexOf(".")!=-1) {
					name = name.substring(0, name.indexOf("."));
				}*/
				videoItem.setName(name);
				videoItemRepository.save(videoItem);
				itemsAdded.add(videoItem);
			}
		});		
		return itemsAdded;
	}
	
	public VideoItem enqueue(VideoItem videoItem) {	
		VideoItem video = videoItemRepository.findByName(videoItem.getName());
		if(video == null) {
			return null;
		}
		if (queueItemRepository.findByStatusAndVideo(0, video)==null) {	
			QueueItem queueItem = new QueueItem();
			queueItem.setVideo(video);
			queueItem.setStatus(0);
			queueItemRepository.save(queueItem);
			return queueItem.getVideo();
		}else {
			return null;
		}		
	}
	
	public VideoItem getNext() {
		QueueItem queueItem = queueItemRepository.findFirstByStatus(0);
		if (queueItem != null) {
			queueItem.setStatus(1);
			queueItemRepository.save(queueItem);
		}else {
			queueItem = new QueueItem();
			queueItem.setStatus(1);
			queueItem.setVideo(getRandom());
			queueItemRepository.save(queueItem);
		}//get next random one	
		return queueItem.getVideo();		
		
		
	}
	
	public List<VideoItem> getAllSongs(){
		return videoItemRepository.findAll();
	}
	
	private VideoItem getRandom() {
		List<VideoItem> list = videoItemRepository.findAll();
		Integer total = list.size();
		Random rand = new Random();
		return list.get(rand.nextInt(total));
	}

}
