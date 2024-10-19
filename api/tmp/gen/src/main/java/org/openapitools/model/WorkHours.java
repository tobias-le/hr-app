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
 * WorkHours
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T21:46:30.130008+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public class WorkHours {

  private Integer workHoursId;

  private TimeCell timeCell;

  private String title;

  private Float hoursWorked;

  public WorkHours workHoursId(Integer workHoursId) {
    this.workHoursId = workHoursId;
    return this;
  }

  /**
   * The unique identifier for the work hours entry
   * @return workHoursId
   */
  
  @Schema(name = "workHoursId", description = "The unique identifier for the work hours entry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("workHoursId")
  public Integer getWorkHoursId() {
    return workHoursId;
  }

  public void setWorkHoursId(Integer workHoursId) {
    this.workHoursId = workHoursId;
  }

  public WorkHours timeCell(TimeCell timeCell) {
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

  public WorkHours title(String title) {
    this.title = title;
    return this;
  }

  /**
   * The title or description of the work done
   * @return title
   */
  
  @Schema(name = "title", description = "The title or description of the work done", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public WorkHours hoursWorked(Float hoursWorked) {
    this.hoursWorked = hoursWorked;
    return this;
  }

  /**
   * The number of hours worked
   * @return hoursWorked
   */
  
  @Schema(name = "hoursWorked", description = "The number of hours worked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hoursWorked")
  public Float getHoursWorked() {
    return hoursWorked;
  }

  public void setHoursWorked(Float hoursWorked) {
    this.hoursWorked = hoursWorked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkHours workHours = (WorkHours) o;
    return Objects.equals(this.workHoursId, workHours.workHoursId) &&
        Objects.equals(this.timeCell, workHours.timeCell) &&
        Objects.equals(this.title, workHours.title) &&
        Objects.equals(this.hoursWorked, workHours.hoursWorked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workHoursId, timeCell, title, hoursWorked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkHours {\n");
    sb.append("    workHoursId: ").append(toIndentedString(workHoursId)).append("\n");
    sb.append("    timeCell: ").append(toIndentedString(timeCell)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    hoursWorked: ").append(toIndentedString(hoursWorked)).append("\n");
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

