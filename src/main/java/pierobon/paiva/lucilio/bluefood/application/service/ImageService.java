package pierobon.paiva.lucilio.bluefood.application.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pierobon.paiva.lucilio.bluefood.util.IOUtils;

@Service
public class ImageService {
	
	@Value("${bluefood.files.logo}")
	private String logoDir;
	
	@Value("${bluefood.files.food}")
	private String foodDir;
	
	@Value("${bluefood.files.category}")
	private String categoryDir;
	
	public void uploadLogo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, logoDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public void uploadFood(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, foodDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public byte[] getBytes(String type, String imgName) {
		
		try {
			String dir;
			
			if ("food".equals(type)) {
				dir = foodDir;
				
			} else if ("logo".equals(type)) {
				dir = logoDir;
				
			} else if ("category".equals(type)) {
				dir = categoryDir;
			} else {
				throw new Exception(type + "não é um tipo de imagem válido.");
			}
			
			return IOUtils.getBytes(Paths.get(dir, imgName));
		
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		} 
		
	}

}
