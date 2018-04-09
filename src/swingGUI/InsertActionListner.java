package swingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import database.DbOperations;

public class InsertActionListner implements ActionListener {

	JComboBox table_names_insert = null;
	private JFrame sub_frame_purchase = null;
	private JFrame sub_frame_customer = null;

	DbOperations dbOP = new DbOperations();

	public InsertActionListner(JComboBox table_names_insert) {
		this.table_names_insert = table_names_insert;
		this.sub_frame_purchase = new JFrame();
		this.sub_frame_customer = new JFrame();

	}

	@Override
	/**
	 * to check for which table frame needs to be created
	 * 
	 **/
	public void actionPerformed(ActionEvent e) {
		if (this.table_names_insert.getSelectedItem().equals("Select table")) {
			JOptionPane.showMessageDialog(null, "Please select table name");
		} else if (this.table_names_insert.getSelectedItem().equals("Purchases")) {
			createPurchasesFrame(this.sub_frame_purchase);
		} else if (this.table_names_insert.getSelectedItem().equals("Customers")) {
			createCustomerFrame(this.sub_frame_customer);
		}
	}
	
	/**
	 * to create purchase table frame
	 * @param sub_frame_purchase2
	 */
	private void createPurchasesFrame(JFrame sub_frame_purchase2) {
		sub_frame_purchase2.getContentPane().setBackground(new Color(240, 248, 255));
		sub_frame_purchase2.setTitle("Insert into purchase");
		sub_frame_purchase2.setSize(600, 600);
		sub_frame_purchase2.getContentPane().setLayout(null);
		sub_frame_purchase2.setVisible(true);

		JLabel eid_l = new JLabel("Employee Name");
		eid_l.setForeground(new Color(0, 0, 128));
		JLabel cid_l = new JLabel("Customer Name");
		cid_l.setForeground(new Color(0, 0, 128));
		JLabel pid_l = new JLabel("Product Name");
		pid_l.setForeground(new Color(0, 0, 128));
		JLabel pur_qty_l = new JLabel("Quantity");
		pur_qty_l.setForeground(new Color(0, 0, 128));

		List<String> customersList = DbOperations.getCustomers();
		String[] cust_ids = customersList.toArray(new String[0]);
		
		List<String> productsList = DbOperations.getProducts();
		String[] prod_names = productsList.toArray(new String[0]);
		
		List<String> employeesList = DbOperations.getEmployees();
		String[] emp_ids = employeesList.toArray(new String[0]);
		
		JComboBox products_dropdown = new JComboBox(prod_names);
		JComboBox cid_combo = new JComboBox(cust_ids);
		JComboBox eid_combo = new JComboBox(emp_ids);
	
		JTextField pur_qty_t = new JTextField();

		JButton save = new JButton("Save");
		save.setForeground(new Color(0, 0, 128));
		save.setBounds(100, 350, 150, 40);
		
		pid_l.setBounds(40, 100, 150, 40);
		cid_l.setBounds(40, 150, 150, 40);
		eid_l.setBounds(40, 200, 150, 40);
		pur_qty_l.setBounds(40, 250, 150, 40);

		products_dropdown.setBounds(250, 100, 150, 40);
		cid_combo.setBounds(250, 150, 150, 40);
		eid_combo.setBounds(250, 200, 150, 40);
		pur_qty_t.setBounds(250, 250, 150, 40);

		
		
		JButton close = new JButton("Close");
		close.setForeground(new Color(0, 0, 128));
		close.setBounds(300, 350, 150, 40);
		sub_frame_purchase2.getContentPane().add(close);
		

		sub_frame_purchase2.getContentPane().add(pid_l);
		sub_frame_purchase2.getContentPane().add(cid_l);
		sub_frame_purchase2.getContentPane().add(eid_l);
		sub_frame_purchase2.getContentPane().add(pur_qty_l);
		sub_frame_purchase2.getContentPane().add(products_dropdown);
		sub_frame_purchase2.getContentPane().add(cid_combo);
		sub_frame_purchase2.getContentPane().add(eid_combo);
		sub_frame_purchase2.getContentPane().add(pur_qty_t);
		sub_frame_purchase2.getContentPane().add(save);

		JLabel lblNewLabel = new JLabel("Please enter appropriate values to insert");
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(40, 49, 360, 16);
		sub_frame_purchase2.getContentPane().add(lblNewLabel);
		
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cid_combo.setSelectedIndex(0);
				eid_combo.setSelectedIndex(0);
				pur_qty_t.setText("");
				sub_frame_purchase2.dispose();
			}
		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean clear = saveFields(cid_combo, eid_combo, pur_qty_t, products_dropdown);
				if (clear) {
					clearFields();
					sub_frame_purchase2.dispose();
				}
			}

			private void clearFields() {
				cid_combo.setSelectedIndex(0);
				eid_combo.setSelectedIndex(0);
				pur_qty_t.setText("");
			}

			private boolean saveFields(JComboBox cid_combo, JComboBox eid_combo, JTextField pur_qty_t,
					JComboBox products_dropdown2) {
				
				boolean retrunFlag = false;
				String eid = eid_combo.getSelectedItem().toString();
				String cid = cid_combo.getSelectedItem().toString();
				String pid = products_dropdown2.getSelectedItem().toString();

				try {
					 if (pur_qty_t.getText().isEmpty()) {
						 JOptionPane.showMessageDialog(null, "Please enter quantity");
					 }else if(Integer.parseInt(pur_qty_t.getText()) <= 0 ){
						 JOptionPane.showMessageDialog(null, "Please enter quantity in positive or quantity cannot be 0");
					 }else {
						 int quantity = Integer.parseInt(pur_qty_t.getText());
						 retrunFlag = DbOperations.addPurchase(eid, cid, pid, quantity, retrunFlag);
					 }
				}catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Please enter numeric value as quantity");
				}
				return retrunFlag;
			}
		});
	}

	
	/**
	 * to create customer table frame
	 * @param sub_frame_customer2
	 */
	private void createCustomerFrame(JFrame sub_frame_customer2) {
		sub_frame_customer2.getContentPane().setBackground(new Color(240, 248, 255));
		JLabel cid, name, tele;
		JTextField cid_t, name_t, tele_t;
		sub_frame_customer2.setTitle("Insert into customer");
		sub_frame_customer2.setSize(600, 600);
		sub_frame_customer2.getContentPane().setLayout(null);
		sub_frame_customer2.setVisible(true);

		JButton save = new JButton("Save");
		save.setBounds(100, 350, 150, 40);
		save.setForeground(new Color(0, 0, 128));

		cid = new JLabel("Cid");
		cid.setForeground(new Color(0, 0, 128));
		name = new JLabel("Name");
		name.setForeground(new Color(0, 0, 128));
		tele = new JLabel("Telephone#");
		tele.setForeground(new Color(0, 0, 128));

		name_t = new JTextField();
		tele_t = new JTextField();
		cid_t = new JTextField(); 
		
		cid.setBounds(40,100,150,40);
		name.setBounds(40, 150, 150, 40);
		tele.setBounds(40, 200, 150, 40);

		cid_t.setBounds(250, 100, 150, 40);
		name_t.setBounds(250, 150, 150, 40);
		tele_t.setBounds(250, 200, 150, 40);

		sub_frame_customer2.getContentPane().add(cid);
		sub_frame_customer2.getContentPane().add(name);
		sub_frame_customer2.getContentPane().add(tele);
		sub_frame_customer2.getContentPane().add(cid_t);
		sub_frame_customer2.getContentPane().add(name_t);
		sub_frame_customer2.getContentPane().add(tele_t);
		sub_frame_customer2.getContentPane().add(save);

		JLabel lblNewLabel = new JLabel("Please enter appropriate values to insert");
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(40, 49, 360, 16);
		sub_frame_customer2.getContentPane().add(lblNewLabel);
		
		JButton close = new JButton("Close");
		close.setForeground(new Color(0, 0, 128));
		close.setBounds(300, 350, 150, 40);
		sub_frame_customer2.getContentPane().add(close);

		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name_t.setText("");
				tele_t.setText("");
				cid_t.setText("");
				sub_frame_customer2.dispose();
			}
		});
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				boolean clear = saveFields(name_t, tele_t);
				if (clear) {
					clearFields();
					sub_frame_customer2.dispose();
				}
			}

			private void clearFields() {
				name_t.setText("");
				tele_t.setText("");
				cid_t.setText("");
			}

			private boolean saveFields(JTextField name_t, JTextField tele_t) {

				String cid = cid_t.getText();
				String name = name_t.getText();
				String tele_num = tele_t.getText();
				boolean retrunFlag = false;

				if (!dbOP.nameValidation(name)) {
					JOptionPane.showMessageDialog(null, "Please enter valid name");
				} else if (!dbOP.numberValidation(tele_num)) {
					JOptionPane.showMessageDialog(null, "Please enter valid number of format xxx-xxx-xxxx");
				}else if(cid.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter cid");
				}else if(!dbOP.checkValidIds(cid)) {
					JOptionPane.showMessageDialog(null, "Please enter cid starting with c");
				}else {
					retrunFlag = DbOperations.addCustomer(cid,name, tele_num, retrunFlag);
				}
				return retrunFlag;
			}
		});
	}
}
