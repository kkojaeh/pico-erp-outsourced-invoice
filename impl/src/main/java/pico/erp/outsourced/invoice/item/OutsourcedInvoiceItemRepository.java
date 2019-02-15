package pico.erp.outsourced.invoice.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;

@Repository
public interface OutsourcedInvoiceItemRepository {

  OutsourcedInvoiceItem create(@NotNull OutsourcedInvoiceItem item);

  void deleteBy(@NotNull OutsourcedInvoiceItemId id);

  boolean exists(@NotNull OutsourcedInvoiceItemId id);

  Stream<OutsourcedInvoiceItem> findAllBy(@NotNull OutsourcedInvoiceId planId);

  Optional<OutsourcedInvoiceItem> findBy(@NotNull OutsourcedInvoiceItemId id);

  void update(@NotNull OutsourcedInvoiceItem item);

}
