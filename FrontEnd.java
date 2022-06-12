// --== CS400 File Header Information ==--
// Name: Tong Jiao
// Email: tjiao4@wisc.edu
// Team: IC
// Role: Front end engineer
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.io.IOException;
import java.util.Scanner;

public class FrontEnd {

  // The RBT to store bank accounts
  public static RedBlackTree<BankAccount> dataBase = new RedBlackTree<BankAccount>();
  
  // The scanner to read user inputs
  private static Scanner scnr = new Scanner(System.in);
  
  // Record the account that have been logged in
  private static BankAccount currentLoggedIn = null;

  /**
   * A helper method to determine whether a specified account number is in database.
   * @param target The bank account to search for
   * @param subTree Current root, will be used in recursive calls
   * @return the BankAccount Object if it can be found, and null if there is no such account with
   * the specified account number.
   */
  protected static BankAccount search(BankAccount target, RedBlackTree.Node<BankAccount> subTree) {
    while (subTree != null) {
      if (subTree.data.compareTo(target) == 0) {
        return subTree.data;
      } else if (subTree.data.compareTo(target) < 0) {
        return search(target, subTree.rightChild);
      } else {
        return search(target, subTree.leftChild);
      }
    }
    return null;
  }



  /**
   * Create and add a new account to RBT. Generate a random card number for it. If duplicate card 
   * number is generated, try to generate again until a unique card number is got.
   * 
   * @param userName the name specified by the user
   * @param password the password specified by the user
   * @return the BankAccount just added
   */
  public static BankAccount addAccount(String userName, String password) {
    boolean uniqueAccount = false;
    BankAccount toAdd = null;
    while (!uniqueAccount) {
      toAdd = new BankAccount(userName, password);
      try {
        dataBase.insert(toAdd);
        uniqueAccount = true;
      } catch (IllegalArgumentException e) {
        ;
      } catch (NullPointerException e) { // when BankAccount is null
        ;
      }
    }
    return toAdd;
  }

  /**
   * Transfer a specified amount of money from the logged in amount to another account specified by
   * the user.
   * @param accountNumberTo account number that the user wants to transfer to 
   * @param amount the amount that the user wants to transfer to
   * @return true if transfer is successful, false otherwise.
   */
  public static boolean transfer(int accountNumberTo, float amount) {
    try {
      BankAccount result = search(new BankAccount(accountNumberTo), dataBase.root);
      if (result == null) {
        return false;
      }
      currentLoggedIn.transfer(currentLoggedIn, result, amount);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Save a specified amount of money into the currently logged in account.
   * @param amount the amount that the user wants to save 
   */
  public static void save(float amount) {
    currentLoggedIn.save(amount);
  }

  /**
   * Withdraw a specified amount of money into the currently logged in account.
   * @param amount the amount that the user wants to withdraw 
   * @return true if it is successfully done, false if the balance is insufficient
   */
  public static boolean withDraw(float amount) {
    try {
      currentLoggedIn.withdraw(amount);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  /**
   * Log in a specified account and set the currentLoggedIn field to the account that the user
   * wants to log in.
   * @param account The account to be logged in
   * @param password The password provided by the user
   * @return true if the user name and password matches, false otherwise.
   */
  public static boolean login(BankAccount account, String password) {
    if (password.equals(account.getPassWord())) {
      currentLoggedIn = account;
      return true;
    }
    return false;
  }
  
  /**
   * Change the user's password to the new password specified by the user.
   * @param newPassword the new password that the user specifies
   * @return true if the password is different as the user's original password, false otherwise.
   */
  public static boolean changePassword(String newPassword) {
    if (newPassword.equals(currentLoggedIn.getPassWord())) {
      return false;
    } else {
      currentLoggedIn.changePassWord(newPassword);
      return true;
    }
  }

  /**
   * Run the bank account manager application
   * 
   * @param args user input
   */
  public static void main(String[] args) throws IOException {
    String userInput = "";
    Scanner inputScanner = null;
    
    do {
      if (currentLoggedIn != null) {
        System.out.println("-----------------------------------------");
        System.out.println("Current account name: " + currentLoggedIn.getUserName());
        System.out.println("Current account number: " + currentLoggedIn.getAccountNumber());
        System.out.println("Current balance: " + currentLoggedIn.getBalance());
        System.out.println("-----------------------------------------");
        System.out.println("Type your command to continue:\n"
          + "Create account: C \nTransfer: T \nSave: S \nWithdraw: W \nSwitch account: L "
          + "\nChange Password: P \nQuit: Q");
      } else {
        System.out.println("Type your command to continue:\n"
            + "Create account: C \nLogin: L "
            + "\nQuit: Q");
      }
      userInput = scnr.next();

      // Create account ------------------------------------------------

      if (userInput.trim().toUpperCase().equals("C")) {
        inputScanner = new Scanner(System.in);
        System.out.println("Please enter your name:");
        String name = inputScanner.nextLine();
        System.out.println("Please enter your password:");
        String password = inputScanner.nextLine();
        BankAccount added = addAccount(name, password);
        System.out.println("-----------------------------------------");
        System.out.println("Successfully created an account for you!");
        System.out.println("Your account number is: " + added.getAccountNumber());
        System.out.println("Its password is: " + added.getPassWord());
        System.out.println("-----------------------------------------");

        // Login ------------------------------------------------

      } else if (userInput.trim().toUpperCase().equals("L")) {
        int wrongCount = 0;
        System.out.println("Please enter card number:");
        inputScanner = new Scanner(System.in);
        int inputNumber = Integer.parseInt(inputScanner.nextLine());
        BankAccount result = search(new BankAccount(inputNumber), dataBase.root);

        if (result == null) {
          System.out.println("Your account number does not exist!");
        } else {
          System.out.println("Please enter password:");
          String userPassword = inputScanner.nextLine();
          if (login(result, userPassword)) {
            System.out.println("Successfully log in!");
          } else {
            wrongCount++;
            while (wrongCount < 3) {
              System.out.println("Incorrect Password! Please enter password again:");
              userPassword = inputScanner.nextLine();
              if (login(result, userPassword)) {
                System.out.println("Successfully log in!");
                break;
              } else {
                wrongCount++;
              }
            }
            if (wrongCount >= 3) {
              inputScanner.close();
              throw new IllegalArgumentException("You enter password incorrectly too many times!");
            }
          }
        }

        // Need to log in first -------------------------------------
        
      } else if(currentLoggedIn == null && !userInput.trim().toUpperCase().equals("Q")) {
        System.out.println("Please log in first!");

        // Transfer ------------------------------------------------

      } else if (userInput.trim().toUpperCase().equals("T")) {
        inputScanner = new Scanner(System.in);
        System.out.println("Please enter the account number that you want to transfer to:");
        int transferTo = Integer.parseInt(inputScanner.nextLine());
        System.out.println("Please enter the amount that you want to transfer:");
        float amount = Float.parseFloat(inputScanner.nextLine());
        if (!transfer(transferTo, amount)) {
          System.out.println(
              "Transfer failed because insufficient balance or the target account does not exist.");
        } else {
          System.out.println("Transfer has been done.");
        }

        // Save------------------------------------------------

      } else if (userInput.trim().toUpperCase().equals("S")) {
        inputScanner = new Scanner(System.in);
        System.out.println("Please enter the amount you want to save:");
        float amount = Float.parseFloat(inputScanner.nextLine());
        save(amount);
        System.out
            .println("Save has been done. Currently balance is: " + currentLoggedIn.getBalance());

        // Withdraw ------------------------------------------------

      } else if (userInput.trim().toUpperCase().equals("W")) {
        inputScanner = new Scanner(System.in);
        System.out.println("Please enter the amount you want to withdraw:");
        float amount = Float.parseFloat(inputScanner.nextLine());
        if (withDraw(amount)) {
          System.out.println("Withdraw has been done.");
        } else {
          System.out.println("Insufficient balance!");
        } ;

        // Change password ------------------------------------------------

      } else if (userInput.trim().toUpperCase().equals("P")) {
        System.out.println("Please enter your new password:");
        inputScanner = new Scanner(System.in);
        String newPassword = inputScanner.nextLine();
        if (changePassword(newPassword)) {
          System.out.println("Your password has been changed successfully.");
        } else {
          System.out.println("New password cannot be the same as original password!");
        }
      }
    } while (!(userInput.trim().toUpperCase().equals("Q")));
    if (inputScanner != null) inputScanner.close(); // Close Scanner

    System.out.println("Thanks for using!");
  }

}
