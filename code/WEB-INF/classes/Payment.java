import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@WebServlet("/Payment")

public class Payment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();


        Utilities utility = new Utilities(request, pw);
        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to Pay");
            response.sendRedirect("Login");
            return;
        }
        // get the payment details like credit card no address processed from cart servlet

        String userAddress = request.getParameter("userAddress");
        String creditCardNo = request.getParameter("creditCardNo");
        if (!userAddress.isEmpty() && !creditCardNo.isEmpty()) {
            SimpleDateFormat df = new SimpleDateFormat("HHmmss");//设置日期格式
            int orderId = Integer.parseInt(df.format(new Date()));  //设置订单号为当前下单时间的时分秒

            for (OrderItem oi : utility.getCustomerOrders()) {

                //set the parameter for each column and execute the prepared statement
                utility.storePayment(orderId, oi.getName(), oi.getPrice(), userAddress, creditCardNo);
            }

            //remove the order details from cart after processing

            OrdersHashMap.orders.remove(utility.username());
            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Order</a>");
            pw.print("</h2><div class='entry'>");

            pw.print("<h2>Your Order");
            pw.print("&nbsp&nbsp");
            pw.print("is stored ");
            pw.print("<br>Your Order No is " + (orderId));


            //获得delivery date
            SimpleDateFormat deliveryDateFormat = new SimpleDateFormat("MM-dd");//设置日期格式
            String today = deliveryDateFormat.format(new Date());
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(deliveryDateFormat.parse(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 14);  // number of days to add
            today = deliveryDateFormat.format(c.getTime());

//            String dt = "2008-01-01";  // Start date
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar c = Calendar.getInstance();
//            c.setTime(sdf.parse(dt));
//            c.add(Calendar.DATE, 1);  // number of days to add
//            dt = sdf.format(c.getTime());  // dt is now the new date


            pw.print("<br>Estimated delivery date: " + today);
            pw.print("</h2></div></div></div>");
            utility.printHtml("Footer.html");
        } else {
            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Order</a>");
            pw.print("</h2><div class='entry'>");

            pw.print("<h4 style='color:red'>Please enter valid address and credit card number</h4>");
            pw.print("</h2></div></div></div>");
            utility.printHtml("Footer.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
    }
}
