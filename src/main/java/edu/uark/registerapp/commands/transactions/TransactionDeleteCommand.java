package edu.uark.registerapp.commands.transactions;
  
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.models.repositories.TransactionEntryRepository;

@Service
public class TransactionDeleteCommand implements VoidCommandInterface {
        @Transactional
        @Override
        public void execute() {
                final Optional<TransactionEntryEntity> transactionEntryEntity =
                        this.transactionEntryRepository.findById(this.transactionEntryId);
                if (!transactionEntryEntity.isPresent()) { // No record with the associated record ID exists in the database.
                        throw new NotFoundException("TransactionEntry");
                }

                this.transactionEntryRepository.delete(transactionEntryEntity.get());
        }

        // Properties
        private UUID transactionEntryId;
        public UUID getTransactionEntryId() {
                return this.transactionEntryId;
        }
        public TransactionDeleteCommand setTransactionEntryId(final UUID transactionEntryId) {
                this.transactionEntryId = transactionEntryId;
                return this;
        }

        @Autowired
        private TransactionEntryRepository transactionEntryRepository;
}
