package pico.erp.outsourced.invoice.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourced.invoice.OutsourcedInvoice;
import pico.erp.shared.data.UnitKind;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutsourcedInvoiceItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  OutsourcedInvoiceItemId id;

  OutsourcedInvoice invoice;

  ItemId itemId;

  UnitKind unit;

  ItemSpecCode itemSpecCode;

  InvoiceItemId invoiceItemId;

  BigDecimal quantity;

  String remark;


  public OutsourcedInvoiceItem() {

  }

  public OutsourcedInvoiceItemMessages.Create.Response apply(
    OutsourcedInvoiceItemMessages.Create.Request request) {
    this.invoice = request.getInvoice();
    if (!isUpdatable()) {
      throw new OutsourcedInvoiceItemExceptions.CannotCreateException();
    }
    this.id = request.getId();

    this.itemId = request.getItemId();
    this.itemSpecCode = request.getItemSpecCode();
    this.unit = request.getUnit();
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();

    return new OutsourcedInvoiceItemMessages.Create.Response(
      Arrays.asList(new OutsourcedInvoiceItemEvents.CreatedEvent(this.id))
    );
  }

  public OutsourcedInvoiceItemMessages.Update.Response apply(
    OutsourcedInvoiceItemMessages.Update.Request request) {
    if (!isUpdatable()) {
      throw new OutsourcedInvoiceItemExceptions.CannotUpdateException();
    }
    this.itemSpecCode = request.getItemSpecCode();
    this.unit = request.getUnit();
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();
    return new OutsourcedInvoiceItemMessages.Update.Response(
      Arrays.asList(new OutsourcedInvoiceItemEvents.UpdatedEvent(this.id))
    );
  }

  public OutsourcedInvoiceItemMessages.Delete.Response apply(
    OutsourcedInvoiceItemMessages.Delete.Request request) {
    if (!isUpdatable()) {
      throw new OutsourcedInvoiceItemExceptions.CannotDeleteException();
    }
    return new OutsourcedInvoiceItemMessages.Delete.Response(
      Arrays.asList(new OutsourcedInvoiceItemEvents.DeletedEvent(this.id))
    );
  }

  public OutsourcedInvoiceItemMessages.Invoice.Response apply(
    OutsourcedInvoiceItemMessages.Invoice.Request request) {
    if (invoiceItemId != null) {
      throw new OutsourcedInvoiceItemExceptions.CannotInvoiceException();
    }
    this.invoiceItemId = request.getInvoiceItemId();
    return new OutsourcedInvoiceItemMessages.Invoice.Response(
      Arrays.asList(new OutsourcedInvoiceItemEvents.InvoicedEvent(this.id))
    );
  }


  public boolean isUpdatable() {
    return this.invoice.isUpdatable();
  }


}
