# Accounting Ledger

I built this project using Java to record deposits and payments and keep track of transaction history. It runs in the terminal and saves the transactions in a CSV file, so the information is still there when the program starts again.

The user can view all transactions, show only deposits or payments, and search through the records using the Reports menu.

## Requirements

- Java JDK 18
- IntelliJ IDEA

## How to Run

1. Install Java JDK 18 and IntelliJ IDEA.
2. Download and extract the project from GitHub.
3. Open the folder containing `pom.xml` in IntelliJ and let Maven load.
4. Select JDK 18 as the Project SDK.
5. Make sure `src/main/resources/transactions.csv` exists.
6. Run the class containing the `main` method. The home menu will appear in the terminal.

The working directory should be the main project folder so the program can find the transaction file.

## Using the Program

### Home Menu

Enter the letter for the option you want:

| Option | Action |
| --- | --- |
| D | Add a deposit |
| P | Make a payment |
| L | Open the ledger |
| X | Select the exit option |

When adding a deposit or payment, the program asks for a description, amount, and vendor. It adds the current date and time and saves the transaction in the file.

### Ledger

The ledger menu lets the user view all transactions, deposits only, or payments only. The user can also open the Reports menu or choose Home.

### Reports

The Reports menu includes:

- **Month To Date:** Shows transactions starting from the first day of the month the user enters.
- **Previous Month:** Shows transactions from the previous calendar month.
- **Year To Date:** Shows transactions starting from January 1 of the year the user enters.
- **Search by Vendor:** Shows transactions matching the entered vendor name.
- **Previous Year:** Shows transactions from the previous calendar year.
- **Custom Search:** Searches using the fields the user fills in.

### Custom Search

The user can search by start date, end date, description, vendor, and amount. They can leave any field empty to skip that filter.

For example, entering a vendor and an amount searches for transactions matching both values. Leaving every field empty displays all transactions.

Dates must use `yyyy-MM-dd`, such as `2026-11-05`.

## Transaction File

The program stores its data in:

`src/main/resources/transactions.csv`

The first line is the header:

```text
date|time|description|vendor|amount
```

Each line after the header is one transaction:

```text
2026-11-05|10:30:00|Paycheck|Employer|1500.00
2026-11-05|11:15:00|Groceries|Store|-65.00
```

Deposits are positive amounts, and payments are negative amounts. New transactions are added to the file without removing the previous records.

## Code I Am Proud Of

The custom search is the part I want to highlight:

```java
boolean comparingCondtion =
        (startDate.isEmpty() || listDateConversion.isAfter(startDateConversion)) &&
        (description.isEmpty() || userInfoList[2].contains(description)) &&
        (vendor.isEmpty() || userInfoList[3].trim().equalsIgnoreCase(vendor)) &&
        (amount.isEmpty() || Double.parseDouble(userInfoList[4].trim()) == Double.parseDouble(amount)) &&
        (endDate.isEmpty() || listDateConversion.isBefore(endDateConversion));
```

At first, I thought I needed a separate condition for every combination of fields the user could enter. That would have made the method much longer.

Instead, I used `isEmpty()` with `||` to skip fields that were left empty. Then I connected the conditions with `&&`, so all the fields the user filled in must match the transaction.

## Sorting Transactions

I also added a method that reads the transactions into an `ArrayList`, sorts them, and writes them back to the same file.

```java
transactions.sort(Collections.reverseOrder());
```

Because each transaction starts with a date in `yyyy-MM-dd` format, reverse sorting puts the newest dates first. The header is kept separate and written back at the top of the file.

## Challenges and What I Learned

One challenge was converting the custom search input. An empty string cannot be converted to a date or number, so I needed to check whether the user entered a value before converting it.

I also worked with `Scanner`. After reading an amount with `nextDouble()`, I used `nextLine()` to read the leftover newline before asking for the next input.

This project helped me practice reading and writing files, working with dates, using an `ArrayList`, and combining conditions to filter records.

## Project Development

I added custom search to the Reports menu and worked on letting the user skip fields. I trimmed the input and checked for empty values before converting dates and amounts.

Later, I added the sorting method so other parts of the program could read transactions in the order saved in the file.

## Current Limitations

- Menu input needs more validation to handle empty input and invalid numbers.
- Custom search currently excludes transactions exactly on the start and end dates.
- Description matching is case sensitive. Vendor matching ignores capitalization.
- Month To Date and Year To Date currently check the starting date but do not exclude future transactions.
- The time format needs to use `HH:mm:ss` for correct sorting within the same day.
- The exit option and navigation between menus need improvement.


