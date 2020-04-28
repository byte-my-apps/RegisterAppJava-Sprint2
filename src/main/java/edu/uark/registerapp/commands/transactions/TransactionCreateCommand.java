package edu.uark.registerapp.commands.transactions;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.models.api.Transaction;
import edu.uark.registerapp.models.api.TransactionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.models.entities.ProductEntity;
import edu.uark.registerapp.models.entities.TransactionEntity;
import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.models.repositories.ProductRepository;
import edu.uark.registerapp.models.repositories.TransactionEntryRepository;
import edu.uark.registerapp.models.repositories.TransactionRepository;

@Service
public class TransactionCreateCommand implements ResultCommandInterface<Transaction> {
	@Override
	public Transaction execute() {
		long transactionTotal = 0L;
		final List<TransactionEntryEntity> transactionEntryEntities = new LinkedList<>();

		if (this.transactionEntries != null && !this.transactionEntries.isEmpty()) {
			for (TransactionEntry transactionEntry : this.transactionEntries) {
			double purchasedQuantity = transactionEntry.getQuantity();

			transactionTotal += (transactionEntry.getPrice() * purchasedQuantity);

			transactionEntryEntities.add(
				(new TransactionEntryEntity())
					.setPrice(transactionEntry.getPrice())
					.setProductId(transactionEntry.getId())
					.setQuantity(purchasedQuantity));
			}
		}

		final TransactionEntity transactionEntity = this.createTransaction(
			transactionEntryEntities,
			transactionTotal);

		this.apiTransaction.setId(transactionEntity.getId());
		this.apiTransaction.setCreatedOn(transactionEntity.getCreatedOn());
		this.apiTransaction.setCashierId(transactionEntity.getCashierId());
		this.apiTransaction.setTotal(transactionEntity.getTotal());
		this.apiTransaction.setTransactionType(transactionEntity.getType());
		this.apiTransaction.setReferenceId(transactionEntity.getReferenceId());

		return this.apiTransaction;
	}

	// Helper methods
	@Transactional
	private TransactionEntity createTransaction(
		final List<TransactionEntryEntity> transactionEntryEntities,
		final long transactionTotal
	) {

		final TransactionEntity transactionEntity =
			this.transactionRepository.save(
				(new TransactionEntity(this.employeeId, transactionTotal, 1)));

		for (TransactionEntryEntity transactionEntryEntity : transactionEntryEntities) {
			transactionEntryEntity.setTransactionId(transactionEntity.getId());

			this.transactionEntryRepository.save(transactionEntryEntity);
		}

		return transactionEntity;
	}

	// Properties
	private Transaction apiTransaction;
	public Transaction getApiTransaction() {
		return this.apiTransaction;
	}
	public TransactionCreateCommand setApiTransaction(final Transaction apiTransaction) {
		this.apiTransaction = apiTransaction;
		return this;
	}

	private List<TransactionEntry> transactionEntries;
	public List<TransactionEntry> getTransactionEntries() {
		return this.transactionEntries;
	}
	public TransactionCreateCommand setTransactionEntries(
			List<TransactionEntry> transactionEntries) {
		this.transactionEntries = transactionEntries;
		return this;
	}

	private UUID employeeId;
	public UUID getEmployeeId() {
		return this.employeeId;
	}
	public TransactionCreateCommand setEmployeeId(final UUID employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	@Autowired
	ProductRepository productRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;
}
