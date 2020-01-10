import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
                	
public class MySqlDataStoreUtilities
{
static Connection conn = null;

public static void getConnection()
{
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HW2database?serverTimezone=UTC&useSSL=false", "root", "19940510");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


	public static boolean deleteOrder(int orderId) {
		try {

			getConnection();
			String deleteOrderQuery = "Delete from customerOrders where OrderId=?";
			PreparedStatement pst = conn.prepareStatement(deleteOrderQuery);
			pst.setInt(1, orderId);
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

public static void insertOrder(int orderId,String userName,String orderName,double orderPrice,String userAddress,String creditCardNo)
{
	try
	{
		Date current_date = new Date();

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
		getConnection();
		String insertIntoCustomerOrderQuery = "INSERT INTO customerOrders(OrderId,UserName,OrderName,OrderPrice,userAddress,creditCardNo,orderTime) "
		+ "VALUES (?,?,?,?,?,?,?);";
			
		PreparedStatement pst = conn.prepareStatement(insertIntoCustomerOrderQuery);
		//set the parameter for each column and execute the prepared statement
		pst.setInt(1,orderId);
		pst.setString(2,userName);
		pst.setString(3,orderName);
		pst.setDouble(4,orderPrice);
		pst.setString(5,userAddress);
		pst.setString(6,creditCardNo);
		pst.setString(7, SimpleDateFormat.format(current_date.getTime()));
		pst.execute();
	}
	catch(Exception e)
	{
	
	}		
}

public static HashMap<Integer, ArrayList<OrderPayment>> selectOrder()
{	

	HashMap<Integer, ArrayList<OrderPayment>> orderPayments=new HashMap<Integer, ArrayList<OrderPayment>>();
		
	try
	{					

		getConnection();
        //select the table 
		String selectOrderQuery ="select * from customerorders";			
		PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
		ResultSet rs = pst.executeQuery();	
		ArrayList<OrderPayment> orderList=new ArrayList<OrderPayment>();
		while(rs.next())
		{
			if(!orderPayments.containsKey(rs.getInt("OrderId")))
			{	
				ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
				orderPayments.put(rs.getInt("orderId"), arr);
			}
			ArrayList<OrderPayment> listOrderPayment = orderPayments.get(rs.getInt("OrderId"));		
			System.out.println("data is"+rs.getInt("OrderId")+orderPayments.get(rs.getInt("OrderId")));

			//add to orderpayment hashmap
			OrderPayment order= new OrderPayment(rs.getInt("OrderId"),rs.getString("userName"),rs.getString("orderName"),rs.getDouble("orderPrice"),rs.getString("userAddress"),rs.getString("creditCardNo"));
			listOrderPayment.add(order);
					
		}
				
					
	}
	catch(Exception e)
	{
		
	}
	return orderPayments;
}


public static boolean insertUser(String username,String password,String repassword,String usertype)
{
	try
	{	

		getConnection();
		String insertIntoCustomerRegisterQuery = "INSERT INTO Registration(username,password,repassword,usertype) "
		+ "VALUES (?,?,?,?);";	
				
		PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
		pst.setString(1,username);
		pst.setString(2,password);
		pst.setString(3,repassword);
		pst.setString(4,usertype);
		pst.execute();
	}
	catch(Exception e)
	{
	
	}
	return true;	
}

public static HashMap<String,User> selectUser()
{	
	HashMap<String,User> hm=new HashMap<String,User>();
	try 
	{
		getConnection();
		Statement stmt=conn.createStatement();
		String selectCustomerQuery="select * from  Registration";
		ResultSet rs = stmt.executeQuery(selectCustomerQuery);
		while(rs.next())
		{	User user = new User(rs.getString("username"),rs.getString("password"),rs.getString("usertype"));
				hm.put(rs.getString("username"), user);
		}
	}
	catch(Exception e)
	{
	}
	return hm;			
}
	public static HashMap<String, Product> selectInventory() {
		HashMap<String, Product> hm = new HashMap<String, Product>();
		try {
			getConnection();

			String selectAcc = "select * from Productdetails";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Product product = new Product(rs.getString("productName"), rs.getDouble("productPrice"), Integer.parseInt(rs.getString("inventory")));
				hm.put(rs.getString("Id"), product);
				product.setId(rs.getString("Id"));
			}
		} catch (Exception e) {
		}
		return hm;
	}

	public static HashMap<String, Product> selectOnSale() {
		HashMap<String, Product> hm = new HashMap<String, Product>();
		try {
			getConnection();

			String selectAcc = "select * from Productdetails where productCondition = ?";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			pst.setString(1, "New");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Product product = new Product(rs.getString("productName"), rs.getDouble("productPrice"), Integer.parseInt(rs.getString("inventory")));
				hm.put(rs.getString("Id"), product);
				product.setId(rs.getString("Id"));
			}
		} catch (Exception e) {
		}
		return hm;
	}
	public static HashMap<String, Product> selectRebate() {
		HashMap<String, Product> hm = new HashMap<String, Product>();
		try {
			getConnection();

			String selectAcc = "select * from Productdetails where productDiscount <= 10";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Product product = new Product(rs.getString("productName"), rs.getDouble("productPrice"), Double.parseDouble(rs.getString("productDiscount")));
				hm.put(rs.getString("Id"), product);
				product.setId(rs.getString("Id"));
			}
		} catch (Exception e) {
		}
		return hm;
	}
	public static HashMap<String, OrderPayment> selectDailyTransaction() {
		HashMap<String, OrderPayment> hm = new HashMap<String, OrderPayment>();
		try {
			getConnection();

			String selectAcc = "SELECT count(orderTime) as soldAmount, orderTime from CustomerOrders group by orderTime";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			ResultSet rs = pst.executeQuery();

			int i = 0;
			while (rs.next()) {
				OrderPayment orderPayment = new OrderPayment(rs.getInt("soldAmount"), rs.getDate("orderTime"));
				i++;
				hm.put(String.valueOf(i), orderPayment);
				//orderPayment.setId(rs.getString("Id"));
			}
		} catch (Exception e) {
		}
		return hm;
	}
	public static ArrayList<OrderPayment> selectDailyTransactionForChart() {
		ArrayList<OrderPayment> orderPaymentArrayList = new ArrayList<OrderPayment>();
		try {
			getConnection();

			String selectAcc = "SELECT count(orderTime) as soldAmount, orderTime from CustomerOrders group by orderTime";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				OrderPayment orderPayment = new OrderPayment(rs.getInt("soldAmount"), rs.getDate("orderTime"));
				orderPaymentArrayList.add(orderPayment);
			}
		} catch (Exception e) {
		}
		return orderPaymentArrayList;
	}
	public static HashMap<String, OrderPayment> selectSaleAmount() {
		HashMap<String, OrderPayment> hm = new HashMap<String, OrderPayment>();
		try {
			getConnection();

			String selectAcc = "select DISTINCT(temp.orderName),temp.saleAmount,CustomerOrders.orderPrice from CustomerOrders, (select orderName, count(orderName) as saleAmount from CustomerOrders group by orderName) as temp where CustomerOrders.orderName = temp.orderName";
			PreparedStatement pst = conn.prepareStatement(selectAcc);
			ResultSet rs = pst.executeQuery();

			int i = 0;
			while (rs.next()) {
				OrderPayment orderPayment = new OrderPayment(rs.getString("orderName"), rs.getDouble("orderPrice"), rs.getInt("saleAmount"));
				i++;
				hm.put(String.valueOf(i), orderPayment);
				//orderPayment.setOrderId(Integer.parseInt(rs.getString("Id")));
			}
		} catch (Exception e) {
		}
		return hm;
	}
	
}	