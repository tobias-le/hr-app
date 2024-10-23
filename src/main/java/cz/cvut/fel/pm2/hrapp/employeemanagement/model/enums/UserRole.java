package cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;


import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets UserRole
 */

public enum UserRole {
  
  EMPLOYEE("EMPLOYEE"),
  
  MANAGER("MANAGER"),
  
  HR("HR");

  private String value;

  UserRole(String value) {
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
  public static UserRole fromValue(String value) {
    for (UserRole b : UserRole.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

