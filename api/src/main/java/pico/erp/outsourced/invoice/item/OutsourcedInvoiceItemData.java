package pico.erp.outsourced.invoice.item;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;
import pico.erp.shared.data.UnitKind;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutsourcedInvoiceItemData {

  OutsourcedInvoiceItemId id;

  OutsourcedInvoiceId invoiceId;

  InvoiceItemId invoiceItemId;

  ItemId itemId;

  BigDecimal quantity;

  UnitKind unit;

  ItemSpecCode itemSpecCode;

  String remark;

}
