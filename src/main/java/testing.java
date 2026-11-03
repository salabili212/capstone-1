import java.time.LocalDate;
import java.util.Scanner;

import java.util.regex.Pattern;

public class testing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the date integer ex: month11/days03/year2026\n Only numbers will get accept\n");
        String previousMonth = scanner.nextLine();
        LocalDate date = LocalDate.parse(previousMonth);
        date = date.withDayOfMonth(1);//This will rest the days of the month to show all transaction for the whole month


    }
}
