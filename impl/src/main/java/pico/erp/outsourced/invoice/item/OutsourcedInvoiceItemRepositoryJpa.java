package pico.erp.outsourced.invoice.item;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;

@Repository
interface OutsourcedInvoiceItemEntityRepository extends
  CrudRepository<OutsourcedInvoiceItemEntity, OutsourcedInvoiceItemId> {

  @Query("SELECT i FROM OutsourcedInvoiceItem i WHERE i.invoiceId = :invoiceId ORDER BY i.createdDate")
  Stream<OutsourcedInvoiceItemEntity> findAllBy(@Param("invoiceId") OutsourcedInvoiceId invoiceId);

}

@Repository
@Transactional
public class OutsourcedInvoiceItemRepositoryJpa implements OutsourcedInvoiceItemRepository {

  @Autowired
  private OutsourcedInvoiceItemEntityRepository repository;

  @Autowired
  private OutsourcedInvoiceItemMapper mapper;

  @Override
  public OutsourcedInvoiceItem create(OutsourcedInvoiceItem planItem) {
    val entity = mapper.jpa(planItem);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(OutsourcedInvoiceItemId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(OutsourcedInvoiceItemId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<OutsourcedInvoiceItem> findAllBy(OutsourcedInvoiceId invoiceId) {
    return repository.findAllBy(invoiceId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<OutsourcedInvoiceItem> findBy(OutsourcedInvoiceItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(OutsourcedInvoiceItem planItem) {
    val entity = repository.findById(planItem.getId()).get();
    mapper.pass(mapper.jpa(planItem), entity);
    repository.save(entity);
  }
}
