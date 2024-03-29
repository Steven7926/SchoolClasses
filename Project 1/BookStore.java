/* Name: Steven Hudson
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 26, 2020
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class BookStore extends JFrame
{
    HashMap<String, String> inventory = new HashMap <String, String>();     // Stores the inventory
    ArrayList<String> order = new ArrayList <String>();                     // Stores the entire confirmed order of an item
    ArrayList<String> view = new ArrayList <String>();                      // Stores the information for view order button
    ArrayList<Float> subs = new ArrayList <Float>();                        // Stores the subtotal as a float for calculations and total order subtotal display
    File file = new File("inventory.txt");
    int orderNum = 1;
    int subNum = 0;

    public static void addInventory(File file, HashMap<String, String> inventory)   // Reads in the inventory file into the HashMap as Strings
    {
        try
        {
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                String bookId = line.substring(0, line.indexOf(","));
                String bookTitle = line.substring(
                                        line.indexOf("\""), line.indexOf("\"", line.indexOf("\"") + 1) + 1
                                    );
                Float bookPrice = Float.parseFloat(line.substring(line.indexOf("\"", line.indexOf("\"") + 1) + 2));
                String priceAsStr = String.valueOf(bookPrice);
                inventory.put(bookId, bookTitle + ", " + priceAsStr);
            }
            scan.close();
        }
        catch(Exception a)
        {
            System.out.println("ERROR: THere was a problem reading in the inventory file to the HashMap.");
        }
    }

    public static String getDateLog()       // Gets the date in the format DDMMYYYYHHmm for use when writing to Transaction Log
    {
        Date current = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("DDMMYYYYHHmm");
        String date = dateFormat.format(current);
        return date;
    }

    public static String getDatewTime()     // Gets the Date, Time, and TimeZone for display and Transaction Log
    {
        Date currentOne = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/YY, ");
        String date = dateFormat.format(currentOne);

        Date currentTwo = new Date();
        SimpleDateFormat dateFormatTwo = new SimpleDateFormat("hh:mm:ss aa z");      
        String dateTwo = dateFormatTwo.format(currentTwo);

        if (dateTwo.indexOf("0") == 0)      // When hour is 01, 02, 03....09, remove the leading 0
            dateTwo = dateTwo.substring(1);

        String theBigOne = date + dateTwo;
        return theBigOne;
    }

    public static void sendToTransactionLog(ArrayList<String> order)        // Write to Transaction Log
    {
        try
        {
            FileWriter writer = new FileWriter("transactions.txt", true);   // Boolean true ensures the file is not overwritten
            for (int i = 0; i < order.size(); i++)
                writer.write(order.get(i) + "\n");
            writer.close();
        }
        catch (Exception a)
        {
            System.out.println("ERROR: There was a problem writing to the Transactions Log.");
        }

    }

    public BookStore()
    {
        super("Old School iPads");
      
        
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        setLayout(layout);

        JLabel numItems = new JLabel("Enter the number of items for this order: ");
        JTextField numField = new JTextField("", 25);
        JLabel bookId = new JLabel("Enter the Book ID for item #" + orderNum + ": ");
        JTextField idField = new JTextField("", 31);
        JLabel itemQuantity = new JLabel("Enter the quantity for item #" + orderNum + ": ");
        JTextField quantityField = new JTextField("", 31);
        JLabel itemInfo = new JLabel("Item #" + orderNum + " info: ");
        JTextField itemInfoField = new JTextField("", 41);
        JLabel orderSub = new JLabel("Order subtotal for " + subNum + " item(s): ");
        JTextField orderSubField = new JTextField("", 32);
        JButton process = new JButton("Process item #" + orderNum);
        JButton confirm = new JButton("Confirm item #" + orderNum);
        JButton viewOrder = new JButton("View Order");
        JButton finish = new JButton("Finish Order");
        JButton newOrder = new JButton("New Order");
        JButton exit = new JButton("Exit");

        numItems.setForeground(Color.WHITE);
        numField.setForeground(Color.BLACK);
        bookId.setForeground(Color.WHITE);
        idField.setForeground(Color.BLACK);
        itemQuantity.setForeground(Color.WHITE);
        quantityField.setForeground(Color.BLACK);
        itemInfo.setForeground(Color.WHITE);
        itemInfoField.setForeground(Color.BLACK);
        orderSub.setForeground(Color.WHITE);
        orderSubField.setForeground(Color.BLACK);
        process.setBackground(Color.green);
        process.setPreferredSize(new Dimension (162, 20));
        confirm.setBackground(Color.green);
        confirm.setPreferredSize(new Dimension(162, 20));
        viewOrder.setBackground(Color.green);
        viewOrder.setPreferredSize(new Dimension (162, 20));
        finish.setBackground(Color.green);
        finish.setPreferredSize(new Dimension (162, 20));
        newOrder.setBackground(Color.green);
        newOrder.setPreferredSize(new Dimension(162,20));
        exit.setBackground(Color.green);
        exit.setPreferredSize(new Dimension (162, 20));
        itemInfoField.setEditable(false);
        orderSubField.setEditable(false);

        add(numItems);   
        add(numField);   
        add(bookId);     
        add(idField);    
        add(itemQuantity);   
        add(quantityField);  
        add(itemInfo);      
        add(itemInfoField);  
        add(orderSub); 
        add(orderSubField);
        add(process);
        add(confirm);
        add(viewOrder);
        add(finish);
        add(newOrder);
        add(exit);

        confirm.setEnabled(false);
        viewOrder.setEnabled(false);
        finish.setEnabled(false);


        process.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            { 
                addInventory(file, inventory);
                if (!(numField.getText()).isBlank() && !(idField.getText()).isBlank() && !(quantityField.getText()).isBlank())
                {
                    int quantity = Integer.parseInt(quantityField.getText());   // Grab the quantity of the book as an integer
                    String booksId = idField.getText();                         // Get the booksId as a string for HashMap lookup
                    Float discount;
                    numField.setEditable(false);
                    if (quantity <= 4)                                          // Discount dependent on the qunatity of the book being purchased
                        discount = 0f;                                          // 1-4 = 0%, 5-9 = 10%, 10-14 = 15%
                    else if (quantity > 4 && quantity < 10)
                        discount = 0.1f;
                    else if (quantity > 9 && quantity < 15)
                        discount = 0.15f;
                    else 
                        discount = 0f;

                    if (inventory.containsKey(booksId))                         // Ensure the book is in the inventory file using the HashMap
                    {
                        String bookName = inventory.get(booksId);                                            // Get the books name from the HashMap inventory
                        Float totalPriceDiscount = (Float.parseFloat(                                       // Get the total price of one of the books as a float
                                                        (bookName.substring(                               //i.e (bookprice * quantity) - ((bookprice * quantity) * discount)
                                                            bookName.lastIndexOf(", ") + 2))) * quantity)
                                                             - ((Float.parseFloat((bookName.substring(
                                                                    bookName.lastIndexOf(", ") + 2)))
                                                                     * quantity) * discount
                                                    );

                        itemInfoField.setText(                                                          // Set the Book Info Field
                                                booksId + " " + inventory.get(booksId) + " " + quantity 
                                                + " " + String.format("%.0f",(discount)*100) + "%" + " " 
                                                + "$" + String.format("%.2f", totalPriceDiscount)
                                            );

                        orderSubField.setText(                                                         // Set the Order Subtotal Field
                                                "$" + String.valueOf(Float.parseFloat(
                                                                    String.format("%.2f", totalPriceDiscount)
                                                                    ))
                                            );

                        itemInfo.setText("Item #" + orderNum + " info: ");
                        confirm.setEnabled(true);
                        process.setEnabled(false);

                        String confirmedOrder = (                                                   // Gets the entirity of the data needed for the Transaction Log
                                                    getDateLog() + ", " + booksId + ", " + inventory.get(booksId) +
                                                     ", " + quantity + ", " + String.format("%.0f",(discount)*100) + 
                                                     "%" + ", " + "$" + String.format("%.2f", totalPriceDiscount) + 
                                                     ", " + getDatewTime()
                                                );

                        order.add(confirmedOrder);                                              // Add transaction log data to the order ArrayList to use later for transactions
                        subs.add(totalPriceDiscount);                                           // Add subtotal to subtotal ArrayList for later calculations
                        view.add(                                                               // Add data needed for view button to the view ArrayList
                                    booksId + " " + inventory.get(booksId) + " " + quantity + 
                                    " " + String.format("%.0f",(discount)*100) + "%" + " " 
                                    + "$" + String.format("%.2f", totalPriceDiscount)
                                );
                    }
                    else 
                        JOptionPane.showMessageDialog(null, "Book ID " + booksId + " is not in our inventory.", "Invalid Book Id", JOptionPane.ERROR_MESSAGE);
                }            
            }
        });

        confirm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            { 
                JOptionPane.showMessageDialog(null, "Item #" + orderNum + " accepted", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                orderNum += 1;
                subNum += 1;
                Float subTot = 0f;
                if (orderNum > Integer.parseInt(numField.getText()))        // If we've reached the number of items in the order
                {
                    confirm.setEnabled(false);
                    process.setEnabled(false);
                    viewOrder.setEnabled(true);
                    finish.setEnabled(true);

                    for (int i = 0; i < subs.size(); i++)
                        subTot += subs.get(i);

                    orderSub.setText("Order subtotal for " + subNum + " item(s): ");
                    orderSubField.setText("$" + String.format("%.2f", subTot));
                    idField.setEnabled(false);
                    quantityField.setEnabled(false);
                }
                else
                {
                    process.setEnabled(true);
                    viewOrder.setEnabled(true);
                    confirm.setEnabled(false);
                    finish.setEnabled(true);
                    idField.setText("");
                    quantityField.setText("");
                    process.setText("Process item #" + orderNum);
                    confirm.setText("Confirm item #" + orderNum);
                    bookId.setText("Enter the Book ID for item #" + orderNum + ": ");
                    itemQuantity.setText("Enter the quantity for item #" + orderNum + ": ");

                    for (int i = 0; i < subs.size(); i++)
                        subTot += subs.get(i);

                    orderSub.setText("Order subtotal for " + subNum + " item(s): ");
                    orderSubField.setText("$" + String.format("%.2f", subTot));
                }
            }
        });

        viewOrder.addActionListener(new ActionListener(){       // Grab necessary data from the view ArrayList
            @Override
            public void actionPerformed(ActionEvent e)
            { 
                String viewed = "";

                for (int i = 0; i < order.size(); i++)
                {
                    String iteminfo = (i + 1) + ". " + view.get(i);
                    viewed += iteminfo + "\n";
                }
                JOptionPane.showMessageDialog(null, viewed, "Current Order" , JOptionPane.INFORMATION_MESSAGE);
            }
        });

        finish.addActionListener(new ActionListener(){        // Grab necessary data from the subtotal ArrayList
            @Override
            public void actionPerformed(ActionEvent e)
            { 
               String date = "Date: " + getDatewTime() + "\n\n";
               String numLine = "Number of line items: " + numField.getText() + "\n\n";
               String fullOrder = "Item#/ID/Title/Price/Qty/Disc %/Subtotal: \n\n";
               String thankYou = "Thank you for shopping at Old School iPads!";
               String tax = "\nTax Rate:     6%\n\n";
               String orderSubStr = "";
               Float orderSub = 0f;
               Float taxRate = 0.06f;
               Float taxAmt;
               for (int i = 0; i < order.size(); i++)
                {
                    String iteminfo = (i + 1) + ". " + view.get(i);
                    orderSub += subs.get(i); 
                    fullOrder += iteminfo + "\n";
                }
                orderSubStr = "Order Subtotal:  $" + String.format("%.2f", orderSub) + "\n\n";
                taxAmt = orderSub * taxRate;
                String taxAmount = "Tax Amount:     $" + String.format("%.2f", taxAmt) + "\n\n";
                Float orderTotal = orderSub + taxAmt;
                String totalStr = "Order Total:     $" + String.format("%.2f", orderTotal) + "\n\n";
                String allTogether = date + numLine + fullOrder + tax + orderSubStr + taxAmount + totalStr + thankYou;

                JOptionPane.showMessageDialog(null, allTogether, "Finished Order" , JOptionPane.INFORMATION_MESSAGE);
                sendToTransactionLog(order);
                finish.setEnabled(false);
            }
        });

        newOrder.addActionListener(new ActionListener(){    // Restart the program for a new order
            @Override
            public void actionPerformed(ActionEvent e)
            { 
                try
                {
                    Runtime.getRuntime().exec("java BookStoreDriver");
                    System.exit(0);
                }
                catch(Exception a)
                {
                    System.out.println("ERROR: There was a problem restarting the program.");
                }
            }
        });

        exit.addActionListener(new ActionListener(){        // Exit the program
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }

        });
    }

}