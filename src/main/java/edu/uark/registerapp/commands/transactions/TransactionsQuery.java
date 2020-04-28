package edu.uark.registerapp.commands.transactions;
  
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.models.api.TransactionEntry;
import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.models.repositories.TransactionEntryRepository;

@Service
public class TransactionsQuery implements ResultCommandInterface<List<TransactionEntry>> {
        @Override
        public LinkedList<TransactionEntry> execute() {
                final LinkedList<TransactionEntry> transactionentrys = new LinkedList<TransactionEntry>();

                for (final TransactionEntryEntity transactionEntryEntity : transactionEntryRepository.findAll()) {
                        transactionentrys.addLast(new TransactionEntry(transactionEntryEntity));
                }

                return transactionentrys;
        }

        @Autowired
        TransactionEntryRepository transactionEntryRepository;
}
