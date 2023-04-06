package co.com.evermore.consumer.ontopmock;

import co.com.evermore.consumer.ontopmock.dto.*;
import co.com.evermore.model.provider.ProviderTransaction;
import co.com.evermore.model.provider.ProviderTransactionResponse;
import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderAdapter implements ProviderTransactionGateway {

    @Value("${provider.api.uri.transaction}")
    private String providerTransactionUri;
    @Value("${ontop.information.business-name}")
    private String onTopBusinessName;
    @Value("${ontop.bank-account.type}")
    private String onTopAccountType;
    @Value("${ontop.bank-account.number}")
    private String onTopAccountNumber;
    @Value("${ontop.bank-account.currency}")
    private String onTopAccountCurrency;
    @Value("${ontop.bank-account.routing-number}")
    private String onTopAccountRoutingNumber;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final String EMPTY_SPACE = " ";
    private static final String EMPTY_STRING = "";

    @Override
    public Mono<ProviderTransactionResponse> createProviderTransaction(ProviderTransaction providerTransaction) {

        String uri = UriComponentsBuilder
                .fromPath(providerTransactionUri)
                .build()
                .toUriString();

        ProviderTransactionRequestDto requestBody = getBody(providerTransaction);

        log.info("Executing a bank transfer transaction with body: {}", requestBody);

        return webClient
                .post()
                .uri(uri)
                .bodyValue(requestBody)
                .exchangeToMono(clientResponse -> clientResponse.statusCode().isError()
                        ? Mono.error(new Exception("Error")) //TODO add better error handling
                        : clientResponse.bodyToMono(ProviderTransactionResponseDto.class))
                .flatMap(ProviderTransactionResponseDto::toMonoProviderTransaction)
                .map(providerTransactionResponse -> addJsonStringsToEntity(requestBody, providerTransactionResponse))
                .onErrorResume(Exception.class, error -> Mono.error(new Exception("Error happened while creating a wallet transaction", error)));
    }

    private ProviderTransactionResponse addJsonStringsToEntity(ProviderTransactionRequestDto request, ProviderTransactionResponse providerTransactionResponse) {

        String requestJson;
        String responseJson;

        try {
            requestJson = objectMapper.writeValueAsString(request);
            responseJson = objectMapper.writeValueAsString(providerTransactionResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }

        return providerTransactionResponse.toBuilder()
                .request(requestJson)
                .response(responseJson)
                .build();
    }

    private ProviderTransactionRequestDto getBody(ProviderTransaction providerTransaction) {
        return ProviderTransactionRequestDto.builder()
                .providerTransactionSourceDto(buildOnTopSourceInfo())
                .providerTransactionDestinationDto(buildDestination(providerTransaction))
                .amount(providerTransaction.getAmount())
                .build();
    }

    private ProviderTransactionDestinationDto buildDestination(ProviderTransaction providerTransaction) {
        return ProviderTransactionDestinationDto.builder()
                .name(buildFullName(providerTransaction))
                .providerTransactionAccountDto(ProviderTransactionAccountDto.builder()
                        .accountNumber(providerTransaction.getUserBankAccount().getAccountNumber())
                        .currency(providerTransaction.getUserBankAccount().getCurrency())
                        .routingNumber(providerTransaction.getUserBankAccount().getRoutingNumber())
                        .build())
                .build();
    }

    private String buildFullName(ProviderTransaction providerTransaction) {

        String firstName = hasText(providerTransaction.getFirstName())
                ? providerTransaction.getFirstName()
                : EMPTY_STRING;

        String lastName = hasText(providerTransaction.getLastName())
                ? providerTransaction.getLastName()
                : EMPTY_STRING;

        return firstName + EMPTY_SPACE + lastName;
    }

    private ProviderTransactionSourceDto buildOnTopSourceInfo() {
        return ProviderTransactionSourceDto.builder()
                .type(onTopAccountType)
                .providerTransactionSourceInformationDto(ProviderTransactionSourceInformationDto.builder()
                        .name(onTopBusinessName)
                        .build())
                .providerTransactionAccountDto(ProviderTransactionAccountDto.builder()
                        .accountNumber(onTopAccountNumber)
                        .currency(onTopAccountCurrency)
                        .routingNumber(onTopAccountRoutingNumber)
                        .build())
                .build();
    }


}
