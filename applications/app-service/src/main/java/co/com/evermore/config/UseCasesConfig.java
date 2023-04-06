package co.com.evermore.config;

import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import co.com.evermore.usecase.TransactionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.evermore.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public TransactionHistoryUseCase getAllTransactions(
            TransactionHistoryRepository transactionHistoryRepository
    ) {
        return new TransactionHistoryUseCase(
                transactionHistoryRepository
        );
    }

    @Bean
    public TransactionUseCase createNewTransaction(
            WalletGateway walletGateway,
            ProviderTransactionGateway providerTransactionGateway,
            UserRepository userRepository,
            TransactionHistoryRepository transactionHistoryRepository
    ) {
        return new TransactionUseCase(
                walletGateway,
                providerTransactionGateway,
                userRepository,
                transactionHistoryRepository
        );
    }


}
