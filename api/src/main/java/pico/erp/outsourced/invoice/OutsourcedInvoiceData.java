package pico.erp.outsourced.invoice;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Address;

@Data
public class OutsourcedInvoiceData {

  OutsourcedInvoiceId id;

  InvoiceId invoiceId;

  CompanyId receiverId;

  CompanyId supplierId;

  Address receiveAddress;

  OffsetDateTime dueDate;

  OutsourcedInvoiceStatusKind status;

  ProjectId projectId;

  String remark;

  boolean cancelable;

  boolean receivable;

  boolean determinable;

  boolean updatable;

}
