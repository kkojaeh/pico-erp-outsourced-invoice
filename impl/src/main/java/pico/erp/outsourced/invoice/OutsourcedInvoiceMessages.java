package pico.erp.outsourced.invoice;

import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Address;
import pico.erp.shared.event.Event;

public interface OutsourcedInvoiceMessages {

  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      OutsourcedInvoiceId id;

      @Valid
      @NotNull
      ProjectId projectId;

      @Valid
      @NotNull
      CompanyId receiverId;

      @Valid
      @NotNull
      CompanyId senderId;

      @Valid
      @NotNull
      Address receiveAddress;

      @Future
      @NotNull
      LocalDateTime dueDate;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Update {

    @Data
    class Request {

      @Valid
      @NotNull
      ProjectId projectId;

      @Future
      @NotNull
      LocalDateTime dueDate;

      @Valid
      @NotNull
      CompanyId receiverId;

      @Valid
      @NotNull
      CompanyId senderId;

      @Valid
      @NotNull
      Address receiveAddress;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Determine {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Receive {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Cancel {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Invoice {

    @Data
    class Request {

      InvoiceId invoiceId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }


}
