package pico.erp.outsourced.invoice.item;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.invoice.item.InvoiceItemData;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourced.invoice.OutsourcedInvoice;
import pico.erp.outsourced.invoice.OutsourcedInvoiceExceptions;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;
import pico.erp.outsourced.invoice.OutsourcedInvoiceMapper;
import pico.erp.shared.data.Auditor;

@Mapper
public abstract class OutsourcedInvoiceItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Lazy
  @Autowired
  protected ItemSpecService itemSpecService;

  @Lazy
  @Autowired
  private OutsourcedInvoiceItemRepository outsourcedRequestItemRepository;

  @Autowired
  private OutsourcedInvoiceMapper requestMapper;

  @Lazy
  @Autowired
  private InvoiceItemService invoiceItemService;

  protected OutsourcedInvoiceItemId id(OutsourcedInvoiceItem outsourcedRequestItem) {
    return outsourcedRequestItem != null ? outsourcedRequestItem.getId() : null;
  }

  @Mappings({
    @Mapping(target = "invoiceId", source = "invoice.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract OutsourcedInvoiceItemEntity jpa(OutsourcedInvoiceItem data);

  public OutsourcedInvoiceItem jpa(OutsourcedInvoiceItemEntity entity) {
    return OutsourcedInvoiceItem.builder()
      .id(entity.getId())
      .invoice(map(entity.getInvoiceId()))
      .itemId(entity.getItemId())
      .unit(entity.getUnit())
      .itemSpecCode(entity.getItemSpecCode())
      .invoiceItemId(entity.getInvoiceItemId())
      .quantity(entity.getQuantity())
      .remark(entity.getRemark())
      .build();
  }

  public OutsourcedInvoiceItem map(OutsourcedInvoiceItemId outsourcedRequestItemId) {
    return Optional.ofNullable(outsourcedRequestItemId)
      .map(id -> outsourcedRequestItemRepository.findBy(id)
        .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }

  protected OutsourcedInvoice map(OutsourcedInvoiceId outsourcedInvoiceId) {
    return requestMapper.map(outsourcedInvoiceId);
  }


  protected InvoiceItemData map(InvoiceItemId invoiceItemId) {
    return Optional.ofNullable(invoiceItemId)
      .map(invoiceItemService::get)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "invoiceId", source = "invoice.id")
  })
  public abstract OutsourcedInvoiceItemData map(OutsourcedInvoiceItem item);

  @Mappings({
    @Mapping(target = "invoice", source = "invoiceId")
  })
  public abstract OutsourcedInvoiceItemMessages.Create.Request map(
    OutsourcedInvoiceItemRequests.CreateRequest request);

  public abstract OutsourcedInvoiceItemMessages.Invoice.Request map(
    OutsourcedInvoiceItemRequests.InvoiceRequest request);

  public abstract OutsourcedInvoiceItemMessages.Update.Request map(
    OutsourcedInvoiceItemRequests.UpdateRequest request);

  public abstract OutsourcedInvoiceItemMessages.Delete.Request map(
    OutsourcedInvoiceItemRequests.DeleteRequest request);


  public abstract void pass(
    OutsourcedInvoiceItemEntity from, @MappingTarget OutsourcedInvoiceItemEntity to);


}



