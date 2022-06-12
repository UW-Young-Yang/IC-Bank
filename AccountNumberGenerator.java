// --== CS400 File Header Information ==--
// Name: Xinze Liu
// Email: xliu855@wisc.edu
// Team: IC
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.Random;

/**
 * This class creates a random card number in 9-digits
 * @author Xinze Liu
 *
 */
public class AccountNumberGenerator {

  /**
   * This method generates a random card number in 9-digits for the BankAccount
   * @return an integer of 9-digits
   */
  public static int generate() {
    Random rand = new Random();
    int cardNumber = rand.nextInt(900000000) + 100000000;
    return cardNumber;
  }
}
