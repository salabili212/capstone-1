package com.plurasight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
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
            case 'D' -> addDeposit();
            case 'P' -> makePayment();
            case 'L' -> ledger(scanner,userId);
            case 'X' -> closingMethod();
            default -> System.out.println("Invalid input.\nPlease try again.");
        }
    }

    public static void makePayment(){

    }

    public static void addDeposit(){

    }

    public static void ledger(Scanner scanner,String userId){
        while(true) {
            String ledgerPrompt = "####### Ledger #######\nA)All\nD)Deposit\nP)Payments\nR)Reports\nH)Home";
            System.out.println(ledgerPrompt);
            String userInput = scanner.nextLine();
            userInput = userInput.toUpperCase();
            char userChoice = userInput.charAt(0);
            switch (userChoice) {
                case 'A' -> showAllEntries();//show all the transactions in the account
                case 'D' -> peposit();//Display only the entries that are deposits into the account
                case 'P' -> payment();// Display only the negative entries (or payments)
                case 'R' ->
                        reports(userId, scanner);//A new screen that allows the user to run pre-defined reports or to run a custom search
                case 'H' -> homeScreen(userId, scanner);//back the main page
                default -> System.out.println("Invalid input.\nPlease try again.");
            }
        }

    }

    public static void closingMethod(){
        start();
    }

    public static void reports(String userId, Scanner scanner){
        while(true) {
            //Prompt for the Reports page
            String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR 0.\n" +
                    "1)Month To Date\n2)Previous Month\n3)Year To Date\n4)Previous Year\n5)Search by Vendor\n0)Back\n";
            System.out.println(reportsPrompt);
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> monthToDate(userId,scanner);//show all transaction for all months to the update months
                case 2 -> previousMonth(userId,scanner);//show all the transaction for the prvious month
                case 3 -> yearTODate(userId,scanner);//show all transactions from userI
//                case 4 -> searchByVendor(userId,scanner);//prompt the user for the vendor name and display all entries for that vendor
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
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions"));
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
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions"));
            String userInfo = bufreader.readLine();
            while(userInfo != null){
                userInfo = bufreader.readLine();
                if (userInfo == null) break;//Breaks when userInfo is null to prevent String error
                LocalDate fileDate = LocalDate.parse(userInfo);
                boolean checkDate = (date.getMonthValue() == fileDate.getMonthValue()) && (date.getYear() == fileDate.getYear());
                if (checkDate){
                    System.out.println(userInfo);
                }
            }
            bufreader.close();//after all reading the
            reports(userId,scanner);
        }catch (Exception e) {
            System.out.println("Can not found the file.\nyearToDate()'s method in transaction class\n" + e);
        }
    }

    //This method will read all the transaction from the provide date to today date
    //It depends on the year input
    public static void yearTODate(String userId,Scanner scanner){
        System.out.println("Transaction will display from year date you enter to today date.\nEnter year in numbers exp:2024");
        String yearDate = scanner.nextLine();
        LocalDate userYearDate= LocalDate.of(Integer.parseInt(yearDate.trim()), 1, 1);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader("src/main/resources/transactions"));
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


    public static void searchByVendor(){

    }















}
