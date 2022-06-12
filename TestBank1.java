import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * This class tests the functionality of FrontEnd and the BankAccount
 *
 * @author Hongru Zhou
 */
public class TestBank1 {

    protected BankAccount test;
    protected BankAccount test2;

    @BeforeEach
    public void initiate() {
        // clear the database
        FrontEnd.dataBase = new RedBlackTree<>();
        test = FrontEnd.addAccount("Jack", "123456");
        test2 = FrontEnd.addAccount("Eric", "456789");
        FrontEnd.login(test, "123456");
    }

    /**
     * This method tests each instance method inside the BankAccount class
     */
    @Test
    public void testBankAccount() {
        BankAccount test = new BankAccount("Jack", "123456");
        BankAccount test2 = new BankAccount("Eric", "456789");
        assertEquals("Jack", test.getUserName());
        assertEquals("123456", test.getPassWord());
        assertEquals(0, test.getBalance());
        assertEquals(9, String.valueOf(test.getAccountNumber()).length());

        // test instance method compareTo()
        if (test.getAccountNumber() < test2.getAccountNumber())
            assertEquals(-1, test.compareTo(test2));
        else if (test.getAccountNumber() > test2.getAccountNumber())
            assertEquals(1, test.compareTo(test2));
        else
            assertEquals(0, test.compareTo(test2));
        assertEquals(0, test.compareTo(new BankAccount(test.getAccountNumber())));
        
        // test instance method changePassword()
        test.changePassWord("abcdef");
        assertEquals("abcdef", test.getPassWord());

        // test instance method save()
        test.save(10000);
        assertEquals(10000, test.getBalance());

        // test instance method withdraw()
        test.withdraw(10000);
        assertEquals(0, test.getBalance());
        try {
            test.withdraw(1);
            fail("The balance of any bank account cannot be negative.");
        } catch (IllegalArgumentException e) {
            assertEquals("Your balance is not enough.", e.getMessage());
        }

        // test instanc method transfer()
        test.save(100);
        test.transfer(test, test2, 100);
        assertEquals(0, test.getBalance());
        assertEquals(100, test2.getBalance());
        try {
            test.transfer(test, test2, 100);
            fail("The balance of any bank account cannot be negative.");
        } catch (IllegalArgumentException e) {
            assertEquals("Your balance is not enough.", e.getMessage());
        }
    }

    /**
     * This method tests if addAccount() adds a new account with specified username 
     * and password correctly
     */
    @Test
    public void testCreate() {
        BankAccount searched = FrontEnd.search(new BankAccount(test.getAccountNumber()), FrontEnd.dataBase.root);
        try {
        assertEquals(0, searched.compareTo(test));
        assertEquals("Jack", test.getUserName());
        assertEquals("123456", test.getPassWord());
        assertEquals(0, test.getBalance());
        assertEquals(9, String.valueOf(test.getAccountNumber()).length());
        } catch (NullPointerException e) {
            fail("The new account has not been added correctly.");
        }
    }

    /**
     * This method tests if login() method works correctly
     */
    @Test
    public void testLogin() {
        assertTrue(FrontEnd.login(test, "123456"));
        assertTrue(!FrontEnd.login(test, "123"));
        assertTrue(FrontEnd.login(test2, "456789"));
    }

    /**
     * This method tests if save() method add specified amount 
     * of money into the current logged in account
     */
    @Test
    public void testSave () {
        try {
            FrontEnd.save(10000);
            FrontEnd.login(test2, "456789");
            FrontEnd.save(10000);
            assertEquals(10000, test.getBalance());
            assertEquals(10000, test2.getBalance());
        } catch (NullPointerException e) {
            fail("The current loged in account has not been assigned.");
        }
    }

    /**
     * This method tests if withdraw() method with draw specified amount 
     * of money from the current logged in account, and it should return 
     * false if there is not enough money, otherwise true
     */
    @Test
    public void testWithdraw() {
        try {
            FrontEnd.save(10000);
            assertTrue(FrontEnd.withDraw(10000));
            assertEquals(0, test.getBalance());
            assertTrue(!FrontEnd.withDraw(10000));
        } catch (NullPointerException e) {
            fail("The current loged in account has not been assigned.");
        }
    }

    /**
     * This method tests if transder() withdraws money from the current account 
     * and save the same amount of money into the specified account
     */
    @Test
    public void testTransfer() {
        try {
            FrontEnd.save(10000);
            assertTrue(FrontEnd.transfer(test2.getAccountNumber(), 10000));
            assertEquals(0, test.getBalance());
            assertEquals(10000, test2.getBalance());
            assertTrue(!FrontEnd.transfer(test2.getAccountNumber(), 10000));
        } catch (NullPointerException e) {
            fail("The current loged in account has not been assigned.");
        }
    }

    /**
     * This method tries to add 10 different accounts into the bank, and save 10 
     * dollars into the first account. Then, the first account will transfer 1 dollor 
     * to other 9 accounts. After all transfers have been done, check the balance of 
     * each account, they should all be 1 dollar
     */
    @Test
    public void generalTest() {
        ArrayList<BankAccount> accountList = new ArrayList<>();
        ArrayList<Integer> accountNumberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            accountList.add(FrontEnd.addAccount(String.valueOf(i), String.valueOf(i)));
            accountNumberList.add(accountList.get(i).getAccountNumber());
        }
        FrontEnd.login(accountList.get(0), accountList.get(0).getPassWord());
        FrontEnd.save(10);
        for (int i = 0; i < 10 - 1; i++) {
            assertTrue(FrontEnd.transfer(accountNumberList.get(i + 1), 1));
            assertEquals(10 - 1 - i, accountList.get(0).getBalance());
            assertEquals(1, accountList.get(i + 1).getBalance());
        }
    }
}
