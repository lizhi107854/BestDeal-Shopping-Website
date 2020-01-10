import java.io.IOException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DealMatchesUtilities")

public class DealMatchesUtilities extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

			HashMap<String,Product> selectedproducts=new HashMap<String,Product>();
			//System.out.println(selectedproducts.size());
		try
			{
				
		pw.print("<div id='content'>");
		pw.print("<div class='post'>");
		pw.print("<h2 class='title'>");
		pw.print("<a href='#'>Welcome to BestDeal </a></h2>");
		
		pw.print("<div class='entry'>");
		pw.print("<img src='images/home.jpg'style='width: 600px; display: block; margin-left: auto; margin-right: auto' />");
		//pw.print("<br> <br>");
		//pw.print("<h2>The world trusts us to deliver SPEEDY service for video-gaming fans</h2>");
		pw.print("<br> <br>");
		pw.print("<h2>We are trying to find the best price for you!</h2>");
		
			String line=null;
			String TOMCAT_HOME = System.getProperty("catalina.home");

			HashMap<String,Product> productmap=AjaxUtility.getData();
			
			for(Map.Entry<String, Product> entry : productmap.entrySet())
			{
				
			if(selectedproducts.size()<2 && !selectedproducts.containsKey(entry.getKey())){
				//System.out.println(selectedproducts.size());
				
				
			BufferedReader reader = new BufferedReader(new FileReader (new File(TOMCAT_HOME+"/webapps/HW4_lz_code/DealMatches.txt")));
			line=reader.readLine().toLowerCase();
//		

			if(line==null)
			{
				pw.print("<h2 align='center'>No Offers Found</h2>");
				break;
			}	
			else
			{	
			do {	
			      
				  if(line.contains(entry.getKey()))
				  {
				 
					pw.print("<h2>"+line+"</h2>");
					pw.print("<br>");
					selectedproducts.put(entry.getKey(),entry.getValue());
					//System.out.println(selectedproducts.size());
					break;
				  }
				  
			    }while((line = reader.readLine()) != null);
			
			 }
			 }
			}
			}
			catch(Exception e)
			{
			pw.print("<h2 align='center'>No Offers Found</h2>");
			}
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='post'>");
		pw.print("<h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Deal Matches</a>");
		pw.print("</h2>");
		pw.print("<div class='entry'>");
		if(selectedproducts.size()==0)
		{
		pw.print("<h2 align='center'>No Deals Found</h2>");	
		}
		else
		{
		pw.print("<table id='bestseller'>");
		pw.print("<tr>");
		//System.out.println(selectedproducts.entrySet());
		for(Map.Entry<String, Product> entry : selectedproducts.entrySet()){
//			System.out.println(selectedproducts.entrySet());
//			System.out.println(entry.getValue().getName());
		pw.print("<td><div id='shop_item'><h3>"+entry.getValue().getId()+"</h3>");
		pw.print("<strong>"+entry.getValue().getPrice()+"$</strong>");
		pw.print("<ul>");
		pw.print("<li id='item'><img src='images/"+entry.getValue().getType()+"/"+entry.getValue().getImage()+"' alt='' />");
		pw.print("</li><li>");
//		pw.print("<form action='Cart' method='post'><input type='submit' class='btnbuy' value='Buy Now'>");
//		pw.print("<input type='hidden' name='name' value='"+entry.getKey()+"'>");
//		pw.print("<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>");
//		pw.print("<input type='hidden' name='maker' value='"+entry.getValue().getRetailer()+"'>");
//		pw.print("<input type='hidden' name='access' value=''>");
			//System.out.println(entry.getKey());
			//System.out.println(entry.getValue().getName());
			pw.print("<li><form method='post' action='Cart'>" +
					"<input type='hidden' name='name' value='"+entry.getValue().getName()+"'>"+
					"<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>"+
					"<input type='hidden' name='maker' value='"+entry.getValue().getRetailer()+"'>"+
					"<input type='hidden' name='price' value='"+entry.getValue().getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
		pw.print("</form></li><li>");
		pw.print("<form action='WriteReview' method='post'><input type='submit' class='btnreview' value='WriteReview'>");
		pw.print("<input type='hidden' name='name' value='"+entry.getValue().getId()+"'>");
		pw.print("<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>");
		pw.print("<input type='hidden' name='maker' value='"+entry.getValue().getRetailer()+"'>");
		pw.print("<input type='hidden' name='price' value='"+entry.getValue().getPrice()+"'>");
		pw.print("</form></li>");
		pw.print("<li>");
		pw.print("<form action='ViewReview' method='post'><input type='submit' class='btnreview' value='ViewReview'>");
		pw.print("<input type='hidden' name='name' value='"+entry.getValue().getId()+"'>");
		pw.print("<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>");
		pw.print("</form></li></ul></div></td>");
		}
		pw.print("</tr></table>");
		}
		pw.print("</div></div></div>");
		
	}
}
