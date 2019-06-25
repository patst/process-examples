package de.patst.process;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;

@Configuration
public class TransactionInterceptorConfig {

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource,
                                                           ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource) {
            @Override
            protected void doCommit(DefaultTransactionStatus status) {
                super.doCommit(status);
                TransactionWatcher.incrementSuccessfulTransaction();
            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) {
                TransactionWatcher.incrementRollbackTransactions();
                super.doRollback(status);
            }
        };
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

}
