package pico.erp.outsourced.invoice;

import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.invoice.InvoiceId;

@Repository
interface OutsourcedInvoiceEntityRepository extends
  CrudRepository<OutsourcedInvoiceEntity, OutsourcedInvoiceId> {

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM OutsourcedInvoice i WHERE i.invoiceId = :invoiceId")
  boolean exists(@Param("invoiceId") InvoiceId invoiceId);

  @Query("SELECT i FROM OutsourcedInvoice i WHERE i.invoiceId = :invoiceId")
  OutsourcedInvoiceEntity findBy(@Param("invoiceId") InvoiceId invoiceId);

}

@Repository
@Transactional
public class OutsourcedInvoiceRepositoryJpa implements OutsourcedInvoiceRepository {

  @Autowired
  private OutsourcedInvoiceEntityRepository repository;

  @Autowired
  private OutsourcedInvoiceMapper mapper;

  @Override
  public OutsourcedInvoice create(OutsourcedInvoice plan) {
    val entity = mapper.jpa(plan);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(OutsourcedInvoiceId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(OutsourcedInvoiceId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(InvoiceId invoiceId) {
    return repository.exists(invoiceId);
  }

  @Override
  public Optional<OutsourcedInvoice> findBy(OutsourcedInvoiceId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public Optional<OutsourcedInvoice> findBy(InvoiceId invoiceId) {
    return Optional.ofNullable(repository.findBy(invoiceId))
      .map(mapper::jpa);
  }

  @Override
  public void update(OutsourcedInvoice plan) {
    val entity = repository.findOne(plan.getId());
    mapper.pass(mapper.jpa(plan), entity);
    repository.save(entity);
  }
}
