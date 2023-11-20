package org.example.service;

import org.example.data.dto.BankDto;

import java.util.List;

public interface BankService {
    BankDto getBankById(Long id);
    List<BankDto> getAllBanks();
    BankDto createBank(BankDto bankDto);
    BankDto updateBank(BankDto bankDto);
    void deleteBank(BankDto bankDto);
}
