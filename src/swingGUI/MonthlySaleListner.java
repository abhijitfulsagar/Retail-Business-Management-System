package swingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.DbOperations;

public class MonthlySaleListner implements ActionListener{

	static JScrollPane scrollPane = new JScrollPane();

	DbOperations dbOP = new DbOperations();
	
	/**
	 * to create frame for monthly sale
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame sub_frame_mon_sale = new JFrame();
		sub_frame_mon_sale.setTitle("Monthly sale report");
		sub_frame_mon_sale.getContentPane().setBackground(new Color(240, 248, 255));
		sub_frame_mon_sale.setSize(600,700);
		sub_frame_mon_sale.getContentPane().setLayout(null);
		sub_frame_mon_sale.setVisible(true);
		
		
		scrollPane.setBounds(40,250,500,300);		
		sub_frame_mon_sale.add(scrollPane);
		
		
		JLabel eid_l = new JLabel("Select employee ID");
		eid_l.setForeground(new Color(0, 0, 128));


		List<String> list = DbOperations.getEmployees();
		String[] employee_ids = list.toArray(new String[0]);
		
		JComboBox eid_combo = new JComboBox(employee_ids);
		
		JButton get_sale_b = new JButton("Get sale");
		get_sale_b.setForeground(new Color(0, 0, 128));
		JButton close_b = new JButton("Close");
		close_b.setForeground(new Color(0, 0, 128));
		
		eid_l.setBounds(40, 100,160,40);    
		eid_combo.setBounds(212, 100,160,40); 
		
		get_sale_b.setBounds(414,101,150, 40);
		close_b.setBounds(240,568,150, 40);
		
		sub_frame_mon_sale.getContentPane().add(eid_l);
		sub_frame_mon_sale.getContentPane().add(eid_combo);
		sub_frame_mon_sale.getContentPane().add(get_sale_b);
		sub_frame_mon_sale.getContentPane().add(close_b);
		
		JLabel lblNewLabel = new JLabel("Please enter employee id for monthly sale report");
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(40, 54, 435, 16);
		sub_frame_mon_sale.getContentPane().add(lblNewLabel);
		
		
		performAction(get_sale_b,eid_combo,sub_frame_mon_sale);

		close_b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sub_frame_mon_sale.dispose();
			}
		});
		
	}

	/**
	 * to get the monthly sale for a particular employee eid
	 * @param get_sale_b
	 * @param eid_combo
	 * @param sub_frame_mon_sale2
	 */
	private void performAction(JButton get_sale_b, JComboBox eid_combo, JFrame sub_frame_mon_sale2) {
	get_sale_b.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrollPane.setVisible(false);
				DbOperations.getMonthlySale(eid_combo.getSelectedItem().toString(),sub_frame_mon_sale2);
			}
		});
		
	}

	/**
	 * to display the monthly sales for a particular employee eid 
	 * @param dataColumns
	 * @param dataRows
	 * @param sub_frame_mon_sale2
	 */
	public static void showEmpResult(ArrayList<Object> dataColumns, ArrayList<Object[]> dataRows,
			JFrame sub_frame_mon_sale2) {
		sub_frame_mon_sale2.repaint();				
		scrollPane.setVisible(true);
		JLabel lblYourMonthlyReport = new JLabel("Your Monthly Report");
		lblYourMonthlyReport.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		lblYourMonthlyReport.setForeground(new Color(0, 0, 128));
		lblYourMonthlyReport.setBounds(42, 172, 185, 16);
		sub_frame_mon_sale2.getContentPane().add(lblYourMonthlyReport);
		
		
		JTable table  = new JTable();
		 
		Object[][] rowsData;
		Object[] columnNames;
		
		rowsData = new Object[dataRows.size()][];
		for (int i = 0; i < rowsData.length; i++) {
			rowsData[i] = dataRows.get(i);
		}
		columnNames = dataColumns.toArray();	
		
		DefaultTableModel model = new DefaultTableModel(rowsData,columnNames);		
		table.setModel(model);
		model.fireTableDataChanged();
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);				
		
	}

}
