import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LaptopList")

public class LaptopList extends HttpServlet {

	/* Trending Page Displays all the Tablets and their Information in Game Speed */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

	/* Checks the Tablets type whether it is microsft or apple or samsung */

		String name = null;
		String CategoryName = request.getParameter("maker");
		HashMap<String, Laptop> hm = new HashMap<String, Laptop>();

		if (CategoryName == null)	
		{
			hm.putAll(SaxParserDataStore.laptopHashmap);
			name = "";
		} 
		else 
		{
			if(CategoryName.equals("Apple")) 
			{	
				for(Map.Entry<String,Laptop> entry : SaxParserDataStore.laptopHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Apple"))
				  {
					 hm.put(entry.getValue().getId(),entry.getValue());
				  }
				}
				name ="Apple";
			} 
			else if (CategoryName.equals("Lenovo"))
			{
				for(Map.Entry<String,Laptop> entry : SaxParserDataStore.laptopHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Lenovo"))
				  {
					 hm.put(entry.getValue().getId(),entry.getValue());
				  }
				}
				name = "Lenovo";
			} 
			else if (CategoryName.equals("Samsung")) 
			{
				for(Map.Entry<String,Laptop> entry : SaxParserDataStore.laptopHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Samsung"))
				 {
					 hm.put(entry.getValue().getId(),entry.getValue());
				 }
				}	
				name = "Samsung";
			}
	    }

		/* Header, Left Navigation Bar are Printed.

		All the tablets and tablet information are dispalyed in the Content Section

		and then Footer is Printed*/

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>" + name + " Laptops</a>");
		pw.print("</h2><div class='entry'><table id='bestseller'>");
		int i = 1;
		int size = hm.size();
		for (Map.Entry<String, Laptop> entry : hm.entrySet()) {
			Laptop laptop = entry.getValue();
			if (i % 3 == 1)
				pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>" + laptop.getName() + "</h3>");
			pw.print("<strong>" + laptop.getPrice() + "$</strong><ul>");
			pw.print("<li id='item'><img src='images/Laptops/"
					+ laptop.getImage() + "' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" +
					"<input type='hidden' name='name' value='"+laptop.getName()+"'>"+
					"<input type='hidden' name='type' value='laptop'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+laptop.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
			pw.print("<li><form method='post' action='WriteReview'>"+"<input type='hidden' name='name' value='"+laptop.getName()+"'>"+
					"<input type='hidden' name='type' value='laptops'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+laptop.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
				    "<input type='submit' value='WriteReview' class='btnreview'></form></li>");
			pw.print("<li><form method='post' action='ViewReview'>"+"<input type='hidden' name='name' value='"+laptop.getName()+"'>"+
					"<input type='hidden' name='type' value='laptops'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+laptop.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
				    "<input type='submit' value='ViewReview' class='btnreview'></form></li>");
			pw.print("</ul></div></td>");
			if (i % 3 == 0 || i == size)
				pw.print("</tr>");
			i++;
		}
		pw.print("</table></div></div></div>");
		utility.printHtml("Footer.html");
	}
}
