package com.qp.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qp.model.YtSearchItem;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

@Service
@Configurable
public class YtSearcherSelenium {
	
	@Value("${chromedriver}")
	private String chromedriver;
	
	 public List<YtSearchItem> search(String request) {
		 System.setProperty("webdriver.chrome.driver", chromedriver);		
		 WebDriver driver = new ChromeDriver();
		 String url = "https://www.youtube.com/results?search_query="
				 +request.replace(' ', '+');
		 driver.get(url);
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 for (int i = 0; i<8 ;i++) {
			 js.executeScript("window.scrollBy(0,250)");
		 }
		 List<WebElement> list = driver.findElements(By.tagName("ytd-video-renderer"));
		 List<YtSearchItem> response = new ArrayList<>();
		 for(WebElement el :list) {
			 YtSearchItem item = new YtSearchItem();
			 WebElement img = el.findElement(By.tagName("img"));
			 item.setThumbnail(img.getAttribute("src"));
			 WebElement title = el.findElement(By.id("video-title"));
			 item.setId(title.getAttribute("href"));
			 item.setName(title.getText());
			 response.add(item);
		 }		
		 driver.close();
		 return response;
	 }
	 
}
