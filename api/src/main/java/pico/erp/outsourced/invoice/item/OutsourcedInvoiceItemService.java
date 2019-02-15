package pico.erp.outsourced.invoice.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;

public interface OutsourcedInvoiceItemService {

  OutsourcedInvoiceItemData create(
    @Valid @NotNull OutsourcedInvoiceItemRequests.CreateRequest request);

  void delete(@Valid @NotNull OutsourcedInvoiceItemRequests.DeleteRequest request);

  boolean exists(@Valid @NotNull OutsourcedInvoiceItemId id);

  OutsourcedInvoiceItemData get(@Valid @NotNull OutsourcedInvoiceItemId id);

  List<OutsourcedInvoiceItemData> getAll(OutsourcedInvoiceId invoiceId);

  void invoice(@Valid @NotNull OutsourcedInvoiceItemRequests.InvoiceRequest request);

  void update(@Valid @NotNull OutsourcedInvoiceItemRequests.UpdateRequest request);


}
