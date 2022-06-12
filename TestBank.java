// --== CS400 File Header Information ==--
// Name: Young Yang
// Email: xyang532@wisc.edu
// Team: IC
// Role: Data Wranglers
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader:

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * This class tests the fundamental methods in the front end. And login() method is not included
 * because it attaches the login state to the calling of the save(), withdraw(), and transfer() method.
 * 
 * @author Young Yang
 */
public class TestBank {

    /**
     * Refresh the red black tree.
     */
    @BeforeEach
    void initial() {
        FrontEnd.dataBase = new RedBlackTree<BankAccount>();
    }

    /**
     * Tests whether addAccount() method sucessfully create a BankAccount object 
     * which include correct information.
     */
    @Test
    void testCreateAccount() {
        BankAccount testAccount1 =  FrontEnd.addAccount("test", "123456");
        BankAccount testAccount2 =  FrontEnd.addAccount("", "");
        assertTrue(testAccount1.getUserName().equals("test"));
        assertTrue(testAccount1.getPassWord().equals("123456"));
        assertTrue(testAccount2.getUserName().equals(""));
        assertTrue(testAccount2.getPassWord().equals(""));
    }

    /**
     * Tests whether search() method search successfully on existing account number.
     */
    @Test
    void testSearch() {
        BankAccount testAccount1 =  FrontEnd.addAccount("test1", "123456");
        BankAccount testAccount2 =  FrontEnd.addAccount("test2", "123456");
        BankAccount testAccount3 =  FrontEnd.addAccount("", "");
        int accountNumber1 = testAccount1.getAccountNumber();
        int accountNumber2 = testAccount2.getAccountNumber();
        int accountNumber3 = testAccount3.getAccountNumber();
        assertTrue(FrontEnd.search(new BankAccount(accountNumber1), FrontEnd.dataBase.root).equals(testAccount1));
        assertTrue(FrontEnd.search(new BankAccount(accountNumber2), FrontEnd.dataBase.root).equals(testAccount2));
        assertTrue(FrontEnd.search(new BankAccount(accountNumber3), FrontEnd.dataBase.root).equals(testAccount3));
    }

    /**
     * Tests whether search() method can deal with non existing account number.
     */
    @Test
    void testIllegalSearch() {
        FrontEnd.addAccount("test1", "123456");
        assertNull(FrontEnd.search(new BankAccount(123456), FrontEnd.dataBase.root)); // Non existing account number
    }

    /**
     * Tests whether save() method successfully save money to the account.
     */
    @Test
    void testSave() {
        BankAccount testAccount =  FrontEnd.addAccount("test", "123456");
        float currentBalance = testAccount.getBalance();
        FrontEnd.login(testAccount, "123456");
        FrontEnd.save(100.100f);
        assertTrue(currentBalance + 100.100f == testAccount.getBalance());
    }

    /**
     * Tests whether withdraw() method successfully withdraw money from the account.
     */
    @Test
    void testWithdraw() {
        BankAccount testAccount =  FrontEnd.addAccount("test", "123456");
        FrontEnd.login(testAccount, "123456");
        FrontEnd.save(100.100f);
        float currentBalance = testAccount.getBalance();
        FrontEnd.withDraw(50f);
        assertTrue(currentBalance - 50f == testAccount.getBalance());
    }

    /**
     * Tests whether withdraw() can deal with illegal withdraw and return false.
     */
    @Test
    void testIllegalWithdraw() {
        BankAccount testAccount =  FrontEnd.addAccount("test", "123456");
        FrontEnd.login(testAccount, "123456");
        assertFalse(FrontEnd.withDraw(50f));
    }

    /**
     * Tests whether transfer() method successfully transfer money from one account to another.
     */
    @Test
    void testTransfer() {
        BankAccount testAccount1 =  FrontEnd.addAccount("test1", "123456");
        BankAccount testAccount2 =  FrontEnd.addAccount("test2", "123456");
        FrontEnd.login(testAccount1, "123456");
        FrontEnd.save(100.100f);
        float currentBalance1 = testAccount1.getBalance();
        float currentBalance2 = testAccount2.getBalance();
        FrontEnd.transfer(testAccount2.getAccountNumber(), 50f);
        assertTrue(currentBalance1 - 50f == testAccount1.getBalance());
        assertTrue(currentBalance2 + 50f == testAccount2.getBalance());
    }

    /**
     * Tests whether transfer() method can deal with illegal transfer and return false.
     */
    @Test
    void testIllegalTransfer() {
        BankAccount testAccount1 =  FrontEnd.addAccount("test1", "123456");
        BankAccount testAccount2 =  FrontEnd.addAccount("test2", "123456");
        FrontEnd.login(testAccount1, "123456");
        assertFalse(FrontEnd.transfer(testAccount2.getAccountNumber(), 50f));  // Illegal transfer amount
        testAccount1.save(50f);
        assertFalse(FrontEnd.transfer(123, 50f));  // Illegal account number
    }

    /**
     * Tests whether changePassword() method change password successfully.
     */
    @Test
    void testChangePassWord() {
        BankAccount testAccount1 = FrontEnd.addAccount("test1", "123456");
        FrontEnd.login(testAccount1, "123456");
        assertFalse(FrontEnd.changePassword("123456")); // change it into the same password
        assertTrue(FrontEnd.changePassword("654321")); // change it into a new password
        assertTrue(FrontEnd.login(testAccount1, "654321")); // login with the new password again
    }
    
}
