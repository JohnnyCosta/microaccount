package org.micro.idgenerator;

import org.micro.domain.port.IdGenerator;

import java.util.UUID;

public class UUIDGenerator implements IdGenerator {

  @Override
  public String generate()  {
    return UUID
      .randomUUID().toString();
  }
}
