package com.qp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "queue")
public class QueueItem {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "time")
	private LocalDateTime timePlayed;
	
	@Column(name = "status")
	private Integer status;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	private VideoItem video;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(LocalDateTime timePlayed) {
		this.timePlayed = timePlayed;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	public VideoItem getVideo() {
		return video;
	}

	public void setVideo(VideoItem video) {
		this.video = video;
	}

}
