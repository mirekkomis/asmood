package pwr.ibi.asmood.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultCaret;

import pwr.ibi.asmood.logic.ProgressListener;
import pwr.ibi.asmood.utils.Operator;
import pwr.ibi.asmood.utils.SelectedAS;


/**
 *
 * @author Acer
 */
public class MyInterface extends JFrame
{
	private static final long serialVersionUID = -3196600042294504431L;
	
	JPanel reader, algorithm;
	DataPanel dataPanel;
	
    JScrollPane pane1, pane2, pane3, pane4;
    MyMenu menu;
    JTabbedPane myTabs;
    JProgressBar reader_progress, algorithm_progress;
    
    Operator operator = new Operator();
    
    JButton buttonRead, buttonFileChooser,buttonAlgorithm;

	public JTextField file_path;

	public JTextField comparision_file_path;

	public JLabel comparision_labl_file;

	public JButton comparision_buttonFileChooser;

	public JTextField comparision_file_startime;

	public JLabel comparision_labl_startime;

    
    public MyInterface(){
        declare();
    }
    
    public void updateAll(){}
    private void declare(){
        
        /*
         Setting the LookAndFeel Theme
         */
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){ }
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setBounds(5,5,1150,750);
        setTitle("ASmood");
        setLayout(null);
        
        menu = new MyMenu(this);
        menu.setBounds(953, 15, 175, 689);
        
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        operator.setLogArea(textArea);
        
        pane1 = new JScrollPane();
        pane1.setPreferredSize(new Dimension(500,500));
        pane2 = new JScrollPane();
        pane2.setPreferredSize(new Dimension(500,500));
        pane3 = new JScrollPane();
        pane3.setPreferredSize(new Dimension(500,500));
        pane4 = new JScrollPane(textArea);	
        pane4.setPreferredSize(new Dimension(400,400));
        
        
        
        myTabs = new JTabbedPane();
        myTabs.add("Dane AS", pane1);
        myTabs.add("Ping", pane2);
        myTabs.add("Trace", pane3);
        myTabs.add("Log", pane4);
        myTabs.setBounds(10, 10, 940, 695);
        myTabs.setVisible(true);
        
        myTabs.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) 
			{

				int x = myTabs.getSelectedIndex();
				if(x==1 || x == 2)
				{
				}
				else
				{
					
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
        
        add(myTabs);
        add(menu);


		algorithm.setEnabled(false);
        setVisible(true);
        
        myTabs.setComponentAt(0,dataPanel =  new DataPanel());
    }

class MyMenu extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6020444224671436627L;
	MyInterface owner;
    JTextField field, timeout_val,systematic_val2, trace_nr_val, ping_nr_val, n_N_val2;
    JLabel timeout_label, ping_nr,trace_nr;
	private JLabel port_label;
	private JTextField port_val;
	private JLabel host_nr_label;
	private JTextField host_nr_val;
	private JButton search_button;
    
    public MyMenu(MyInterface observer){
        owner = observer;
        setLayout(null);
        TitledBorder gener = BorderFactory.createTitledBorder("Wczytywanie");
        TitledBorder optim = BorderFactory.createTitledBorder("Wyszukiwanie Hostów");
        TitledBorder algor = BorderFactory.createTitledBorder("Parametry Badania");
        
        
        reader = new JPanel();
        reader.setBorder(gener);
        reader.setBounds(1, 10, 175, 200);
        reader.setLayout(null);
        
        
        file_path = new JTextField("");
		file_path.setBounds(8, 36, 120, 25);
		file_path.setColumns(10);
		
		final JLabel labl_file = new JLabel("Adres Pliku");
		labl_file.setBounds(16, 22, 100, 14);
		
		buttonFileChooser = new JButton("^");
		buttonFileChooser.setBounds(130, 35, 40, 26);
		buttonFileChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("");
				 FileFilter filter1 = new ExtensionFileFilter("csv", new String[] { "csv"});
				    fileChooser.setFileFilter(filter1);
				int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fileChooser.setVisible(true);
                }
                if(fileChooser.getSelectedFile()!=null)
                {
	                String path = fileChooser.getSelectedFile().getAbsolutePath();
	                if(path!= null && !path.equals(""))
	                	file_path.setText(path);
                }
			}
			
		});
		
		final JCheckBox filter_check = new JCheckBox("Filtrowanie TCP i UDP");
		filter_check.setSelected(true);
		filter_check.setEnabled(false);
		filter_check.setBounds(10, 60, 150, 20);
		
		reader_progress = new JProgressBar();
		reader_progress.setBounds(10, 90, 155, 20);
		
		final JLabel readen = new JLabel("");
		readen.setBackground(Color.RED);
		readen.setBounds(35, 100, 140, 40);
		
		buttonRead = new JButton("Wczytaj");
        buttonRead.setBounds(16, 150, 140, 30);
        buttonRead.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	Thread t;
            	(t = new Thread(){
            		public void run() 
            		{
            			operator.read(file_path.getText(), filter_check.isSelected(), 
            					new ProgressListener() {
							@Override
							public void onProgres(int progress_percent) 
							{
								reader_progress.setValue(progress_percent);
							}
							
							@Override
							public void onFinish() 
							{
								dataPanel.updateDataSet(operator.getLog());
							}

							@Override
							public void onStart() {
								readen.setText("Inicjalizacja...");
							}

							@Override
							public void onValueChange(int current,
									int whole_number) {
								readen.setText(current+"/"+whole_number);
								
							}
						});
            		}
            		
            	}).setPriority(Thread.MAX_PRIORITY);
            	t.start();
            }
        });
        
        reader.add(reader_progress);
        reader.add(buttonRead);
		reader.add(file_path);
		reader.add(labl_file);
		reader.add(buttonFileChooser);
		reader.add(readen);
        
        
        JPanel params_pan = new JPanel();
        params_pan.setBorder(optim);
        params_pan.setBounds(1, 220, 175, 200);
        params_pan.setLayout(null);
        
        
        port_label = new JLabel("Port:");
        port_label.setBounds(5, 20, 150, 20);
        params_pan.add(port_label);
        
        port_val = new JTextField("80");
        port_val.setBounds(20, 40, 60, 20);
        params_pan.add(port_val);
        
        host_nr_label = new JLabel("Hostów:");
        host_nr_label.setBounds(5, 60, 150, 20);
        params_pan.add(host_nr_label);
        
        host_nr_val = new JTextField("10");
        host_nr_val.setBounds(20, 80, 60, 20);
        params_pan.add(host_nr_val);
        
        timeout_label = new JLabel("TimeOut[ms]");
		timeout_label.setBounds(5, 100, 150, 20);
		params_pan.add(timeout_label);
		
		timeout_val = new JTextField("300");
		timeout_val.setBounds(20, 120, 60, 20);
		params_pan.add(timeout_val);
		  
		
        search_button = new JButton("Wyszukaj");
        search_button.setBounds(22, 150, 130, 30);
        params_pan.add(search_button);
        search_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(search_button.getText().equals("Wyszukaj"))
				{
					search_button.setText("Stop");
					new Thread(new Runnable() {		
						@Override
						public void run() 
						{
							LinkedList<SelectedAS> list = dataPanel.getSelected();
							int port = Integer.parseInt(port_val.getText());
							int timeout = Integer.parseInt(timeout_val.getText());
							int maxhosts = Integer.parseInt(host_nr_val.getText());
							operator.scan(list, port, timeout, maxhosts);
							search_button.setText("Wyszukaj");
						}
						
					}).start();
				}
				else
				{
					operator.stopSearch();
					search_button.setText("Wyszukaj");
					
				}
			}
		});
        
        
//        timeout_label = new JLabel("TimeOut[ms]");
//        timeout_label.setBounds(5, 100, 150, 20);
//        params_pan.add(timeout_label);
//        
//        ping_nr = new JLabel("Zapytań Ping");
//        ping_nr.setBounds(5, 140, 150, 20);
//        params_pan.add(ping_nr);
//        
//        trace_nr = new JLabel("Zapytań Trace");
//        trace_nr.setBounds(5, 180, 150, 20);
//        params_pan.add(trace_nr);
//        
//        timeout_val = new JTextField("1000");
//        timeout_val.setBounds(20, 120, 60, 20);
//        params_pan.add(timeout_val);
//        
//        ping_nr_val = new JTextField("10");
//        ping_nr_val.setBounds(20, 160, 60, 20);
//        params_pan.add(ping_nr_val);
//        
//        trace_nr_val = new JTextField("3");
//        trace_nr_val.setBounds(20, 200, 60, 20);
//        params_pan.add(trace_nr_val);
        
        algorithm = new JPanel();
        algorithm.setBorder(algor);
        algorithm.setBounds(1, 470, 175, 210);
        algorithm.setLayout(null);
        
        algorithm_progress = new JProgressBar();
        algorithm_progress.setBounds(10, 235, 155, 20);
        
        buttonAlgorithm = new JButton("Algorytm");
        buttonAlgorithm.setBounds(16, 260, 140, 30);
        buttonAlgorithm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	Thread t = new Thread(){
            		public void run() 
            		{
            			calculateAlgorithm();
            		};
            	};
            	t.setPriority(Thread.MAX_PRIORITY);
            	t.start();
		            	
            }      
		});
        
        add(algorithm);
        add(params_pan);
        add(reader);
    }
    
    private void calculateAlgorithm()
    {
    	 
    }
    
}
	
    
    
    
	class ExtensionFileFilter extends FileFilter {
		  String description;
	
		  String extensions[];
	
		  public ExtensionFileFilter(String description, String extension) {
		    this(description, new String[] { extension });
		  }
	
		  public ExtensionFileFilter(String description, String extensions[]) {
		    if (description == null) {
		      this.description = extensions[0];
		    } else {
		      this.description = description;
		    }
		    this.extensions = (String[]) extensions.clone();
		    toLower(this.extensions);
		  }
	
		  private void toLower(String array[]) {
		    for (int i = 0, n = array.length; i < n; i++) {
		      array[i] = array[i].toLowerCase();
		    }
		  }
	
		  public String getDescription() {
		    return description;
		  }
	
		  public boolean accept(File file) {
		    if (file.isDirectory()) {
		      return true;
		    } else {
		      String path = file.getAbsolutePath().toLowerCase();
		      for (int i = 0, n = extensions.length; i < n; i++) {
		        String extension = extensions[i];
		        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
		          return true;
		        }
		      }
		    }
		    return false;
		  }
	}
}