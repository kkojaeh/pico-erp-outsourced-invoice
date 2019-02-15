package pico.erp.outsourced.invoice;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class OutsourcedInvoiceApi {

  public final static ApplicationId ID = ApplicationId.from("outsourced-invoice");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    OUTSOURCED_INVOICE_PUBLISHER,
    OUTSOURCED_INVOICE_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }
}
