package pico.erp.outsourced.invoice.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.invoice.InvoiceApplication
import pico.erp.item.ItemApplication
import pico.erp.item.ItemId
import pico.erp.item.spec.ItemSpecCode
import pico.erp.outsourced.invoice.*
import pico.erp.process.ProcessApplication
import pico.erp.project.ProjectApplication
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.UnitKind
import pico.erp.user.UserApplication
import spock.lang.Specification

@SpringBootTest(classes = [OutsourcedInvoiceApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [
  UserApplication, ItemApplication, ProjectApplication, ProcessApplication, CompanyApplication, InvoiceApplication
])
@Transactional
@Rollback
@ActiveProfiles("test")
class OutsourcedInvoiceItemServiceSpec extends Specification {

  @Lazy
  @Autowired
  OutsourcedInvoiceService invoiceService

  @Lazy
  @Autowired
  OutsourcedInvoiceItemService invoiceItemService

  def invoiceId = OutsourcedInvoiceId.from("outsourced-invoice-test")

  def id = OutsourcedInvoiceItemId.from("outsourced-invoice-item-1")


  def itemId = ItemId.from("item-2")

  def itemSpecCode = ItemSpecCode.NOT_APPLICABLE

  def unknownId = OutsourcedInvoiceItemId.from("unknown")

  def unit = UnitKind.EA


  def setup() {

  }

  def cancelInvoice() {
    invoiceService.cancel(
      new OutsourcedInvoiceRequests.CancelRequest(
        id: invoiceId
      )
    )
  }

  def determineInvoice() {
    invoiceService.determine(
      new OutsourcedInvoiceRequests.DetermineRequest(
        id: invoiceId
      )
    )
  }

  def receiveInvoice() {
    invoiceService.receive(
      new OutsourcedInvoiceRequests.ReceiveRequest(
        id: invoiceId
      )
    )
  }

  def createItem() {
    invoiceItemService.create(
      new OutsourcedInvoiceItemRequests.CreateRequest(
        id: id,
        invoiceId: invoiceId,
        itemId: itemId,
        itemSpecCode: itemSpecCode,
        unit: unit,
        quantity: 100,
        remark: "품목 비고"
      )
    )
  }

  def createItem2() {
    invoiceItemService.create(
      new OutsourcedInvoiceItemRequests.CreateRequest(
        id: OutsourcedInvoiceItemId.from("outsourced-invoice-item-2"),
        invoiceId: invoiceId,
        itemId: itemId,
        itemSpecCode: itemSpecCode,
        unit: unit,
        quantity: 100,
        remark: "품목 비고"
      )
    )
  }

  def updateItem() {
    invoiceItemService.update(
      new OutsourcedInvoiceItemRequests.UpdateRequest(
        id: id,
        itemSpecCode: itemSpecCode,
        unit: unit,
        quantity: 200,
        remark: "품목 비고2",
      )
    )
  }

  def deleteItem() {
    invoiceItemService.delete(
      new OutsourcedInvoiceItemRequests.DeleteRequest(
        id: id
      )
    )
  }


  def "존재 - 아이디로 확인"() {
    when:
    createItem()
    def exists = invoiceItemService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = invoiceItemService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    createItem()
    def item = invoiceItemService.get(id)
    then:
    item.id == id
    item.itemId == itemId
    item.itemSpecCode == itemSpecCode
    item.unit == unit
    item.invoiceId == invoiceId
    item.quantity == 100
    item.remark == "품목 비고"

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    invoiceItemService.get(unknownId)

    then:
    thrown(OutsourcedInvoiceItemExceptions.NotFoundException)
  }

  def "생성 - 작성 후 생성"() {
    when:
    createItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() > 0
  }

  def "생성 - 확정 후 생성"() {
    when:
    createItem()
    determineInvoice()
    createItem2()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 2

  }


  def "생성 - 취소 후 생성"() {
    when:
    createItem()
    cancelInvoice()
    createItem2()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotCreateException)
  }


  def "생성 - 수령 후 생성"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    createItem2()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotCreateException)
  }

  def "수정 - 작성 후 수정"() {
    when:
    createItem()
    updateItem()
    def item = invoiceItemService.get(id)

    then:
    item.quantity == 200
    item.remark == "품목 비고2"
  }

  def "수정 - 확정 후 수정"() {
    when:
    createItem()
    determineInvoice()
    updateItem()
    def item = invoiceItemService.get(id)

    then:
    item.quantity == 200
    item.remark == "품목 비고2"

  }

  def "수정 - 취소 후 수정"() {
    when:
    createItem()
    cancelInvoice()
    updateItem()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotUpdateException)
  }


  def "수정 - 수령 후 수정"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    updateItem()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotUpdateException)
  }


  def "삭제 - 작성 후 삭제"() {
    when:
    createItem()
    deleteItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 0
  }

  def "삭제 - 확정 후 삭제"() {
    when:
    createItem()
    determineInvoice()
    deleteItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 0

  }

  def "삭제 - 취소 후 삭제"() {
    when:
    createItem()
    cancelInvoice()
    deleteItem()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotDeleteException)
  }

  def "삭제 - 수령 후 삭제"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    deleteItem()
    then:
    thrown(OutsourcedInvoiceItemExceptions.CannotDeleteException)
  }


}
