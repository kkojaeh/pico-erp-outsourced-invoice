package pico.erp.outsourced.invoice;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.outsourced.invoice.item.QOutsourcedInvoiceItemEntity;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Give
@Transactional(readOnly = true)
@Validated
public class OutsourcedInvoicedQueryJpa implements OutsourcedInvoiceQuery {


  private final QOutsourcedInvoiceEntity invoice = QOutsourcedInvoiceEntity.outsourcedInvoiceEntity;

  private final QOutsourcedInvoiceItemEntity invoiceItem = QOutsourcedInvoiceItemEntity.outsourcedInvoiceItemEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<OutsourcedInvoiceView> retrieve(OutsourcedInvoiceView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<OutsourcedInvoiceView>(entityManager);
    val select = Projections.bean(OutsourcedInvoiceView.class,
      invoice.id,
      invoice.invoiceId,
      invoice.receiverId,
      invoice.senderId,
      invoice.projectId,
      invoice.receiveAddress,
      invoice.dueDate,
      invoice.status,
      invoice.createdBy,
      invoice.createdDate
    );

    query.select(select);
    query.from(invoice);

    val builder = new BooleanBuilder();

    /*if (!isEmpty(filter.getCode())) {
      builder.and(invoice.code.value
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")));
    }*/

    if (filter.getReceiverId() != null) {
      builder.and(invoice.receiverId.eq(filter.getReceiverId()));
    }

    if (filter.getSenderId() != null) {
      builder.and(invoice.senderId.eq(filter.getSenderId()));
    }

/*    if (filter.getChargerId() != null) {
      builder.and(invoice.chargerId.eq(filter.getChargerId()));
    }*/

    if (filter.getProjectId() != null) {
      builder.and(invoice.projectId.eq(filter.getProjectId()));
    }

    if (filter.getItemId() != null) {
      builder.and(
        invoice.id.in(
          JPAExpressions.select(invoiceItem.invoiceId)
            .from(invoiceItem)
            .where(invoiceItem.itemId.eq(filter.getItemId()))
        )
      );
    }

    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(invoice.status.in(filter.getStatuses()));
    }

    if (filter.getStartDueDate() != null) {
      builder.and(invoice.dueDate.goe(filter.getStartDueDate()));
    }
    if (filter.getEndDueDate() != null) {
      builder.and(invoice.dueDate.loe(filter.getEndDueDate()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
