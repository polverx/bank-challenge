package co.com.evermore.config;

import co.com.evermore.api.TransactionHistoryController;
import co.com.evermore.model.user.gateways.TransactionHistoryRepository;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "co.com.evermore.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        @Primary
        public TransactionHistoryUseCase getAllTransactions(TransactionHistoryRepository transactionHistoryRepository){
                return new TransactionHistoryUseCase(transactionHistoryRepository);
        }


}
