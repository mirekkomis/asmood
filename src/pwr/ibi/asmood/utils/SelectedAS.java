package pwr.ibi.asmood.utils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class SelectedAS extends ASDescription {

	public boolean selected = false;
	private ArrayList<String> responded = new ArrayList<>();
	private JButton plus = new JButton("+");
	public static final int ROW_HEIGHT = 30;
	
	private SelectedASListener listener;
	
	
	public Color color = Color.WHITE;
	
	public int getRowHeight()
	{
		return selected ? ROW_HEIGHT*(responded.size()+1) : ROW_HEIGHT; 
	}
	
	public SelectedAS(ASDescription desc, SelectedASListener listener)
	{
		this(desc.getBottomIP(), desc.getTopIP(), desc.getDescription());
		plus.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				plus.setText("-");
				plus.invalidate();
			}
		});
		
		this.listener = listener;
	}
	
	public String[] getIPList()
	{
		if(!selected)
			return new String[]{getIPRange()};
		String[] out = new String[responded.size()+1];
		out[0] = getIPRange();
		
		for(int i = 1; i <= responded.size(); i++)
			out[i] = " - "+responded.get(i-1);
		
		return out;
	}
	
	public SelectedAS(long bottomIP, long topIP, String descr) {
		super(bottomIP, topIP, descr);
	}

	public void onClicked()
	{
		if(selected)
		{
			selected = false;
			plus.setText("+");
			plus.invalidate();
			Operator.LOG("unselected");
		}
		else
		{
			selected = true;
			plus.setText("-");
			plus.invalidate();
			Operator.LOG("selected");
		}
	}
	
	public void addResponded(String resp)
	{
		if(!responded.contains(resp))
		{
			responded.add(resp);
			if(listener!=null && selected)
				listener.notifyListener();
		}
	}
	
	public int getRespondedNumber()
	{
		return responded.size();
	}
	
	public JButton getButton()
	{
		return plus;
	}
	
	
	public void onFinishScan(int maxHosts)
	{
		if(maxHosts > 0)
		{
			if(responded.size()==0)
			{
				color = Color.RED;
			}
			else if(responded.size() == maxHosts)
			{
				color = Color.GREEN;
			}
			else
				color = Color.orange;
		}
		if(listener!=null)
			listener.notifyListener();
	}
	
	public interface SelectedASListener
	{
		public abstract void notifyListener();
	}
}
