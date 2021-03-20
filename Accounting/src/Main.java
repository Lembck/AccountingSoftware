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
  double sum(ArrayList<AnAccount> accounts) {
    double tempAmount = 0.0;
    for (AnAccount acc : accounts) {
      tempAmount += acc.amount;
    }
    return tempAmount;
  }

  double sum(Account acc) {
    return acc.amount;
  }

  String getString(AnAccount acc, String prefix) {
    return acc.getString(prefix);
  }

  public String getString(String string, double amount) {
    return string + new String(new char[60-string.length()-Double.toString(amount).length()]).replace("\0", "-") + amount;
  }
}

interface IAccount {

}

abstract class AnAccount {
  String title;
  double amount;
  boolean isDebit;

  AnAccount(String title, double amount, boolean isDebit) {
    this.title = title;
    this.amount = amount;
    this.isDebit = isDebit;
  }

  abstract String getString(String prefix);

  AnAccount(String title, boolean isDebit) {
    this.title = title;
    this.amount = 0;
    this.isDebit = isDebit;
  }

  AnAccount(String title) {
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

class Account extends AnAccount {

  Account(String title) {
    super(title);
  }

  Account(String title, boolean isDebit) {
    super(title, isDebit);
  }

  Account(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
  }

  String getString(String prefix) {
    return new ArrayListAccountUtils().getString(prefix + this.title, this.amount);
  }
}

class SummaryAccount extends AnAccount {
  ArrayList<AnAccount> subAccounts;

  SummaryAccount(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
    this.subAccounts = new ArrayList<AnAccount>();
  }

  SummaryAccount(String title, double amount, boolean isDebit, ArrayList<AnAccount> subAccounts) {
    super(title, amount, isDebit);
    this.subAccounts = subAccounts;
  }

  SummaryAccount(String title, boolean isDebit, ArrayList<AnAccount> subAccounts) {
    super(title, isDebit);
    this.subAccounts = subAccounts;
  }

  SummaryAccount(String title, ArrayList<AnAccount> subAccounts) {
    super(title);
    this.subAccounts = subAccounts;
  }

  SummaryAccount(String title, boolean isDebit) {
    super(title, isDebit);
    this.subAccounts = new ArrayList<AnAccount>();
  }

  SummaryAccount(String title) {
    super(title);
    this.subAccounts = new ArrayList<AnAccount>();
  }

  void checkAmount() {
    this.amount = new ArrayListAccountUtils().sum(this.subAccounts);
  }

  void addSubAccount(Account acc) {
    this.subAccounts.add(acc);
    this.checkAmount();
  }

  String getString(String prefix) {
    String temp = new ArrayListAccountUtils().getString(prefix + this.title, this.amount);
    for (AnAccount a : this.subAccounts) {
      temp += "\n" + new ArrayListAccountUtils().getString(a, prefix + "  ");
    }
    return temp;
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
  ArrayList<AnAccount> assets;
  ArrayList<AnAccount> liabilities;
  ArrayList<AnAccount> stockholdersEquity;
  Date date;
  String companyName;
  String units;

  BalanceSheet(ArrayList<AnAccount> assets, ArrayList<AnAccount> liabilities, ArrayList<AnAccount> stockholdersEquity) {
    this.assets = assets;
    this.liabilities = liabilities;
    this.stockholdersEquity = stockholdersEquity;
  }

  BalanceSheet() {
    this.assets = new ArrayList<AnAccount>(List.of(
        new Account("Cash"), 
        new SummaryAccount("Accounts Receivable"),
        new SummaryAccount("Inventory"),
        new SummaryAccount("Property, Plant, and Equipment")));
    this.liabilities = new ArrayList<AnAccount>(List.of(
        new Account("Accounts Payable", false),
        new SummaryAccount("Notes Payable", false, new ArrayList<AnAccount>(List.of(
            new Account("Short-term Notes Payable", false), 
            new Account("Long-term Notes Payable", false))))));
    this.stockholdersEquity = new ArrayList<AnAccount>(List.of(
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
      ArrayList<AnAccount> assets, ArrayList<AnAccount> liabilities, ArrayList<AnAccount> stockholdersEquity) {
    this(assets, liabilities, stockholdersEquity);
    this.companyName = companyName;
    this.date = date;
    this.units = units;
  }

  void print() {
    String header = this.companyName + "\n" + "Balance Sheet" + "\n" + "At " 
        + date.month + "/" + date.day + "/" + date.year + "\n" + "(in " + this.units + " dollars)";
    System.out.println(header);

    System.out.println("Assets:");

    for (AnAccount acc : assets) {
      System.out.println(new ArrayListAccountUtils().getString(acc, "  "));
    }

    System.out.println(new ArrayListAccountUtils().getString("Total Assets", new ArrayListAccountUtils().sum(assets)));

    System.out.println("Liabilities:");

    for (AnAccount acc : liabilities) {
      System.out.println(new ArrayListAccountUtils().getString(acc, "  "));
    }
    double liabilitiesCost = new ArrayListAccountUtils().sum(liabilities);
    System.out.println(new ArrayListAccountUtils().getString("Total Liabilities", liabilitiesCost));

    System.out.println("Stockholder's Equity:");

    for (AnAccount acc : stockholdersEquity) {
      System.out.println(new ArrayListAccountUtils().getString(acc, "  "));
    }

    System.out.println(new ArrayListAccountUtils().getString("Total Stockholder's Equity", new ArrayListAccountUtils().sum(stockholdersEquity)));

    System.out.println(new ArrayListAccountUtils().getString("Total Liabilities and Stockholder's Equity", new ArrayListAccountUtils().sum(stockholdersEquity)));
  }
}

class IncomeStatement {
  ArrayList<AnAccount> revenues;
  ArrayList<AnAccount> expenses;

  IncomeStatement(ArrayList<AnAccount> revenues, ArrayList<AnAccount> expenses) {
    this.revenues = revenues;
    this.expenses = expenses;
  }

  IncomeStatement() {
    this.revenues = new ArrayList<AnAccount>(List.of(
        new Account("Sales Revenue")));
    this.expenses = new ArrayList<AnAccount>(List.of(
        new Account("Cost of Goods Sold", false),
        new SummaryAccount("Selling, General, and Administrative Expenses", false, new ArrayList<AnAccount>()),
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

