package faang.school.accountservice.service;

import faang.school.accountservice.entity.AccountNumberSequence;
import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class FreeAccountNumbersService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;

    @Transactional
    public void getFreeAccountNumber(AccountType accountType, Consumer<String> consumer) {
        Long freeAccountNumber = freeAccountNumberRepository.getAccountNumber(accountType.ordinal())
                .orElseGet(() -> {
                    generateAccountNumber(accountType);
                    throw new RuntimeException("Not found free account number");
                });
        consumer.accept(freeAccountNumber.toString());

    }

    @Transactional
    public void generateAccountNumber(AccountType accountType) {
        AccountNumberSequence sequence = accountNumberSequenceRepository.findByAccountType(accountType)
                .orElseThrow(() -> new EntityNotFoundException("No found account with this type"));

        Long currentCount = accountNumberSequenceRepository.incrementCurrentCount(sequence.getAccountType().ordinal(), sequence.getCurrentCount())
                .orElseThrow(() -> new RuntimeException("Failed to increment current count"));

        String accountNumber = String.format("%s%08d", accountType.getAssociatedString(), currentCount);
        FreeAccountNumber freeAccountNumber = FreeAccountNumber.builder().accountType(accountType).accountNumber(accountNumber).build();
        freeAccountNumberRepository.save(freeAccountNumber);
    }
}
