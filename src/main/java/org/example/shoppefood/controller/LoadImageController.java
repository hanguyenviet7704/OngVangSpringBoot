package org.example.shoppefood.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.*;
@Controller
public class LoadImageController {
	@Value("${upload.path}")
	private String pathUploadImage;
	@GetMapping(value = "loadImage")
	@ResponseBody
	public ResponseEntity<byte[]> loadImage(@RequestParam("imageName") String imageName) {
		File file = new File(pathUploadImage + File.separator + imageName);
		if (!file.exists()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		try (InputStream inputStream = new FileInputStream(file);
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			byte[] temp = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(temp)) != -1) {
				buffer.write(temp, 0, bytesRead);
			}
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(buffer.toByteArray());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
