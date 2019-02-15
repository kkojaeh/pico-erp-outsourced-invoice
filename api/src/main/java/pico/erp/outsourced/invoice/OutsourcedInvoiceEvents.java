package pico.erp.outsourced.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface OutsourcedInvoiceEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.created";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ProgressedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.progressed";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeterminedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.determined";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.updated";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.canceled";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ReceivedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.received";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class SentEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.sent";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class RejectedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.rejected";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InvoicedEvent implements Event {

    public final static String CHANNEL = "event.outsourced-invoice.invoiced";

    private OutsourcedInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }


}
