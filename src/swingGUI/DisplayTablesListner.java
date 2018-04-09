package swingGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.DbOperations;

public class DisplayTablesListner implements ActionListener {
	
	DbOperations dbOP = new DbOperations();
	JComboBox table_names_show = null;
	
	public DisplayTablesListner(JComboBox table_names_show) {
		this.table_names_show = table_names_show;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String option = this.table_names_show.getSelectedItem().toString();
		
		switch(option) {
		case "Select table":
			JOptionPane.showMessageDialog(null, "Please select table name");
			break;
			
		case "Customers":
			dbOP.getTable("Customers");
			break;
			
		case "Products":
			dbOP.getTable("Products");
			break;
			
		case "Purchases":
			dbOP.getTable("Purchases");
			break;
			
		case "Employees":
			dbOP.getTable("Employees");
			break;
			
		case "Logs":
			dbOP.getTable("Logs");
			break;
			
		case "Supplies":
			dbOP.getTable("Supplies");
			break;
			
		case "Suppliers":
			dbOP.getTable("Suppliers");
			break;
			
		case "Discounts":
			dbOP.getTable("Discounts");
			break;
		default:
			 break;
		}
	}

	/**
	 * It is used to display the table data in table format
	 * @param dataColumns
	 * @param dataRows
	 */
	public static void showTable(ArrayList<Object> dataColumns, ArrayList<Object[]> dataRows) {
		
		JFrame sub_frame_display_table = new JFrame();;
		
		sub_frame_display_table.setTitle("Display Tables");
		sub_frame_display_table.setSize(800,700);
		sub_frame_display_table.setLayout(null);
		sub_frame_display_table.setVisible(true);
		sub_frame_display_table.getContentPane().setLayout(null);
		sub_frame_display_table.getContentPane().setBackground(new Color(240, 248, 255));
		
		JTable table  = new JTable();
		JButton close_b = new JButton("Close");
		Object[][] rowsData;
		Object[] columnNames;
		
		rowsData = new Object[dataRows.size()][];
		for (int i = 0; i < rowsData.length; i++) {
			rowsData[i] = dataRows.get(i);
		}
		columnNames = dataColumns.toArray();

		DefaultTableModel model = new DefaultTableModel(rowsData,columnNames);	
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(80,100,600,300);
		close_b.setBounds(300,550,150, 40);
		
		sub_frame_display_table.add(scrollPane);
		sub_frame_display_table.add(close_b);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		table.setModel(model);
		model.fireTableDataChanged();
		
		close_b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				scrollPane.removeAll();
				table.removeAll();
				sub_frame_display_table.dispose();
			}
		});
	}

}
