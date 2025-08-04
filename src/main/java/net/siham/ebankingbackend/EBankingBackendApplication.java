package net.siham.ebankingbackend;

import net.siham.ebankingbackend.dtos.BankAccountDTO;
import net.siham.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.siham.ebankingbackend.dtos.CustomerDTO;
import net.siham.ebankingbackend.dtos.SavingBankAccountDTO;
import net.siham.ebankingbackend.entities.*;
import net.siham.ebankingbackend.enums.AccountStatus;
import net.siham.ebankingbackend.enums.OperationType;
import net.siham.ebankingbackend.exceptions.CustomerNotFoundException;
import net.siham.ebankingbackend.repositories.AccountOperationRepository;
import net.siham.ebankingbackend.repositories.BankAccountRepository;
import net.siham.ebankingbackend.repositories.CustomerRepository;
import net.siham.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Leila", "Ismail", "Ahmed").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            });

            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount : bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accounId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accounId = ((SavingBankAccountDTO) bankAccount).getId();
                    } else {
                        accounId = ((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accounId, 10000 + Math.random() * 120000, "Credit");
                    bankAccountService.debit(accounId,1000 + Math.random() * 9000,"Debit");

                }
            }
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository
    ) {
        return args -> {
            Stream.of("Leila", "Ismail", "Ahmed").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };


    }

}
