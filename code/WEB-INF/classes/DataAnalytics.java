import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.*;

import javax.servlet.http.HttpSession;

@WebServlet("/DataAnalytics")

public class DataAnalytics extends HttpServlet {
    static DBCollection myReviews;
    /* Trending Page Displays all the Consoles and their Information in Game Speed*/

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);


        //check if the user is logged in
        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to View Reviews");
            response.sendRedirect("Login");
            return;
        }


        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Data Analytics on Review</a>");
        pw.print("</h2><div class='entry'>");
        pw.print("<table id='bestseller'>");
        pw.print("<form method='post' action='FindReviews'>");

        pw.print("<table id='bestseller'>");
        pw.print("<tr>");
        pw.print("<td> <input type='checkbox' name='queryCheckBox' value='productName'> Select </td>");
        pw.print("<td> Product Name: </td>");
        pw.print("<td>");
        pw.print("<select name='productName'>");
        pw.print("<option value='ALL_PRODUCTS'>All Products</option>");
        pw.print("<option value='Sony-X800G'>Sony-X800G</option>");
        pw.print("<option value='Sony-X950G'>Sony-X950G</option>");
        pw.print("<option value='Sony-A8G'>Sony-A8G</option>");
        pw.print(" <option value='Sony-A9G'>Sony-A9G</option>");
        pw.print("<option value='LG-C9PUA'>LG-C9PUA</option>");

        pw.print("<option value='LG-UM7300PUA'>LG-UM7300PUA</option>");
        pw.print("<option value='LG-UM6910PUC'>LG-UM6910PUC</option>");
        pw.print("<option value='Samsung-NU6900'>Samsung-NU6900</option>");
        pw.print("<option value='Samsung-Q70'>Samsung-Q70</option>");
        pw.print("<option value='Samsung-samrt-4K'>Samsung-samrt-4K</option>");
        pw.print("<option value='Samsung-Q80'>Samsung-Q80</option>");
        pw.print("<option value='Bose-Bass-Module-700'>Bose-Bass-Module-700</option>");
        pw.print("<option value='Bose-Soundlink-Color'>Bose-Soundlink-Color</option>");
        pw.print("<option value='Bose-Soundlink-Revove'>Bose-Soundlink-Revove</option>");
        pw.print("<option value='Bose-SoundTouch'>Bose-SoundTouch</option>");
        pw.print("<option value='JBL-Charge4'>JBL-Charge4</option>");
        pw.print("<option value='JBL-BoomBox'>JBL-BoomBox</option>");
        pw.print("<option value='JBL-PartyBox'>JBL-PartyBox</option>");
        pw.print("<option value='BeatBox'>BeatBox</option>");
        pw.print("<option value='Beats-pill'>Beats-pill</option>");
        pw.print("<option value='iphone11-pro'>iphone11-pro</option>");
        pw.print("<option value='iphoneXS'>iphoneXS</option>");
        pw.print("<option value='iphone8'>iphone8</option>");
        pw.print("<option value='Google-Pixel-3'>Google-Pixel-3</option>");
        pw.print("<option value='Google-Pixel-3-XL'>Google-Pixel-3-XL</option>");
        pw.print("<option value='Google-Pixel-3a'>Google-Pixel-3a</option>");
        pw.print("<option value='Samsung-Galaxy-S10'>Samsung-Galaxy-S10</option>");
        pw.print("<option value='Samsung-Galaxy-S9'>Samsung-Galaxy-S9</option>");
        pw.print("<option value='Samsung-Galaxy-Note10+'>Samsung-Galaxy-Note10+</option>");
        pw.print("<option value='Samsung-Galaxy-S10e'>Samsung-Galaxy-S10e</option>");
        pw.print("<option value='Macbook-Ai'>Macbook-Ai</option>");
        pw.print("<option value='Macbook-pro'>Macbook-pro</option>");
        pw.print("<option value='Macbook-Retina'>Macbook-Retina</option>");
        pw.print("<option value='Lenovo-Yoga-C630'>Lenovo-Yoga-C630</option>");
        pw.print("<option value='Lenovo-Yoga-730'>Lenovo-Yoga-730</option>");
        pw.print("<option value='Lenovo-IdeaPad'>Lenovo-IdeaPad</option>");
        pw.print("<option value='Lenovo-S340'>Lenovo-S340</option>");
        pw.print("<option value='Samsung-Notebook7'>Samsung-Notebook7</option>");
        pw.print("<option value='Samsung-Notebook9pro'>Samsung-Notebook9pro</option>");
        pw.print("<option value='Samsung-chromebook'>Samsung-chromebook</option>");
        pw.print("<option value='Echo-Show5'>Echo-Show5</option>");
        pw.print("<option value='Echo-SmartSpeaker'>Echo-SmartSpeaker</option>");
        pw.print("<option value='Echo-Dot'>Echo-Dot</option>");
        pw.print("<option value='Google-Home'>Google-Home</option>");
        pw.print("<option value='Google-HomeMini'>Google-HomeMini</option>");
        pw.print("<option value='Google-Home-Speaker300'>Google-Home-Speaker300</option>");
        pw.print("<option value='AppleWatch-Nike-Series5'>AppleWatch-Nike-Series5</option>");
        pw.print("<option value='AppleWatch-Nike-Series4'>AppleWatch-Nike-Series4</option>");
        pw.print("<option value='AppleWatch-Nike-Series3'>AppleWatch-Nike-Series3</option>");
        pw.print("<option value='Huawei-Terra-B19'>Huawei-Terra-B19</option>");
        pw.print("<option value='Huawei-Bans3-pro'>Huawei-Bans3-pro</option>");
        pw.print("<option value='Huawei-GT-Sport'>Huawei-GT-Sport</option>");
        pw.print("<option value='Samsung-Galaxy-Active2'>Samsung-Galaxy-Active2</option>");
        pw.print("<option value='Samsung-Galaxy-Active'>Samsung-Galaxy-Active</option>");
        pw.print("<option value='Samsung-Fit2'>Samsung-Fit2</option>");
        pw.print("<option value='AppleWatch-Series5'>AppleWatch-Series5</option>");
        pw.print("<option value='AppleWatch-Series4'>AppleWatch-Series4</option>");
        pw.print("<option value='AppleWatch-Series3'>AppleWatch-Series3</option>");
        pw.print("<option value='Huawei-SmartWatch'>Huawei-SmartWatch</option>");
        pw.print("<option value='Huawei-Watch2'>Huawei-Watch2</option>");
        pw.print("<option value='Garmin-Forerunner235'>Garmin-Forerunner235</option>");
        pw.print("<option value='Garmin-6X'>Garmin-6X</option>");
        pw.print("<option value='Garmin-fenix'>Garmin-fenix</option>");
        pw.print("<option value='Beats-solo3'>Beats-solo3</option>");
        pw.print("<option value='Beats-studio'>Beats-studio</option>");
        pw.print("<option value='Beats-Powerbeats'>Beats-Powerbeats</option>");
        pw.print("<option value='Beats-Hearphones'>Beats-Hearphones</option>");
        pw.print("<option value='Beats-Noise-Canceling'>Beats-Noise-Canceling</option>");
        pw.print("<option value='Beats-SoundLink-Wireless'>Beats-SoundLink-Wireless</option>");
        pw.print("<option value='Sony-WH1000XM3'>Sony-WH1000XM3</option>");
        pw.print("<option value='Sony-XB900N'>Sony-XB900N</option>");
        pw.print("<option value='Sony-WH-RF400'>Sony-WH-RF400</option>");
        pw.print("<option value='BasicPlan'>Basic Plan</option>");
        pw.print("<option value='PremiumPlan'>Premium Plan</option>");
        pw.print("<option value='UltimatePlan'>Ultimate Plan</option>");





        pw.print("</td>");
        pw.print("<tr>");
        pw.print("<td> <input type='checkbox' name='queryCheckBox' value='productPrice'> Select </td>");
        pw.print("<td> Product Price: </td>");
        pw.print(" <td>");
        pw.print("  <input type='number' name='productPrice' value = '0' size=10  /> </td>");
        pw.print("<td>");
        pw.print("<input type='radio' name='comparePrice' value='EQUALS_TO' checked> Equals <br>");
        pw.print("<input type='radio' name='comparePrice' value='GREATER_THAN'> Greater Than <br>");
        pw.print("<input type='radio' name='comparePrice' value='LESS_THAN'> Less Than");
        pw.print("</td></tr>");


        pw.print("<tr><td> <input type='checkbox' name='queryCheckBox' value='reviewRating'> Select </td>");
        pw.print(" <td> Review Rating: </td>");
        pw.print(" <td>");
        pw.print(" <select name='reviewRating'>");
        pw.print(" <option value='1' selected>1</option>");
        pw.print(" <option value='2'>2</option>");
        pw.print(" <option value='3'>3</option>");
        pw.print("   <option value='4'>4</option>");
        pw.print("  <option value='5'>5</option>");
        pw.print("</td>");
        pw.print("<td>");
        pw.print("<input type='radio' name='compareRating' value='EQUALS_TO' checked> Equals <br>");
        pw.print("<input type='radio' name='compareRating' value='GREATER_THAN'> Greater Than");
        pw.print("</td></tr>");

        pw.print("<tr>");
        pw.print("<td> <input type='checkbox' name='queryCheckBox' value='retailerCity'> Select </td>");
        pw.print("<td> Retailer City: </td>");
        pw.print("<td>");
        pw.print("<input type='text' name='retailerCity' /> </td>");

        pw.print("</tr>");

        pw.print("<tr>");
        pw.print("<td> <input type='checkbox' name='queryCheckBox' value='retailerZipcode'> Select </td>");
        pw.print(" <td> Retailer Zip code: </td>");
        pw.print(" <td>");
        pw.print("<input type='text' name='retailerZipcode' /> </td>");
        pw.print("</tr>");
        pw.print("<tr><td>");
        pw.print("<input type='checkbox' name='extraSettings' value='GROUP_BY'> Group By");
        pw.print("</td>");
        pw.print("<td>");
        pw.print("<select name='groupByDropdown'>");
        pw.print("<option value='GROUP_BY_CITY' selected>City</option>");
        pw.print("<option value='GROUP_BY_PRODUCT'>Product Name</option>");
        pw.print("</td><td>");
        pw.print("<input type='radio' name='dataGroupBy' value='Count' checked> Count <br>");
        pw.print("<input type='radio' name='dataGroupBy' value='Detail'> Detail <br>");
        pw.print("</td></tr>");


        pw.print("<tr>");
        pw.print("<td colspan = '4'> <input type='submit' value='Find Data' class='btnbuy' /> </td>");
        pw.print("</tr>");


        pw.print("</table>");
        pw.print("</div></div></div>");
        utility.printHtml("Footer.html");


    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

    }

}
