package pico.erp.outsourced.invoice.item;

import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemRequests;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourced.invoice.OutsourcedInvoiceEvents;
import pico.erp.outsourced.invoice.OutsourcedInvoiceService;

@SuppressWarnings("unused")
@Component
public class OutsourcedInvoiceItemEventListener {

  private static final String LISTENER_NAME = "listener.outsourced-invoice-item-event-listener";

  @Autowired
  private OutsourcedInvoiceItemService outsourcedInvoiceItemService;

  @ComponentAutowired
  private InvoiceItemService invoiceItemService;

  @Autowired
  private OutsourcedInvoiceService outsourcedInvoiceService;

  @ComponentAutowired
  private ItemLotService itemLotService;

  @ComponentAutowired
  private ItemSpecService itemSpecService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceEvents.InvoicedEvent.CHANNEL)
  public void onOutsourcedInvoiceInvoiced(OutsourcedInvoiceEvents.InvoicedEvent event) {
    val outsourcedInvoice = outsourcedInvoiceService.get(event.getId());

    outsourcedInvoiceItemService.getAll(event.getId())
      .forEach(item -> {
        val itemId = item.getItemId();
        val itemSpecCode = item.getItemSpecCode();
        val invoiceItemId = InvoiceItemId.generate();
        invoiceItemService.create(
          InvoiceItemRequests.CreateRequest.builder()
            .id(invoiceItemId)
            .invoiceId(outsourcedInvoice.getInvoiceId())
            .itemId(itemId)
            .itemSpecCode(itemSpecCode)
            .quantity(item.getQuantity())
            .unit(item.getUnit())
            .remark(item.getRemark())
            .build()
        );
        outsourcedInvoiceItemService.invoice(
          OutsourcedInvoiceItemRequests.InvoiceRequest.builder()
            .id(item.getId())
            .invoiceItemId(invoiceItemId)
            .build()
        );
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceItemEvents.UpdatedEvent.CHANNEL)
  public void onOutsourcedInvoiceItemUpdated(OutsourcedInvoiceItemEvents.UpdatedEvent event) {
    val outsourcedInvoiceItem = outsourcedInvoiceItemService.get(event.getId());
    val invoiceItemId = outsourcedInvoiceItem.getInvoiceItemId();
    if (invoiceItemId != null) {
      invoiceItemService.update(
        InvoiceItemRequests.UpdateRequest.builder()
          .id(invoiceItemId)
          .quantity(outsourcedInvoiceItem.getQuantity())
          .remark(outsourcedInvoiceItem.getRemark())
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcedInvoiceEvents.ReceivedEvent.CHANNEL)
  public void onOutsourcedInvoiceReceived(OutsourcedInvoiceEvents.ReceivedEvent event) {

  }


}
