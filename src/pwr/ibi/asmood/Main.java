package pwr.ibi.asmood;

import pwr.ibi.asmood.gui.MyInterface;

public class Main {

	
	public static void main(String[] args) 
	{
		new MyInterface();
	}
}

/*
 
 		//
//		long addr = 469495040;//(long)(3758088192l-3758088703l)/2;
//		long addr2 = 469495551;
//		
//		
//		
//		
//		System.out.println(inetToIP(addr)+" - "+inetToIP(addr2));
//		if(true)
//		return;
//			
//	static long num1 = 16777216l, num2 = 65536l, num3 =  256l;
//
//	
//	private static final String os = System.getProperty("os.name");

//	  public static String traceRoute(InetAddress address){
//	    String route = "";
//	    try {
//	        Process traceRt;
//	        if(os.contains("win")) traceRt = Runtime.getRuntime().exec("tracert " + address.getHostAddress());
//	        else traceRt = Runtime.getRuntime().exec("traceroute " + address.getHostAddress());
//
//	        // read the output from the command
//	        route = traceRt.getInputStream().toString();
//
//	        // read any errors from the attempted command
//	        String errors = traceRt.getErrorStream().toString();
//	        if(errors != "") System.out.println(errors);
//	    }
//	    catch (IOException e) {
//	    	System.out.println("error while performing trace route command");
//	    }
//
//	    return route;
//	  }
	  
//	public static String inetToIP(long addr)
//	{
//		return 		(int)((addr/num1)%256)+"."+
//				  	(int)((addr/num2)%256)+"."+
//				  	(int)((addr/num3)%256)+"."+
//				  	((int)((addr)%256));
//	}
	
//	public static void pingServer() {
//		try {
//			String command = "traceroute en.wikipedia.org"; 
//			Process p = Runtime.getRuntime().exec(command);
//			int status = p.waitFor();
//			InputStream input = p.getInputStream(); 
//			BufferedReader in = new BufferedReader(new InputStreamReader(input));
//			StringBuffer buffer = new StringBuffer();
//			String line = "";
//			while ((line = in.readLine()) != null) {
//					buffer.append(line);
//					buffer.append("\n");
//				}
//
//
//			String bufferStr = buffer.toString();
//		        System.out.println(bufferStr);
//
//			} catch (Exception e) {
//				System.out.println("---------------exception-----------ping");
//				System.out.println(e.getMessage());
//				// e.printStackTrace();
//	
//			}
//		}
//	
//	public static String ping()
//	{
//		try {
//			Process ping = Runtime.getRuntime().exec("ping -c 1 en.wikipedia.org");
//			return ping.getInputStream().toString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "error";
//	}
//		String host = inetToIP(addr+1);
//		pingServer();
////		long time;
//		int timeOut = 3000;
//		for(int i = 0; i < 5; i++)
//		{
//			try {
//				time = System.currentTimeMillis();
//				
//				InetAddress.getByName(host).isReachable(timeOut);
//		
//				time = System.currentTimeMillis() - time;
//				if(time < timeOut)
//				{
//					System.out.println(host+" REACHABLE-TIME: "+time+" ===============================");
//					traceRoute(InetAddress.getByName(host));
//				}
//				else
//					System.out.println(host+" TIME OUT: "+time+" =====================================");
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.out.println(host+" UnknownHost=============================================");
//			} catch (IOException e) {
//				System.out.println(host+" IOException=============================================");
//				e.printStackTrace();
//			}
//		}
 
 
 	
//	static long num1 = 16777216l, num2 = 65536l, num3 =  256l;
//
//	
//	private static final String os = System.getProperty("os.name");

//	  public static String traceRoute(InetAddress address){
//	    String route = "";
//	    try {
//	        Process traceRt;
//	        if(os.contains("win")) traceRt = Runtime.getRuntime().exec("tracert " + address.getHostAddress());
//	        else traceRt = Runtime.getRuntime().exec("traceroute " + address.getHostAddress());
//
//	        // read the output from the command
//	        route = traceRt.getInputStream().toString();
//
//	        // read any errors from the attempted command
//	        String errors = traceRt.getErrorStream().toString();
//	        if(errors != "") System.out.println(errors);
//	    }
//	    catch (IOException e) {
//	    	System.out.println("error while performing trace route command");
//	    }
//
//	    return route;
//	  }
	  
//	public static String inetToIP(long addr)
//	{
//		return 		(int)((addr/num1)%256)+"."+
//				  	(int)((addr/num2)%256)+"."+
//				  	(int)((addr/num3)%256)+"."+
//				  	((int)((addr)%256));
//	}
	
//	public static void pingServer() {
//		try {
//			String command = "traceroute en.wikipedia.org"; 
//			Process p = Runtime.getRuntime().exec(command);
//			int status = p.waitFor();
//			InputStream input = p.getInputStream(); 
//			BufferedReader in = new BufferedReader(new InputStreamReader(input));
//			StringBuffer buffer = new StringBuffer();
//			String line = "";
//			while ((line = in.readLine()) != null) {
//					buffer.append(line);
//					buffer.append("\n");
//				}
//
//
//			String bufferStr = buffer.toString();
//		        System.out.println(bufferStr);
//
//			} catch (Exception e) {
//				System.out.println("---------------exception-----------ping");
//				System.out.println(e.getMessage());
//				// e.printStackTrace();
//	
//			}
//		}
//	
//	public static String ping()
//	{
//		try {
//			Process ping = Runtime.getRuntime().exec("ping -c 1 en.wikipedia.org");
//			return ping.getInputStream().toString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "error";
//	}
 
 * **/
