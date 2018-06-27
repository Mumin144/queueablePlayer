package com.qp.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class FileStorageService {

	
	//final static String folderPath = "files/";
	@Value("${filePath}")
	private String folderPath;
	//final static String folderPath;
	
	
	public boolean checkIfFileExists(String path) {
		File f = new File(folderPath + path);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
	
	public String saveFileFromStream (InputStream stream, String fileName) throws IOException {
		int x=0;
		File file;
		if(checkIfFileExists(folderPath+fileName)) {
			do {
				x++;
			}while(checkIfFileExists(folderPath+fileName+x));
			file = new File(folderPath, fileName+x);
			
		}else {
			file = new File(folderPath, fileName);
			
		}
		java.nio.file.Files.copy(
				stream,
				file.toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		IOUtils.closeQuietly(stream);
		return file.getName();
	}
	
	public FileSystemResource getFile(String path) {
		File file = new File(path);		
		return  new FileSystemResource(file);
	}
	
	public URI getFileUri (String path) {
		File file = new File(path);
		System.out.println(file.toURI());
		return file.toURI();
	}
	 
	public List<Path> getFileList(){
		List<Path> songList = new ArrayList<Path>();
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(path -> songList.add(path) );
		} catch (IOException e) {			
			e.printStackTrace();
			return null;
		} 
		return songList;
	}
}
