package com.plurasight;
//import all needed packages
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Date;
import java.util.regex.Pattern;

public class Transactions {
    //Prompt for the Ledger page
    String ledgerPrompt = "####### Ledger #######\n" +
            "A)All\nD)Deposit\nP)Payments\nR)Reports\nH)Home";
    //Prompt for the Reports page
    String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR 0.\n" +
            "1)Month To Date\n2)Previous Month\n3)Year To Date\n4)Previous Year\n5)Search by Vendor\n0)Back\n";


    //Start method that check if the user has an account so can get accesses to his account
    public static void start(int stop){
       if(stop == 0){
           System.out.println("Thank......:)");
       }else {
           homeScreen(new Scanner(System.in));
       }


    }



    public static void homeScreen(Scanner scanner){
        while(true) {
            String homePrompt = "####### Home #######\n" +
                    "Please enter a character for the section want to choose\n" +
                    "D)Add Deposit\nP)Make Payment (Debit)\nL)Ledger\nX)Exit";

            //print out the mnue for the user to choose a section
            System.out.println(homePrompt);
            String userInput = scanner.nextLine();
            userInput = userInput.toUpperCase().trim();
            char userChoice = userInput.charAt(0);
            String filePath = "src/main/resources/transactions.csv";

            switch (userChoice) {
                case 'D' -> addDeposit(scanner, filePath);
                case 'P' -> makePayment(scanner, filePath);
                case 'L' -> ledger(scanner, filePath);
                case 'X' -> closingMethod(0);
                default -> System.out.println("Invalid input.\nPlease try again.");
            }
        }
    }

    public static void makePayment(Scanner scanner,String fileTransaction){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd|hh:mm:ss");
        LocalDateTime datetime = LocalDateTime.now();
        String dateAndTime = datetime.format(format);
        System.out.println("Payment description:");
        String description = scanner.nextLine().trim();
        System.out.println("Payment amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // clean the left over
        System.out.println("Vendor Name:");
        String vendor = scanner.nextLine();

        try {
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileTransaction,true));
            bufwriter.write(dateAndTime + "|" + description + "|" + vendor + "|-" + amount);
            bufwriter.newLine();
            bufwriter.close();
            System.out.println("****** Payment was successfully ******");
            homeScreen(scanner);
        }catch (Exception e){
            System.out.println("File not found.Payment() method" + e);
        }
    }

    public static void addDeposit(Scanner scanner, String fileTransaction){

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd|hh:mm:ss");
        LocalDateTime datetime = LocalDateTime.now();
        String dateAndTime = datetime.format(format);
        System.out.println("Deposit description:");
        String description = scanner.nextLine();
        System.out.println("Deposit amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("****** Deposit was successfully ******");

        try {
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileTransaction,true));
            bufwriter.write(dateAndTime + "|" + description + "|" + vendor + "|" + amount);
            bufwriter.newLine();
            bufwriter.close();
            homeScreen(scanner);
        }catch (Exception e){
            System.out.println("File not found.Deposit() method" + e);
        }
    }

    public static void ledger(Scanner scanner,String filePath){
        while(true) {
            String ledgerPrompt = "####### Ledger #######\nA)All\nD)Deposit\nP)Payments\nR)Reports\nH)Home";
            System.out.println(ledgerPrompt);
            String userInput = scanner.nextLine();
            userInput = userInput.toUpperCase();
            char userChoice = userInput.charAt(0);
            switch (userChoice) {
                case 'A' -> showAllEntries(filePath);//show all the transactions in the account
                case 'D' -> deposit(filePath);//Display only the entries that are deposits into the account
                case 'P' -> payment(filePath);// Display only the negative entries (or payments)
                case 'R' -> reports(scanner,filePath);//A new screen that allows the user to run pre-defined reports or to run a custom search
                case 'H' -> homeScreen(scanner);//back the main page
                default -> System.out.println("Invalid input.\nPlease try again.");
            }
        }

    }

    public static void closingMethod(int zero){
        start(zero);
    }





    //this function show all the deposit history
    public static void deposit(String fileTransaction){
        //inside the fileTransaction the path for the file transaction should read from exp:"src/document"
        //Display only the entries that are deposits into the account
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userTransaction = bufreader.readLine();
            while(userTransaction != null){
                userTransaction = bufreader.readLine();
                if (userTransaction == null) break;
                String[] userTransactionList = userTransaction.trim().split("\\|");
                double amount = Double.parseDouble(userTransactionList[4]);
                if (amount > 0){
                    System.out.println(userTransaction);
                }
            }
            bufreader.close();
            ledger(new Scanner(System.in),fileTransaction);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }

    public static void payment(String fileTransaction){
        //inside the fileTransaction the path for the file where thr program read from exp:"src/document"
        //Display only the entries that are deposits into the account
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userTransaction = bufreader.readLine();
            while(userTransaction != null){
                userTransaction = bufreader.readLine();
                if (userTransaction == null) break;
                String[] userTransactionList = userTransaction.trim().split("\\|");
                double amount = Double.parseDouble(userTransactionList[4]);
                if (amount < 0){
                    System.out.println(userTransaction);
                }
            }
            bufreader.close();
            ledger(new Scanner(System.in),fileTransaction);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }


    public static void showAllEntries(String fileTransaction){
        //inside the fileTransaction the path for the file transaction should read from exp:"src/document"
        //Display only the entries that are deposits into the account
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userTransaction = bufreader.readLine();
            while(userTransaction != null){
                userTransaction = bufreader.readLine();
                if (userTransaction == null) break;
                System.out.println(userTransaction);
            }
            bufreader.close();
            ledger(new Scanner(System.in),fileTransaction);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }

    public static void reports(Scanner scanner,String fileTransaction){
        while(true) {
            //Prompt for the Reports page
            String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR 0.\n" +
                    "1)Month To Date\n2)Previous Month\n3)Year To Date\n4)Search by Vendor\n5)Previous Year\n6)Custom Search\n0)Back\n";
            System.out.println(reportsPrompt);
            String userChoice = scanner.nextLine();
            int userChoiceInt = Integer.parseInt(userChoice);
            switch (userChoiceInt) {
                case 1 -> monthToDate(scanner,fileTransaction);//show all transaction for all months to the update months
                case 2 -> previousMonth(scanner,fileTransaction);//show all the transaction for the prvious month
                case 3 -> yearTODate(scanner,fileTransaction);//show all transactions from userI
                case 4 -> searchByVendor(scanner,fileTransaction);//prompt the user for the vendor name and display all entries for that vendor
                case 5 -> previousYear(scanner,fileTransaction);
                case 6 -> customSearch(scanner,fileTransaction);
                case 0 -> ledger(scanner,fileTransaction);
                default -> System.out.println("Invalid number.\nTry again please.:)\n");
            }
        }
    }


    //This method will read all the transaction from the provide date to today date
    //User will give the date for the month, and it will show all transaction from that month to to today's date
    public static void monthToDate(Scanner scanner,String fileTransaction){
        DateTimeFormatter foramter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Transaction will display from month date you enter to today date.\nEnter month date:year-month-day:");
        String monthDate = scanner.nextLine();
        LocalDate userMonthDate =  LocalDate.parse(monthDate.trim(),foramter).withDayOfMonth(1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userInfo = bufreader.readLine();
            while(userInfo != null){
                userInfo = bufreader.readLine();
                if (userInfo == null) break;
                String[] userInfoList = ( userInfo.trim() ).split("\\|");
                LocalDate fileMonthDate = LocalDate.parse(userInfoList[0]);
                boolean checkDate = fileMonthDate.isBefore(userMonthDate);
                if (!checkDate){
                    System.out.println(userInfo);
                }

            }
            //will close the file after reading
            bufreader.close();
            reports(scanner,fileTransaction);// going back to the report method
        }catch ( Exception e ) {
            System.out.println("Can not found the file.\nMonthToDate()'s method in transaction class\n" + e);
        }
    }

    //This method show all the transaction for the previous month
    //user provide the month date, and it gives the transactions for the month before it
    public static void previousMonth(Scanner scanner,String fileTransaction){
        LocalDate date = LocalDate.now().minusMonths(1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userInfo = bufreader.readLine();
            while(userInfo != null){
                userInfo = bufreader.readLine();
                if (userInfo == null) break;//Breaks when userInfo is null to prevent String error
                String[] dateList = userInfo.trim().split("\\|");
                LocalDate fileDate = LocalDate.parse(dateList[0]);
                boolean checkDate = (date.getMonthValue() == fileDate.getMonthValue()) && (date.getYear() == fileDate.getYear());
                if (checkDate){
                    System.out.println(userInfo);
                }
            }
            bufreader.close();//after all reading the
            reports(scanner,fileTransaction);
        }catch (Exception e) {
            System.out.println("Can not found the file.\npreviousMonth()'s method in transaction class\n" + e);
        }
    }

    //This method will read all the transaction from the provide date to today date
    //It depends on the year input
    public static void yearTODate(Scanner scanner,String fileTransaction){
        System.out.println("Transaction will display from year date you enter to today date.\nEnter year in numbers exp:2024");
        String yearDate = scanner.nextLine();
        LocalDate userYearDate= LocalDate.of(Integer.parseInt(yearDate.trim()), 1, 1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userInfo = bufreader.readLine();
            while(userInfo != null){
                userInfo = bufreader.readLine();
                if (userInfo == null) break;//Breaks when userInfo is null to prevent String error
                String[] userInfoList = (userInfo.trim()).split("\\|");
                LocalDate fileYearDate = LocalDate.parse(userInfoList[0]);
                boolean checkDate = fileYearDate.isBefore(userYearDate);
                if (!checkDate){
                    System.out.println(userInfo);
                }
            }
            bufreader.close();//after all reading the
            reports(scanner,fileTransaction);
        }catch (Exception e) {
            System.out.println("Can not found the file.\nyearToDate()'s method in transaction class\n" + e);
        }
    }




    public static void searchByVendor(Scanner scanner,String fileTransaction){
        System.out.println("Please Enter Vendor: ");
        String userVendor = scanner.nextLine().toLowerCase().trim();

        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(fileTransaction));
            String userInfo = bufreader.readLine();
            while(userInfo != null){
                userInfo = bufreader.readLine();
                if (userInfo == null) break;//Breaks when userInfo is null to prevent String error
                String[] userInfoList = (userInfo.trim()).split("\\|");
                String fileVendor = userInfoList[3].trim().toLowerCase();
                boolean checkDate = userVendor.equalsIgnoreCase(fileVendor);
                if (checkDate){
                    System.out.println(userInfo);
                }
            }
            bufreader.close();//after all reading the
            reports(scanner,fileTransaction);//when finsh showing the detailes will call the report method again
        }catch (Exception e) {
            System.out.println("Can not found the file.\nsearchByVendor()'s method in transaction class\n" + e);
        }
    }


    public static void customSearch(Scanner scanner,String fileTransaction){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("******** Custom Search ********\nPlease fill out the custom's fields");
        System.out.println("Start Date:");
        String startDate = scanner.nextLine().trim();
        System.out.println("End Date:");
        String endDate = scanner.nextLine().trim();
        System.out.println("Description:");
        String description = scanner.nextLine().trim();
        System.out.println("Vendor:");
        String vendor = scanner.nextLine().trim();
        System.out.println("Amount:");
        String amount = scanner.nextLine().trim();

        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(fileTransaction));
            String userInfo = bufReader.readLine();
            while(userInfo != null){
                userInfo = bufReader.readLine();
                if (userInfo == null) break;
                String[] userInfoList = userInfo.split("\\|");
                LocalDate endDateConversion = null;
                LocalDate startDateConversion = null;
                if (!endDate.isEmpty()){
                    endDateConversion = LocalDate.parse(endDate,formatter);
                }
                if (!startDate.isEmpty()){
                    startDateConversion = LocalDate.parse(startDate,formatter);
                }
                LocalDate listDateConversion = LocalDate.parse(userInfoList[0],formatter);

                //This boolean expersion will check all the expect status whether the status are correct or empty
                boolean comparingCondtion = ((startDate.isEmpty() || listDateConversion.isAfter(startDateConversion)) &&
                        (description.isEmpty() || userInfoList[2].contains(description))
                        && (vendor.isEmpty() || userInfoList[3].trim().equalsIgnoreCase(vendor)) &&
                        (amount.isEmpty() || Double.parseDouble(userInfoList[4].trim()) == Double.parseDouble(amount)) &&
                        (endDate.isEmpty() || listDateConversion.isBefore(endDateConversion)));

                //will give an out if the comparingCondtion is true.
                if (comparingCondtion){
                    System.out.println(userInfo);
                }
            }
            bufReader.close();
            reports(scanner,fileTransaction);

        }catch (Exception e){
            System.out.println("customSearch() method:). \n" + e);
        }

    }














}
