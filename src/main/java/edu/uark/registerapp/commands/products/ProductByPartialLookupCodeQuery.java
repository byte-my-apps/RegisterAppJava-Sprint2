package edu.uark.registerapp.commands.products;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.entities.ProductEntity;
import edu.uark.registerapp.models.repositories.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductByPartialLookupCodeQuery implements ResultCommandInterface<Product[]> {
  @Override
  public Product[] execute() {
    this.validateProperties();

    List<Product> products = new LinkedList<>();
    final List<ProductEntity> productEntities =
        this.productRepository.findByLookupCodeContainingIgnoreCase(
            this.partialLookupCode);

    for (ProductEntity productEntity : productEntities) {
      products.add(new Product(productEntity));
    }

    return (Product[])products.toArray();
  }


  // Helper methods
  private void validateProperties() {
    if (StringUtils.isBlank(this.partialLookupCode)) {
      throw new UnprocessableEntityException("lookupcode");
    }
  }

  // Properties
  private String partialLookupCode;
  public String getPartialLookupCode() {
    return this.partialLookupCode;
  }
  public ProductByPartialLookupCodeQuery setLookupCode(final String lookupCode) {
    this.partialLookupCode = lookupCode;
    return this;
  }

  @Autowired
  private ProductRepository productRepository;
}
