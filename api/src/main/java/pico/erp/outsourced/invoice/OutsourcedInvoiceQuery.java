package pico.erp.outsourced.invoice;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcedInvoiceQuery {

  Page<OutsourcedInvoiceView> retrieve(@NotNull OutsourcedInvoiceView.Filter filter,
    @NotNull Pageable pageable);

}
