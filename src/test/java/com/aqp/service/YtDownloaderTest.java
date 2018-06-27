package com.aqp.service;

import org.junit.Before;
import org.junit.Test;

import com.qp.service.YtDownloader;

public class YtDownloaderTest {
	
	private YtDownloader classTested;

	@Before
	public void setUp() throws Exception {
		
		classTested = new YtDownloader();
	}

	@Test
	public void testDownloadYT() {
		
	}

	/*@Test
	public void testGetVideoLink() {
		assertNotNull( classTested.getVideoLink("https://www.youtube.com/watch?v=tPEE9ZwTmy0"));
		
		assertNull("wrong link", classTested.getVideoLink("asd"));
	}*/

}
