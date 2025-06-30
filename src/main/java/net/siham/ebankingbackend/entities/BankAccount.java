package net.siham.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.siham.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
//DiscriminatorType par defaut String la valeur max 255 charachters
//@DiscriminatorColumn(name = "TYPE", length= 4)
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch= FetchType.EAGER)
    private List<AccountOperation> accountOperations;
}
