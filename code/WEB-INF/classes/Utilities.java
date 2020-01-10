import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
@WebServlet("/Utilities")

/* 
	Utilities class contains class variables of type HttpServletRequest, PrintWriter,String and HttpSession.

	Utilities class has a constructor with  HttpServletRequest, PrintWriter variables.
	  
*/

public class Utilities extends HttpServlet{
	HttpServletRequest req;
	PrintWriter pw;
	String url;
	HttpSession session; 
	public Utilities(HttpServletRequest req, PrintWriter pw) {
		this.req = req;
		this.pw = pw;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}



	/*  Printhtml Function gets the html file name as function Argument, 
		If the html file name is Header.html then It gets Username from session variables.
		Account ,Cart Information ang Logout Options are Displayed*/

    public void printHtml(String file) {
        String result = HtmlToString(file);
        //to print the right navigation in header of username cart and logout etc
        if (file.equals("Header.html")) {
            result = result + "<div id='menu' style='float: right;'><ul>";
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                username = Character.toUpperCase(username.charAt(0)) + username.substring(1);

                String userType = session.getAttribute("userType").toString();
                switch (userType) {
                    case "Customer":
                        result = result + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>"
                                +"<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li>"
                                + "<li><a href='Account'><span class='glyphicon'>Account</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                    case "StoreManager":
                        result = result + "<li><a href='StoreManagerHome'><span class='glyphicon'>ViewProduct</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>"
                                +"<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li>"
                                +"<li><a href='Inventory'><span class='glyphicon'>Inventory</span></a></li>"
                                +"<li><a href='SalesReport'><span class='glyphicon'>SalesReport</span></a></li>"
                                +"<li><a href='DataVisualization'><span class='glyphicon'>Trending</span></a></li>"
                                +"<li><a href='DataAnalytics'><span class='glyphicon'>DataAnalytics</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                    case "Salesman":
                        result = result + "<li><a href='SalesmanHome'><span class='glyphicon'>ViewOrder</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>"
                                +"<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                }
            } else
                result = result + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>" + "<li><a href='Login'><span class='glyphicon'>Login</span></a></li>";
            result = result + "<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li></ul></div></div><div id='page'>";
            pw.print(result);
        } else
            pw.print(result);
    }



    /*  getFullURL Function - Reconstructs the URL user request  */

	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}

	/*  HtmlToString - Gets the Html file and Converts into String and returns the String.*/
	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} 
		catch (Exception e) {
		}
		return result;
	} 

	/*  logout Function removes the username , usertype attributes from the session variable*/

	public void logout(){
		session.removeAttribute("username");
		session.removeAttribute("usertype");
	}
	
	/*  logout Function checks whether the user is loggedIn or Not*/

	public boolean isLoggedin(){
		if (session.getAttribute("username")==null)
			return false;
		return true;
	}

	/*  username Function returns the username from the session variable.*/
	
	public String username(){
		if (session.getAttribute("username")!=null)
			return session.getAttribute("username").toString();
		return null;
	}
	
	/*  usertype Function returns the usertype from the session variable.*/
	public String usertype(){
		if (session.getAttribute("usertype")!=null)
			return session.getAttribute("usertype").toString();
		return null;
	}
	
	/*  getUser Function checks the user is a customer or retailer or manager and returns the user class variable.*/
	public User getUser(){
		String usertype = usertype();
		HashMap<String, User> hm=new HashMap<String, User>();
		try
		{		
			hm=MySqlDataStoreUtilities.selectUser();
		}
		catch(Exception e)
		{
		}	
		User user = hm.get(username());
		return user;
	}
	
	public void removeOldOrder(int orderId, String orderName, String customerName) {
        MySqlDataStoreUtilities.deleteOrder(orderId);
    }
	public void removeItemFromCart(String itemName) {
        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
        int index = 0;

        //遍历所有orderItem，找到需要删除的item的index。
        for (OrderItem oi : orderItems) {
            if (oi.getName().equals(itemName)) {
                break;
            } else index++;
        }
        orderItems.remove(index);
    }
	/*  getCustomerOrders Function gets  the Orders for the user*/
	public ArrayList<OrderItem> getCustomerOrders(){
		ArrayList<OrderItem> order = new ArrayList<OrderItem>(); 
		if(OrdersHashMap.orders.containsKey(username()))
			order= OrdersHashMap.orders.get(username());
		return order;
	}

	/*  getOrdersPaymentSize Function gets  the size of OrderPayment */
	public int getOrderPaymentSize(){
		
		HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
		int size=0;
		try
		{
			orderPayments =MySqlDataStoreUtilities.selectOrder();
				
		}
		catch(Exception e)
		{
			
		}
		for(Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()){
				size=entry.getKey();
		}
			
		return size;		
	}

	/*  CartCount Function gets  the size of User Orders*/
	public int CartCount(){
		if(isLoggedin())
		return getCustomerOrders().size();
		return 0;
	}
	
	/* StoreProduct Function stores the Purchased product in Orders HashMap according to the User Names.*/

	public void storeProduct(String name,String type,String maker, String acc){
		if(!OrdersHashMap.orders.containsKey(username())){	
			ArrayList<OrderItem> arr = new ArrayList<OrderItem>();
			OrdersHashMap.orders.put(username(), arr);
		}
		ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
		if(type.equals("television")){
			Television television;
			television = SaxParserDataStore.televisionHashmap.get(name);
			OrderItem orderitem = new OrderItem(television.getName(), television.getPrice(), television.getImage(), television.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("soundSystem")){
			SoundSystem soundSystem = null;
			soundSystem = SaxParserDataStore.soundSystemHashmap.get(name);
			OrderItem orderitem = new OrderItem(soundSystem.getName(), soundSystem.getPrice(), soundSystem.getImage(), soundSystem.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("phone")){
			Phone phone = null;
			phone = SaxParserDataStore.phoneHashmap.get(name);
			OrderItem orderitem = new OrderItem(phone.getName(), phone.getPrice(), phone.getImage(), phone.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("laptop")){
			Laptop laptop = null;
			laptop = SaxParserDataStore.laptopHashmap.get(name);
			OrderItem orderitem = new OrderItem(laptop.getName(), laptop.getPrice(), laptop.getImage(), laptop.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("voiceAssistant")){
			VoiceAssistant voiceAssistant = null;
			voiceAssistant = SaxParserDataStore.voiceAssistantHashmap.get(name);
			OrderItem orderitem = new OrderItem(voiceAssistant.getName(), voiceAssistant.getPrice(), voiceAssistant.getImage(), voiceAssistant.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("fitnessWatch")){
			FitnessWatch fitnessWatch = null;
			fitnessWatch = SaxParserDataStore.fitnessWatchHashmap.get(name);
			OrderItem orderitem = new OrderItem(fitnessWatch.getName(), fitnessWatch.getPrice(), fitnessWatch.getImage(), fitnessWatch.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("smartWatch")){
			SmartWatch smartWatch = null;
			smartWatch = SaxParserDataStore.smartWatchHashmap.get(name);
			OrderItem orderitem = new OrderItem(smartWatch.getName(), smartWatch.getPrice(), smartWatch.getImage(), smartWatch.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("headPhone")){
			HeadPhone headPhone = null;
			headPhone = SaxParserDataStore.headPhoneHashmap.get(name);
			OrderItem orderitem = new OrderItem(headPhone.getName(), headPhone.getPrice(), headPhone.getImage(), headPhone.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("wirelessPlan")){
			WirelessPlan wirelessPlan = null;
			wirelessPlan = SaxParserDataStore.wirelessPlanHashmap.get(name);
			OrderItem orderitem = new OrderItem(wirelessPlan.getName(), wirelessPlan.getPrice(), wirelessPlan.getImage(), wirelessPlan.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("accessories")){	
			Accessory accessory = SaxParserDataStore.accessories.get(name); 
			OrderItem orderitem = new OrderItem(accessory.getName(), accessory.getPrice(), accessory.getImage(), accessory.getRetailer());
			orderItems.add(orderitem);
		}
		
	}
	public boolean isItemExist(String itemCatalog, String itemName) {

        HashMap<String, Object> hm = new HashMap<String, Object>();

        switch (itemCatalog) {
            case "FitnessWatch":
                hm.putAll(SaxParserDataStore.fitnessWatchHashmap);
                break;
            case "SmartWatch":
                hm.putAll(SaxParserDataStore.smartWatchHashmap);
                break;
            case "Television":
                hm.putAll(SaxParserDataStore.televisionHashmap);
                break;
            case "wirelessPlan":
                hm.putAll(SaxParserDataStore.wirelessPlanHashmap);
                break;
            case "Headphone":
                hm.putAll(SaxParserDataStore.headPhoneHashmap);
                break;
            case "Phone":
                hm.putAll(SaxParserDataStore.phoneHashmap);
                break;
            case "Laptop":
                hm.putAll(SaxParserDataStore.laptopHashmap);
                break;
            case "VoiceAssistant":
                hm.putAll(SaxParserDataStore.voiceAssistantHashmap);
                break;
            case "Accessory":
                hm.putAll(SaxParserDataStore.accessories);
                break;
        }
        return true;
    }
    public boolean storeNewProduct(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        String name = String.valueOf(map.get("name"));
        double price = Double.parseDouble(String.valueOf(map.get("price")));
        String image = String.valueOf(map.get("image"));
        String retailer = String.valueOf(map.get("manufacturer"));
        String condition = String.valueOf(map.get("condition"));
        double discount = Double.parseDouble(String.valueOf(map.get("discount")));
        String catalog = String.valueOf(map.get("productCatalog"));

        switch (catalog) {
            case "FitnessWatch":
                FitnessWatch fitnessWatch = new FitnessWatch();
                fitnessWatch.setId(id);
                fitnessWatch.setName(name);
                fitnessWatch.setPrice(price);
                fitnessWatch.setImage(image);
                fitnessWatch.setRetailer(retailer);
                fitnessWatch.setCondition(condition);
                fitnessWatch.setDiscount(discount);
                SaxParserDataStore.fitnessWatchHashmap.put(id, fitnessWatch);
                return true;
            case "SmartWatch":
                SmartWatch smartWatch = new SmartWatch();
                smartWatch.setId(id);
                smartWatch.setName(name);
                smartWatch.setPrice(price);
                smartWatch.setImage(image);
                smartWatch.setRetailer(retailer);
                smartWatch.setCondition(condition);
                smartWatch.setDiscount(discount);
                SaxParserDataStore.smartWatchHashmap.put(id, smartWatch);
                return true;
            case "Television":
                Television television = new Television();
                television.setId(id);
                television.setName(name);
                television.setPrice(price);
                television.setImage(image);
                television.setRetailer(retailer);
                television.setCondition(condition);
                television.setDiscount(discount);
                SaxParserDataStore.televisionHashmap.put(id, television);
                return true;
            case "SoundSystem":
                SoundSystem soundSystem = new SoundSystem();
                soundSystem.setId(id);
                soundSystem.setName(name);
                soundSystem.setPrice(price);
                soundSystem.setImage(image);
                soundSystem.setRetailer(retailer);
                soundSystem.setCondition(condition);
                soundSystem.setDiscount(discount);
                SaxParserDataStore.soundSystemHashmap.put(id, soundSystem);
                return true;
            case "HeadPhone":
                HeadPhone headphone = new HeadPhone();
                headphone.setId(id);
                headphone.setName(name);
                headphone.setPrice(price);
                headphone.setImage(image);
                headphone.setRetailer(retailer);
                headphone.setCondition(condition);
                headphone.setDiscount(discount);
                SaxParserDataStore.headPhoneHashmap.put(id, headphone);
                return true;
            case "Phone":
                Phone phone = new Phone();
                phone.setId(id);
                phone.setName(name);
                phone.setPrice(price);
                phone.setImage(image);
                phone.setRetailer(retailer);
                phone.setCondition(condition);
                phone.setDiscount(discount);
                SaxParserDataStore.phoneHashmap.put(id, phone);
                return true;
            case "Laptop":
                Laptop laptop = new Laptop();
                laptop.setId(id);
                laptop.setName(name);
                laptop.setPrice(price);
                laptop.setImage(image);
                laptop.setRetailer(retailer);
                laptop.setCondition(condition);
                laptop.setDiscount(discount);
                SaxParserDataStore.laptopHashmap.put(id, laptop);
                return true;
            case "VoiceAssistant":
                VoiceAssistant voiceAssistant = new VoiceAssistant();
                voiceAssistant.setId(id);
                voiceAssistant.setName(name);
                voiceAssistant.setPrice(price);
                voiceAssistant.setImage(image);
                voiceAssistant.setRetailer(retailer);
                voiceAssistant.setCondition(condition);
                voiceAssistant.setDiscount(discount);
                SaxParserDataStore.voiceAssistantHashmap.put(id, voiceAssistant);
                return true;
            case "Accessory":
                Accessory accessory = new Accessory();
                accessory.setId(id);
                accessory.setName(name);
                accessory.setPrice(price);
                accessory.setImage(image);
                accessory.setRetailer(retailer);
                accessory.setCondition(condition);
                accessory.setDiscount(discount);
                SaxParserDataStore.accessories.put(id, accessory);
                return true;
        }
        return false;
    }
    public boolean removeProduct(String productId, String catalog) {
        switch (catalog) {
            case "Fitness Watch":
                SaxParserDataStore.fitnessWatchHashmap.remove(productId);
                return true;
            case "Smart Watch":

                SaxParserDataStore.smartWatchHashmap.remove(productId);
                return true;
            case "Television":

                SaxParserDataStore.televisionHashmap.remove(productId);
                return true;
            case "SoundSystem":

                SaxParserDataStore.soundSystemHashmap.remove(productId);
                return true;
            case "Headphone":

                SaxParserDataStore.headPhoneHashmap.remove(productId);
                return true;
            case "Phone":

                SaxParserDataStore.phoneHashmap.remove(productId);
                return true;
            case "Laptop":

                SaxParserDataStore.laptopHashmap.remove(productId);
                return true;
            case "Voice Assistant":

                SaxParserDataStore.voiceAssistantHashmap.remove(productId);
                return true;
            case "Accessory":

                SaxParserDataStore.accessories.remove(productId);
                return true;
        }
        return false;
    }

    public boolean updateProduct(String id, String name, String price, String manufacturer, String condition, String discount, String image, String catalog) {
        switch (catalog) {
            case "Fitness Watch":
                FitnessWatch fitnessWatch = new FitnessWatch();
                fitnessWatch.setId(id);
                fitnessWatch.setName(name);
                fitnessWatch.setPrice(Double.parseDouble(price));
                fitnessWatch.setRetailer(manufacturer);
                fitnessWatch.setCondition(condition);
                fitnessWatch.setDiscount(Double.parseDouble(discount));
                fitnessWatch.setImage(image);
                SaxParserDataStore.fitnessWatchHashmap.remove(id);
                SaxParserDataStore.fitnessWatchHashmap.put(id, fitnessWatch);

                return true;
            case "Smart Watch":

                SmartWatch smartWatch = new SmartWatch();
                smartWatch.setId(id);
                smartWatch.setName(name);
                smartWatch.setPrice(Double.parseDouble(price));
                smartWatch.setRetailer(manufacturer);
                smartWatch.setCondition(condition);
                smartWatch.setDiscount(Double.parseDouble(discount));
                smartWatch.setImage(image);
                SaxParserDataStore.smartWatchHashmap.remove(id);
                SaxParserDataStore.smartWatchHashmap.put(id, smartWatch);
                return true;
            case "Television":

                Television television = new Television();
                television.setId(id);
                television.setName(name);
                television.setPrice(Double.parseDouble(price));
                television.setRetailer(manufacturer);
                television.setCondition(condition);
                television.setDiscount(Double.parseDouble(discount));
                television.setImage(image);
                SaxParserDataStore.televisionHashmap.remove(id);
                SaxParserDataStore.televisionHashmap.put(id, television);
                return true;
            case "SoundSystem":

                SoundSystem soundSystem = new SoundSystem();
                soundSystem.setId(id);
                soundSystem.setName(name);
                soundSystem.setPrice(Double.parseDouble(price));
                soundSystem.setRetailer(manufacturer);
                soundSystem.setCondition(condition);
                soundSystem.setDiscount(Double.parseDouble(discount));
                soundSystem.setImage(image);
                SaxParserDataStore.soundSystemHashmap.remove(id);
                SaxParserDataStore.soundSystemHashmap.put(id, soundSystem);
                return true;
            case "Headphone":

                HeadPhone headphone = new HeadPhone();
                headphone.setId(id);
                headphone.setName(name);
                headphone.setPrice(Double.parseDouble(price));
                headphone.setRetailer(manufacturer);
                headphone.setCondition(condition);
                headphone.setDiscount(Double.parseDouble(discount));
                headphone.setImage(image);
                SaxParserDataStore.headPhoneHashmap.remove(id);
                SaxParserDataStore.headPhoneHashmap.put(id, headphone);
                return true;
            case "Phone":

                Phone phone = new Phone();
                phone.setId(id);
                phone.setName(name);
                phone.setPrice(Double.parseDouble(price));
                phone.setRetailer(manufacturer);
                phone.setCondition(condition);
                phone.setDiscount(Double.parseDouble(discount));
                phone.setImage(image);
                SaxParserDataStore.phoneHashmap.remove(id);
                SaxParserDataStore.phoneHashmap.put(id, phone);
                return true;
            case "Laptop":

                Laptop laptop = new Laptop();
                laptop.setId(id);
                laptop.setName(name);
                laptop.setPrice(Double.parseDouble(price));
                laptop.setRetailer(manufacturer);
                laptop.setCondition(condition);
                laptop.setDiscount(Double.parseDouble(discount));
                laptop.setImage(image);
                SaxParserDataStore.laptopHashmap.remove(id);
                SaxParserDataStore.laptopHashmap.put(id, laptop);
                return true;
            case "Voice Assistant":

                VoiceAssistant voiceAssistant = new VoiceAssistant();
                voiceAssistant.setId(id);
                voiceAssistant.setName(name);
                voiceAssistant.setPrice(Double.parseDouble(price));
                voiceAssistant.setRetailer(manufacturer);
                voiceAssistant.setCondition(condition);
                voiceAssistant.setDiscount(Double.parseDouble(discount));
                voiceAssistant.setImage(image);
                SaxParserDataStore.voiceAssistantHashmap.remove(id);
                SaxParserDataStore.voiceAssistantHashmap.put(id, voiceAssistant);
                return true;
            case "Accessory":

                Accessory accessory = new Accessory();
                accessory.setId(id);
                accessory.setName(name);
                accessory.setPrice(Double.parseDouble(price));
                accessory.setRetailer(manufacturer);
                accessory.setCondition(condition);
                accessory.setDiscount(Double.parseDouble(discount));
                accessory.setImage(image);
                SaxParserDataStore.accessories.remove(id);
                SaxParserDataStore.accessories.put(id, accessory);
                return true;
        }
        return false;
    }

    public void cancelOrder(int orderId) {

        MySqlDataStoreUtilities.deleteOrder(orderId);

    }

    
    public void updateOrder(int orderId, String customerName,
                            String orderName, double orderPrice, String userAddress, String creditCardNo) {
        MySqlDataStoreUtilities.deleteOrder(orderId);
        MySqlDataStoreUtilities.insertOrder(orderId, customerName, orderName, orderPrice, userAddress, creditCardNo);
    }


    public void readXML() {

        String filepath = "/hw2_lz/web/ProductCatalog.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    public String getRealPath(String catalog) {
        String realPath = "images";
        switch (catalog) {
            case "FitnessWatch":
                realPath = realPath + "/fitnessWatch";
                break;
            case "SmartWatch":
                realPath = realPath + "/smartWatch";
                break;
            case "SoundSystem":
                realPath = realPath + "/soundSystem";
                break;
            case "Television":
                realPath = realPath + "/television";
                break;
            case "HeadPhone":
                realPath = realPath + "/headPhone";
                break;
            case "WirelessPlan":
            	realPath = realPath + "/wirelessPlan";
            	break;
            case "Phone":
                realPath = realPath + "/phone";
                break;
            case "Laptop":
                realPath = realPath + "/laptop";
                break;
            case "VoiceAssistant":
                realPath = realPath + "/voiceAssistant";
                break;
            case "Accessory":
                realPath = realPath + "/accessory";
                break;
        }

        return realPath;
    }
	public boolean isContainsStr(String string) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(string);
        return m.matches();
    }
    public void storeNewOrder(int orderId, String orderName, String customerName, double orderPrice, String userAddress, String creditCardNo) {
        MySqlDataStoreUtilities.insertOrder(orderId, orderName, customerName, orderPrice, userAddress, creditCardNo);

    }
    public boolean updateOrderFile(HashMap<Integer, ArrayList<OrderPayment>> orderPayments) {
        String TOMCAT_HOME = System.getProperty("catalina.home");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(TOMCAT_HOME + "/webapps/Tutorial_1lz/PaymentDetails.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(orderPayments);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }
        return true;
    }
	// store the payment details for orders
	public void storePayment(int orderId,
		String orderName,double orderPrice,String userAddress,String creditCardNo){
		HashMap<Integer, ArrayList<OrderPayment>> orderPayments= new HashMap<Integer, ArrayList<OrderPayment>>();
			// get the payment details file 
		try
		{
			orderPayments=MySqlDataStoreUtilities.selectOrder();
		}
		catch(Exception e)
		{
			
		}
		if(orderPayments==null)
		{
			orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
		}
			// if there exist order id already add it into same list for order id or create a new record with order id
			
		if(!orderPayments.containsKey(orderId)){	
			ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
			orderPayments.put(orderId, arr);
		}
		ArrayList<OrderPayment> listOrderPayment = orderPayments.get(orderId);		
		OrderPayment orderpayment = new OrderPayment(orderId,username(),orderName,orderPrice,userAddress,creditCardNo);
		listOrderPayment.add(orderpayment);	
			
			// add order details into database
		try
		{	
			MySqlDataStoreUtilities.insertOrder(orderId,username(),orderName,orderPrice,userAddress,creditCardNo);
		}
		catch(Exception e)
		{
			System.out.println("inside exception file not written properly");
		}	
	}
	public String storeReview(String productname,String producttype,String productmaker,String reviewrating,String reviewdate,String  reviewtext,String reatilerpin,String price,String city,String retailerstate, String usergender,String useroccupatation){
	String message=MongoDBDataStoreUtilities.insertReview(productname,username(),producttype,productmaker,reviewrating,reviewdate,reviewtext,reatilerpin,price,city,retailerstate,usergender,useroccupatation);
		if(!message.equals("Successfull"))
		{ return "UnSuccessfull";
		}
		else
		{
		HashMap<String, ArrayList<Review>> reviews= new HashMap<String, ArrayList<Review>>();
		try
		{
			reviews=MongoDBDataStoreUtilities.selectReview();
		}
		catch(Exception e)
		{
			
		}
		if(reviews==null)
		{
			reviews = new HashMap<String, ArrayList<Review>>();
		}
			// if there exist product review already add it into same list for productname or create a new record with product name
			
		if(!reviews.containsKey(productname)){	
			ArrayList<Review> arr = new ArrayList<Review>();
			reviews.put(productname, arr);
		}
		ArrayList<Review> listReview = reviews.get(productname);		
		Review review = new Review(productname,username(),producttype,productmaker,reviewrating,reviewdate,reviewtext,reatilerpin,price,city,retailerstate,usergender,useroccupatation);
		listReview.add(review);	
			
			// add Reviews into database
		
		return "Successfull";	
		}
	}
	
	/* getConsoles Functions returns the Hashmap with all consoles in the store.*/

	public HashMap<String, Television> getTelevision(){
			HashMap<String, Television> hm = new HashMap<String, Television>();
			hm.putAll(SaxParserDataStore.televisionHashmap);
			return hm;
	}
	
	/* getGames Functions returns the  Hashmap with all Games in the store.*/

	public HashMap<String, SoundSystem> getSoundSystem(){
			HashMap<String, SoundSystem> hm = new HashMap<String, SoundSystem>();
			hm.putAll(SaxParserDataStore.soundSystemHashmap);
			return hm;
	}
	
	/* getTablets Functions returns the Hashmap with all Tablet in the store.*/

	public HashMap<String, Phone> getPhone(){
			HashMap<String, Phone> hm = new HashMap<String, Phone>();
			hm.putAll(SaxParserDataStore.phoneHashmap);
			return hm;
	}
	public HashMap<String, Laptop> getLaptop(){
			HashMap<String, Laptop> hm = new HashMap<String, Laptop>();
			hm.putAll(SaxParserDataStore.laptopHashmap);
			return hm;
	}
	public HashMap<String, VoiceAssistant> getVoiceAssistant(){
			HashMap<String, VoiceAssistant> hm = new HashMap<String, VoiceAssistant>();
			hm.putAll(SaxParserDataStore.voiceAssistantHashmap);
			return hm;
	}
	public HashMap<String, FitnessWatch> getFitnessWatch(){
			HashMap<String, FitnessWatch> hm = new HashMap<String, FitnessWatch>();
			hm.putAll(SaxParserDataStore.fitnessWatchHashmap);
			return hm;
	}
	public HashMap<String, SmartWatch> getSmartWatch(){
			HashMap<String, SmartWatch> hm = new HashMap<String, SmartWatch>();
			hm.putAll(SaxParserDataStore.smartWatchHashmap);
			return hm;
	}
	public HashMap<String, HeadPhone> getHeadPhone(){
			HashMap<String, HeadPhone> hm = new HashMap<String, HeadPhone>();
			hm.putAll(SaxParserDataStore.headPhoneHashmap);
			return hm;
	}
	public HashMap<String, WirelessPlan> getWirelessPlan(){
			HashMap<String, WirelessPlan> hm = new HashMap<String, WirelessPlan>();
			hm.putAll(SaxParserDataStore.wirelessPlanHashmap);
			return hm;
	}
	
	/* getProducts Functions returns the Arraylist of consoles in the store.*/

	public ArrayList<String> getProductTelevision(){
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Television> entry : getTelevision().entrySet()){			
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	
	/* getProducts Functions returns the Arraylist of games in the store.*/

	public ArrayList<String> getProductSoundSystem(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, SoundSystem> entry : getSoundSystem().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	
	/* getProducts Functions returns the Arraylist of Tablets in the store.*/

	public ArrayList<String> getProductPhone(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Phone> entry : getPhone().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	public ArrayList<String> getProductLaptop(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Laptop> entry : getLaptop().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	public ArrayList<String> getProductVoiceAssistant(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, VoiceAssistant> entry : getVoiceAssistant().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}public ArrayList<String> getProductFitnessWatch(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, FitnessWatch> entry : getFitnessWatch().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	public ArrayList<String> getProductSmartWatch(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, SmartWatch> entry : getSmartWatch().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	public ArrayList<String> getProductHeadPhone(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, HeadPhone> entry : getHeadPhone().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
	public ArrayList<String> getProductWirelessPlan(){		
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, WirelessPlan> entry : getWirelessPlan().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}
}
