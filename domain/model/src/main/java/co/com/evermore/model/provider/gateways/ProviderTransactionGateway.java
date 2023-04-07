package co.com.evermore.model.provider.gateways;

import co.com.evermore.model.provider.ProviderTransaction;
import co.com.evermore.model.provider.ProviderTransactionResponse;
import reactor.core.publisher.Mono;

public interface ProviderTransactionGateway {
    Mono<ProviderTransactionResponse> createBankTransaction(ProviderTransaction providerTransaction);
}
