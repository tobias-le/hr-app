package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.model.TimeCell;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * TimeOffRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T21:46:30.130008+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public class TimeOffRequest {

  private Integer timeOffId;

  private TimeCell timeCell;

  private String reason;

  public TimeOffRequest timeOffId(Integer timeOffId) {
    this.timeOffId = timeOffId;
    return this;
  }

  /**
   * The unique identifier for the time off request
   * @return timeOffId
   */
  
  @Schema(name = "timeOffId", description = "The unique identifier for the time off request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOffId")
  public Integer getTimeOffId() {
    return timeOffId;
  }

  public void setTimeOffId(Integer timeOffId) {
    this.timeOffId = timeOffId;
  }

  public TimeOffRequest timeCell(TimeCell timeCell) {
    this.timeCell = timeCell;
    return this;
  }

  /**
   * Get timeCell
   * @return timeCell
   */
  @Valid 
  @Schema(name = "timeCell", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeCell")
  public TimeCell getTimeCell() {
    return timeCell;
  }

  public void setTimeCell(TimeCell timeCell) {
    this.timeCell = timeCell;
  }

  public TimeOffRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * The reason for the time off request
   * @return reason
   */
  
  @Schema(name = "reason", description = "The reason for the time off request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeOffRequest timeOffRequest = (TimeOffRequest) o;
    return Objects.equals(this.timeOffId, timeOffRequest.timeOffId) &&
        Objects.equals(this.timeCell, timeOffRequest.timeCell) &&
        Objects.equals(this.reason, timeOffRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOffId, timeCell, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeOffRequest {\n");
    sb.append("    timeOffId: ").append(toIndentedString(timeOffId)).append("\n");
    sb.append("    timeCell: ").append(toIndentedString(timeCell)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

