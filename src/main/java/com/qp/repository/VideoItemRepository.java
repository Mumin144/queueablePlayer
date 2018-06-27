package com.qp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qp.model.VideoItem;

@Repository
public interface VideoItemRepository extends JpaRepository<VideoItem, Integer> {

	VideoItem findByfilePath (String path);
	
	VideoItem findByName (String name);
	
	
}
