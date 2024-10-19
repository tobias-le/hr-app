package cz.cvut.fel.pm2.hrapp.model.requests;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.fel.pm2.hrapp.model.TimeCell;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;

/**
 * TimeOffRequest
 */
public class TimeOffRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer timeOffId;

  @ManyToOne
  @JoinColumn(name = "timeCellId", nullable = false)
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

