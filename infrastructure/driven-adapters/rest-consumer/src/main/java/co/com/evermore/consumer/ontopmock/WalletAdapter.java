package co.com.evermore.consumer.ontopmock;

import co.com.evermore.consumer.ontopmock.dto.BalanceResponseDto;
import co.com.evermore.consumer.ontopmock.dto.WalletTransactionDto;
import co.com.evermore.model.wallet.WalletBalance;
import co.com.evermore.model.wallet.WalletTransaction;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletAdapter implements WalletGateway {

    @Value("${wallet.api.uri.balance}")
    private String balanceUri;
    @Value("${wallet.api.uri.transaction}")
    private String walletTransactionUri;
    private final WebClient webClient;

    @Override
    public Mono<WalletBalance> getUserBalance(BigInteger userId) {

        String uri = UriComponentsBuilder
                .fromPath(balanceUri)
                .queryParam("user_id", userId)
                .build()
                .toUriString();

        return webClient
                .get()
                .uri(uri)
                .exchangeToMono(clientResponse -> clientResponse.statusCode().isError()
                        ? Mono.error(new Exception("Error")) //TODO add better error handling
                        : clientResponse.bodyToMono(BalanceResponseDto.class)
                )
                .flatMap(balanceResponseDto -> Mono.fromCallable(balanceResponseDto::getWalletBalance))
                .onErrorResume(Exception.class, error -> Mono.error(new Exception("Error happened while fetching balance", error)));
    }

    @Override
    public Mono<WalletTransaction> createWalletTransaction(WalletTransaction walletTransaction) {

        String uri = UriComponentsBuilder
                .fromPath(walletTransactionUri)
                .build()
                .toUriString();

        WalletTransactionDto requestBody = getWalletTransactionBody(walletTransaction);

        log.info("Executing a wallet transaction with body: {}", requestBody);

        return webClient
                .post()
                .uri(uri)
                .bodyValue(requestBody)
                .exchangeToMono(clientResponse -> clientResponse.statusCode().isError()
                        ? Mono.error(new Exception("Error")) //TODO add better error handling
                        : clientResponse.bodyToMono(WalletTransactionDto.class)
                )
                .flatMap(WalletTransactionDto::toMonoWalletTransaction)
                .onErrorResume(Exception.class, error -> Mono.error(new Exception("Error happened while creating a wallet transaction", error)));
    }

    private WalletTransactionDto getWalletTransactionBody(WalletTransaction walletTransaction) {
        return WalletTransactionDto.builder()
                .amount(walletTransaction.getAmount())
                .userId(walletTransaction.getUserId())
                .build();
    }
}
