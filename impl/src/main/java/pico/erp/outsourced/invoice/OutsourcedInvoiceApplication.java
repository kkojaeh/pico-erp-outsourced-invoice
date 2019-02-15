package pico.erp.outsourced.invoice;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.AuditApi;
import pico.erp.invoice.InvoiceApi;
import pico.erp.item.ItemApi;
import pico.erp.outsourced.invoice.OutsourcedInvoiceApi.Roles;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.user.UserApi;

@Slf4j
@SpringBootConfigs
public class OutsourcedInvoiceApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "outsourced-invoice/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(OutsourcedInvoiceApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Override
  public Set<ApplicationId> getDependencies() {
    return Stream.of(
      UserApi.ID,
      ItemApi.ID,
      AuditApi.ID,
      InvoiceApi.ID
    ).collect(Collectors.toSet());
  }

  @Override
  public ApplicationId getId() {
    return OutsourcedInvoiceApi.ID;
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  @Public
  public Role outsourcedInvoiceManager() {
    return Roles.OUTSOURCED_INVOICE_MANAGER;
  }

  @Bean
  @Public
  public Role outsourcedInvoicePublisher() {
    return Roles.OUTSOURCED_INVOICE_PUBLISHER;
  }


  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
