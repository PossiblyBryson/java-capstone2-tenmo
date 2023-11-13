package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private static int PENDING = 1;
    private static int APPROVED =2;
    private static int REJECTED = 3;

    private static int REQUEST = 1;
    private static int SEND = 2;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        int userId = currentUser.getUser().getId();
        Account currentAccount = accountService.getAccountByUserId(userId);
        int accountId = currentAccount.getAccountId();

        System.out.println(accountService.getBalance(accountId));
	}

	private void viewTransferHistory() {
        int id = currentUser.getUser().getId();
        Transfer[] transfers = transferService.listTransfers(id);
        Account userAccount = accountService.getAccountByUserId(id);
        System.out.println("ID: Transfers to/from: Amount: ");
        System.out.println("--------------------------");
        for(Transfer transfer:transfers){
            Account otherAccount = null;
            User otherUser = null;
            if(userAccount.getAccountId() == transfer.getAccountFrom() ){
                otherAccount = accountService.getAccountByAccountId(transfer.getAccountTo());
                otherUser = accountService.getUserById(otherAccount.getUserId());
                System.out.println(transfer.getTransferId() + "      " + "To : " + otherUser.getUsername() + "       $" + transfer.getAmount() );
            }
            else{
                otherAccount = accountService.getAccountByAccountId(transfer.getAccountFrom());
                otherUser = accountService.getUserById(otherAccount.getUserId());
                System.out.println(transfer.getTransferId() + "      " + "From : " + otherUser.getUsername() + "       $" + transfer.getAmount() );
            }

        }

        int enteredTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):");
        for(Transfer transfer: transfers){
            if(enteredTransferId == 0){
                return;
            } else if (transfer.getTransferId() == enteredTransferId) {
                System.out.println("ID:" + transfer.getTransferId());
                System.out.println("From:" + transfer.getAccountFrom());
                System.out.println("To:" + transfer.getAccountTo());
                if(transfer.getTransferTypeId() == SEND){System.out.println("Type:Send");}
                else if (transfer.getTransferTypeId() == REQUEST) {System.out.println("Type:Request");}
                if(transfer.getTransferStatusId() == PENDING ){System.out.println("Type: Pending");}
                else if (transfer.getTransferStatusId() == APPROVED) {System.out.println("Type: Approved");}
                else if (transfer.getTransferStatusId() == REJECTED) {System.out.println("Type: Rejected");}
                System.out.println("Amount:" + transfer.getAmount());
            }
        }
	}

	private void viewPendingRequests() {
        int userId = currentUser.getUser().getId();
        Account userAccount = accountService.getAccountByUserId(userId);
        int accountId = userAccount.getAccountId();
        Transfer[] transfers = transferService.listPendingTransfers(accountId);
        System.out.println("Pending Transfers:");
        System.out.println("--------------------------");
        System.out.println("ID: To: Amount: ");
        System.out.println("--------------------------");
        for(Transfer transfer:transfers) {
            Account otherAccount = null;
            User otherUser = null;
            otherAccount = accountService.getAccountByAccountId(transfer.getAccountTo());
            otherUser = accountService.getUserById(otherAccount.getUserId());
            System.out.println(transfer.getTransferId() + "      " + " To : " + otherUser.getUsername() + "       $" + transfer.getAmount());
        }
        int enteredTransferId = consoleService.promptForInt("Please enter transfer ID to accept/reject(0 to cancel):");
        for(Transfer transfer: transfers){
            if(enteredTransferId == 0){
                return;
            } else if (transfer.getTransferId() == enteredTransferId) {
                System.out.println("ID:" + transfer.getTransferId());
                System.out.println("From:" + transfer.getAccountFrom());
                System.out.println("To:" + transfer.getAccountTo());
                if(transfer.getTransferTypeId() == SEND){System.out.println("Type:Send");}
                else if (transfer.getTransferTypeId() == REQUEST) {System.out.println("Type:Request");}
                if(transfer.getTransferStatusId() == PENDING ){System.out.println("Type: Pending");}
                else if (transfer.getTransferStatusId() == APPROVED) {System.out.println("Type: Approved");}
                else if (transfer.getTransferStatusId() == REJECTED) {System.out.println("Type: Rejected");}
                System.out.println("Amount:" + transfer.getAmount());
                System.out.println("1: Approve");
                System.out.println("2: Reject");
                System.out.println("0: Don't approve or reject");
                int acceptOrReject = consoleService.promptForInt("Please enter a number to accept or reject a transfer:");
                if(acceptOrReject == 1){
                    boolean didItWork = transferService.acceptRequest(enteredTransferId);
                    if(didItWork){
                        System.out.println("Transfer successfully accepted");
                    }
                    else{
                        System.out.println("Transfer not successfully accepted");
                    }
                }
                else if(acceptOrReject==2){
                    boolean didItWork = transferService.denyRequest(enteredTransferId);
                    if(didItWork){
                        System.out.println("Transfer successfully denied");
                    }
                    else {
                        System.out.println("Transfer not successfully denied");
                    }
                }
                else{
                    return;
                }


            }
        }






		
	}

	private void sendBucks() {
        User[] users = accountService.listUsers();
        int idSelection = -1;
        BigDecimal amountSelection = new BigDecimal(0);
        System.out.println("Select someone to send TE bucks to: ");
        for(User user: users){
            System.out.println(user.getId() + " " + user.getUsername());
        }
        idSelection= consoleService.promptForMenuSelection("Enter ID of user you are sending to (0 to cancel):");
        amountSelection = consoleService.promptForBigDecimal("Enter amount you'd like to send: ");


        boolean didItWork = false;
        for(User user: users){
            if(idSelection == user.getId()){
                 didItWork = transferService.sendTEBucks(amountSelection, idSelection , currentUser.getUser().getId() );
                 if(didItWork){
                     System.out.println("Successfully sent");
                     return;
                 }
            }
        }




	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        User[] users = accountService.listUsers();
        int idSelection = -1;
        BigDecimal amountSelection = new BigDecimal(0);
        System.out.println("------------");
        System.out.println("ID:    NAME:");
        System.out.println("------------");
        for(User user: users){
            System.out.println(user.getId() + " " + user.getUsername());
        }
        idSelection= consoleService.promptForMenuSelection("Enter ID of user you are requesting from (0 to cancel):");
        amountSelection = consoleService.promptForBigDecimal("Enter amount you'd like to request: ");

        boolean didItWork=false;
        for(User user:users){
            if(idSelection == user.getId()){
                int accountFromId=accountService.getAccountByUserId(user.getId()).getAccountId();
                int accountToId = accountService.getAccountByUserId(currentUser.getUser().getId()).getAccountId();
                didItWork=transferService.requestTransfer(accountFromId,accountToId,amountSelection);
                if(didItWork){
                    System.out.println("Request successfully made!");
                    return;
                }
            }
        }
		
	}

}
