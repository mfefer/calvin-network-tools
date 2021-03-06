package edu.ucdavis.watershed;

import edu.ucdavis.watershed.Dss;
import hec.heclib.dss.HecDss;
import edu.ucdavis.watershed.Csv;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CMD {
	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		CommandLineInput input = mapper.readValue(new File(args[0]), CommandLineInput.class);

		HecDss dssFile = Dss.open(input.getPath());

		if( input.isExport() ) {
			String root = "";
			if( input.getExportRoot() != null ) {
				root = input.getExportRoot();
			}
			
			Dss.exportJson(dssFile, root, input.getRegex());
			return;
		}
		
		for( Config config: input.getData() ) {
			CsvData data = Csv.parseCsv(config.getCsvFilePath(), config.getType());
			Dss.write(config, data, dssFile, input.getPath());
		}

		printSuccess();
	}

	public static void printError(String message) {
		System.out.println("{\"error\":true,\"message\":\""+message+"\"}");
	}

	public static void printSuccess() {
		System.out.println("{\"success\":true}");
	}
}
