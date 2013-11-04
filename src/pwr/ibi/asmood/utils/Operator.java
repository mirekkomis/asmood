package pwr.ibi.asmood.utils;

import java.awt.Color;
import java.awt.GradientPaint;
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

import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import pwr.ibi.asmood.logic.ProgressListener;

public class Operator 
{
	ASDescription[] log;
	
	private static JTextArea info_area;
	
	ResearchResultManager researchResultManager;
	ArrayList<ResearchResult> list;
	
	public void read(String path, boolean filtering, ProgressListener listener)
	{
		
		if(listener!=null)
			listener.onStart();

		log = CSVReader.readCSV("C:\\Users\\Adam\\Documents\\studia\\mgr semestr II\\IBI\\l\\GeoIPASNum2\\GeoIPASNum2.csv", listener);

		if(listener!=null)
			listener.onFinish();
		
		/*ArrayList<TestResult> results = new ArrayList<TestResult>();
		results.add(new TestResult("ping", "as1", "111.111.111.111", 1, 5));
		results.add(new TestResult("ping", "as2", "111.111.111.112", 2, 6));
		results.add(new TestResult("ping", "as3", "111.111.111.113", 3, 7));
		results.add(new TestResult("traceroute", "as4", "111.111.111.114", 4, 8));
		CSVWriter.writeCSV(results);*/
	}
	
	public ChartPanel createChart() {
	  	ChartPanel panel;
        JFreeChart chart = ChartFactory.createBarChart(
            "Czasy odpowiedzi hostów",    // chart title
            "Host",                // domain axis label
            "Czas odpowiedzi",           // range axis label
            createLogDataset(),       // data
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

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        
        panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(300, 300));
        panel.setVisible(true);
        
        return panel;
	}
	
	private CategoryDataset createLogDataset(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		float time_delta = 60,
			  current_time = time_delta;
		int   i=0,  
			  cat_nr = 1, 
			  udp_acc = 0,
			  tcp_acc = 0,
			  other_acc = 0;
		byte  protocol;
		
		researchResultManager = CSVReader.readCSVTests("C:\\Users\\Adam\\workspace\\asmood1383570012005.csv", null);
		ResearchResult[] researchResults = researchResultManager.getResearchResult();
		for(ResearchResult  item: researchResults){
			System.out.println(item.toString());
		}
		System.out.println(researchResultManager.getAsnList());
		researchResultManager.readAsnTests("as1");
		list = researchResultManager.getAsnPingTests();
		System.out.println(list);
		for(ResearchResult item: list){
			dataset.addValue(item.getResult(), item.getHost(), ""+cat_nr);
		}
	    return dataset;
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
	
	
	private ArrayList<TestResult> startResearch(int[] params, LinkedList<SelectedAS> list)
	{
		ArrayList<TestResult> results = new ArrayList<>();
		
		int timeOut = params[0];
		int pingTimes = params[1];	// pings per host
		int traceTimes = params[2]; // traces per host
		
		results.addAll(pingTest(list, pingTimes, timeOut));
		results.addAll(traceTest(list, traceTimes));
		
		CSVWriter.writeCSV(results);
		
		return results;
	}
	
	private ArrayList<TestResult> pingTest(LinkedList<SelectedAS> list, int times, int timeOut)
	{
		ArrayList<TestResult> results = new ArrayList<>();
		for(SelectedAS as : list)
		{
			for(String host : as.responded)
				for(int i = 0; i < times; i++)
				{
					long time = System.currentTimeMillis();
					int result = (int)ping(host, timeOut);
					results.add(new TestResult("PING", as.getASN(), host, result, time));
					LOG("Test(\'PING\'-"+as.getASN()+"):"+host+" = "+(result == -1 ? "timeOut" : result));
				}
		}
		return results;
	}
	
	
	private ArrayList<TestResult> traceTest(LinkedList<SelectedAS> list, int times)
	{
		
		//init Trace test - lookup for all ipranges for one AS;
		Map<String,long[][]> asn_map = new HashMap<String, long[][]>();
		
		if(log != null && list != null)
		{
			for(SelectedAS as : list)
			{
				if(!asn_map.containsKey(as.getASN()))
				{
					asn_map.put(as.getASN(), AsIpRangeLookup(as.getASN()));
				}
			}
		}
		
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
					LOG("Test(\'TRACE\'-"+as.getASN()+"):"+host+" = "+result);
				}
		}
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
	public String[] traceroute(String host) throws IOException
	{
		Process traceRt;
		ArrayList<String> hops = new ArrayList<>();
		String line;
		
        traceRt = Runtime.getRuntime().exec("traceroute -n "+host+" | tail -n+2 | awk \'{ print $2 }\'");
        BufferedReader input = new BufferedReader(new InputStreamReader(
        		traceRt.getInputStream()));
        while ((line = input.readLine()) != null) 
        {
        	hops.add(line);
        }
        String[] out = new String[hops.size()];
        hops.toArray(out);
        return out;
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
