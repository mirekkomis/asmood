package pwr.ibi.asmood.utils;

public class ASDescription {
	
	
	
	private long bottomIP, topIP;
	private String descr = "";
	private String ASN = "";
	
	public ASDescription(String[] fields)
	{
		descr = "";
		if(fields.length > 2)
		{
			bottomIP = Long.parseLong(fields[0]);
			topIP = Long.parseLong(fields[1]);
			String[] tmp = fields[2].substring(1,fields[2].length()-1).split(" ");
			ASN = tmp[0];
			for(int i = 1; i < tmp.length; i++)
				descr += tmp[i];
			for(int i = 3; i < fields.length; i++)
				descr += fields[i];
		}
	}
	
	public ASDescription(long bottomIP, long topIP, String asn, String descr) {
		super();
		this.ASN = asn;
		this.bottomIP = bottomIP;
		this.topIP = topIP;
		this.descr = descr;
	}
	
	public String getDescription()
	{
		return ASN+" : "+descr;
	}
	
	public String getASN()
	{
		return ASN;
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
	public static long IpToLong(String ip)
	{
		String[] oct = ip.split("\\.");
		return oct.length == 4 ? Long.parseLong(oct[0])*num1
				+ Long.parseLong(oct[1])*num2
				+ Long.parseLong(oct[2])*num3
				+ Long.parseLong(oct[3]) : -1;
	}
}
