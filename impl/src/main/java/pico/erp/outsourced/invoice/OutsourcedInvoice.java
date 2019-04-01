package pico.erp.outsourced.invoice;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.outsourced.invoice.OutsourcedInvoiceEvents.DeterminedEvent;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Address;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutsourcedInvoice implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  OutsourcedInvoiceId id;

  InvoiceId invoiceId;

  CompanyId receiverId;

  CompanyId senderId;

  Address receiveAddress;

  LocalDateTime dueDate;

  ProjectId projectId;

  String remark;

  OutsourcedInvoiceStatusKind status;

  public OutsourcedInvoice() {

  }

  public OutsourcedInvoiceMessages.Create.Response apply(
    OutsourcedInvoiceMessages.Create.Request request) {
    this.id = request.getId();
    this.projectId = request.getProjectId();
    this.dueDate = request.getDueDate();
    this.receiverId = request.getReceiverId();
    this.senderId = request.getSenderId();
    this.receiveAddress = request.getReceiveAddress();
    this.remark = request.getRemark();
    this.status = OutsourcedInvoiceStatusKind.DRAFT;
    return new OutsourcedInvoiceMessages.Create.Response(
      Arrays.asList(new OutsourcedInvoiceEvents.CreatedEvent(this.id))
    );
  }

  public OutsourcedInvoiceMessages.Update.Response apply(
    OutsourcedInvoiceMessages.Update.Request request) {
    if (!isUpdatable()) {
      throw new OutsourcedInvoiceExceptions.CannotUpdateException();
    }
    this.projectId = request.getProjectId();
    this.dueDate = request.getDueDate();
    this.receiverId = request.getReceiverId();
    this.senderId = request.getSenderId();
    this.receiveAddress = request.getReceiveAddress();
    this.remark = request.getRemark();
    return new OutsourcedInvoiceMessages.Update.Response(
      Arrays.asList(new OutsourcedInvoiceEvents.UpdatedEvent(this.id))
    );
  }

  public OutsourcedInvoiceMessages.Determine.Response apply(
    OutsourcedInvoiceMessages.Determine.Request request) {
    if (!isDeterminable()) {
      throw new OutsourcedInvoiceExceptions.CannotDetermineException();
    }
    this.status = OutsourcedInvoiceStatusKind.DETERMINED;
    return new OutsourcedInvoiceMessages.Determine.Response(
      Arrays.asList(new DeterminedEvent(this.id))
    );
  }

  public OutsourcedInvoiceMessages.Cancel.Response apply(
    OutsourcedInvoiceMessages.Cancel.Request request) {
    if (!isCancelable()) {
      throw new OutsourcedInvoiceExceptions.CannotCancelException();
    }
    this.status = OutsourcedInvoiceStatusKind.CANCELED;
    return new OutsourcedInvoiceMessages.Cancel.Response(
      Arrays.asList(new OutsourcedInvoiceEvents.CanceledEvent(this.id))
    );
  }

  public OutsourcedInvoiceMessages.Receive.Response apply(
    OutsourcedInvoiceMessages.Receive.Request request) {
    if (!isReceivable()) {
      throw new OutsourcedInvoiceExceptions.CannotReceiveException();
    }
    this.status = OutsourcedInvoiceStatusKind.RECEIVED;
    return new OutsourcedInvoiceMessages.Receive.Response(
      Arrays.asList(new OutsourcedInvoiceEvents.ReceivedEvent(this.id))
    );
  }

  public OutsourcedInvoiceMessages.Invoice.Response apply(
    OutsourcedInvoiceMessages.Invoice.Request request) {
    if (!isInvoiceable()) {
      throw new OutsourcedInvoiceExceptions.CannotInvoiceException();
    }
    this.invoiceId = request.getInvoiceId();
    return new OutsourcedInvoiceMessages.Invoice.Response(
      Arrays.asList(new OutsourcedInvoiceEvents.InvoicedEvent(this.id))
    );
  }


  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isDeterminable() {
    return status.isDeterminable();
  }

  public boolean isInvoiceable() {
    return status.isInvoiceable() && invoiceId == null;
  }

  public boolean isReceivable() {
    return status.isReceivable();
  }

  public boolean isUpdatable() {
    return status.isUpdatable();
  }


}
