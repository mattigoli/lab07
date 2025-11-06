package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {
    private static final int AMOUNT = 100;
    private static final int WITHDRAW = 50;
    private static final int NEGATIVE_WITHDRAW = -50;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("mario", "rossi", 1);
        this.bankAccount = new StrictBankAccount(mRossi, 0.0);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(0.0, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        bankAccount.deposit(mRossi.getUserID(), AMOUNT); 

        for (int i = 0; i < 10; i++) {
            int transactionsBefore = bankAccount.getTransactionsCount();
            double expectedFee = StrictBankAccount.MANAGEMENT_FEE + transactionsBefore * StrictBankAccount.TRANSACTION_FEE;
            
            if(bankAccount.getBalance() > expectedFee){
                double expectedBalance = bankAccount.getBalance() - expectedFee;

                bankAccount.chargeManagementFees(mRossi.getUserID());
                assertEquals(expectedBalance, bankAccount.getBalance());
                assertEquals(0, bankAccount.getTransactionsCount()); 
            } else {
                bankAccount.chargeManagementFees(mRossi.getUserID());
                assertEquals(transactionsBefore, bankAccount.getTransactionsCount());
            }
            
        }
        
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try{
            bankAccount.withdraw(mRossi.getUserID(), NEGATIVE_WITHDRAW);
        } catch (IllegalArgumentException e){
            assertNotNull(e.getMessage());
            assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        for(int i = 0; i < 10; i++){
            double expectedFee = StrictBankAccount.MANAGEMENT_FEE + bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE;
            double expectedBalance = bankAccount.getBalance() - expectedFee;
            if(bankAccount.getBalance() > expectedFee){
                bankAccount.withdraw(mRossi.getUserID(), WITHDRAW);
                assertEquals(expectedBalance, bankAccount.getBalance());
            } else {
                try{
                    bankAccount.withdraw(mRossi.getUserID(), WITHDRAW);
                } catch (IllegalArgumentException e){
                    assertNotNull(e.getMessage());
                    assertEquals("Insufficient balance", e.getMessage());
                }
            }
        }
    }
}
