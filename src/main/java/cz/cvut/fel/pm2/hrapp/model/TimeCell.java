package cz.cvut.fel.pm2.hrapp.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * TimeCell
 */
public class TimeCell {

  private Integer timeCellId;

  private Employee employee;

  private Employee supervisor;

  /**
   * The status of the time cell
   */
  public enum StatusEnum {
    PENDING("Pending"),
    
    APPROVED("Approved"),
    
    DENIED("Denied");

    private String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  /**
   * The type of time off
   */
  public enum TimeOffTypeEnum {
    PAID("Paid"),
    
    UNPAID("Unpaid"),
    
    MEDICAL("Medical"),
    
    NONE("None");

    private String value;

    TimeOffTypeEnum(String value) {
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
    public static TimeOffTypeEnum fromValue(String value) {
      for (TimeOffTypeEnum b : TimeOffTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TimeOffTypeEnum timeOffType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startTimeDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endTimeDate;

  public TimeCell timeCellId(Integer timeCellId) {
    this.timeCellId = timeCellId;
    return this;
  }

  /**
   * The unique identifier for the time cell
   * @return timeCellId
   */
  
  @Schema(name = "timeCellId", description = "The unique identifier for the time cell", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeCellId")
  public Integer getTimeCellId() {
    return timeCellId;
  }

  public void setTimeCellId(Integer timeCellId) {
    this.timeCellId = timeCellId;
  }

  public TimeCell employee(Employee employee) {
    this.employee = employee;
    return this;
  }

  /**
   * Get employee
   * @return employee
   */
  @Valid
  @Schema(name = "employee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employee")
  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public TimeCell supervisor(Employee supervisor) {
    this.supervisor = supervisor;
    return this;
  }

  /**
   * Get supervisor
   * @return supervisor
   */
  @Valid 
  @Schema(name = "supervisor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supervisor")
  public Employee getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(Employee supervisor) {
    this.supervisor = supervisor;
  }

  public TimeCell status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * The status of the time cell
   * @return status
   */
  
  @Schema(name = "status", description = "The status of the time cell", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public TimeCell timeOffType(TimeOffTypeEnum timeOffType) {
    this.timeOffType = timeOffType;
    return this;
  }

  /**
   * The type of time off
   * @return timeOffType
   */
  
  @Schema(name = "timeOffType", description = "The type of time off", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOffType")
  public TimeOffTypeEnum getTimeOffType() {
    return timeOffType;
  }

  public void setTimeOffType(TimeOffTypeEnum timeOffType) {
    this.timeOffType = timeOffType;
  }

  public TimeCell startTimeDate(LocalDate startTimeDate) {
    this.startTimeDate = startTimeDate;
    return this;
  }

  /**
   * The start date and time of the time cell
   * @return startTimeDate
   */
  @Valid 
  @Schema(name = "startTimeDate", description = "The start date and time of the time cell", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startTimeDate")
  public LocalDate getStartTimeDate() {
    return startTimeDate;
  }

  public void setStartTimeDate(LocalDate startTimeDate) {
    this.startTimeDate = startTimeDate;
  }

  public TimeCell endTimeDate(LocalDate endTimeDate) {
    this.endTimeDate = endTimeDate;
    return this;
  }

  /**
   * The end date and time of the time cell
   * @return endTimeDate
   */
  @Valid 
  @Schema(name = "endTimeDate", description = "The end date and time of the time cell", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endTimeDate")
  public LocalDate getEndTimeDate() {
    return endTimeDate;
  }

  public void setEndTimeDate(LocalDate endTimeDate) {
    this.endTimeDate = endTimeDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeCell timeCell = (TimeCell) o;
    return Objects.equals(this.timeCellId, timeCell.timeCellId) &&
        Objects.equals(this.employee, timeCell.employee) &&
        Objects.equals(this.supervisor, timeCell.supervisor) &&
        Objects.equals(this.status, timeCell.status) &&
        Objects.equals(this.timeOffType, timeCell.timeOffType) &&
        Objects.equals(this.startTimeDate, timeCell.startTimeDate) &&
        Objects.equals(this.endTimeDate, timeCell.endTimeDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeCellId, employee, supervisor, status, timeOffType, startTimeDate, endTimeDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeCell {\n");
    sb.append("    timeCellId: ").append(toIndentedString(timeCellId)).append("\n");
    sb.append("    employee: ").append(toIndentedString(employee)).append("\n");
    sb.append("    supervisor: ").append(toIndentedString(supervisor)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timeOffType: ").append(toIndentedString(timeOffType)).append("\n");
    sb.append("    startTimeDate: ").append(toIndentedString(startTimeDate)).append("\n");
    sb.append("    endTimeDate: ").append(toIndentedString(endTimeDate)).append("\n");
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

