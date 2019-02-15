package pico.erp.outsourced.invoice;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.invoice.InvoiceId;

@Repository
public interface OutsourcedInvoiceRepository {

  OutsourcedInvoice create(@NotNull OutsourcedInvoice orderAcceptance);

  void deleteBy(@NotNull OutsourcedInvoiceId id);

  boolean exists(@NotNull OutsourcedInvoiceId id);

  boolean exists(@NotNull InvoiceId invoiceId);

  Optional<OutsourcedInvoice> findBy(@NotNull OutsourcedInvoiceId id);

  Optional<OutsourcedInvoice> findBy(@NotNull InvoiceId invoiceId);

  void update(@NotNull OutsourcedInvoice orderAcceptance);

}
