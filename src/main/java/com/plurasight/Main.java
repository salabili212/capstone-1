package com.plurasight;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

import static com.plurasight.Transactions.homeScreen;
import static com.plurasight.Transactions.start;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userId = start();
        System.out.println(userId);

        if(userId != null) {
            //homeScreen(userId,scanner);
            reports(userId,scanner);
        }




    }



}
