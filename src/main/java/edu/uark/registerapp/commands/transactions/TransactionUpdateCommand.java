package edu.uark.registerapp.commands.transactions;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.TransactionEntry;
import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.models.repositories.TransactionEntryRepository;

@Service
public class TransactionUpdateCommand implements ResultCommandInterface<TransactionEntry> {
        @Transactional
        @Override
        public TransactionEntry execute() {

                final Optional<TransactionEntryEntity> transactionEntryEntity =
                        this.transactionEntryRepository.findById(this.transactionEntryId);
                if (!transactionEntryEntity.isPresent()) { // No record with the associated record ID exists in the database.
                        throw new NotFoundException("TransactionEntry");
                }

                // Synchronize any incoming changes for UPDATE to the database.
                this.apiTransactionEntry = transactionEntryEntity.get().synchronize(this.apiTransactionEntry);

                // Write, via an UPDATE, any changes to the database.
                this.transactionEntryRepository.save(transactionEntryEntity.get());

                return this.apiTransactionEntry;
        }


        // Properties
        private UUID transactionEntryId;
        public UUID getTransactionEntryId() {
                return this.transactionEntryId;
        }
        public TransactionUpdateCommand setTransactionEntryId(final UUID transactionEntryId) {
                this.transactionEntryId = transactionEntryId;
                return this;
        }

        private TransactionEntry apiTransactionEntry;
        public TransactionEntry getApiTransactionEntry() {
                return this.apiTransactionEntry;
        }

        public TransactionUpdateCommand setApiTransactionEntry(final TransactionEntry apiTransactionEntry) {
                this.apiTransactionEntry = apiTransactionEntry;
                return this;
        }

        @Autowired
        private TransactionEntryRepository transactionEntryRepository;
}





