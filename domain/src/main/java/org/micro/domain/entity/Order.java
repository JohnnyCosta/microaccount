package org.micro.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @NonNull
  private String orderId;

  @NonNull
	private String accountId;

  @NonNull
  private Float priceLimit;

  private Float btcPrice;

  @NonNull
  private Boolean finished;

}
