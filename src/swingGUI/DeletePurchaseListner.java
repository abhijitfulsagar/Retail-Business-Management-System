package swingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import database.DbOperations;

public class DeletePurchaseListner implements ActionListener {

	
	DbOperations dbOP = new DbOperations();
	
	/**
	 * To create UI for DeletePurchase 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame sub_frame_delete = sub_frame_delete = new JFrame();
		sub_frame_delete.getContentPane().setBackground(new Color(240, 248, 255));
		sub_frame_delete.setTitle("Delete purchase");
		sub_frame_delete.setSize(600,600);
		sub_frame_delete.getContentPane().setLayout(null);
		sub_frame_delete.setVisible(true);
		
		JLabel pur_id_l = new JLabel("Enter purchase ID");
		pur_id_l.setForeground(new Color(0, 0, 128));
		JButton delete_pur_b = new JButton("Delete");
		delete_pur_b.setForeground(new Color(0, 0, 128));
		JButton close_b = new JButton("Close");
		close_b.setForeground(new Color(0, 0, 128));
		
		
		List<String> list = DbOperations.getPurchases();
		String[] purchase_ids = list.toArray(new String[0]);
		
		sub_frame_delete.repaint();
		DefaultComboBoxModel model = new DefaultComboBoxModel(purchase_ids);
		JComboBox pid_combo = new JComboBox(model);
		pid_combo.validate();
		
		
		pur_id_l.setBounds(91,160,150, 40);
		pid_combo.setBounds(290,160,150, 40);
		delete_pur_b.setBounds(91,282,150, 40);
		close_b.setBounds(290,282,150, 40);
		
		sub_frame_delete.getContentPane().add(pur_id_l);
		sub_frame_delete.getContentPane().add(pid_combo);
		sub_frame_delete.getContentPane().add(delete_pur_b);
		sub_frame_delete.getContentPane().add(close_b);
		
		JLabel lblNewLabel = new JLabel("Enter purchase id to delete purchase");
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		lblNewLabel.setBounds(91, 90, 316, 16);
		sub_frame_delete.getContentPane().add(lblNewLabel);
		
		performActions(sub_frame_delete,pid_combo,close_b,delete_pur_b,close_b);
	}
	
	/**
	 * To delete particular purchase using its id
	 * @param sub_frame_delete2
	 * @param pid_combo
	 * @param close_b
	 * @param delete_pur_b
	 * @param close_b2
	 */
	private void performActions(JFrame sub_frame_delete2, JComboBox pid_combo, JButton close_b, JButton delete_pur_b,
			JButton close_b2) {
	close_b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pid_combo.invalidate();
				sub_frame_delete2.dispose();
			}
		});
	delete_pur_b.addActionListener( new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String pur_id = pid_combo.getSelectedItem().toString();
			boolean success = dbOP.deletePurchase(pur_id);
			if(success) {
				JOptionPane.showMessageDialog(null, "Record deleted successfully");
			}
			sub_frame_delete2.dispose();
		}
	});
	
	}

}
