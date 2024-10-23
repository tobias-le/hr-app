package cz.cvut.fel.pm2.hrapp.hoursmanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;

/**
 * WorkHours
 */
public class WorkHours {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer workHoursId;

  @ManyToOne
  @JoinColumn(name = "timeCellId", nullable = false)
  private TimeCell timeCell;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
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

