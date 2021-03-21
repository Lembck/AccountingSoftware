import java.util.ArrayList;
import java.util.Hashtable;
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

class ALAUtils {
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

  public ArrayList<AnAccount> updateAccount(ArrayList<AnAccount> accounts, String title, double amount) {
    for (AnAccount a : accounts) {
      if (a.title == title) {
        a.amount += amount;
      }
    }
    return accounts;
  }

  public AnAccount getAccount(ArrayList<AnAccount> accounts, String title) {
    for (AnAccount a : accounts) {
      if (a.title == title) {
        return a;
      }
    }
    return null;
  }

  public void transact(Transaction transaction) {
    transaction.account.transact(transaction.amount);
  }

  public void transact(ArrayList<Transaction> transactions) {
    for (Transaction t : transactions) {
      this.transact(t);
    }
    
  }

  public boolean containsTitle(ArrayList<AnAccount> accounts, String title) {
    for (AnAccount a : accounts) {
      if (a.title == title) {
        return true;
      }
    }
    return false;
  }

  public void updateAccount(AnAccount acc, double amount) {
    
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

  void transact(double amount) {
    this.amount += amount;
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
    return new ALAUtils().getString(prefix + this.title, this.amount);
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
    this.amount = new ALAUtils().sum(this.subAccounts);
  }

  void addSubAccount(Account acc) {
    this.subAccounts.add(acc);
    this.checkAmount();
  }

  String getString(String prefix) {
    String temp = new ALAUtils().getString(prefix + this.title, this.amount);
    for (AnAccount a : this.subAccounts) {
      temp += "\n" + new ALAUtils().getString(a, prefix + "  ");
    }
    return temp;
  }

}

class HiddenAccount extends AnAccount {

  HiddenAccount(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
  }
  
  HiddenAccount(String title, boolean isDebit) {
    super(title, isDebit);
  }
  
  HiddenAccount(String title) {
    super(title);
  }

  String getString(String prefix) {
    return "";
  }
  
}

class Transaction {
  AnAccount account;
  double amount;
  boolean isDebit;

  Transaction(AnAccount account, double amount, boolean isDebit) {
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

interface Sheet {
  void updateAccount(String title, double amount);

  AnAccount getAccount(String title);
}

class BalanceSheet implements Sheet {
  ArrayList<AnAccount> assets;
  ArrayList<AnAccount> liabilities;
  ArrayList<AnAccount> stockholdersEquity;

  BalanceSheet(ArrayList<AnAccount> assets, ArrayList<AnAccount> liabilities, ArrayList<AnAccount> stockholdersEquity) {
    this.assets = assets;
    this.liabilities = liabilities;
    this.stockholdersEquity = stockholdersEquity;
  }

  BalanceSheet() {
    this.assets = new ArrayList<AnAccount>(List.of(
        new Account("Cash"), 
        new Account("Short-term investments"), 
        new SummaryAccount("Accounts Receivable"),
        new SummaryAccount("Notes Receivable"),
        new SummaryAccount("Inventory"),
        new Account("Supplies"),
        new Account("Prepaid Expenses"), 
        new Account("Long-Term Investments"),
        new SummaryAccount("Property, Plant, and Equipment", 
          new ArrayList<AnAccount>(List.of(
            new Account("Equipment"),
            new Account("Buildings"),
            new Account("Land")))),
        new Account("Intangibles")));
    this.liabilities = new ArrayList<AnAccount>(List.of(
        new Account("Accounts Payable", false),
        new Account("Accrued Expenses", false),
        new SummaryAccount("Notes Payable", false, new ArrayList<AnAccount>(List.of(
            new Account("Short-term Notes Payable", false), 
            new Account("Long-term Notes Payable", false)))),
        new Account("Taxes Payable", false),
        new Account("Unearned Revenues", false),
        new Account("Bonds Payable", false)));
    this.stockholdersEquity = new ArrayList<AnAccount>(List.of(
        new Account("Common stock", false),
        new Account("Retained Earnings", false),
        new Account("Additional Paid-in Capital", false)));
  }

  void print() {
    System.out.println("Assets:");

    for (AnAccount acc : assets) {
      System.out.println(new ALAUtils().getString(acc, "  "));
    }

    System.out.println(new ALAUtils().getString("Total Assets", new ALAUtils().sum(assets)));

    System.out.println("Liabilities:");

    for (AnAccount acc : liabilities) {
      System.out.println(new ALAUtils().getString(acc, "  "));
    }
    double liabilitiesCost = new ALAUtils().sum(liabilities);
    System.out.println(new ALAUtils().getString("Total Liabilities", liabilitiesCost));

    System.out.println("Stockholder's Equity:");

    for (AnAccount acc : stockholdersEquity) {
      System.out.println(new ALAUtils().getString(acc, "  "));
    }

    System.out.println(new ALAUtils().getString("Total Stockholder's Equity", 
        new ALAUtils().sum(stockholdersEquity)));

    System.out.println(new ALAUtils().getString("Total Liabilities and Stockholder's Equity", 
        new ALAUtils().sum(liabilities) + new ALAUtils().sum(stockholdersEquity)));
  }
  
  boolean balances() {
    return new ALAUtils().sum(assets) == new ALAUtils().sum(liabilities)
        + new ALAUtils().sum(stockholdersEquity);
  }

  public void updateAccount(String title, double amount) {
    if (new ALAUtils().containsTitle(this.assets, title)) {
      new ALAUtils().getAccount(this.assets, title).amount += amount;
    } else if (new ALAUtils().containsTitle(this.liabilities, title)) {
      new ALAUtils().getAccount(this.liabilities, title).amount += amount;
    } else if (new ALAUtils().containsTitle(this.stockholdersEquity, title)) {
      new ALAUtils().getAccount(this.stockholdersEquity, title).amount += amount;
    }
  }

  public AnAccount getAccount(String title) {
    ArrayList<AnAccount> potentialAccounts = new ArrayList<AnAccount>(
        List.of(new ALAUtils().getAccount(assets, title), 
            new ALAUtils().getAccount(liabilities, title),
            new ALAUtils().getAccount(stockholdersEquity, title)));
    for (AnAccount a : potentialAccounts) {
      if (a != null) {
        return a;
      }
    }
    return null;
  }
}

class IncomeStatement implements Sheet {
  ArrayList<AnAccount> revenues;
  ArrayList<AnAccount> expenses;
  double netIncome;

  IncomeStatement(ArrayList<AnAccount> revenues, ArrayList<AnAccount> expenses) {
    this.revenues = revenues;
    this.expenses = expenses;
    this.calculateNetIncome();
  }

  IncomeStatement() {
    this.revenues = new ArrayList<AnAccount>(List.of(
        new Account("Sales Revenue"),
        new Account("Fee Revenue"),
        new Account("Interest Revenue"),
        new Account("Rent Revenue")));
    this.expenses = new ArrayList<AnAccount>(List.of(
        new Account("Cost of Goods Sold", false),
        new SummaryAccount("Selling, General, and Administrative Expenses", false, 
            new ArrayList<AnAccount>(List.of(new Account("Wages Expense", false),
                new Account("Rent Expense", false),
                new Account("Depreciation Expense", false),
                new Account("Insurance Expense", false),
                new Account("Repair Expense", false)))),
        new Account("Income Tax Expense")));
    this.calculateNetIncome();
  }
  
  void calculateNetIncome() {
    this.netIncome = new ALAUtils().sum(revenues) - new ALAUtils().sum(expenses);
  }

  public void updateAccount(String title, double amount) {
    if (new ALAUtils().containsTitle(this.revenues, title)) {
      AnAccount acc = new ALAUtils().getAccount(this.revenues, title);
      new ALAUtils().updateAccount(acc, amount);
    } else if (new ALAUtils().containsTitle(this.expenses, title)) {
      new ALAUtils().getAccount(this.expenses, title).amount += amount;
    }
  }

  public AnAccount getAccount(String title) {
    ArrayList<AnAccount> potentialAccounts = new ArrayList<AnAccount>(
        List.of(new ALAUtils().getAccount(revenues, title), 
            new ALAUtils().getAccount(expenses, title)));
    for (AnAccount a : potentialAccounts) {
      if (a != null) {
        return a;
      }
    }
    return null;
  }
  
  void print() {
    System.out.println("Revenues:");

    for (AnAccount acc : this.revenues) {
      System.out.println(new ALAUtils().getString(acc, "  "));
    }

    System.out.println(new ALAUtils().getString("Operating Revenues", new ALAUtils().sum(this.revenues)));

    System.out.println("Expenses:");

    for (AnAccount acc : this.expenses) {
      if (acc.title == "Income Tax Expense") {
        String temp = new ALAUtils().getString("Income before income taxes", 
            new ALAUtils().sum(this.revenues) - (new ALAUtils().sum(this.expenses) - acc.amount));
        System.out.print(temp + "\n");
      }
      System.out.println(new ALAUtils().getString(acc, "  "));
    }
    double liabilitiesCost = new ALAUtils().sum(this.expenses);
    System.out.println(new ALAUtils().getString("Total Liabilities", liabilitiesCost));

    System.out.println(new ALAUtils().getString("Net Income", 
        new ALAUtils().sum(revenues) - new ALAUtils().sum(expenses)));
  }
}


class Company {
  String companyName;
  String units;
  Hashtable<Date, BalanceSheet> balanceSheets;
  Hashtable<Date, IncomeStatement> incomeStatements;

  Company(String companyName) {
    this.units = "millions of";
    this.companyName = companyName;
    this.balanceSheets = new Hashtable<Date, BalanceSheet>();
    this.incomeStatements = new Hashtable<Date, IncomeStatement>();
  }

  public AnAccount getAccount(String title, Date date) {
    ArrayList<Sheet> potentialSheets = new ArrayList<Sheet>(
        List.of(this.balanceSheets.get(date),
            this.incomeStatements.get(date)));  
    
    for (Sheet s : potentialSheets) {
      if (s != null) {
        return s.getAccount(title);
      }
    }
    return null;
  }
  
  public void printBalanceSheet(Date date) {
    String header = this.companyName + "\n" + "Balance Sheet" + "\n" + "At " 
    + date.month + "/" + date.day + "/" + date.year + "\n" + "(in " + this.units + " dollars)";
    System.out.println(header);
    this.balanceSheets.get(date).print();
  }

  public void addBalanceSheet(Date date) {
    this.balanceSheets.put(date, new BalanceSheet());
  }
  
  public void addBalanceSheet(Date date, BalanceSheet bs) {
    this.balanceSheets.put(date, bs);
  }

  public void addIncomeStatement(Date date) {
    this.incomeStatements.put(date, new IncomeStatement());
  }
  
  public void addIncomeStatement(Date date, IncomeStatement is) {
    this.incomeStatements.put(date, new IncomeStatement());
  }

  public void printIncomement(Date date) {
    String header = this.companyName + "\n" + "Income Statement" + "\n" + "For the Year Ended " 
        + date.month + "/" + date.day + "/" + date.year + "\n" + "(in " + this.units + " dollars)";
        System.out.println(header);
        this.incomeStatements.get(date).print();
  }

}

class Examples {
  Examples() {}

  void testCompany(Tester t) {
    Date currentDate = new Date(2020, 12, 31);
    Company comp = new Company("Tesla");
    comp.addBalanceSheet(currentDate);
    comp.addIncomeStatement(currentDate);
    //comp.printBalanceSheet(currentDate);
    t.checkExpect(comp.balanceSheets.get(currentDate).balances(), true);
    
    //AnAccount commonStock = comp.getAccount("Common stock", currentDate);
    comp.balanceSheets.get(currentDate);
    
    AnAccount cashAccount = new ALAUtils().getAccount(comp.balanceSheets.get(currentDate).assets, "Cash");
    Transaction cashTra = new Transaction(cashAccount, 100, true);
    
    AnAccount AccPay = new ALAUtils().getAccount(comp.balanceSheets.get(currentDate).liabilities, "Accounts Payable");
    Transaction AccPayTra = new Transaction(AccPay, 100, true);
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(cashTra, AccPayTra)));
    
    comp.printBalanceSheet(currentDate);
    t.checkExpect(comp.balanceSheets.get(currentDate).balances(), true);
    
    comp.printIncomement(currentDate);
  }
}

