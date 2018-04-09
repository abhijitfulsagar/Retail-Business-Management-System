package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import oracle.jdbc.internal.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;
import swingGUI.DisplayTablesListner;
import swingGUI.MonthlySaleListner;

public class DbOperations {

	private static Connection conn = null;

	/**
	 * to get the database connection
	 */
	public static void startDbConnection() {
		conn = getConnection();
	}

	/**
	 * to stop the database connection
	 */
	public static void endDbConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getConnection() is used to connect to database 
	 * @return
	 */
	private static Connection getConnection() {
		Connection connection = null;
		try {
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
			connection = ds.getConnection("nkumthe1", "Nabha1707");
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return connection;
	}

	/**
	 * nameValidation() to validate the name 
	 * @param name is the name to be validated
	 * @return
	 */
	public boolean nameValidation(String name) {
		if (!name.isEmpty() && !name.equals(null) && name.length() <= 10) {
			return true;
		}
		return false;
	}

	/**
	 * to validate the given number
	 * @param number is the number to be validated
	 * @return
	 */
	public boolean numberValidation(String number) {
		Pattern p = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
		Matcher m = p.matcher(number);

		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * to check whether customer id starts with 'c'
	 * @param id is the customer id for which we are validating
	 * @return
	 */
	public Boolean checkValidIds(String id) {

		if (id.charAt(0) == 'c') {
			return true;
		}
		return false;
	}


	/**
	 * to get the savings for a particular purchase 
	 * @param pur_id is the purchase id for which we want the savings
	 * @return
	 */
	public String getSaving(String pur_id) {
		String result = null;
		try {
			CallableStatement cs = conn.prepareCall("{? = call RETAILBUISNESS.fPurchase_saving(?)}");
			cs.registerOutParameter(1, OracleTypes.FLOAT);
			cs.setString(2, pur_id);
			cs.execute();
			result = cs.getObject(1).toString();
			cs.close();
			return result;
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, se.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * It is used to delete a particular purchase
	 * @param pur_id is the purchase which is to be deleted
	 * @return
	 */
	public boolean deletePurchase(String pur_id) {
		try {
			CallableStatement cs = conn.prepareCall("begin RETAILBUISNESS.delete_purchase(:1); end;");
			cs.setInt(1, Integer.parseInt(pur_id));
			cs.executeQuery();
			cs.close();
			return true;
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, se.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * It is used to get the monthly sale for particular employee
	 * @param eid_t 
	 * @param sub_frame_mon_sale2
	 */
	public static void getMonthlySale(String eid_t, JFrame sub_frame_mon_sale2) {
		ArrayList<Object> dataColumns = new ArrayList<Object>();
		ArrayList<Object[]> dataRows = new ArrayList<Object[]>();
		try {
			CallableStatement cs = conn.prepareCall("begin RETAILBUISNESS.monthly_sale_activities(?,?); end;");
			cs.setString(1, eid_t);
			cs.registerOutParameter(2, OracleTypes.CURSOR);
			cs.execute();
			ResultSet rs = (ResultSet) cs.getObject(2);

			int columns_count = rs.getMetaData().getColumnCount();

			for (int i = 1; i <= columns_count; i++) {
				dataColumns.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				Object[] data = new Object[columns_count];
				for (int i = 0; i < columns_count; i++) {
					data[i] = rs.getObject(i+1);
				}
				dataRows.add(data);
			}

			cs.close();
			MonthlySaleListner.showEmpResult(dataColumns, dataRows, sub_frame_mon_sale2);

		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, se.getLocalizedMessage());
		}
	}

	/**
	 * It is used to get entire table data for a particular table('tableName')   
	 * @param tableName
	 */
	public void getTable(String tableName) {
		ArrayList<Object> dataColumns = new ArrayList<Object>();
		ArrayList<Object[]> dataRows = new ArrayList<Object[]>();
		try {
			CallableStatement cs = null;
			switch (tableName) {
			case "Customers":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowCustomers(?)");
				break;

			case "Products":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowProducts(?)");
				break;

			case "Supplies":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowSupplies(?)");
				break;

			case "Purchases":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowPurchases(?)");
				break;

			case "Suppliers":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowSuppliers(?)");
				break;
			case "Discounts":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowDiscounts(?)");
				break;

			case "Logs":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowLogs(?)");
				break;

			case "Employees":
				cs = conn.prepareCall("call RETAILBUISNESS.spShowEmployees(?)");
				break;
			default:
				break;
			}

			cs.registerOutParameter(1, OracleTypes.CURSOR);
			cs.executeQuery();
			ResultSet rs = (ResultSet) cs.getObject(1);

			int columns_count = rs.getMetaData().getColumnCount();

			for (int i = 1; i <= columns_count; i++) {
				dataColumns.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				Object[] data = new Object[columns_count];
				for (int i = 0; i < columns_count; i++) {
					data[i] = rs.getObject(i + 1);
				}
				dataRows.add(data);
			}
			rs.close();
			cs.close();
			DisplayTablesListner.showTable(dataColumns, dataRows);
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, se.getLocalizedMessage());
		}

	}

	/** 
	 * It is used to add a customer in customer table where cid,name and tele_num are attributes 
	 * @param cid
	 * @param name
	 * @param tele_num
	 * @param retrunFlag
	 * @return
	 */
	public static boolean addCustomer(String cid,String name, String tele_num, boolean retrunFlag) {
		try {
			CallableStatement cs = conn.prepareCall("begin RETAILBUISNESS.add_customer(:1,:2,:3); end;");
			cs.setString(1, cid);
			cs.setString(2, name);
			cs.setString(3, tele_num);
			cs.executeQuery();
			retrunFlag = !retrunFlag;
			JOptionPane.showMessageDialog(null, "Record saved successfully");
			cs.close();
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				JOptionPane.showMessageDialog(null, "Customer already exists. Please enter another cid");
			}else {
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			}
		}
		return retrunFlag;
	}

	/**
	 * It is used to add a purchase done by a customer in customer table
	 * @param eid
	 * @param cid
	 * @param pid
	 * @param quantity
	 * @param retrunFlag
	 * @return
	 */
	public static boolean addPurchase(String eid, String cid, String pid, int quantity,
			boolean retrunFlag) {

		try {
			CallableStatement cs = conn.prepareCall("begin RETAILBUISNESS.add_purchase(?,?,?,?,?); end;");
			cs.setString(1, eid);
			cs.setString(2, pid);
			cs.setString(3, cid);
			cs.setInt(4, quantity);
			cs.registerOutParameter(5, OracleTypes.VARCHAR);
			cs.executeQuery();
			String msg = cs.getString(5);
			retrunFlag = !retrunFlag;
			if(msg.equals("ONE")) {
				JOptionPane.showMessageDialog(null,"The current qoh of the product is below threshold and new supply required");
				CallableStatement cs1 = conn.prepareCall("begin RETAILBUISNESS.get_qoh(?,?); end;");
				cs1.setString(1, pid);
				cs1.registerOutParameter(2, OracleTypes.INTEGER);
				cs1.executeQuery();
				int qty =  cs1.getInt(2);
				cs1.close();
				JOptionPane.showMessageDialog(null,
						"New value of QOH for updated to " + qty + " for product " + pid);
			}else if(msg.equals("TWO")) {
				JOptionPane.showMessageDialog(null, "Purchase Completed");
			}else if(msg.equals("THREE")) {
				JOptionPane.showMessageDialog(null, "Quantity ordered is more that qoh. Purchase cannot be completed");
			}
			cs.close();
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				JOptionPane.showMessageDialog(null, "Unique key constraint violated for purchase id.");
			}else if(e.getErrorCode() == 1403) {
				JOptionPane.showMessageDialog(null, "QOH is below threshold but no supplier found to supply this product");
			}else {
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			}
		}
		return retrunFlag;
	}

	/**
	 * It is used to get all the employees eid 
	 * @return
	 */
	public static List<String> getEmployees() {
		List<String> emp_ids = new ArrayList<String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT eid FROM employees");
			while (rs.next()) {
				emp_ids.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emp_ids;
	}
	
	/**
	 * It is used to get all the products pid
	 * @return
	 */
	public static List<String> getProducts() {
		List<String> products_names = new ArrayList<String>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT pid FROM products");
			while (rs.next()) {
				products_names.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products_names;
	}
	
	/**
	 * It is used to get all customers cid
	 * @return
	 */
	public static List<String> getCustomers() {
		List<String> cust_ids = new ArrayList<String>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT cid FROM customers");
			while (rs.next()) {
				cust_ids.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cust_ids;
	}

	/**
	 * It is used to get all the purchases id(pur#)
	 * @return
	 */
	public static List<String> getPurchases() {
		List<String> pur_ids = new ArrayList<String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT pur# FROM purchases");
			while (rs.next()) {
				pur_ids.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pur_ids;
	}

}
