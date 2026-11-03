package com.plurasight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Date;
import java.util.regex.Pattern;

public class Transactions {
    private String userId;
    private String userName;
    private String depositHistory;
    private String paymentHistory;
    private double balance;

    //Prompt for the Ledger page
    String ledgerPrompt = "####### Ledger #######\n" +
            "A)All\nD)Deposit\nP)Payments\nR)Reports\nH)Home";
    //Prompt for the Reports page
    String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR 0.\n" +
            "1)Month To Date\n2)Previous Month\n3)Year To Date\n4)Previous Year\n5)Search by Vendor\n0)Back\n";


    //Start method that check if the user has an account so can get accesses to his account
    public static String start(){
        int stop = 999;
        while(stop != 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter your ID.\nID:\nEnter 0 to Exit\n");
            String userId = scanner.nextLine();
            if (userId.equals("0")){stop = 0; break;};
            try {
                //Reading info from a file
                BufferedReader buffer = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
                String userInfo = buffer.readLine();
                while (userInfo != null) {
                    userInfo = buffer.readLine();
                    if (userInfo == null) break;
                    String[] userInfoList = userInfo.split("\\|");
                    if ("123".equals(userId)) return userId;
                }
                if (userId == null) System.out.println("ID not valid in the system.\nTry to enter a valid ID:)\n");
                buffer.close();
            } catch (Exception e) {
                System.out.println("have Problem with reading the file in start method in main\n" + e);
            }
        }
        return null;
    }


    public static void homeScreen(String userId, Scanner scanner){
        String homePrompt = "####### Home #######\n" +
                "Please enter a character for the section want to choose\n" +
                "D)Add Deposit\nP)Make Payment (Debit)\nL)Ledger\nX)Exit";

        //print out the mnue for the user to choose a section
        System.out.println(homePrompt);
        String userInput = scanner.nextLine();
        userInput = userInput.toUpperCase().trim();
        char userChoice = userInput.charAt(0);

        switch (userChoice){
            case 'D' -> addDeposit(scanner,"src/main/resources/transactions.csv");
            case 'P' -> makePayment(scanner,"src/main/resources/transactions.csv");
            case 'L' -> ledger(scanner,userId);
            case 'X' -> closingMethod();
            default -> System.out.println("Invalid input.\nPlease try again.");
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
        System.out.println("Vendor Name:");
        String vendor = scanner.nextLine();

        try {
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileTransaction,true));
            bufwriter.write(dateAndTime + "|" + description + "|" + vendor + "|-" + amount);
            bufwriter.newLine();
            bufwriter.close();
            System.out.println("****** Payment was successfully ******");
        }catch (Exception e){
            System.out.println("File not found.Payment() method" + e);
        }
    }

    public static void addDeposit(Scanner scanner, String fileTransaction){

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd|hh:mm:ss");
        LocalDateTime datetime = LocalDateTime.now();
        String dateAndTime = datetime.format(format);
        System.out.println("Deposit description:");
        String description = scanner.next();
        System.out.println("Deposit amount:");
        double amount = scanner.nextDouble();
        System.out.println("Vendor:");
        String vendor = scanner.next();

        try {
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileTransaction,true));
            bufwriter.write(dateAndTime + "|" + description + "|" + vendor + "|" + amount);
            bufwriter.newLine();
            bufwriter.close();
            homeScreen(userId,scanner);
        }catch (Exception e){
            System.out.println("File not found.Deposit() method" + e);
        }
    }

    public static void ledger(Scanner scanner,String userId){
        while(true) {
            String ledgerPrompt = "####### Ledger #######\nA)All\nD)Deposit\nP)Payments\nR)Reports\nH)Home";
            System.out.println(ledgerPrompt);
            String userInput = scanner.nextLine();
            userInput = userInput.toUpperCase();
            char userChoice = userInput.charAt(0);
            switch (userChoice) {
                case 'A' -> showAllEntries("src/main/resources/transactions.csv",userId);//show all the transactions in the account
                case 'D' -> deposit("src/main/resources/transactions.csv",userId);//Display only the entries that are deposits into the account
                case 'P' -> payment("src/main/resources/transactions.csv",userId);// Display only the negative entries (or payments)
                case 'R' -> reports(userId, scanner);//A new screen that allows the user to run pre-defined reports or to run a custom search
                case 'H' -> homeScreen(userId, scanner);//back the main page
                default -> System.out.println("Invalid input.\nPlease try again.");
            }
        }

    }

    public static void closingMethod(){
        start();
    }





    public static void deposit(String fileTransaction,String userId){
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
            ledger(new Scanner(System.in),userId);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }

    public static void payment(String fileTransaction,String userId){
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
                if (amount < 0){
                    System.out.println(userTransaction);
                }
            }
            bufreader.close();
            ledger(new Scanner(System.in),userId);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }


    public static void showAllEntries(String fileTransaction,String userId){
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
            ledger(new Scanner(System.in),userId);
        }catch (Exception e){
            System.out.println("Error in the showAllEntries() method  inside can not read the file.\n" + e);
        }
    }

    public static void reports(String userId, Scanner scanner){
        while(true) {
            //Prompt for the Reports page
            String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR 0.\n" +
                    "1)Month To Date\n2)Previous Month\n3)Year To Date\n4)Search by Vendor\n0)Back\n";
            System.out.println(reportsPrompt);
            String userChoice = scanner.nextLine();
            int userChoiceInt = Integer.parseInt(userChoice);
            switch (userChoiceInt) {
                case 1 -> monthToDate(userId,scanner);//show all transaction for all months to the update months
                case 2 -> previousMonth(userId,scanner);//show all the transaction for the prvious month
                case 3 -> yearTODate(userId,scanner);//show all transactions from userI
                case 4 -> searchByVendor(userId,scanner);//prompt the user for the vendor name and display all entries for that vendor
                case 0 -> ledger(scanner, userId);
                default -> System.out.println("Invalid number.\nTry again please.:)\n");
            }
        }
    }


    //This method will read all the transaction from the provide date to today date
    //User will give the date for the month, and it will show all transaction from that month to to today's date
    public static void monthToDate(String userId,Scanner scanner){
        System.out.println("Transaction will display from month date you enter to today date.\nEnter month date:year/month/day:");
        String monthDate = scanner.nextLine();
        LocalDate userMonthDate =  LocalDate.parse(monthDate.trim()).withDayOfMonth(1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
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
            bufreader.close();
            reports( userId,scanner );
        }catch ( Exception e ) {
            System.out.println("Can not found the file.\nMonthToDate()'s method in transaction class\n" + e);
        }
    }

    //This method show all the transaction for the previous month
    //user provide the month date, and it gives the transactions for the month before it
    public static void previousMonth(String userId,Scanner scanner){
        LocalDate date = LocalDate.now().minusMonths(1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
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
            reports(userId,scanner);
        }catch (Exception e) {
            System.out.println("Can not found the file.\npreviousMonth()'s method in transaction class\n" + e);
        }
    }

    //This method will read all the transaction from the provide date to today date
    //It depends on the year input
    public static void yearTODate(String userId,Scanner scanner){
        System.out.println("Transaction will display from year date you enter to today date.\nEnter year in numbers exp:2024");
        String yearDate = scanner.nextLine();
        LocalDate userYearDate= LocalDate.of(Integer.parseInt(yearDate.trim()), 1, 1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
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
            reports(userId,scanner);
        }catch (Exception e) {
            System.out.println("Can not found the file.\nyearToDate()'s method in transaction class\n" + e);
        }
    }


    public static void searchByVendor(String userId,Scanner scanner){
        System.out.println("Please Enter Vendor: ");
        String userVendor = scanner.nextLine().toLowerCase().trim();

        try {
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
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
            reports(userId,scanner);
        }catch (Exception e) {
            System.out.println("Can not found the file.\nsearchByVendor()'s method in transaction class\n" + e);
        }
    }















}
