package net.siham.ebankingbackend.mappers;

import net.siham.ebankingbackend.dtos.AccountOperationDTO;
import net.siham.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.siham.ebankingbackend.dtos.CustomerDTO;
import net.siham.ebankingbackend.dtos.SavingBankAccountDTO;
import net.siham.ebankingbackend.entities.AccountOperation;
import net.siham.ebankingbackend.entities.CurrentAccount;
import net.siham.ebankingbackend.entities.Customer;
import net.siham.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
        //je cree un objet de type SavingBankAccountDTO
            SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
            //transferer avec "BeanUtils.copyProperties" l'objet savingAccount vers savingAccountDTO
            BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
            //je prends l'objet customer de saving account, je le transfere en CustomerDTO et je l'affecte
            savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
            savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
            return savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
            SavingAccount savingAccount = new SavingAccount();
            BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
            //transferer savingAccount.setCustomer(...)
            savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
            return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
            CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
            BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
            currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
            currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
            return currentBankAccountDTO;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
            CurrentAccount currentAccount = new CurrentAccount();
            BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
            currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
            return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
           AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
           BeanUtils.copyProperties(accountOperation, accountOperationDTO);
           return accountOperationDTO;
    }
}
