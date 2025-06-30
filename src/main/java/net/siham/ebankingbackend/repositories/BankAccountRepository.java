package net.siham.ebankingbackend.repositories;

import net.siham.ebankingbackend.entities.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, String> {
}
