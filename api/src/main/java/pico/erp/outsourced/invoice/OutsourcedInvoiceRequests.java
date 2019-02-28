package pico.erp.outsourced.invoice;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Address;

public interface OutsourcedInvoiceRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Valid
    @NotNull
    CompanyId receiverId;

    @Valid
    @NotNull
    CompanyId supplierId;

    @Valid
    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    Address receiveAddress;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Valid
    @NotNull
    CompanyId receiverId;

    @Valid
    @NotNull
    CompanyId supplierId;

    @Valid
    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    Address receiveAddress;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DetermineRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ReceiveRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

    /*UserId confirmerId;*/

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InvoiceRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

    @Valid
    @NotNull
    InvoiceId invoiceId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GenerateRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceId id;

  }

}
