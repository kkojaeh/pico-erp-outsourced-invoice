package pico.erp.outsourced.invoice.item;

import java.util.List;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.outsourced.invoice.OutsourcedInvoiceId;
import pico.erp.outsourced.invoice.item.OutsourcedInvoiceItemRequests.DeleteRequest;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class OutsourcedInvoiceItemServiceLogic implements OutsourcedInvoiceItemService {

  @Autowired
  private OutsourcedInvoiceItemRepository itemRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private OutsourcedInvoiceItemMapper mapper;

  @Override
  public OutsourcedInvoiceItemData create(OutsourcedInvoiceItemRequests.CreateRequest request) {
    val item = new OutsourcedInvoiceItem();
    val response = item.apply(mapper.map(request));
    if (itemRepository.exists(item.getId())) {
      throw new OutsourcedInvoiceItemExceptions.AlreadyExistsException();
    }
    val created = itemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.deleteBy(item.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(OutsourcedInvoiceItemId id) {
    return itemRepository.exists(id);
  }


  @Override
  public OutsourcedInvoiceItemData get(OutsourcedInvoiceItemId id) {
    return itemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(OutsourcedInvoiceItemExceptions.NotFoundException::new);
  }

  @Override
  public List<OutsourcedInvoiceItemData> getAll(OutsourcedInvoiceId invoiceId) {
    return itemRepository.findAllBy(invoiceId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void invoice(OutsourcedInvoiceItemRequests.InvoiceRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(OutsourcedInvoiceItemRequests.UpdateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

}
