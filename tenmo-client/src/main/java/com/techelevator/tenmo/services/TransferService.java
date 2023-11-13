package com.techelevator.tenmo.services;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private final String baseUrl;
    private String token;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String token) {
        this.token = token;
    }

    public Transfer[] listTransfers(int id) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfer/account/" + id, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    public Transfer[] listPendingTransfers(int id) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfer/request/" + id, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    public boolean sendTEBucks(BigDecimal amountToAdd, int recepientId, int senderId){
        boolean success = false;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(new Transfer(amountToAdd, recepientId, senderId), headers);
        try{
            ResponseEntity<Boolean> response = restTemplate.exchange(baseUrl +"transfer/" + senderId + "/send", HttpMethod.POST, entity
            , Boolean.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
    public boolean requestTransfer(int accountFrom, int accountTo, BigDecimal amount ){
        boolean success=false;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(new Transfer(amount, accountTo, accountFrom), headers);
        try{
            ResponseEntity<Boolean> response = restTemplate.exchange(baseUrl +"transfer/request", HttpMethod.POST, entity
                    , Boolean.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;

    }
    public boolean acceptRequest(int transferId){
        boolean didItWork=false;
        try{
            ResponseEntity<Boolean> response = restTemplate.exchange(baseUrl +"transfer/request/"+transferId+"/accept", HttpMethod.PUT, makeAuthEntity()
                    , Boolean.class);
            didItWork = true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return didItWork;
    }

    public boolean denyRequest(int transferId){
        boolean didItWork=false;
        try{
            ResponseEntity<Boolean> response = restTemplate.exchange(baseUrl +"transfer/request/"+transferId+"/deny", HttpMethod.PUT, makeAuthEntity()
                    , Boolean.class);
            didItWork = true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return didItWork;
    }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
