package pico.erp.outsourced.invoice;

import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.invoice.InvoiceEvents;
import pico.erp.invoice.InvoiceId;
import pico.erp.invoice.InvoiceRequests;
import pico.erp.invoice.InvoiceService;
import pico.erp.outsourced.invoice.item.OutsourcedInvoiceItemService;

@SuppressWarnings("unused")
@Component
@Transactional
public class OutsourcedInvoiceEventListener {

  private static final String LISTENER_NAME = "listener.outsourced-invoice-event-listener";

  @ComponentAutowired
  private InvoiceService invoiceService;

  @Autowired
  private OutsourcedInvoiceService outsourcedInvoiceService;

  @Autowired
  private OutsourcedInvoiceItemService outsourcedInvoiceItemService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + InvoiceEvents.ReceivedEvent.CHANNEL)
  public void onInvoiceReceived(InvoiceEvents.ReceivedEvent event) {
    val invoiceId = event.getId();
    val exists = outsourcedInvoiceService.exists(invoiceId);
    if (exists) {
      val outsourcedInvoice = outsourcedInvoiceService.get(invoiceId);
      outsourcedInvoiceService.receive(
        OutsourcedInvoiceRequests.ReceiveRequest.builder()
          .id(outsourcedInvoice.getId())
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceEvents.CanceledEvent.CHANNEL)
  public void onOutsourcedInvoiceCanceled(OutsourcedInvoiceEvents.CanceledEvent event) {
    val outsourcedInvoice = outsourcedInvoiceService.get(event.getId());
    val invoiceId = outsourcedInvoice.getInvoiceId();
    if (invoiceId != null) {
      invoiceService.cancel(
        InvoiceRequests.CancelRequest.builder()
          .id(invoiceId)
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceEvents.DeterminedEvent.CHANNEL)
  public void onOutsourcedInvoiceDetermined(OutsourcedInvoiceEvents.DeterminedEvent event) {
    val outsourcedInvoice = outsourcedInvoiceService.get(event.getId());
    val invoiceId = InvoiceId.generate();
    invoiceService.create(
      InvoiceRequests.CreateRequest.builder()
        .id(invoiceId)
        .dueDate(outsourcedInvoice.getDueDate())
        .receiverId(outsourcedInvoice.getReceiverId())
        .senderId(outsourcedInvoice.getSenderId())
        .receiveAddress(outsourcedInvoice.getReceiveAddress())
        .remark(outsourcedInvoice.getRemark())
        .build()
    );
    outsourcedInvoiceService.invoice(
      OutsourcedInvoiceRequests.InvoiceRequest.builder()
        .id(outsourcedInvoice.getId())
        .invoiceId(invoiceId)
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceEvents.UpdatedEvent.CHANNEL)
  public void onOutsourcedInvoiceUpdated(OutsourcedInvoiceEvents.UpdatedEvent event) {
    val outsourcedInvoice = outsourcedInvoiceService.get(event.getId());
    val invoiceId = outsourcedInvoice.getInvoiceId();
    if (invoiceId != null) {
      invoiceService.update(
        InvoiceRequests.UpdateRequest.builder()
          .id(invoiceId)
          .dueDate(outsourcedInvoice.getDueDate())
          .receiverId(outsourcedInvoice.getReceiverId())
          .senderId(outsourcedInvoice.getSenderId())
          .receiveAddress(outsourcedInvoice.getReceiveAddress())
          .remark(outsourcedInvoice.getRemark())
          .build()
      );
    }
  }


}
