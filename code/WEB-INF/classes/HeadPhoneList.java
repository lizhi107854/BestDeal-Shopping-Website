import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HeadPhoneList")

public class HeadPhoneList extends HttpServlet {

	/* Trending Page Displays all the Tablets and their Information in Game Speed */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

	/* Checks the Tablets type whether it is microsft or apple or samsung */

		String name = null;
		String CategoryName = request.getParameter("maker");
		HashMap<String, HeadPhone> hm = new HashMap<String, HeadPhone>();

		if (CategoryName == null)	
		{
			hm.putAll(SaxParserDataStore.headPhoneHashmap);
			name = "";
		} 
		else 
		{
			if(CategoryName.equals("Beats")) 
			{	
				for(Map.Entry<String,HeadPhone> entry : SaxParserDataStore.headPhoneHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Beats"))
				  {
					 hm.put(entry.getValue().getId(),entry.getValue());
				  }
				}
				name ="Beats";
			} 
			else if (CategoryName.equals("Bose"))
			{
				for(Map.Entry<String,HeadPhone> entry : SaxParserDataStore.headPhoneHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Bose"))
				  {
					 hm.put(entry.getValue().getId(),entry.getValue());
				  }
				}
				name = "Bose";
			} 
			else if (CategoryName.equals("Sony")) 
			{
				for(Map.Entry<String,HeadPhone> entry : SaxParserDataStore.headPhoneHashmap.entrySet())
				{
				  if(entry.getValue().getRetailer().equals("Sony"))
				 {
					 hm.put(entry.getValue().getId(),entry.getValue());
				 }
				}	
				name = "Sony";
			}
	    }

		/* Header, Left Navigation Bar are Printed.

		All the tablets and tablet information are dispalyed in the Content Section

		and then Footer is Printed*/

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>" + name + " HeadPhones</a>");
		pw.print("</h2><div class='entry'><table id='bestseller'>");
		int i = 1;
		int size = hm.size();
		for (Map.Entry<String, HeadPhone> entry : hm.entrySet()) {
			HeadPhone headPhone = entry.getValue();
			if (i % 3 == 1)
				pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>" + headPhone.getName() + "</h3>");
			pw.print("<strong>" + headPhone.getPrice() + "$</strong><ul>");
			pw.print("<li id='item'><img src='images/HeadPhones/"
					+ headPhone.getImage() + "' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" +
					"<input type='hidden' name='name' value='"+headPhone.getName()+"'>"+
					"<input type='hidden' name='type' value='HeadPhone'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+headPhone.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
			pw.print("<li><form method='post' action='WriteReview'>"+"<input type='hidden' name='name' value='"+headPhone.getName()+"'>"+
					"<input type='hidden' name='type' value='HeadPhones'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+headPhone.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
				    "<input type='submit' value='WriteReview' class='btnreview'></form></li>");
			pw.print("<li><form method='post' action='ViewReview'>"+"<input type='hidden' name='name' value='"+headPhone.getName()+"'>"+
					"<input type='hidden' name='type' value='HeadPhones'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+headPhone.getPrice()+"'>"+
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
