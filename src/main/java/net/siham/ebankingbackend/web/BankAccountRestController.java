package net.siham.ebankingbackend.web;


import net.siham.ebankingbackend.dtos.AccountHistoryDTO;
import net.siham.ebankingbackend.dtos.AccountOperationDTO;
import net.siham.ebankingbackend.dtos.AccountOperationRequest;
import net.siham.ebankingbackend.dtos.BankAccountDTO;
import net.siham.ebankingbackend.dtos.TransferRequest;
import net.siham.ebankingbackend.entities.BankAccount;
import net.siham.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.siham.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.siham.ebankingbackend.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestController {
   private BankAccountService bankAccountService;
    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
    return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
    return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId, @RequestParam(name="page", defaultValue ="0")int page,
            @RequestParam(name="size", defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

//a faire: debiter crediter effectuer le verment(transfer)
@PostMapping("/accounts/debit")
public ResponseEntity<?> debit(@RequestBody AccountOperationRequest request) {
    try {
        bankAccountService.debit(request.accountId, request.amount, request.description);
        return ResponseEntity.ok().build();
    } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/accounts/credit")
    public ResponseEntity<?> credit(@RequestBody AccountOperationRequest request) {
        try {
            bankAccountService.credit(request.accountId, request.amount, request.description);
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            bankAccountService.transfer(request.accountSource, request.accountDestination, request.amount);
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
   }
}
