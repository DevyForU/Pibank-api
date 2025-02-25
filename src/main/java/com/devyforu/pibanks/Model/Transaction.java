package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id;
    private Transfer Transfer;
    private Account account;
    private Category category;
    private TransactionType type;
    private Instant date;

}
