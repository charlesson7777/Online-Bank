 /*
 * TransactionFactory.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Follows the factory method to create transactions
 * 
 */

package BankObjects;

import java.util.UUID;

public class TransactionFactory {
    public static Transaction create
        (TypeTransaction type, double amount, Currency currency, Long senderid, Long recipientid, UUID uid) {
        Transaction newTransaction;
        UUID tid = UUID.randomUUID();

        switch(type) {
            case DEPOSIT:
                newTransaction = new TransactionDeposit(amount, currency, recipientid, tid, uid);
                break;
            case TRANSFER:
                newTransaction = new TransactionTransfer(amount, currency, senderid, recipientid, tid, uid);
                break;
            case WITHDRAWAL:
                newTransaction = new TransactionWithdrawal(amount, currency, senderid, tid, uid);
                break;
            default:
                throw new IllegalArgumentException("Unknown bank type");
        }

        return newTransaction;
    }
}
