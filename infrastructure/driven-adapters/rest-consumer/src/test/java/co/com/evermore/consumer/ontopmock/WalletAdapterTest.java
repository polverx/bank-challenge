package co.com.evermore.consumer.ontopmock;

import co.com.evermore.consumer.ontopmock.dto.BalanceResponseDto;
import co.com.evermore.model.wallet.WalletBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WalletAdapterTest {

    @InjectMocks
    private WalletAdapter walletAdapter;

    @Mock
    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(walletAdapter, "balanceUri", "http://localhost:8080/balance");
        ReflectionTestUtils.setField(walletAdapter, "walletTransactionUri", "http://localhost:8080/transaction");
    }

    @Test
    public void testGetUserBalanceSuccess() {
        BigInteger userId = BigInteger.valueOf(12345L);

        WalletBalance walletBalance = WalletBalance.builder()
                .balance(BigDecimal.valueOf(1000L))
                .userId(userId)
                .build();

        BalanceResponseDto balanceResponseDto = BalanceResponseDto.builder()
                .balance(BigDecimal.valueOf(1000L))
                .userId(userId)
                .build();

        final WebClient.RequestHeadersUriSpec uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final WebClient.RequestBodySpec headersSpecMock = Mockito.mock(WebClient.RequestBodySpec.class);
        final ClientResponse clientResponse = Mockito.mock(ClientResponse.class);

        when(webClient.get())
                .thenReturn(uriSpecMock);
        when(uriSpecMock.uri(ArgumentMatchers.<String>notNull()))
                .thenReturn(headersSpecMock);
        when(headersSpecMock.header(notNull(), notNull()))
                .thenReturn(headersSpecMock);

        when(headersSpecMock.exchange())
                .thenReturn(Mono.just(clientResponse));

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.OK);
        when(clientResponse.bodyToMono(BalanceResponseDto.class))
                .thenReturn(Mono.just(balanceResponseDto));

        final Mono<WalletBalance> getBalance = walletAdapter.getUserBalance(userId);

        StepVerifier.create(getBalance)
                .expectNext(walletBalance)
                .verifyComplete();
    }


}