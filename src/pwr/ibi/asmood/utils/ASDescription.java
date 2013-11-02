package pwr.ibi.asmood.utils;

public class ASDescription {
	
	
	
	private long bottomIP, topIP;
	private String descr = "";
	
	public ASDescription(String[] fields)
	{
		descr = "";
		if(fields.length > 2)
		{
			bottomIP = Long.parseLong(fields[0]);
			topIP = Long.parseLong(fields[1]);
			for(int i = 2; i < fields.length; i++)
				descr += fields[i];
		}
	}
	
	public ASDescription(long bottomIP, long topIP, String descr) {
		super();
		this.bottomIP = bottomIP;
		this.topIP = topIP;
		this.descr = descr;
	}
	
	public String getDescription()
	{
		return descr;
	}
	
	public long getBottomIP()
	{return bottomIP;}

	public long getTopIP()
	{return topIP;}
	
	public String getIPRange()
	{
		return ""+intToIP(bottomIP)+" - "+intToIP(topIP);
	}
	
	public String[] getStringArr()
	{
		return new String[]{""+intToIP(bottomIP)+" - "+intToIP(topIP),descr};
	}
	static long num1 = 16777216l, num2 = 65536l, num3 =  256l;
	public static String intToIP(long addr)
	{
		return 		(int)((addr/num1)%256)+"."+
				  	(int)((addr/num2)%256)+"."+
				  	(int)((addr/num3)%256)+"."+
				  	((int)((addr)%256));
	}
}
