package pico.erp.outsourced.invoice

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.company.CompanyId
import pico.erp.invoice.InvoiceApplication
import pico.erp.invoice.InvoiceRequests
import pico.erp.invoice.InvoiceService
import pico.erp.item.ItemApplication
import pico.erp.outsourced.invoice.item.OutsourcedInvoiceItemService
import pico.erp.process.ProcessApplication
import pico.erp.project.ProjectApplication
import pico.erp.project.ProjectId
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.Address
import pico.erp.user.UserApplication
import pico.erp.user.UserId
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest(classes = [OutsourcedInvoiceApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [
  UserApplication, ItemApplication, ProjectApplication, ProcessApplication, CompanyApplication, InvoiceApplication
])
@Transactional
@Rollback
@ActiveProfiles("test")
class OutsourcedInvoiceServiceSpec extends Specification {

  @Autowired
  OutsourcedInvoiceService outsourcedInvoiceService

  @Autowired
  OutsourcedInvoiceItemService invoiceItemService

  @Lazy
  @Autowired
  InvoiceService invoiceService

  def id = OutsourcedInvoiceId.from("outsourced-invoice-1")

  def id2 = OutsourcedInvoiceId.from("outsourced-invoice-2")

  def unknownId = OutsourcedInvoiceId.from("unknown")

  def dueDate = LocalDateTime.now().plusDays(7)

  def remark = "요청 비고"

  def dueDate2 = LocalDateTime.now().plusDays(8)

  def remark2 = "요청 비고2"

  def receiverId = CompanyId.from("CUST1")

  def senderId = CompanyId.from("SUPP1")

  def confirmerId = UserId.from("kjh")

  def projectId = ProjectId.from("sample-project1")

  def receiveAddress = new Address(
    postalCode: '13496',
    street: '경기도 성남시 분당구 장미로 42',
    detail: '야탑리더스 410호'
  )


  def setup() {
    outsourcedInvoiceService.create(
      new OutsourcedInvoiceRequests.CreateRequest(
        id: id,
        receiverId: receiverId,
        senderId: senderId,
        projectId: projectId,
        receiveAddress: receiveAddress,
        dueDate: dueDate,
        remark: remark
      )
    )
  }

  def cancelInvoice() {
    outsourcedInvoiceService.cancel(
      new OutsourcedInvoiceRequests.CancelRequest(
        id: id
      )
    )
  }

  def determineInvoice() {
    outsourcedInvoiceService.determine(
      new OutsourcedInvoiceRequests.DetermineRequest(
        id: id
      )
    )
  }


  def receiveInvoice() {
    outsourcedInvoiceService.receive(
      new OutsourcedInvoiceRequests.ReceiveRequest(
        id: id
      )
    )
  }


  def updateInvoice() {
    outsourcedInvoiceService.update(
      new OutsourcedInvoiceRequests.UpdateRequest(
        id: id,
        receiverId: receiverId,
        senderId: senderId,
        receiveAddress: receiveAddress,
        projectId: projectId,
        dueDate: dueDate2,
        remark: remark2
      )
    )
  }


  def receiveInvoiceBy() {
    def invoice = outsourcedInvoiceService.get(id)
    invoiceService.receive(
      new InvoiceRequests.ReceiveRequest(
        id: invoice.invoiceId,
        confirmerId: confirmerId
      )
    )
  }


  def "존재 - 아이디로 존재 확인"() {
    when:
    def exists = outsourcedInvoiceService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = outsourcedInvoiceService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def invoice = outsourcedInvoiceService.get(id)

    then:
    invoice.id == id
    invoice.remark == remark
    invoice.dueDate == dueDate
    invoice.receiverId == receiverId
    invoice.senderId == senderId
    invoice.receiveAddress == receiveAddress

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    outsourcedInvoiceService.get(unknownId)

    then:
    thrown(OutsourcedInvoiceExceptions.NotFoundException)
  }


  def "수정 - 취소 후 수정"() {
    when:
    cancelInvoice()
    updateInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotUpdateException)
  }


  def "수정 - 확정 후 수정"() {
    when:
    determineInvoice()
    updateInvoice()
    def invoice = outsourcedInvoiceService.get(id)
    then:
    invoice.dueDate == dueDate2
    invoice.remark == remark2
  }

  def "수정 - 수령 후 수정"() {
    when:
    determineInvoice()
    receiveInvoice()
    updateInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotUpdateException)
  }


  def "수정 - 작성 후 수정"() {
    when:
    updateInvoice()
    def invoice = outsourcedInvoiceService.get(id)

    then:
    invoice.dueDate == dueDate2
    invoice.remark == remark2
  }

  def "확정 - 작성 후 확정"() {
    when:
    determineInvoice()
    def invoice = outsourcedInvoiceService.get(id)
    then:
    invoice.status == OutsourcedInvoiceStatusKind.DETERMINED
    invoice.invoiceId != null
  }

  def "확정 - 확정 후 확정"() {
    when:
    determineInvoice()
    determineInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotDetermineException)
  }


  def "확정 - 취소 후 확정"() {
    when:
    cancelInvoice()
    determineInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotDetermineException)
  }

  def "확정 - 수령 후 확정"() {
    when:
    determineInvoice()
    receiveInvoice()
    determineInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotDetermineException)
  }

  def "취소 - 취소 후에는 취소"() {
    when:
    cancelInvoice()
    cancelInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotCancelException)
  }

  def "취소 - 확정 후 취소"() {
    when:
    determineInvoice()
    cancelInvoice()
    def invoice = outsourcedInvoiceService.get(id)
    then:
    invoice.status == OutsourcedInvoiceStatusKind.CANCELED
  }


  def "취소 - 수령 후 취소"() {
    when:
    determineInvoice()
    receiveInvoice()
    cancelInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotCancelException)
  }

  def "수령 - 작성 후 수령"() {
    when:
    receiveInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotReceiveException)
  }

  def "수령 - 확정 후 수령"() {
    when:
    determineInvoice()
    receiveInvoiceBy()
    def invoice = outsourcedInvoiceService.get(id)
    then:
    invoice.status == OutsourcedInvoiceStatusKind.RECEIVED

  }


  def "수령 - 취소 후 수령"() {
    when:
    cancelInvoice()
    receiveInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotReceiveException)
  }


  def "수령 - 수령 후 수령"() {
    when:
    determineInvoice()
    receiveInvoice()
    receiveInvoice()
    then:
    thrown(OutsourcedInvoiceExceptions.CannotReceiveException)
  }


}
