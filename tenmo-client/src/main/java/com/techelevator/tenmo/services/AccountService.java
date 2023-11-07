package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
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

    public BigDecimal getBalance(int accountId) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "accounts/" + accountId + "/balance",
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public BigDecimal updateBalance(BigDecimal amountToAdd, int id) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<BigDecimal> entity = new HttpEntity<>(amountToAdd, headers);


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

    public User[] listUsers() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/accounts/users", HttpMethod.GET,
                    makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }
    public User getUserById(int id) {
        User user = null;
        try {
            ResponseEntity<User> response = restTemplate.exchange(baseUrl + "accounts/userById/" + id,
                    HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }
    public Account getAccountByUserId(int userId){
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "accounts/accountByUserId/" + userId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;

    }
    public Account getAccountByAccountId(int accountId){
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "accounts/accountByAccountId/" + accountId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;

    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
