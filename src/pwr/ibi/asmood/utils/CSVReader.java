package pwr.ibi.asmood.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;

import pwr.ibi.asmood.logic.ProgressListener;

public class CSVReader {

	public static ASDescription[] readCSV(String path, ProgressListener progress)
	{
		BufferedReader br = null;
		LineNumberReader lnr = null;	
		String line = "";
		String cvsSplitBy = "[,]";
		int lines_number = 0,line_nr = 0,percent=0, tmp_percent=0;
		LinkedList<ASDescription> temp = new LinkedList<>();
		try 
		{
			if(progress!=null)
			{
				lnr = new LineNumberReader(new FileReader(path));
				lnr.skip(Integer.MAX_VALUE);
				lines_number = lnr.getLineNumber();
				lnr.close();	
			}
			
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) 
			{
				String[] log = line.split(cvsSplitBy);
				temp.add(new ASDescription(log));
				if(progress!=null)
				{
					line_nr++;
					progress.onValueChange(line_nr, lines_number);
					tmp_percent = (int)(100.0*(((double)line_nr)/(double)lines_number));
					if(tmp_percent > percent)
					{
						percent=tmp_percent;
						progress.onProgres(percent);
					}
				}
			}
		} catch (FileNotFoundException e) {
			Operator.LOG(""+e.getMessage());
		} catch (IOException e) {
			Operator.LOG(""+e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Operator.LOG(""+e.getMessage());
				}
			}
		}
		ASDescription[] out = new ASDescription[temp.size()];
		temp.toArray(out);
		temp.clear();
		temp = null;
		System.gc();
		return out;
	}
	
//	private Instances data_set;
//	
//	public enum SELECTOR_TYPE
//	{
//		SYSTEMATIC,
//		RANDOM_N_OF_N,
//		UNIFORM_PROBAB
//		
//	}
//	public boolean readARFF(String path)
//	{
//		boolean result;
//		DataSource data_source = null;
//		try 
//		{
//			data_source = new DataSource(path);
//			data_set = data_source.getDataSet();
//			result = true;
//		} catch (Exception e) 
//		{result = false;}
//		
//		return result && data_source != null && data_set != null;
//	}
//	
//	public static CSVTrafficLog[] readCSV(String path, ProgressListener progress, String[] protocols)
//	{
//		BufferedReader br = null;
//		LineNumberReader lnr = null;	
//		String line = "";
//		String cvsSplitBy = "[,]";
//		String temp_protocol;
//		boolean match = false;
//		int lines_number = 0,line_nr = 0,percent=0, tmp_percent=0;
//		LinkedList<CSVTrafficLog> temp = new LinkedList<>();
//		try 
//		{
//			if(progress!=null)
//			{
//				lnr = new LineNumberReader(new FileReader(path));
//				lnr.skip(Integer.MAX_VALUE);
//				lines_number = lnr.getLineNumber();
//				lnr.close();	
//			}
//			
//			br = new BufferedReader(new FileReader(path));
//			while ((line = br.readLine()) != null) 
//			{
//				String[] log = line.split(cvsSplitBy);
//				if(protocols != null)
//				{
//					match = false;
//					temp_protocol = log[4].substring(1, log[4].length()-1);
//					for(String p : protocols)
//					{
//						if(p.equalsIgnoreCase(temp_protocol))
//						{
//							match = true;
//							break;
//						}
//					}
//					if(match)
//						temp.add(new CSVTrafficLog(log));
//					
//					if(progress!=null)
//					{
//						line_nr++;
//						progress.onValueChange(line_nr, lines_number);
//						tmp_percent = (int)(100.0*(((double)line_nr)/(double)lines_number));
//						if(tmp_percent > percent)
//						{
//							percent=tmp_percent;
//							progress.onProgres(percent);
//							
//						}
//						
//					}
//				}
//				else
//				{
//					temp.add(new CSVTrafficLog(log));
//					if(progress!=null)
//					{
//						line_nr++;
//						progress.onValueChange(line_nr, lines_number);
//						tmp_percent = (int)(100.0*(((double)line_nr)/(double)lines_number));
//						if(tmp_percent > percent)
//						{
//							percent=tmp_percent;
//							progress.onProgres(percent);
//						}
//						
//					}
//				}
//			}
//		} catch (FileNotFoundException e) {
//			Operator.LOG(""+e.getMessage());
//		} catch (IOException e) {
//			Operator.LOG(""+e.getMessage());
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					Operator.LOG(""+e.getMessage());
//				}
//			}
//		}
//		CSVTrafficLog[] out = new CSVTrafficLog[temp.size()];
//		temp.toArray(out);
//		temp.clear();
//		temp = null;
//		System.gc();
//		return out;
//	}
//	
//	public static CSVTrafficLog[] readCSV(String path, ProgressListener progress)
//	{return readCSV(path, progress, null);}
//	
//	public static CSVTrafficLog[] resample(CSVTrafficLog[] input, SELECTOR_TYPE selector, int[] params)
//	{
//		if(selector == SELECTOR_TYPE.SYSTEMATIC)
//			return SystematicSampler(input ,params[0], params[1]);
//		else if(selector == SELECTOR_TYPE.RANDOM_N_OF_N)
//			return RandomSampler(input, params[0], params[1]);
//		else if(selector == SELECTOR_TYPE.UNIFORM_PROBAB)
//			return UniformProbabSampler(input, params[0]);
//		return null;
//	}
//	
//	public static CSVTrafficLog[] resample(CSVTrafficLog[] input, SELECTOR_TYPE selector, double[] params)
//	{
//		if(selector == SELECTOR_TYPE.UNIFORM_PROBAB)
//			return UniformProbabSampler(input,params[0]);
//		else
//			return resample(input, selector, new int[]{(int)params[0],(int)params[1]});
//	}
//	
//	private static CSVTrafficLog[] UniformProbabSampler(CSVTrafficLog[] input, double probability)
//	{
//		int size = (int)((probability/100.0)*input.length);
//		int[] set = getRandomSet(size, input.length);
//		CSVTrafficLog[] result = new CSVTrafficLog[size];
//		for(int i = 0; i < size; i++)
//		{
//			result[i] = input[set[i]];
//		}
//		return result;
//	}
//	
//	private static CSVTrafficLog[] SystematicSampler(CSVTrafficLog[] input, int start, int interval)
//	{
//		int size = (input.length - start)/interval;
//		CSVTrafficLog[] result = new CSVTrafficLog[size];
//		int temp_id;
//		
//		for(int i = 0; i < size; i++)
//		{
//			temp_id = start+(i*interval);
//			result[i] = input[temp_id];
//		}
//		return result;
//	}
//	
//	private static CSVTrafficLog[] RandomSampler(CSVTrafficLog[] input, int n, int N)
//	{
//		int count = (input.length/N),
//				size = count*n, 
//				temp_id;
//			CSVTrafficLog[] result = new CSVTrafficLog[size];
//			for(int i = 0; i < count; i++)
//			{
//				int[] set = getRandomSet(n, N);
//				for(int j = 0; j < n; j++)
//				{
//					temp_id = i*N+set[j]; // row index in data_set;
//					result[i*n+j] = input[temp_id];
//				}
//			}
//			return result;
//	}
//	
//	public ArffTrafficLog[] getSample(SELECTOR_TYPE selector, int[] params)
//	{
//		if(selector == SELECTOR_TYPE.SYSTEMATIC)
//			return SystematicSampler(params[0], params[1]);
//		else if(selector == SELECTOR_TYPE.RANDOM_N_OF_N)
//			return RandomSampler(params[0], params[1]);
//		else if(selector == SELECTOR_TYPE.UNIFORM_PROBAB)
//			return UniformProbabSampler(params[0]);
//		return null;
//	}
//	
//	public ArffTrafficLog[] getSample(SELECTOR_TYPE selector, double[] params)
//	{
//		if(selector == SELECTOR_TYPE.UNIFORM_PROBAB)
//			return UniformProbabSampler(params[0]);
//		else
//			return getSample(selector, new int[]{(int)params[0],(int)params[1]});
//	}
//	
//	private ArffTrafficLog[] SystematicSampler(int start, int interval)
//	{
//		int size = (data_set.size() - start)/interval;
//		ArffTrafficLog[] result = new ArffTrafficLog[size];
//		Instance temp;
//		int temp_id;
//		
//		for(int i = 0; i < size; i++)
//		{
//			temp_id = start+(i*interval);
//			temp = data_set.get(temp_id);
//			result[i] = new ArffTrafficLog(
//					"0",
//					temp.stringValue(1),
//					temp.stringValue(2),
//					temp.stringValue(data_set.attribute("flag")),
//					temp.value(4)+"",
//					temp.value(5)+"");
//			result[i].setID(temp_id);
//		}
//		return result;
//	}
//	
//	private ArffTrafficLog[] RandomSampler(int n, int N)
//	{
//		int count = (data_set.size()/N),
//			size = count*n, 
//			temp_id;
//		ArffTrafficLog[] result = new ArffTrafficLog[size];
//		Instance temp;
//		for(int i = 0; i < count; i++)
//		{
//			int[] set = getRandomSet(n, N);
//			for(int j = 0; j < n; j++)
//			{
//				temp_id = i*N+set[j]; // row index in data_set;
//				temp = data_set.get(temp_id);
//				result[i*n+j] = new ArffTrafficLog(
//						"0",
//						temp.stringValue(1),
//						temp.stringValue(2),
//						temp.stringValue(data_set.attribute("flag")),
//						temp.value(4)+"",
//						temp.value(5)+"");
//				result[i*n+j].setID(temp_id);
//			}
//		}
//		return result;
//	}
//	
//	private ArffTrafficLog[] UniformProbabSampler(double probability)
//	{
//		int size = (int)((probability/100.0)*data_set.size());
//		int[] set = getRandomSet(size, data_set.size());
//		ArffTrafficLog[] result = new ArffTrafficLog[size];
//		Instance temp;
//		for(int i = 0; i < size; i++)
//		{
//			temp = data_set.get(set[i]);
//			result[i] = new ArffTrafficLog(
//					"0",
//					temp.stringValue(1),
//					temp.stringValue(2),
//					temp.stringValue(data_set.attribute("flag")),
//					temp.value(4)+"",
//					temp.value(5)+"");
//			result[i].setID(set[i]);
//		}
//		return result;
//	}
//	
//	private static int[] getRandomSet(int n, int N)
//	{
//		int[] set = new int[n];
//		int current = 0;
//		int rnd, i;
//		do
//		{
//			rnd = (int)(Math.random()*N);
//			for(i = 0; i < current; i++)
//			{
//				if(set[i]==rnd)
//					break;
//			}
//			if(i == current)
//			{	
//				set[current] = rnd;
//				current++;
//			}
//		}while(current < n);
//		
//		Arrays.sort(set);
//		
//		return set;
//	}
}
