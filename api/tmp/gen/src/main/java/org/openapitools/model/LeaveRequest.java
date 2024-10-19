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
 * LeaveRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T21:46:30.130008+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public class LeaveRequest {

  private Integer leaveId;

  private TimeCell timeCell;

  private String reason;

  public LeaveRequest leaveId(Integer leaveId) {
    this.leaveId = leaveId;
    return this;
  }

  /**
   * The unique identifier for the leave request
   * @return leaveId
   */
  
  @Schema(name = "leaveId", description = "The unique identifier for the leave request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaveId")
  public Integer getLeaveId() {
    return leaveId;
  }

  public void setLeaveId(Integer leaveId) {
    this.leaveId = leaveId;
  }

  public LeaveRequest timeCell(TimeCell timeCell) {
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

  public LeaveRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * The reason for the leave request
   * @return reason
   */
  
  @Schema(name = "reason", description = "The reason for the leave request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    LeaveRequest leaveRequest = (LeaveRequest) o;
    return Objects.equals(this.leaveId, leaveRequest.leaveId) &&
        Objects.equals(this.timeCell, leaveRequest.timeCell) &&
        Objects.equals(this.reason, leaveRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaveId, timeCell, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaveRequest {\n");
    sb.append("    leaveId: ").append(toIndentedString(leaveId)).append("\n");
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

