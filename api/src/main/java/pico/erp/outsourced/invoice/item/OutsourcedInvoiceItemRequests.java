package pico.erp.outsourced.invoice.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.UnitKind;

public interface OutsourcedInvoiceItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceItemId id;

    @Valid
    @NotNull
    OutsourcedInvoiceId invoiceId;

    @Valid
    @NotNull
    ItemId itemId;

    @Valid
    @NotNull
    ItemSpecCode itemSpecCode;

    @NotNull
    UnitKind unit;

    @NotNull
    @Min(0)
    BigDecimal quantity;

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
    OutsourcedInvoiceItemId id;

    @Valid
    @NotNull
    ItemSpecCode itemSpecCode;

    @NotNull
    UnitKind unit;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InvoiceRequest {

    @Valid
    @NotNull
    OutsourcedInvoiceItemId id;

    @Valid
    @NotNull
    InvoiceItemId invoiceItemId;

  }
}
