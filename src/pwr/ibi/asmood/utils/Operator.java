package pwr.ibi.asmood.utils;

//import java.text.SimpleDateFormat;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JTextArea;

import pwr.ibi.asmood.logic.ProgressListener;

public class Operator 
{
	ASDescription[] log;
	
	private static JTextArea info_area;
	
	//private final  SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
	
	public void read(String path, boolean filtering, ProgressListener listener)
	{
		
		if(listener!=null)
			listener.onStart();

		log = CSVReader.readCSV(path, listener);

		if(listener!=null)
			listener.onFinish();
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
}
