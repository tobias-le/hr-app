package cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;


import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets WorkPercentage
 */

public enum WorkPercentage {
  
  FULL_TIME("FULL_TIME"),
  
  PART_TIME("PART_TIME");

  private String value;

  WorkPercentage(String value) {
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
  public static WorkPercentage fromValue(String value) {
    for (WorkPercentage b : WorkPercentage.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

