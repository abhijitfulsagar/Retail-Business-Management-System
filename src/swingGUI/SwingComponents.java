package swingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import database.DbOperations;

public class SwingComponents {
	
	private static JComboBox table_names_insert = null;
    private static JComboBox table_names_show = null; 

    /**
     * to create UI
     */
    public void addCompnents(){
		JFrame jf=new JFrame();
		jf.setTitle("Retail Business Managment System");
		jf.getContentPane().setBackground(new Color(240, 248, 255));
		add_dropDown(jf);
		add_buttons(jf);
		jf.setSize(600,800);
		jf.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Retail Business Managment System");
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 19));
		lblNewLabel.setBounds(119, 70, 348, 39);
		jf.getContentPane().add(lblNewLabel);
		jf.setVisible(true);
	}

    /**
     * to create the drop down menus
     * @param jf
     */
	private  void add_dropDown(JFrame jf) {
		  String insert_tables[]={"Select table","Customers","Purchases"}; 
		  String show_tables[] = {"Select table","Customers","Employees","Products","Supplies",
				  "Purchases","Suppliers","Discounts","Logs"};
			table_names_insert = new JComboBox(insert_tables); 
			table_names_insert.setForeground(new Color(0, 0, 128));
		    table_names_show = new JComboBox(show_tables);  
		    table_names_show.setForeground(new Color(0, 0, 128));
		    table_names_insert.setBounds(99, 167,160,40);    
		    table_names_show.setBounds(99, 270,160,40); 
		    jf.getContentPane().add(table_names_insert);
		    jf.getContentPane().add(table_names_show);
	}
	
	/**
	 * to create the buttons 
	 * @param jf
	 */
	private  void add_buttons(JFrame jf) {

		JButton insert = new JButton("Insert");
		insert.setForeground(new Color(0, 0, 128));
		JButton display = new JButton("Display");
		display.setForeground(new Color(0, 0, 128));
		JButton saving = new JButton("Saving");
		saving.setForeground(new Color(0, 0, 128));
		JButton monthly_sale = new JButton("Monthly Sale");
		monthly_sale.setForeground(new Color(0, 0, 128));
		JButton dlt_pur = new JButton("Delete Purchase");
		dlt_pur.setForeground(new Color(0, 0, 128));
		
		JButton close = new JButton("Close");
		close.setForeground(new Color(0, 0, 128));
		
		insert.setBounds(312,167,150, 40);
		display.setBounds(312,270,150, 40); 
		saving.setBounds(40,385,150, 40); 
		monthly_sale.setBounds(212,385,150, 40);
		dlt_pur.setBounds(390,385,150, 40);
		close.setBounds(212,485,150, 40);
		
		jf.getContentPane().add(insert);  
		jf.getContentPane().add(display); 
		jf.getContentPane().add(saving); 
		jf.getContentPane().add(monthly_sale);
		jf.getContentPane().add(dlt_pur);
		jf.getContentPane().add(close);
		
		insert.addActionListener(new InsertActionListner(table_names_insert));
		saving.addActionListener(new DisplaySavingListner());
		dlt_pur.addActionListener(new DeletePurchaseListner());
		monthly_sale.addActionListener(new MonthlySaleListner());
		display.addActionListener(new DisplayTablesListner(table_names_show));
		
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DbOperations.endDbConnection();
				jf.dispose();
			}
		});
	}
}
