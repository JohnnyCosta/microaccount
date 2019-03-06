package org.micro.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAccountVM {
  private String accountId;
  private Float btcPrice;
}
