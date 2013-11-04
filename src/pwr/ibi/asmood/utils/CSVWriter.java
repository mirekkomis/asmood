package pwr.ibi.asmood.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pwr.ibi.asmood.utils.Operator.TestResult;

public class CSVWriter {

	public static void writeCSV(ArrayList<TestResult> results){
		try {
			FileWriter writer = new FileWriter(System.getProperty("user.dir") + System.currentTimeMillis() +".csv");
			
			for(TestResult item: results){
				writer.append(item.testType+","+item.ASN+","+item.Host+","+item.result+","+item.time+"\n");
			}
			
			writer.flush();
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
