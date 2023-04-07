package co.com.evermore.consumer.ontopmock;

import co.com.evermore.consumer.ex.ServiceException;
import co.com.evermore.consumer.ontopmock.dto.BalanceResponseDto;
import co.com.evermore.consumer.ontopmock.dto.WalletTransactionDto;
import co.com.evermore.model.common.ex.BusinessException;
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

import java.math.BigDecimal;
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
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .exchange()
                .flatMap(clientResponse -> clientResponse.statusCode().isError()
                        ? clientResponse
                        .bodyToMono(String.class)
                        .flatMap(responseBody -> Mono.error(new ServiceException(clientResponse.statusCode(), responseBody)))
                        : clientResponse.bodyToMono(BalanceResponseDto.class)
                )
                .map(BalanceResponseDto::getWalletBalance)
                .onErrorResume(e -> Mono.error(new BusinessException(BusinessException.Type.SERVICE_EXCEPTION, e.getMessage())));
    }

    @Override
    public Mono<WalletTransaction> createWalletTransaction(BigDecimal amount, BigInteger userId, Boolean withdraw) {

        String uri = UriComponentsBuilder
                .fromPath(walletTransactionUri)
                .build()
                .toUriString();

        WalletTransactionDto requestBody = getWalletTransactionBody(amount, userId, withdraw);

        log.info("Executing a wallet transaction with body: {}", requestBody);

        return webClient
                .post()
                .uri(uri)
                .bodyValue(requestBody)
                .exchangeToMono(clientResponse -> clientResponse.statusCode().isError()
                        ? clientResponse
                        .bodyToMono(String.class)
                        .flatMap(responseBody -> Mono.error(new ServiceException(clientResponse.statusCode(), responseBody)))
                        : clientResponse.bodyToMono(WalletTransactionDto.class)
                )
                .map(WalletTransactionDto::toMonoWalletTransaction)
                .onErrorResume(e -> Mono.error(new BusinessException(BusinessException.Type.SERVICE_EXCEPTION, e.getMessage())));
    }

    private WalletTransactionDto getWalletTransactionBody(BigDecimal amount, BigInteger userId, boolean withdraw) {
        return WalletTransactionDto.builder()
                .amount(withdraw ? amount.negate() : amount)
                .userId(userId)
                .build();
    }
}
