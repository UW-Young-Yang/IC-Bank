// --== CS400 File Header Information ==--
// Name: Xinze Liu
// Email: xliu855@wisc.edu
// Team: IC
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

/**
 * This class creates a bank account object includes the name of the user, the card number, password
 * and the balance. This class also provides a series of operation on the bank account.
 * 
 * @author Xinze Liu
 *
 */
public class BankAccount implements Comparable<BankAccount> {
  private String userName;
  private int accountNumber;
  private String password;
  private float balance;

  /**
   * This method returns the user name of the account
   * 
   * @return user name of the account
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * This method returns the card(account) number of the account
   * 
   * @return card(account) number of the account
   */
  public int getAccountNumber() {
    return this.accountNumber;
  }

  /**
   * This method returns the password of the account
   * 
   * @return the password of the account
   */
  public String getPassWord() {
    return this.password;
  }

  /**
   * This method returns the balance(remaining money) of the account
   * 
   * @return the balance of the account
   */
  public float getBalance() {
    return this.balance;
  }

  /**
   * This method creates a new account and loads the user's name. It also randomly generate an
   * account number and loads the password. The default balance is 0.
   * 
   * @param userName the name that the user want to use for this bank account
   * @param password the password that the user set for this bank account
   */
  public BankAccount(String userName, String password) {
    this.userName = userName;
    this.accountNumber = AccountNumberGenerator.generate();
    this.password = password;
    balance = 0;
  }
  
  /**
   * This method creates a new account according to provided account number. It is included to make
   * search() easier and is not expected to be used elsewhere.
   * 
   * @param accountNumer the provided accountNumber
   * @author Tong Jiao 
   */
  public BankAccount(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * This method allows the user to change the password of their bank account
   * 
   * @param newPassWord the new password that the user wants to use.
   */
  public void changePassWord(String newPassWord) {
    this.password = newPassWord;
  }

  /**
   * This method saves some amount of money that the user wants to save.
   * 
   * @param amount the amount of money that the user wants to save
   */
  public void save(float amount) {
    this.balance += amount;
  }

  /**
   * This method withdraws some amount of money that the user wants to withdraw.
   * 
   * @param amount the amount of money that the user wants to withdraw
   */
  public void withdraw(float amount) {
    if (amount <= this.balance) {
      this.balance -= amount;
    } else {
      throw new IllegalArgumentException("Your balance is not enough.");
    }
  }

  /**
   * This money allows the user to transfer some amount of money to another account.
   * 
   * @param from   the account of the user
   * @param to     the account that the user wants to transfer money to
   * @param amount the amount of money that the user wants to transfer
   */
  public void transfer(BankAccount from, BankAccount to, float amount){
    if (amount <= this.balance) {
      from.withdraw(amount);
      to.save(amount);
    } else {
      throw new IllegalArgumentException("Your balance is not enough.");
    }
  }

  /**
   * This method compares the account number of the current account to another account. If it is
   * less than the target account, -1 will be returned. If it is greater, 1 will be returned. And
   * returns 0 when they are the same.
   * 
   * @param target the account that the current account is compared to
   * @return -1, 1, 0 if it is less than, greater than or equal to the target account
   */
  @Override
  public int compareTo(BankAccount target) {
    if (this.accountNumber < target.accountNumber) {
      return -1;
    } else if (this.accountNumber > target.accountNumber) {
      return 1;
    }
    return 0;
  }


}
