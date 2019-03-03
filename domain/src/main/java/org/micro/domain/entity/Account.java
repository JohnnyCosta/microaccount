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
public class Account {

  @NonNull
	private String id;

  @NonNull
	private String name;

  @NonNull
  private Long usd;

  @NonNull
  private Long btc;

}
