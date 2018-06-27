package com.qp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qp.model.QueueItem;
import com.qp.model.VideoItem;

@Repository
public interface QueueItemRepository extends JpaRepository<QueueItem, Integer> {

	QueueItem findByStatusAndVideo (Integer status, VideoItem videoItem);
	
	QueueItem findFirstByStatus(Integer status);
}
