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
    String amountAsString;
    if (amount < 0) {
      amountAsString = "(" + Double.toString(amount*-1) + ")";
    } else {
      amountAsString = Double.toString(amount);
    }
    return string + new String(new char[60-string.length()-amountAsString.length()]).replace("\0", "-") + amountAsString;
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
      //System.out.println(a.title);
      if (a.title == title) {
        return a;
      } else {
        AnAccount temp = a.getSubAccount(title);
        if (temp != null) {
          return temp;
        }
      }
    }
    return null;
  }
  
  public double getValue(ArrayList<AnAccount> accounts, String title) {
    return this.getAccount(accounts, title).amount;
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

  public double sum(AnAccount account) {
    return account.sum();
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

  abstract AnAccount getSubAccount(String title2);

  abstract double sum();

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

  void calculateAmount(ArrayList<AnAccount> assets) {
    //System.out.println("heree");
    return;
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

  double sum() {
    return this.amount;
  }

  AnAccount getSubAccount(String title) {
    return null;
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

  double sum() {
    double temp = 0;
    for (AnAccount a : this.subAccounts) {
      temp += a.amount;
    }
    return temp;
  }

  AnAccount getSubAccount(String title) {
    for (AnAccount a : this.subAccounts) {
      if (a.title == title) {
        return a;
      } else {
        AnAccount temp = a.getSubAccount(title);
        if (temp != null) {
          return temp;
        }
      }
    }
    return null;
  }

  public void calculateAmount(ArrayList<AnAccount> accounts) {
    for (AnAccount a : this.subAccounts) {
      a.calculateAmount(accounts);
    }
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

  double sum() {
    return this.amount;
  }

  AnAccount getSubAccount(String title) {
    return null;
  }

}

class SumAccount extends AnAccount {
  ArrayList<String> titles;

  SumAccount(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
  }

  SumAccount(String title, boolean isDebit) {
    super(title, isDebit);
  }

  SumAccount(String title) {
    super(title);
  }

  public SumAccount(String title, ArrayList<String> titles, boolean isDebit) {
    super(title, 0, isDebit);

    this.titles = titles;

    // TODO Auto-generated constructor stub
  }

  public void calculateAmount(ArrayList<AnAccount> accounts) {
    double tempAmount = 0;
    for (String title : titles) {
      //System.out.println(title);
      //System.out.println(new ALAUtils().getAccount(accounts, title).amount);
      tempAmount += new ALAUtils().getAccount(accounts, title).amount;
    }
    this.amount = tempAmount;
  }

  AnAccount getSubAccount(String title) {
    return null;
  }

  @Override
  double sum() {
    return this.amount;
  }

  String getString(String prefix) {
    return new ALAUtils().getString(prefix + "  " + this.title, this.amount);
  }
}

class HeaderAccount extends SummaryAccount {



  HeaderAccount(String title, ArrayList<AnAccount> subAccounts) {
    super(title, subAccounts);
    // TODO Auto-generated constructor stub
  }

  HeaderAccount(String title, boolean isDebit, ArrayList<AnAccount> subAccounts) {
    super(title, isDebit, subAccounts);
    // TODO Auto-generated constructor stub
  }

  HeaderAccount(String title, double amount, boolean isDebit) {
    super(title, amount, isDebit);
  }

  HeaderAccount(String title, boolean isDebit) {
    super(title, isDebit);
  }

  HeaderAccount(String title) {
    super(title);
  }

  String getString(String prefix) {
    String temp = this.title;
    for (AnAccount a : this.subAccounts) {
      temp += "\n" + new ALAUtils().getString(a, prefix);
    }
    return temp;
    
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
  
  Transaction(AnAccount account, double amount) {
    this.account = account;
    this.amount = amount;
    this.isDebit = true;
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
  ArrayList<AnAccount> LnSE;

  BalanceSheet(ArrayList<AnAccount> assets, ArrayList<AnAccount> LnSE) {
    this.assets = assets;
    this.LnSE = LnSE;
  }

  BalanceSheet() {
    this.assets = new ArrayList<AnAccount>(List.of(
        new HeaderAccount("Current Assets", new ArrayList<AnAccount>(List.of(
            new Account("Cash"), 
            new Account("Short-term Investments"), 
            new SummaryAccount("Accounts Receivable"),
            new Account("Supplies"),
            new Account("Prepaid Expenses"),
            new SumAccount("Total Current Assets", new ArrayList<String>(List.of("Cash",
                "Short-term Investments", "Accounts Receivable", "Supplies", "Prepaid Expenses")), true)))),
        new HeaderAccount("Property, Plant, and Equipment", 
            new ArrayList<AnAccount>(List.of(
                new Account("Equipment"),
                new Account("Buildings"),
                new Account("Land"),
                new SumAccount("Total Cost", new ArrayList<String>(List.of("Equipment", "Buildings", "Land")), true),
                new Account("Accumulated Depreciation", false),
                new SumAccount("Net Property and Equipment", new ArrayList<String>(
                    List.of("Total Cost", "Accumulated Depreciation")), true)))),
        new SummaryAccount("Inventory"),
        new SummaryAccount("Notes Receivable"),
        new Account("Long-Term Investments"),
        new Account("Intangibles"),
        new SumAccount("Total Assets", new ArrayList<String>(List.of("Total Current Assets", "Net Property and Equipment", 
            "Inventory", "Notes Receivable", "Long-Term Investments", "Intangibles")), true)));
    this.LnSE = new ArrayList<AnAccount>(List.of(
        new HeaderAccount("Current Liabilities", new ArrayList<AnAccount>(List.of(
            new Account("Accounts Payable", false),
            new Account("Unearned Revenue", false),
            new Account("Dividends Payable", false),
            new Account("Income Tax Payable", false),
            new SummaryAccount("Accrued Expenses Payable", false, new ArrayList<AnAccount>(List.of(
                new Account("Wages Payable", false), 
                new Account("Utilities Payable", false),
                new SumAccount("Total Current Liabilities", new ArrayList<String>(List.of("Accounts Payable", "Unearned Revenue", 
                    "Accrued Expenses", "Income Tax Payable", "Wages Payable", "Utilities Payable")), false))))))),
        new SummaryAccount("Notes Payable", false, new ArrayList<AnAccount>(List.of(
            new Account("Short-term Notes Payable", false), 
            new Account("Long-term Notes Payable", false)))),
        new Account("Other Liabilities", false),
        new SumAccount("Total Liabilities", new ArrayList<String>(List.of("Total Current Liabilities", "Notes Payable", "Other Liabilities")), false),
        new HeaderAccount("Stockholder's Equity:", new ArrayList<AnAccount>(List.of(new Account("Common Stock", false),
            new Account("Additional Paid-in Capital", false),
            new Account("Treasury Stock", false),
            new Account("Retained Earnings", false),
            new SumAccount("Total Stockholder's Equity", new ArrayList<String>(List.of(
                "Common Stock", "Additional Paid-in Capital", "Treasury Stock", "Retained Earnings")), false)))),
        new SumAccount("Total Liabilities and Stockholder's Equity", 
            new ArrayList<String>(List.of("Total Liabilities", "Total Stockholder's Equity")), false)));

  }


  void print() {
    String toBePrinted = "";
    toBePrinted += "ASSETS" + "\n";

    for (AnAccount acc : this.assets) {
      toBePrinted += new ALAUtils().getString(acc, "  ") + "\n";
    }

    toBePrinted += "LIABILITIES AND STOCKHOLDER'S EQUITY" + "\n";

    for (AnAccount acc : this.LnSE) {
      toBePrinted += new ALAUtils().getString(acc, "  ") + "\n";
    }

    System.out.println(toBePrinted);
  }

  boolean balances() {
    return new ALAUtils().getValue(this.assets, "Total Assets") == new ALAUtils().getValue(this.LnSE, "Total Liabilities and Stockholder's Equity");
  }

  public void updateAccount(String title, double amount) {
    if (new ALAUtils().containsTitle(this.assets, title)) {
      new ALAUtils().getAccount(this.assets, title).amount += amount;
    } else if (new ALAUtils().containsTitle(this.LnSE, title)) {
      new ALAUtils().getAccount(this.LnSE, title).amount += amount;
    }
  }

  public AnAccount getAccount(String title) {
    ArrayList<AnAccount> potentialAccounts = new ArrayList<AnAccount>(
        List.of(new ALAUtils().getAccount(assets, title), 
            new ALAUtils().getAccount(this.LnSE, title)));
    for (AnAccount a : potentialAccounts) {
      if (a != null) {
        return a;
      }
    }
    return null;
  }

  public void updateRetainedEarnings(double amount) {
    this.updateAccount("Retained Earnings", amount);
  }

  public void updateSumAccounts() {
    for (AnAccount acc : this.assets) {
      acc.calculateAmount(this.assets);
    }
    for (AnAccount acc : this.LnSE) {
      acc.calculateAmount(this.LnSE);
    }
  }
}

class IncomeStatement implements Sheet {
  ArrayList<AnAccount> revenues;
  ArrayList<AnAccount> expenses;
  Account netIncome;

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
            new ArrayList<AnAccount>(List.of(
                new Account("Wages Expense", false),
                new Account("Rent Expense", false),
                new Account("Depreciation Expense", false),
                new Account("Insurance Expense", false),
                new Account("Repair Expense", false)))),
        new Account("Income Tax Expense")));
    this.netIncome = new Account("Net Income", false);
    this.calculateNetIncome();
  }

  public double calculateNetIncome() {
    this.netIncome.amount = new ALAUtils().sum(revenues) - new ALAUtils().sum(expenses);
    return this.netIncome.amount;
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

class StatementOfSE implements Sheet {
  HiddenAccount begCommonStock;
  HiddenAccount stockIssuance;
  Account endCommonStock;

  HiddenAccount begRetainedEarnings;
  HiddenAccount netIncome;
  HiddenAccount dividendsDeclared;
  Account endRetainedEarnings;

  StatementOfSE(double begCommonStockAmount, double begRetainedEarningsAmount, double netIncomeAmount) {
    this.begCommonStock = new HiddenAccount("Beg. Common Stock", begCommonStockAmount, false);
    this.begRetainedEarnings = new HiddenAccount("Beg. Retained Earnings", begRetainedEarningsAmount, false);

    this.stockIssuance = new HiddenAccount("Stock Issuance", false);
    this.netIncome = new HiddenAccount("Net Income", netIncomeAmount, false);
    this.dividendsDeclared = new HiddenAccount("Dividends Declared", false);

    this.endCommonStock = new Account("Ending Common Stock", false);
    this.endRetainedEarnings = new Account("Ending Retained Earnings", false);

    this.updateCommonStock();
    this.updateRetainedEarnings();
  }

  public void updateAccount(String title, double amount) {
    // TODO Auto-generated method stub
  }

  @Override
  public AnAccount getAccount(String title) {
    // TODO Auto-generated method stub
    return this.endCommonStock;
  }

  public void updateNetIncome(double netIncomeAmount) {
    this.netIncome.amount = netIncomeAmount;
  }

  public void updateCommonStock() {
    this.endCommonStock.amount = this.begCommonStock.amount + this.stockIssuance.amount;
  }

  public double updateRetainedEarnings() {
    this.endRetainedEarnings.amount = this.begRetainedEarnings.amount + this.netIncome.amount
        - this.dividendsDeclared.amount;
    return this.endRetainedEarnings.amount;
  }

  public void print() {
    System.out.println(new ALAUtils().getString(this.endCommonStock, ""));
    System.out.println(new ALAUtils().getString(this.endRetainedEarnings, ""));
  }

}

class Company {
  String companyName;
  String units;
  Hashtable<Date, BalanceSheet> balanceSheets;
  Hashtable<Date, IncomeStatement> incomeStatements;
  Hashtable<Date, StatementOfSE> statementsOfSE;

  Company(String companyName) {
    this.units = "millions of";
    this.companyName = companyName;
    this.balanceSheets = new Hashtable<Date, BalanceSheet>();
    this.incomeStatements = new Hashtable<Date, IncomeStatement>();
    this.statementsOfSE = new Hashtable<Date, StatementOfSE>();
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

  public void printIncomeStatement(Date date) {
    String header = this.companyName + "\n" + "Income Statement" + "\n" + "For the Year Ended " 
        + date.month + "/" + date.day + "/" + date.year + "\n" + "(in " + this.units + " dollars)";
    System.out.println(header);
    this.incomeStatements.get(date).print();
  }

  public void updateNetIncome(Date date) {
    double netIncome = this.incomeStatements.get(date).calculateNetIncome();
    this.statementsOfSE.get(date).updateNetIncome(netIncome);
  }

  public void updateRetainedEarnings(Date date) {
    double retainedEarnings = this.statementsOfSE.get(date).updateRetainedEarnings();
    this.balanceSheets.get(date).updateRetainedEarnings(retainedEarnings);
  }

  public void printStatementOfSE(Date date) {
    String header = this.companyName + "\n" + "Statement of Stockholder's Equity" + "\n" + "For the Year Ended " 
        + date.month + "/" + date.day + "/" + date.year + "\n" + "(in " + this.units + " dollars)";
    System.out.println(header);
    this.statementsOfSE.get(date).print();

  }

  public void addStatementOfSE(Date date) {
    this.statementsOfSE.put(date, new StatementOfSE(10, 10, 0));
  }

  public void addStatementOfSE(Date date, StatementOfSE se) {
    this.statementsOfSE.put(date, se);

  }

  public void printSheets(Date date) {
    this.updateNetIncome(date);
    this.updateRetainedEarnings(date);

    this.printBalanceSheet(date);
    this.printIncomeStatement(date);
    this.printStatementOfSE(date);
  }

  public double getCurrentRatio(Date date) {
    return new ALAUtils().getValue(this.balanceSheets.get(date).assets, "Total Current Assets") / 
        new ALAUtils().getValue(this.balanceSheets.get(date).LnSE, "Total Current Liabilities"); 
  }

  public void generateSumAccounts(Date date) {
    this.balanceSheets.get(date).updateSumAccounts();
  }
}

class Examples {
  Examples() {}

  Date currentDate;
  Company comp;

  void makeCompany() {
    this.currentDate = new Date(2020, 12, 31);
    this.comp = new Company("Tesla");
    this.comp.addBalanceSheet(this.currentDate);
    this.comp.addIncomeStatement(this.currentDate);
    this.comp.addStatementOfSE(this.currentDate);
  }

  void testTransactions(Tester t) {
    this.makeCompany();
    
    //Chapter 2 transactions
    
    //a)
    AnAccount cashAccount = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).assets, "Cash");
    Transaction cashTra1 = new Transaction(cashAccount, 300);

    AnAccount ComSto = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).LnSE, "Common Stock");
    Transaction ComStoTra1 = new Transaction(ComSto, 1);
    
    AnAccount APIC = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).LnSE, "Additional Paid-in Capital");
    Transaction APICTra1 = new Transaction(APIC, 299);
    
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(cashTra1, ComStoTra1, APICTra1)));
    
    //b)
    Transaction cashTra2 = new Transaction(cashAccount, 2);
    
    AnAccount NotPay = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).LnSE, "Notes Payable");
    Transaction NotPayTra = new Transaction(NotPay, 2);
    
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(cashTra2, NotPayTra)));
    
    //c)
    AnAccount equipment = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).assets, "Equipment");
    AnAccount intangibles = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).assets, "Intangibles");
    
    Transaction cashTra3 = new Transaction(cashAccount, -54);
    Transaction equipmentTra1 = new Transaction(equipment, 52);
    Transaction intangiblesTra1 = new Transaction(intangibles, 3);
    Transaction notPayTra1 = new Transaction(NotPay, 1);
    
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(cashTra3, equipmentTra1, intangiblesTra1, notPayTra1)));

    //d)
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(new Transaction(cashAccount, -1), new Transaction(NotPay, -1))));
    
    //e)
    AnAccount investments = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).assets, "Short-term Investments");
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(new Transaction(cashAccount, -44), new Transaction(investments, 44))));
    
    //f)
    AnAccount divPayable = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).LnSE, "Dividends Payable");
    AnAccount retainedEarn = new ALAUtils().getAccount(this.comp.balanceSheets.get(this.currentDate).LnSE, "Retained Earnings");
    new ALAUtils().transact(new ArrayList<Transaction>(List.of(new Transaction(divPayable, 2), new Transaction(retainedEarn, -2))));
    
    this.comp.generateSumAccounts(currentDate);
    
    this.comp.printBalanceSheet(currentDate);
    
    if(this.comp.balanceSheets.get(currentDate).balances()) {
      System.out.println("Balance Sheet Balances!!");
    } else {
      System.out.println("Balance Sheet does not balance!!");
    }

    
    
    //System.out.println(this.comp.getCurrentRatio(currentDate));
  }
}

