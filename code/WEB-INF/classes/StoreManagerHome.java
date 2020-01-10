import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@WebServlet("/StoreManagerHome")
public class StoreManagerHome extends HttpServlet {

    private String error_msg;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        displayStoreManagerHome(request, response, pw, "");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        //Add New product
        Map<String, Object> map = new HashMap<String, Object>(); //保存表单提交的数据(新建product)

        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        String catalog;
        try {
            List<FileItem> parseRequest = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : parseRequest) {
                boolean formField = fileItem.isFormField();
                if (formField) {
                    //普通表单项
                    String fieldName = fileItem.getFieldName();
                    String fieldValue = fileItem.getString();
                    map.put(fieldName, fieldValue);
                } else {
                    //图片上传项，获得文件名称和内容

                    catalog = String.valueOf(map.get("productCatalog"));
                    String realPath = utility.getRealPath(catalog);

                    String fileName = fileItem.getName();
                    String path = this.getServletContext().getRealPath(realPath);
                    InputStream inputStream = fileItem.getInputStream();
                    OutputStream outputStream = new FileOutputStream(path + "/" + fileName);
                    IOUtils.copy(inputStream, outputStream);
                    inputStream.close();
                    outputStream.close();
                    fileItem.delete();

                    map.put("image", fileName);
                }
            }
                        if (utility.storeNewProduct(map) && AjaxUtility.storeData(map)) {
                //添加成功
                error_msg = "Completed!";
                displayStoreManagerHome(request, response, pw, "newProduct");
            } else {
                //添加失败
                error_msg = "Cannot add new product!";
                displayStoreManagerHome(request, response, pw, "newProduct");
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }


    private void displayStoreManagerHome(HttpServletRequest request,
                                         HttpServletResponse response, PrintWriter pw, String flag)  //error: true代表有错误，false代表没有错误
    {

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Create New product");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        if (flag.equals("newProduct"))
            pw.print("<h4 style='color:red'>" + error_msg + "</h4>");
        //显示创建product的表格
        pw.print("<form action='StoreManagerHome' method='post' enctype='multipart/form-data'>");
        pw.print("<table style='width:100%'><tr><td>");

        pw.print("<h4>Product ID</h4></td><td><input type='text' name='id' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Product Name</h4></td><td><input type='text' name='name' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Product Catalog</h4><td><select name='productCatalog' class='input'>" +
                "<option value='FitnessWatch' selected>Fitness watch</option>" +
                "<option value='SmartWatch'>Smart watch</option>" +
                "<option value='HeadPhone'>Headphone</option>" +
                "<option value='WirelessPlan'>WirelessPlan</option>" +
                "<option value='SoundSystem'>SoundSystem</option>" +
                "<option value='Phone'>Phone</option>" +
                "<option value='Laptop'>Laptop</option>" +
                "<option value='VoiceAssistant'>Voice assistant</option>" +
                "<option value='Accessory'>Accessory</option></select>");
        pw.print("</td></tr></td><tr><td>");


        pw.print("<h4>Price</h4></td><td><input type='text' name='price' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");
        pw.print("<h4>Manufacturer</h4></td><td><input type='text' name='manufacturer' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Condition</h4><td><select name='condition' class='input'>" +
                "<option value='New' selected>New</option>" +
                "<option value='Used'>Used</option>" +
                "<option value='Refurbished'>Refurbished</option></select>");
        pw.print("</td></tr></td><tr><td>");

        pw.print("<h4>Discount</h4></td><td><input type='text' name='discount' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");


        pw.print("<h4>Image</h4></td><td><img id=\"preview\" /><br/><input type='file' name='image' class='input' required></input>");
        pw.print("</td></tr><tr><td>");


        pw.print("<input type='submit' class='btnbuy' value='Create' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>");
        pw.print("</td></tr><tr><td></td><td>");
        pw.print("</td></tr></table>");
        pw.print("</form></div></div>");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //显示product的详细信息

        pw.print("<div class='post'>");
        // pw.print("<form method='post' action='RemoveUpdateProduct'>");
        pw.print("<h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>View Products</a></h2>");
        pw.print("<div class='entry'>");
        pw.print("<table class='gridtable'>");

        if (flag.equals("RemoveUpdateProduct"))
            pw.print("<h4 style='color:red'>" + error_msg + "</h4>");


        //按钮显示
//        pw.print("<div align='left' style='float:left'>");
//        pw.print("<input type='submit' name='Product' value='Update Product' class='btnbuy'>");
//        pw.print("</div>");
//        pw.print("<div align='right'>");
//        pw.print("<input type='submit' name='Product' value='Remove Product' class='btnbuy'>");
//        pw.print("</div>");
//        pw.print("<br>");


        //表头
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("<td>Price</td>");
        pw.print("<td>Manufacturer</td>");
        pw.print("<td>Condition</td>");
        pw.print("<td>Discount</td>");
        pw.print("<td>Catalog</td>");
        pw.print("</tr>");

        //内容  //fitnessWatch
        for (Map.Entry<String, FitnessWatch> entry : SaxParserDataStore.fitnessWatchHashmap.entrySet()) {
            FitnessWatch fitnessWatch = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
//            pw.print("<td><input type='radio' name='productId' value='" + fitnessWatch.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + fitnessWatch.getName() + "</td>" +
                    "<td>" + fitnessWatch.getPrice() + "</td>" +
                    "<td>" + fitnessWatch.getRetailer() + "</td>" +
                    "<td>" + fitnessWatch.getCondition() + "</td>" +
                    "<td>" + fitnessWatch.getDiscount() + "</td>" +
                    "<td>Fitness Watch</td>");

            pw.print("<input type='hidden' name='productId' value='" + fitnessWatch.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + fitnessWatch.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + fitnessWatch.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + fitnessWatch.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + fitnessWatch.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + fitnessWatch.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Fitness Watch'>");
            pw.print("<input type='hidden' name='image' value='" + fitnessWatch.getImage() + "'>");
            pw.print("</tr>");

            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
            //pw.print("<br>");
        }

        //内容  //smartWatch
        for (Map.Entry<String, SmartWatch> entry : SaxParserDataStore.smartWatchHashmap.entrySet()) {
            SmartWatch smartWatch = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            //pw.print("<td><input type='radio' name='productId' value='" + smartWatch.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + smartWatch.getName() + "</td>" +
                    "<td>" + smartWatch.getPrice() + "</td>" +
                    "<td>" + smartWatch.getRetailer() + "</td>" +
                    "<td>" + smartWatch.getCondition() + "</td>" +
                    "<td>" + smartWatch.getDiscount() + "</td>" +
                    "<td>Smart Watch</td>");
            pw.print("<input type='hidden' name='productId' value='" + smartWatch.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + smartWatch.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + smartWatch.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + smartWatch.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + smartWatch.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + smartWatch.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Smart Watch'>");
            pw.print("<input type='hidden' name='image' value='" + smartWatch.getImage() + "'>");
            pw.print("</tr>");

            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Headphone
        for (Map.Entry<String, HeadPhone> entry : SaxParserDataStore.headPhoneHashmap.entrySet()) {
            HeadPhone headPhone = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            //pw.print("<td><input type='radio' name='productId' value='" + headphone.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + headPhone.getName() + "</td>" +
                    "<td>" + headPhone.getPrice() + "</td>" +
                    "<td>" + headPhone.getRetailer() + "</td>" +
                    "<td>" + headPhone.getCondition() + "</td>" +
                    "<td>" + headPhone.getDiscount() + "</td>" +
                    "<td>Headphone</td>");
            pw.print("<input type='hidden' name='productId' value='" + headPhone.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + headPhone.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + headPhone.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + headPhone.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + headPhone.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + headPhone.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Headphone'>");
            pw.print("<input type='hidden' name='image' value='" + headPhone.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Virtual Reality
        for (Map.Entry<String, SoundSystem> entry : SaxParserDataStore.soundSystemHashmap.entrySet()) {
            SoundSystem soundSystem = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            // pw.print("<td><input type='radio' name='productId' value='" + virtualReality.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + soundSystem.getName() + "</td>" +
                    "<td>" + soundSystem.getPrice() + "</td>" +
                    "<td>" + soundSystem.getRetailer() + "</td>" +
                    "<td>" + soundSystem.getCondition() + "</td>" +
                    "<td>" + soundSystem.getDiscount() + "</td>" +
                    "<td>Virtual Reality</td>");
            pw.print("<input type='hidden' name='productId' value='" + soundSystem.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + soundSystem.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + soundSystem.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + soundSystem.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + soundSystem.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + soundSystem.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='SoundSystem'>");
            pw.print("<input type='hidden' name='image' value='" + soundSystem.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Pet Tracker
        for (Map.Entry<String, WirelessPlan> entry : SaxParserDataStore.wirelessPlanHashmap.entrySet()) {
            WirelessPlan wirelessPlan = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            // pw.print("<td><input type='radio' name='productId' value='" + petTracker.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + wirelessPlan.getName() + "</td>" +
                    "<td>" + wirelessPlan.getPrice() + "</td>" +
                    "<td>" + wirelessPlan.getRetailer() + "</td>" +
                    "<td>" + wirelessPlan.getCondition() + "</td>" +
                    "<td>" + wirelessPlan.getDiscount() + "</td>" +
                    "<td>Pet Tracker</td>");
            pw.print("<input type='hidden' name='productId' value='" + wirelessPlan.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + wirelessPlan.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + wirelessPlan.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + wirelessPlan.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + wirelessPlan.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + wirelessPlan.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='WirelessPlan'>");
            pw.print("<input type='hidden' name='image' value='" + wirelessPlan.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Phone
        for (Map.Entry<String, Phone> entry : SaxParserDataStore.phoneHashmap.entrySet()) {
            Phone phone = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            // pw.print("<td><input type='radio' name='productId' value='" + phone.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + phone.getName() + "</td>" +
                    "<td>" + phone.getPrice() + "</td>" +
                    "<td>" + phone.getRetailer() + "</td>" +
                    "<td>" + phone.getCondition() + "</td>" +
                    "<td>" + phone.getDiscount() + "</td>" +
                    "<td>Phone</td>");
            pw.print("<input type='hidden' name='productId' value='" + phone.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + phone.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + phone.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + phone.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + phone.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + phone.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Phone'>");
            pw.print("<input type='hidden' name='image' value='" + phone.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Laptop
        for (Map.Entry<String, Laptop> entry : SaxParserDataStore.laptopHashmap.entrySet()) {
            Laptop laptop = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            //pw.print("<td><input type='radio' name='productId' value='" + laptop.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + laptop.getName() + "</td>" +
                    "<td>" + laptop.getPrice() + "</td>" +
                    "<td>" + laptop.getRetailer() + "</td>" +
                    "<td>" + laptop.getCondition() + "</td>" +
                    "<td>" + laptop.getDiscount() + "</td>" +
                    "<td>Laptop</td>");
            pw.print("<input type='hidden' name='productId' value='" + laptop.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + laptop.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + laptop.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + laptop.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + laptop.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + laptop.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Laptop'>");
            pw.print("<input type='hidden' name='image' value='" + laptop.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Voice Assistant
        for (Map.Entry<String, VoiceAssistant> entry : SaxParserDataStore.voiceAssistantHashmap.entrySet()) {
            VoiceAssistant voiceAssistant = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            pw.print("<tr>");
            // pw.print("<td><input type='radio' name='productId' value='" + voiceAssistant.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + voiceAssistant.getName() + "</td>" +
                    "<td>" + voiceAssistant.getPrice() + "</td>" +
                    "<td>" + voiceAssistant.getRetailer() + "</td>" +
                    "<td>" + voiceAssistant.getCondition() + "</td>" +
                    "<td>" + voiceAssistant.getDiscount() + "</td>" +
                    "<td>Voice Assistant</td>");
            pw.print("<input type='hidden' name='productId' value='" + voiceAssistant.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + voiceAssistant.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + voiceAssistant.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + voiceAssistant.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + voiceAssistant.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + voiceAssistant.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Voice Assistant'>");
            pw.print("<input type='hidden' name='image' value='" + voiceAssistant.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        //内容  //Accessory
        for (Map.Entry<String, Accessory> entry : SaxParserDataStore.accessories.entrySet()) {
            Accessory accessory = entry.getValue();
            pw.print("<form method='post' action='RemoveUpdateProduct'>");
            if (accessory.getName() == null || accessory.getName().isEmpty()) {
                continue;
            }
            pw.print("<tr>");
            // pw.print("<td><input type='radio' name='productId' value='" + accessory.getId() + "'></td>");  //修改为商品ID

            pw.print("<td>" + accessory.getName() + "</td>" +
                    "<td>" + accessory.getPrice() + "</td>" +
                    "<td>" + accessory.getRetailer() + "</td>" +
                    "<td>" + accessory.getCondition() + "</td>" +
                    "<td>" + accessory.getDiscount() + "</td>" +
                    "<td>Accessory</td>");
            pw.print("<input type='hidden' name='productId' value='" + accessory.getId() + "'>");
            pw.print("<input type='hidden' name='productName' value='" + accessory.getName() + "'>");
            pw.print("<input type='hidden' name='price' value='" + accessory.getPrice() + "'>");
            pw.print("<input type='hidden' name='manufacturer' value='" + accessory.getRetailer() + "'>");
            pw.print("<input type='hidden' name='condition' value='" + accessory.getCondition() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + accessory.getDiscount() + "'>");
            pw.print("<input type='hidden' name='catalog' value='Accessory'>");
            pw.print("<input type='hidden' name='image' value='" + accessory.getImage() + "'>");
            pw.print("</tr>");
            pw.print("<tr>");
            //pw.print("<td></td>");
            pw.print("<td><div align=\"left\" style=\"float:left\"><input type='submit' name='Product' value='Update' class='btnbuy'></div>");
            pw.print("<div align=\"right\"><input type='submit' name='Product' value='Remove' class='btnbuy'></div></td>");
            pw.print("</tr>");
            pw.print("</form>");
        }

        pw.print("</table>");
        pw.print("</div></div></div>");
    }
}
