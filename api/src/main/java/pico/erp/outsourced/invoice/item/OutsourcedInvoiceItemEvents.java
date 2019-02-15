package pico.erp.outsourced.invoice.item;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface OutsourcedInvoiceItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice-item.created";

    private OutsourcedInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice-item.updated";

    private OutsourcedInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice-item.deleted";

    private OutsourcedInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InvoicedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice-item.invoiced";

    private OutsourcedInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class GeneratedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice-item.generated";

    private List<OutsourcedInvoiceItemId> ids;

    public String channel() {
      return CHANNEL;
    }

  }
}
