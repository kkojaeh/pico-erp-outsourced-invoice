package pico.erp.outsourced.invoice;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.invoice.InvoiceId;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.CancelRequest;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.DetermineRequest;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.InvoiceRequest;
import pico.erp.outsourced.invoice.OutsourcedInvoiceRequests.ReceiveRequest;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class OutsourcedInvoiceServiceLogic implements OutsourcedInvoiceService {

  @Autowired
  private OutsourcedInvoiceRepository outsourcedInvoiceRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private OutsourcedInvoiceMapper mapper;

  @Override
  public void cancel(CancelRequest request) {
    val outsourcedInvoice = outsourcedInvoiceRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
    val response = outsourcedInvoice.apply(mapper.map(request));
    outsourcedInvoiceRepository.update(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public OutsourcedInvoiceData create(OutsourcedInvoiceRequests.CreateRequest request) {
    val outsourcedInvoice = new OutsourcedInvoice();
    val response = outsourcedInvoice.apply(mapper.map(request));
    if (outsourcedInvoiceRepository.exists(outsourcedInvoice.getId())) {
      throw new OutsourcedInvoiceExceptions.AlreadyExistsException();
    }
    val created = outsourcedInvoiceRepository.create(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void determine(DetermineRequest request) {
    val outsourcedInvoice = outsourcedInvoiceRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
    val response = outsourcedInvoice.apply(mapper.map(request));
    outsourcedInvoiceRepository.update(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(OutsourcedInvoiceId id) {
    return outsourcedInvoiceRepository.exists(id);
  }

  @Override
  public boolean exists(InvoiceId invoiceId) {
    return outsourcedInvoiceRepository.exists(invoiceId);
  }

  @Override
  public OutsourcedInvoiceData get(OutsourcedInvoiceId id) {
    return outsourcedInvoiceRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
  }

  @Override
  public OutsourcedInvoiceData get(InvoiceId invoiceId) {
    return outsourcedInvoiceRepository.findBy(invoiceId)
      .map(mapper::map)
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
  }

  @Override
  public void invoice(InvoiceRequest request) {
    val outsourcedInvoice = outsourcedInvoiceRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
    val response = outsourcedInvoice.apply(mapper.map(request));
    outsourcedInvoiceRepository.update(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void receive(ReceiveRequest request) {
    val outsourcedInvoice = outsourcedInvoiceRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
    val response = outsourcedInvoice.apply(mapper.map(request));
    outsourcedInvoiceRepository.update(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(OutsourcedInvoiceRequests.UpdateRequest request) {
    val outsourcedInvoice = outsourcedInvoiceRepository.findBy(request.getId())
      .orElseThrow(OutsourcedInvoiceExceptions.NotFoundException::new);
    val response = outsourcedInvoice.apply(mapper.map(request));
    outsourcedInvoiceRepository.update(outsourcedInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

}
