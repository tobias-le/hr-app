package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.model.HomeAddress;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Person
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T21:46:30.130008+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public class Person {

  private String name;

  private HomeAddress homeAddress;

  private String email;

  public Person name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The person's full name
   * @return name
   */
  
  @Schema(name = "name", description = "The person's full name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Person homeAddress(HomeAddress homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  /**
   * Get homeAddress
   * @return homeAddress
   */
  @Valid 
  @Schema(name = "homeAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("homeAddress")
  public HomeAddress getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(HomeAddress homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Person email(String email) {
    this.email = email;
    return this;
  }

  /**
   * The person's email address
   * @return email
   */
  @javax.validation.constraints.Email 
  @Schema(name = "email", description = "The person's email address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals(this.name, person.name) &&
        Objects.equals(this.homeAddress, person.homeAddress) &&
        Objects.equals(this.email, person.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, homeAddress, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Person {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

