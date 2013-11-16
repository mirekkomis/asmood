package pwr.ibi.asmood.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import pwr.ibi.asmood.logic.ProgressListener;

public class Operator 
{
	ASDescription[] log;
	
	private static JTextArea info_area;
	
	public ArrayList<ResearchResultManager> researchResultManager = new ArrayList<ResearchResultManager>();
	ArrayList<ResearchResult> list;
	
	public void read(String path, boolean filtering, ProgressListener listener)
	{
		
		if(listener!=null)
			listener.onStart();

		log = CSVReader.readCSV(path, listener);
//		log = CSVReader.readCSV("C:\\Users\\Adam\\Documents\\studia\\mgr semestr II\\IBI\\l\\GeoIPASNum2\\GeoIPASNum2.csv", listener);

		if(listener!=null)
			listener.onFinish();
	}
	
	public void readResult(String path, boolean filtering, ProgressListener listener){
		if(listener!=null)
			listener.onStart();

		
		researchResultManager.add(CSVReader.readCSVTests(path, listener));
//		researchResultManager = CSVReader.readCSVTests("C:\\Users\\Adam\\workspace\\asmood1384616644663.csv", listener);
		
		if(listener!=null)
			listener.onFinish();
	}
	
	public ChartPanel pingPanel;
	public ChartPanel createPingChart() {
	  	
        JFreeChart chart = ChartFactory.createLineChart(
            "Czasy odpowiedzi hostów",    // chart title
            "Host",                // domain axis label
            "Czas odpowiedzi",           // range axis label
            createDataset(false, 0),       // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        pingPanel = new ChartPanel(chart);
        pingPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        pingPanel.setVisible(true);
        
        return pingPanel;
	}
	
	public ChartPanel tracePanel;
	public ChartPanel createTraceChart() {
	  	
        JFreeChart chart = ChartFactory.createLineChart(
            "Iloœæ odwiedzonych wêz³ów wewn¹trz ASa",    // chart title
            "Host",                // domain axis label
            "Iloœæ wêz³ów",           // range axis label
            createDataset(true, 0),       // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        tracePanel = new ChartPanel(chart);
        tracePanel.setPreferredSize(new java.awt.Dimension(300, 300));
        tracePanel.setVisible(true);
        
        return tracePanel;
	}
	
	public DefaultCategoryDataset pingDataset = new DefaultCategoryDataset();;
	public DefaultCategoryDataset traceDataset = new DefaultCategoryDataset();;
	public CategoryDataset createDataset(boolean trace, int index){
		if(trace){
			list = researchResultManager.get(index).getAsnTracerouteTests();
			for(ResearchResult item: list){
				traceDataset.addValue(item.getResult(),"Badanie "+index, item.getHost());
			}
			return traceDataset;
		}else{
			list = researchResultManager.get(index).getAsnPingTests();
			for(ResearchResult item: list){
				pingDataset.addValue(item.getResult(),"Badanie "+index, item.getHost());
			}
			return pingDataset;
		}
	}
	
	public ASDescription[] getLog()
	{
		return log;
	}
	
	private boolean stop_search = false;
	
	public void scan(LinkedList<SelectedAS> list, int port, int timeout, int maxhosts)
	{
		LOG("Scan started");
		String addr  = "";
		for(SelectedAS as : list)
		{
			long low = as.getBottomIP()+1, hi = as.getTopIP();
			while(low < hi && as.getRespondedNumber() < maxhosts && !stop_search) 
			{
				addr = ASDescription.intToIP(low);
				low++;
				
				if(portIsOpen(addr, port, timeout))
				{
					as.addResponded(addr);
					LOG("Scaning: "+addr+":"+port+" = port open");
				}
				else
				{
					LOG("Scaning: "+addr+":"+port+" = not responded");
				}
			}
			as.onFinishScan(maxhosts);
		}
		stop_search = false;
	}
	
	public void stopSearch()
	{
		stop_search = true;
	}
	
	
	public void startResearch(int[] params, LinkedList<SelectedAS> list, final ProgressListener listener)
	{
		
		ArrayList<TestResult> results = new ArrayList<>();
		
		int timeOut = params[0];
		int pingTimes = params[1];	// pings per host
		int traceTimes = params[2]; // traces per host
		
		if(listener!=null)listener.onStart();
		
		// The Ping test is 40% of testing
		results.addAll(pingTest(list, pingTimes, timeOut, new ProgressListener() {
			@Override public void onValueChange(int current, int whole_number) {}
			@Override public void onStart() {}
			
			@Override public void onProgres(int progress_percent) 
			{
				if(listener!=null)
					listener.onProgres(progress_percent*40/100);
			}
			
			@Override public void onFinish() 
			{
				if(listener!=null)listener.onProgres(40);
			}
		}));
		
		
		// The Trace test is 58% of testing
		results.addAll(traceTest(list, traceTimes, new ProgressListener() {
			@Override public void onValueChange(int current, int whole_number) {}
			@Override public void onStart() {}
			
			@Override public void onProgres(int progress_percent) 
			{	
				if(listener!=null)
					listener.onProgres(40+((progress_percent*58)/100));
			}
			
			@Override public void onFinish() 
			{
				if(listener!=null)listener.onProgres(98);
			}
		}));
		
		//Saving results is 2%
		CSVWriter.writeCSV(results);
		
		if(listener!=null)listener.onFinish();
	}
	
	private ArrayList<TestResult> pingTest(LinkedList<SelectedAS> list, int times, int timeOut, ProgressListener listener)
	{
		
		if(listener!=null)listener.onStart();
		
		ArrayList<TestResult> results = new ArrayList<>();
		
		float number = 0;
		
		for(SelectedAS as : list)
			number+=as.getRespondedNumber();
		
		float step = ((number*(float)times)/100.0f);
		float percent = 0;
		
		for(SelectedAS as : list)
		{
			for(String host : as.responded)
				for(int i = 0; i < times; i++)
				{
					long time = System.currentTimeMillis();
					int result = (int)ping(host, timeOut);
					results.add(new TestResult("PING", as.getASN(), host, result, time));
					LOG("Test(\'ping\'-"+as.getASN()+"):"+host+" = "+(result == -1 ? "timeOut" : result));
					
					if(listener!=null && ((int)percent) < ((int)(percent+step)))
					{
						listener.onProgres((int)percent);
					}
					
					
				}
		}
		
		if(listener!=null)listener.onFinish();
		
		return results;
	}
	
	
	private ArrayList<TestResult> traceTest(LinkedList<SelectedAS> list, int times, ProgressListener listener)
	{
		if(listener!=null)listener.onStart();
		//init Trace test - lookup for all ipranges for one AS;
		Map<String,long[][]> asn_map = new HashMap<String, long[][]>();
		
		int total = 0;
		
		if(log != null && list != null)
		{
			for(SelectedAS as : list)
			{
				total+=as.getRespondedNumber();
				if(!asn_map.containsKey(as.getASN()))
				{
					asn_map.put(as.getASN(), AsIpRangeLookup(as.getASN()));
				}
			}
		}
		float percent = 10;
		
		listener.onProgres((int)percent);
		
		float step = 90f/((float)(total*times));
		
		ArrayList<TestResult> results = new ArrayList<>();
		for(SelectedAS as : list)
		{
			for(String host : as.responded)
				for(int i = 0; i < times; i++)
				{
					String[] trace = null;
					
					try {
						trace = traceroute(host);
					} catch (IOException e) {}
					
					long time = System.currentTimeMillis();
					int result = hopsInAS(as.getASN(), asn_map, trace);
					results.add(new TestResult("TRACE", as.getASN(), host, result, time));
					LOG("Test(\'traceroute\'-"+as.getASN()+"):"+host+" = "+result);
					
					if(listener!=null && ((int)percent) < ((int)(percent+step)))
					{
						percent += step;
						listener.onProgres((int)percent);
					}
				}
		}
		
		if(listener!=null)listener.onFinish();
		
		return results;
	}
	
	private long[][] AsIpRangeLookup(String ASN)
	{
		ArrayList<long[]> list = new ArrayList<>();
		
		for(ASDescription as : log)
		{
			if(as.getASN().equals(ASN))
				list.add(new long[]{as.getBottomIP(), as.getTopIP()});
		}
		long[][] out = new long[list.size()][2];
		list.toArray(out);
		return out;
	}
	
	private boolean portIsOpen(String ip, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
	
	private int hopsInAS(String ASN, Map<String,long[][]> asn_map, String[] trace)
	{
		int cnt = 0;
		long[][] ip_range;
		if(asn_map.containsKey(ASN) && (ip_range = asn_map.get(ASN)) != null 
				&& trace != null && trace.length > 0)
		{
			for(String s : trace)
			{
				if(!s.equals("*"))
				{
					long ip = SelectedAS.IpToLong(s);
					if(isInIpRange(ip_range, ip))
						cnt++;
				}
			}
		}
		
		return cnt;
	}
	
	private boolean isInIpRange(long[][] ip_range, long ip)
	{
		for(long[] range : ip_range)
		{
			if(range[0] < ip && range[1] > ip)
				return true;
		}
		return false;
	}
	/**
	 * Dla Linuxia
	 * @return Tablica adresow IP - lista hopow w traceroute
	 * */
	public static String[] traceroute(String host) throws IOException
	{
		Process traceRt;
		ArrayList<String> hops = new ArrayList<>();
		String line;
		String command = "traceroute -n "+host;
        traceRt = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(
        		traceRt.getInputStream()));
        while ((line = input.readLine()) != null) 
        {
        	hops.add(line);
        }
        hops.remove(0);
        String[] out = new String[hops.size()];
        hops.toArray(out);
        
        for(int i = 0; i < out.length; i++)
        {
        	out[i] = ipLookup(out[i]);
        }
        return out;
	}
	
	static String IPADDRESS_PATTERN = 
	        "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
	
	private static String ipLookup(String input)
	{
		
		Matcher matcher = pattern.matcher(input);
		        if (matcher.find()) {
		            return matcher.group();
		        }
		        else{
		            return "0.0.0.0";
		        }
	}
	
	/**
	 * @return czas odpowiedzi hosta w ms lub -1 gdy host nie odpowiada
	 * */
	public long ping(String host, int timeout)
	{
		long time = System.currentTimeMillis();
		try {
			time = System.currentTimeMillis();
			InetAddress.getByName(host).isReachable(timeout);
			time = System.currentTimeMillis() - time;
		} 
		catch (UnknownHostException e) 
		{return -1;} 
		catch (IOException e) 
		{return -1;}
		
		return time < timeout ? time : -1;
		
	}
	
	
	
	public void setLogArea(JTextArea area)
	{
		info_area = area;
	}
	public static void LOG(String message)
	{
		if(info_area!=null)
		{
			info_area.append(message+"\n");
			//info_area.repaint();
		}
	}
	
	class TestResult
	{
		String testType, ASN, Host;
		int result;
		long time;
		public TestResult(String testType, String aSN, String host, int result,
				long time) {
			super();
			this.testType = testType;
			ASN = aSN;
			Host = host;
			this.result = result;
			this.time = time;
		}
	}
}
