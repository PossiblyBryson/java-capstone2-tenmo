package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.http.HttpResponse;

public class AccountService {

    private final String baseUrl;
    private String token;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String token) {
        this.token = token;
    }

    public BigDecimal getBalance(int id) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "accounts/" + id + "/balance",
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        BigDecimal updatedBalance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "accounts/" + id
                + "/balance", HttpMethod.PUT, makeAuthEntity(), BigDecimal.class);
            updatedBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return updatedBalance;
    }

    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        BigDecimal updatedBalance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "accounts/" + id
                    + "/balance", HttpMethod.PUT, makeAuthEntity(), BigDecimal.class);
            updatedBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return updatedBalance;
    }

    public String[] listAccounts() {
        String[] accounts = null;
        try {
            ResponseEntity<String[]> response = restTemplate.exchange(baseUrl + "accounts/usernames", HttpMethod.GET,
                    makeAuthEntity(), String[].class);
            accounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    private HttpEntity<Account> createAccountEntity(Account account) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(this.token);
        return new HttpEntity<>(account, header);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
