package com.SSProj.filedownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController

public class FiledownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiledownloadApplication.class, args);
	}
		@RequestMapping(value = "/download", method = RequestMethod.GET)
		public ResponseEntity<Object> downloadFile() throws IOException {

			FileWriter filewriter = null;

			try {
				CSVData csv1 = new CSVData();
				csv1.setId("1");
				csv1.setName("SStefanovskyi");
				csv1.setNumber("5601");

				CSVData csv2 = new CSVData();
				csv2.setId("2");
				csv2.setName("Serhii Stefanovskyi");
				csv2.setNumber("8710");

				List<CSVData> csvDataList = new ArrayList<>();
				csvDataList.add(csv1);
				csvDataList.add(csv2);

				StringBuilder filecontent = new StringBuilder("ID, NAME, NUMBER\n");
				for (CSVData csv : csvDataList) {
					filecontent.append(csv.getId()).append(",").append(csv.getName()).append(",").append(csv.getNumber()).append("\n");
				}

				String filename = "C:\\Users\\SStefanovskyi\\Desktop\\filedownload";

				filewriter = new FileWriter(filename);
				filewriter.write(filecontent.toString());
				filewriter.flush();

				File file = new File(filename);

				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Pragma", "no-cache");
				headers.add("Expires", "0");

				ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
				return responseEntity;
			} catch (Exception e) {
				return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			finally {
				if(filewriter!=null)
					filewriter.close();
			}

		}


}
