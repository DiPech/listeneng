package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final PlatformTransactionManager platformTransactionManager;

    @Override
    public void runInNewTransaction(Runnable runnable) {
        TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txTemplate.execute(status -> {
            runnable.run();
            return null;
        });
    }

}
