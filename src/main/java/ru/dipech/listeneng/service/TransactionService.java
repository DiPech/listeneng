package ru.dipech.listeneng.service;

public interface TransactionService {

    void runInNewTransaction(Runnable runnable);

}
