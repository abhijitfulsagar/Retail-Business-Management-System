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
import javax.swing.JTextField;

import database.DbOperations;

public class DisplaySavingListner implements ActionListener {
	DbOperations dbOP = new DbOperations();

	@Override
	/**
	 * to create the UI for Savings
	 */
	public void actionPerformed(ActionEvent e) {
		
		JFrame sub_frame_saving =  new JFrame();
		sub_frame_saving.setTitle("Monthly Saving");
		sub_frame_saving.setSize(600,600);
		sub_frame_saving.getContentPane().setLayout(null);
		sub_frame_saving.getContentPane().setBackground(new Color(240, 248, 255));
		sub_frame_saving.setVisible(true);
		
		JLabel pur_id_l = new JLabel("Select purchase ID");
		pur_id_l.setForeground(new Color(0, 0, 128));
		JLabel saving_result_l = new JLabel("Your saving");
		saving_result_l.setForeground(new Color(0, 0, 128));
		JTextField saving_result_t = new JTextField();
		JButton saving_result_b = new JButton("Get Saving");
		saving_result_b.setForeground(new Color(0, 0, 128));
		JButton close_b = new JButton("Close");
		close_b.setForeground(new Color(0, 0, 128));
		
		List<String> list = DbOperations.getPurchases();
		String[] purchase_ids = list.toArray(new String[0]);
		
		JComboBox pid_combo = new JComboBox(purchase_ids);
		
		pur_id_l.setBounds(59,115,150, 40);
		saving_result_l.setBounds(59,210,150, 40);
		pid_combo.setBounds(221,115,150, 40);
		saving_result_t.setBounds(221,210,150, 40);
		saving_result_b.setBounds(413,116,150, 40);
		close_b.setBounds(221,296,150, 40);
		

		sub_frame_saving.getContentPane().add(pur_id_l);
		sub_frame_saving.getContentPane().add(saving_result_l);
		sub_frame_saving.getContentPane().add(pid_combo);
		sub_frame_saving.getContentPane().add(saving_result_t);
		sub_frame_saving.getContentPane().add(saving_result_b);
		sub_frame_saving.getContentPane().add(close_b);
		
		JLabel lblNewLabel = new JLabel("Select purchase id to get saving");
		lblNewLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(59, 61, 273, 16);
		sub_frame_saving.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("$");
		label.setBounds(375, 222, 18, 16);
		sub_frame_saving.getContentPane().add(label);
		performActions(sub_frame_saving,saving_result_b,close_b,saving_result_t,pid_combo);
		
	}
	/**
	 * It is used to ge the savings for a particular purchase pur#
	 * @param sub_frame_saving2
	 * @param saving_result_b
	 * @param close_b
	 * @param saving_result_t
	 * @param pid_combo
	 */
	private void performActions(JFrame sub_frame_saving2, JButton saving_result_b, JButton close_b, 
			JTextField saving_result_t, JComboBox pid_combo) {
		
		close_b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saving_result_t.setText("");
				sub_frame_saving2.dispose();	
			}
		});
		
		saving_result_b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String pur_id = pid_combo.getSelectedItem().toString();
				String result = dbOP.getSaving(pur_id);
				saving_result_t.setText(result);
			}
		});
	}

}
