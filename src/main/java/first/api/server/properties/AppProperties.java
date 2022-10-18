package first.api.server.properties;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import first.api.server.context.BaseConstants;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class AppProperties {

  /** Locale resolver (cookie or session) */
  private String localeResolver = "cookie";

  /** name */
  private String name;

  /** version */
  private String version;

  /** description */
  private String description;

  /** build date */
  private String buildDate;

  /** Contact name */
  private String contactName;

  /** Contact name */
  private String contactUrl;

  /** Contact name */
  private String contactEmail;

  /** log appenders */
  private String[] logAppenders =
      new String[] {BaseConstants.LOG_APPENDER_CONSOLE, BaseConstants.LOG_APPENDER_FILE};


  @Override
  public String toString() {
    return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
