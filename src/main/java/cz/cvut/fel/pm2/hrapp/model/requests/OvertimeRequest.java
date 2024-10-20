package cz.cvut.fel.pm2.hrapp.model.requests;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.fel.pm2.hrapp.model.TimeCell;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;


/**
 * OvertimeRequest
 */
public class OvertimeRequest {

  private Integer overtimeId;

  private TimeCell timeCell;

  private Float hoursRequested;

  public OvertimeRequest overtimeId(Integer overtimeId) {
    this.overtimeId = overtimeId;
    return this;
  }

  /**
   * The unique identifier for the overtime request
   * @return overtimeId
   */
  
  @Schema(name = "overtimeId", description = "The unique identifier for the overtime request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overtimeId")
  public Integer getOvertimeId() {
    return overtimeId;
  }

  public void setOvertimeId(Integer overtimeId) {
    this.overtimeId = overtimeId;
  }

  public OvertimeRequest timeCell(TimeCell timeCell) {
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

  public OvertimeRequest hoursRequested(Float hoursRequested) {
    this.hoursRequested = hoursRequested;
    return this;
  }

  /**
   * The number of overtime hours requested
   * @return hoursRequested
   */
  
  @Schema(name = "hoursRequested", description = "The number of overtime hours requested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hoursRequested")
  public Float getHoursRequested() {
    return hoursRequested;
  }

  public void setHoursRequested(Float hoursRequested) {
    this.hoursRequested = hoursRequested;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OvertimeRequest overtimeRequest = (OvertimeRequest) o;
    return Objects.equals(this.overtimeId, overtimeRequest.overtimeId) &&
        Objects.equals(this.timeCell, overtimeRequest.timeCell) &&
        Objects.equals(this.hoursRequested, overtimeRequest.hoursRequested);
  }

  @Override
  public int hashCode() {
    return Objects.hash(overtimeId, timeCell, hoursRequested);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OvertimeRequest {\n");
    sb.append("    overtimeId: ").append(toIndentedString(overtimeId)).append("\n");
    sb.append("    timeCell: ").append(toIndentedString(timeCell)).append("\n");
    sb.append("    hoursRequested: ").append(toIndentedString(hoursRequested)).append("\n");
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

