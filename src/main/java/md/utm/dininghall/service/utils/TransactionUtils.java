package md.utm.dininghall.service.utils;

import org.springframework.transaction.support.TransactionSynchronization;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public class TransactionUtils {
    public static void registerPostCommit(Runnable runnable) {
        registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
    }
}
