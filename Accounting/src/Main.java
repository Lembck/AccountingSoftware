import java.util.ArrayList;
import java.util.List;
import tester.Tester;

class Date {
  int year;
  int month;
  int day;
  
  Date(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }
}

class ArrayListAccountUtils {
  double sum(ArrayList<Account> accounts) {
    double tempAmount = 0.0;
    for (Account acc : accounts) {
      tempAmount += acc.amount;
    }
    return tempAmount;
  }
}

class Account {
  String title;
  double amount;
  boolean isDebit;


  Account(String title, double amount, boolean isDebit) {
    this.title = title;
    this.amount = amount;
    this.isDebit = isDebit;
  }
  
  Account(String title, boolean isDebit) {
    this.title = title;
    this.amount = 0;
    this.isDebit = isDebit;
  }
  
  Account(String title) {
    this.title = title;
    this.amount = 0;
    this.isDebit = true;
  }

  void debit(double change) {
    if (isDebit) {
      this.amount += change;
    } else {
      this.amount -= change;
    }
  }

  void credit(double change) {
    if (isDebit) {
      this.amount -= change;
    } else {
      this.amount += change;
    }
  }
}

class SummaryAccount extends Account {
  ArrayList<Account> subAccounts;

  SummaryAccount(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
    this.subAccounts = new ArrayList<Account>();
  }
  
  SummaryAccount(String title, double amount, boolean isDebit, ArrayList<Account> subAccounts) {
    super(title, amount, isDebit);
    this.subAccounts = subAccounts;
  }
  
  SummaryAccount(String title, boolean isDebit, ArrayList<Account> subAccounts) {
    super(title, isDebit);
    this.subAccounts = subAccounts;
  }
  
  SummaryAccount(String title, ArrayList<Account> subAccounts) {
    super(title);
    this.subAccounts = subAccounts;
  }
  
  SummaryAccount(String title, boolean isDebit) {
    super(title, isDebit);
    this.subAccounts = new ArrayList<Account>();
  }
  
  SummaryAccount(String title) {
    super(title);
    this.subAccounts = new ArrayList<Account>();
  }
  
  void checkAmount() {
    this.amount = new ArrayListAccountUtils().sum(this.subAccounts);
  }
  
  void addSubAccount(Account acc) {
    this.subAccounts.add(acc);
    this.checkAmount();
  }
  
}

class Transaction {
  Account account;
  double amount;
  boolean isDebit;
  
  Transaction(Account account, double amount, boolean isDebit) {
    this.account = account;
    this.amount = amount;
    this.isDebit = isDebit;
  }
}

class JournalEntry {
  ArrayList<Transaction> transactions;
  
  JournalEntry(ArrayList<Transaction> transactions) {
    this.transactions = transactions;
  }
  
  JournalEntry() {
    this.transactions = new ArrayList<Transaction>();
  }
}

class BalanceSheet {
  ArrayList<Account> assets;
  ArrayList<Account> liabilities;
  ArrayList<Account> stockholdersEquity;
  Date date;
  String companyName;
  String units;

  BalanceSheet(ArrayList<Account> assets, ArrayList<Account> liabilities, ArrayList<Account> stockholdersEquity) {
    this.assets = assets;
    this.liabilities = liabilities;
    this.stockholdersEquity = stockholdersEquity;
  }

  BalanceSheet() {
    this.assets = new ArrayList<Account>(List.of(
        new Account("Cash"), 
        new SummaryAccount("Accounts Receivable"),
        new SummaryAccount("Inventory"),
        new SummaryAccount("Property, Plant, and Equipment")));
    this.liabilities = new ArrayList<Account>(List.of(
        new Account("Accounts Payable", false),
        new SummaryAccount("Notes Payable", false, new ArrayList<Account>(List.of(
            new Account("Short-term Notes Payable", false), 
            new Account("Long-term Notes Payable", false))))));
    this.stockholdersEquity = new ArrayList<Account>(List.of(
        new Account("Common stock", false),
        new Account("Retained Earnings", false)));
  }
  
  BalanceSheet(String companyName, Date date, String units) {
    this();
    this.companyName = companyName;
    this.date = date;
    this.units = units;
  }
  
  BalanceSheet(String companyName, Date date, String units, 
      ArrayList<Account> assets, ArrayList<Account> liabilities, ArrayList<Account> stockholdersEquity) {
    this(assets, liabilities, stockholdersEquity);
    this.companyName = companyName;
    this.date = date;
    this.units = units;
  }
  
  void print() {
    System.out.println(this.companyName);
    System.out.println("Balance Sheet");
    System.out.println("At " + date.month + "/" + date.day + "/" + date.year);
    System.out.println("(in" + this.units + " dollars)");
    
    System.out.println("Assets:");
    
    for (Account acc : assets) {
      System.out.println(acc.title + new String(new char[40-acc.title.length()]).replace("\0", "-") + acc.amount);
    }
    
  }
}

class IncomeStatement {
  ArrayList<Account> revenues;
  ArrayList<Account> expenses;
  
  IncomeStatement(ArrayList<Account> revenues, ArrayList<Account> expenses) {
    this.revenues = revenues;
    this.expenses = expenses;
  }
  
  IncomeStatement() {
    this.revenues = new ArrayList<Account>(List.of(
        new Account("Sales Revenue")));
    this.expenses = new ArrayList<Account>(List.of(
        new Account("Cost of Goods Sold", false),
        new SummaryAccount("Selling, General, and Administrative Expenses", false, new ArrayList<Account>()),
        new Account("Income Tax Expense")));
  }
}


class Company {
  String companyName;
  BalanceSheet balanceSheet;
  IncomeStatement incomeStatement;
  
  Company(String companyName) {
    this.balanceSheet = new BalanceSheet(companyName, new Date(2020, 12, 31), "millions of");
    this.incomeStatement = new IncomeStatement();
  }
  
}

class Examples {
  Examples() {}
  
  void testCompany(Tester t) {
    Company x = new Company("Tesla");
    x.balanceSheet.print();
  }
}

