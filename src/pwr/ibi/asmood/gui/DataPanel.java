package pwr.ibi.asmood.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import pwr.ibi.asmood.utils.ASDescription;
import pwr.ibi.asmood.utils.SelectedAS;
import pwr.ibi.asmood.utils.SelectedAS.SelectedASListener;

public class DataPanel extends JPanel implements SelectedASListener{
	
	JPanel content;
	JTextField filter;
	private String[] columnNames = {"Zakres IP","Nazwa"};
	ASDescription[] dataSet;
	
	private LinkedList<ASDescription> filtered = new LinkedList<>();
	AllDataModel data_model;
	JTable table;
	
	JTableButtonRenderer buttonRenderer = new JTableButtonRenderer();
	
	public DataPanel()
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		prepareMenu();
		setBackground(Color.BLUE);
		
		content = new JPanel(new GridLayout(0,2));
		content.setLocation(0, 0);
		content.setBackground(Color.RED);
		
        table = new JTable(data_model = new AllDataModel());
        table2 = new JTable(sel_model = new SelectedDataModel());
        table2.getColumn("-").setCellRenderer(buttonRenderer);
        table2.getColumn("-").setMaxWidth(40);
        table2.getColumn("-").setPreferredWidth(40);
        table2.getColumn("Zakres IP").setCellRenderer(list_rend);
        table2.addMouseListener(new JTableButtonMouseListener(table));
        
        
        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);

        content.add(scrollPane);
        content.add(scrollPane2);
        add(content);
	}
	
	
	private void filter(String filter)
	{
		filtered.clear();
		if(filter == null || filter.trim()== "" )
		{
			for(ASDescription d : dataSet)
					filtered.add(d);
		}
		else
			for(ASDescription d : dataSet)
				if(d.getDescription().contains(filter))
					filtered.add(d);
	}
	
	public void updateDataSet(ASDescription[] data)
	{
		dataSet = data;
		filter(null);
		data_model.fireTableDataChanged();
		table.invalidate();
	}
	
	private void prepareMenu()
	{
		 JPanel menu = new JPanel();
		 menu.setLayout(null);
		 menu.setBounds(0, 0, 110, 90);
		
		 JLabel lab[] = new JLabel[3];
		 
		 lab[0] = new JLabel("Wszystkie ASy");
		 lab[0].setBounds(120, 80, 120, 30);
		 lab[1] = new JLabel("wybrane ASy");
		 lab[1].setBounds(670, 80, 120, 30);
		 lab[2] = new JLabel("Filtrowanie");
		 lab[2].setBounds(20, 20, 100, 30);
		
		filter = new JTextField();
		 filter.setBounds(120, 20, 250, 30);
		 filter.getDocument().addDocumentListener(new DocumentListener() 
		 {
			@Override
			public void removeUpdate(DocumentEvent arg0) 
			{
				filter(filter.getText());
				data_model.fireTableDataChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) 
			{
				filter(filter.getText());
				data_model.fireTableDataChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) 
			{}
		});
		 menu.add(filter);
		 
		JButton[] button = new JButton[2];
		
		button[0] = new JButton(" + ");
		button[0].setBounds(470, 80, 70, 30);
		button[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] rows = table.getSelectedRows();
				for(int r : rows)
				{
					selected.add(new SelectedAS(filtered.get(r), DataPanel.this));
				}
				sel_model.fireTableDataChanged();
				
			}
		});
		
		button[1] = new JButton(" - ");
		button[1].setBounds(550, 80, 70, 30);
		button[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] rows = table2.getSelectedRows();
				ASDescription[] toDel = new ASDescription[rows.length];
				for(int i = 0; i < rows.length; i++)
				{
					toDel[i] = selected.get(rows[i]);
				}
				for(ASDescription r : toDel)
				{
					selected.remove(r);
				}
				sel_model.fireTableDataChanged();
				
			}
		});
		
		for(JButton b : button)
		{
			menu.add(b);
		}
		for(JLabel b : lab)
		{
			menu.add(b);
		}
		this.add(menu);
	}
	
	
	
	private static final long serialVersionUID = -3282024140905847144L;
	
	class AllDataModel extends AbstractTableModel
	{
		private static final long serialVersionUID = -4098140216587483227L;
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return filtered == null ? 0 : filtered.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return arg1 == 0 ? filtered.get(arg0).getIPRange() : filtered.get(arg0).getDescription();
		}
		
		 public String getColumnName(int col) {
		        return columnNames[col];
		 }


		 /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
	}
	
	
	private String[] selColumnNames = {"-","Zakres IP","Nazwa"};
	private LinkedList<SelectedAS> selected = new LinkedList<>();
	private SelectedDataModel sel_model;
	private JTableListRenderer list_rend = new JTableListRenderer();
	JTable table2;
	
	
	
	class SelectedDataModel extends AbstractTableModel
	{
		private static final long serialVersionUID = -4098140216587483227L;

		@Override
		public int getColumnCount() {
			return selColumnNames.length;
		}

		@Override
		public int getRowCount() {
			return selected == null ? 0 : selected.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return arg1 == 0 ? selected.get(arg0).getButton() 
					: arg1 == 1 ? selected.get(arg0).getIPRange() 
					: selected.get(arg0).getDescription();
		}
		
		 public String getColumnName(int col) {
		        return selColumnNames[col];
		 }


		 /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
		 @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
	}
	
		private static class JTableButtonRenderer implements TableCellRenderer {        
       
		@Override public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
	            JButton button = (JButton)value;
	            return button;  
        	}
    	}
		
		private class JTableListRenderer implements TableCellRenderer{
			@Override public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				String[] lst = selected.get(row).getIPList();
				JList<String> list = new JList<String>(lst);
				table.setRowHeight(row, lst.length*SelectedAS.ROW_HEIGHT);
				list.setBackground(selected.get(row).color);
				return list;
			}
		}
	
		private class JTableButtonMouseListener extends MouseAdapter {
			private final JTable table;

	        public JTableButtonMouseListener(JTable table) {
	            this.table = table;
	        }

	        public void mouseClicked(MouseEvent e) {
	            int row = 0, current_height  = 0;
	            int y = e.getY();
	            
	            while(row < selected.size() && (current_height+=selected.get(row).getRowHeight()) < y)
	            {
	            	row++;
	            }
	            
	            int x = e.getX();
	            if (row < table.getRowCount() && row >= 0 && x < 40 && x >= 0) {
	                	selected.get(row).onClicked();
	                	sel_model.fireTableDataChanged();
                }
        }
    }
		
		public LinkedList<SelectedAS> getSelected()
		{
			return selected;
		}

		@Override
		public void notifyListener(){
			sel_model.fireTableDataChanged();
		}
}
