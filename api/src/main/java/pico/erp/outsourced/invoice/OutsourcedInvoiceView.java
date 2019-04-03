package pico.erp.outsourced.invoice;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.item.ItemId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Address;
import pico.erp.shared.data.Auditor;

@Data
public class OutsourcedInvoiceView {

  OutsourcedInvoiceId id;

  ProjectId projectId;

  InvoiceId invoiceId;

  CompanyId receiverId;

  CompanyId senderId;

  Address receiveAddress;

  LocalDateTime dueDate;

  OutsourcedInvoiceStatusKind status;

  Auditor createdBy;

  LocalDateTime createdDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    CompanyId receiverId;

    CompanyId senderId;

    ProjectId projectId;

    ItemId itemId;

    Set<OutsourcedInvoiceStatusKind> statuses;

    LocalDateTime startDueDate;

    LocalDateTime endDueDate;

  }

}
