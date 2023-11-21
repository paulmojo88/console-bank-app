package org.example.data.dao;

import org.example.entity.Account;

import java.util.List;

public interface AccountDao extends Dao<Account, Long>{
    List<Account> getAllByUserId(Long userId);
}
