package cz.cvut.fel.pm2.hrapp.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.processing.Generated;


import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets ContractType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T17:11:43.705298+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public enum ContractType {
  
  FULL_TIME("FULL_TIME"),
  
  PART_TIME("PART_TIME"),
  
  CONTRACT("CONTRACT");

  private String value;

  ContractType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ContractType fromValue(String value) {
    for (ContractType b : ContractType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

