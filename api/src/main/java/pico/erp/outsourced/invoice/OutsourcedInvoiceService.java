package pico.erp.outsourced.invoice;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.invoice.InvoiceId;

public interface OutsourcedInvoiceService {

  void cancel(@Valid @NotNull OutsourcedInvoiceRequests.CancelRequest request);

  OutsourcedInvoiceData create(@Valid @NotNull OutsourcedInvoiceRequests.CreateRequest request);

  void determine(@Valid @NotNull OutsourcedInvoiceRequests.DetermineRequest request);

  boolean exists(@Valid @NotNull OutsourcedInvoiceId id);

  boolean exists(@Valid @NotNull InvoiceId invoiceId);

  OutsourcedInvoiceData get(@Valid @NotNull OutsourcedInvoiceId id);

  OutsourcedInvoiceData get(@Valid @NotNull InvoiceId invoiceId);

  void invoice(@Valid @NotNull OutsourcedInvoiceRequests.InvoiceRequest request);

  void receive(@Valid @NotNull OutsourcedInvoiceRequests.ReceiveRequest request);

  void update(@Valid @NotNull OutsourcedInvoiceRequests.UpdateRequest request);

}
