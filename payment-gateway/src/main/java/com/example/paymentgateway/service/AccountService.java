package com.example.paymentgateway.service;

import com.example.paymentgateway.dao.AccountDao;
import com.example.paymentgateway.entity.Account;
import com.example.paymentgateway.exception.InsufficientAmountException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/*
 getBalance
 withdraw
 deposit
 transfer
*/
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountDao accountDao;

    @Transactional
    public double getBalance(String email){
        return getByEmail(email).orElseThrow(EntityNotFoundException::new).getAmount();
    }

    @Transactional
    public double deposit(String email,double amount){
        double balance = getBalance(email);
        double updatedAmount = (balance + amount);
        Optional<Account> accountOptional = getByEmail(email);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setAmount(updatedAmount);
            accountDao.save(account);
            return updatedAmount;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Transactional
    public double withdraw(String email,double amount){
        double balance = getBalance(email);
        double updatedAmount = (balance - amount);
        if(balance < amount){
           throw new  InsufficientAmountException();
        }
        Optional<Account> accountOptional = getByEmail(email);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            account.setAmount(updatedAmount);
            accountDao.save(account);
            return updatedAmount;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private Optional<Account> getByEmail(String email){
        return accountDao.findByEmail(email);
    }

    @Transactional
    public void transfer(String fromEmail,String toEmail,double amount){
        withdraw(toEmail , amount);
        deposit(fromEmail, amount);
    }
}
