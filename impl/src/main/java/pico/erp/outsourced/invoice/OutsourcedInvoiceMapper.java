package pico.erp.outsourced.invoice;

import java.util.Optional;
import kkojaeh.spring.boot.component.ComponentAutowired;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.invoice.InvoiceData;
import pico.erp.invoice.InvoiceId;
import pico.erp.invoice.InvoiceService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.DetermineRequest;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.ReceiveRequest;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class OutsourcedInvoiceMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @ComponentAutowired
  protected ItemService itemService;

  @ComponentAutowired
  protected ItemSpecService itemSpecService;

  @ComponentAutowired
  private CompanyService companyService;

  @ComponentAutowired
  private UserService userService;

  @Lazy
  @Autowired
  private OutsourcedInvoiceRepository outsourcedInvoiceRepository;


  @ComponentAutowired
  private InvoiceService invoiceService;

  protected Auditor auditor(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::getAuditor)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract OutsourcedInvoiceEntity jpa(OutsourcedInvoice data);

  public OutsourcedInvoice jpa(OutsourcedInvoiceEntity entity) {
    return OutsourcedInvoice.builder()
      .id(entity.getId())
      .projectId(entity.getProjectId())
      .invoiceId(entity.getInvoiceId())
      .receiverId(entity.getReceiverId())
      .senderId(entity.getSenderId())
      .receiveAddress(entity.getReceiveAddress())
      .dueDate(entity.getDueDate())
      .remark(entity.getRemark())
      .status(entity.getStatus())
      .build();
  }

  protected UserData map(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  public OutsourcedInvoice map(OutsourcedInvoiceId outsourcedInvoiceId) {
    return Optional.ofNullable(outsourcedInvoiceId)
      .map(id -> outsourcedInvoiceRepository.findBy(id)
        .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }


  protected InvoiceData map(InvoiceId invoiceId) {
    return Optional.ofNullable(invoiceId)
      .map(invoiceService::get)
      .orElse(null);
  }

  public abstract OutsourcedInvoiceData map(OutsourcedInvoice outsourcedInvoice);

  public abstract OutsourcedInvoiceMessages.Create.Request map(
    OutsourcedInvoiceRequests.CreateRequest request);

  public abstract OutsourcedInvoiceMessages.Update.Request map(
    OutsourcedInvoiceRequests.UpdateRequest request);

  public abstract OutsourcedInvoiceMessages.Invoice.Request map(
    OutsourcedInvoiceRequests.InvoiceRequest request);

  public abstract OutsourcedInvoiceMessages.Determine.Request map(
    DetermineRequest request);

  public abstract OutsourcedInvoiceMessages.Receive.Request map(
    ReceiveRequest request);

  public abstract OutsourcedInvoiceMessages.Cancel.Request map(
    OutsourcedInvoiceRequests.CancelRequest request);

  public abstract void pass(
    OutsourcedInvoiceEntity from, @MappingTarget OutsourcedInvoiceEntity to);


}


