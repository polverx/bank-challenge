package co.com.evermore.config;

import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import co.com.evermore.usecase.TransactionUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.math.BigDecimal;

@Configuration
@ComponentScan(basePackages = "co.com.evermore.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Value("${ontop.fee}")
    private String onTopFee;

    @Bean
    public BigDecimal defaultBigDecimal() {
        return new BigDecimal(onTopFee);
    }

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
                transactionHistoryRepository,
                new BigDecimal(onTopFee)
        );
    }


}
