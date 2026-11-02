package com.plurasight;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

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
    String reportsPrompt = "####### Reports #######\nPlease enter number for the section you need OR H.\n" +
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
                    if (userInfoList[0].equals(userId)) return userId;
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

        userInput = userInput.toUpperCase();
        char userChoice = userInput.charAt(0);
        switch (userChoice){
            case 'D' -> addDeposit();
            case 'P' -> makePayment();
            case 'L' -> ledger(scanner);
            case 'X' -> closingMethod();
            default -> System.out.println("Invalid input.\nPlease try again.");
        }
    }












}
