package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.openapitools.model.Employee;
import org.openapitools.model.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Payslip
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-19T21:46:30.130008+02:00[Europe/Prague]", comments = "Generator version: 7.9.0")
public class Payslip {

  private Integer payslipId;

  private Employee employee;

  private User generatedBy;

  private Float totalWorkedHours;

  private Float totalBonusHours;

  private Float totalSalary;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dateGenerated;

  public Payslip payslipId(Integer payslipId) {
    this.payslipId = payslipId;
    return this;
  }

  /**
   * The unique identifier for the payslip
   * @return payslipId
   */
  
  @Schema(name = "payslipId", description = "The unique identifier for the payslip", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payslipId")
  public Integer getPayslipId() {
    return payslipId;
  }

  public void setPayslipId(Integer payslipId) {
    this.payslipId = payslipId;
  }

  public Payslip employee(Employee employee) {
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

  public Payslip generatedBy(User generatedBy) {
    this.generatedBy = generatedBy;
    return this;
  }

  /**
   * Get generatedBy
   * @return generatedBy
   */
  @Valid 
  @Schema(name = "generatedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedBy")
  public User getGeneratedBy() {
    return generatedBy;
  }

  public void setGeneratedBy(User generatedBy) {
    this.generatedBy = generatedBy;
  }

  public Payslip totalWorkedHours(Float totalWorkedHours) {
    this.totalWorkedHours = totalWorkedHours;
    return this;
  }

  /**
   * Total worked hours
   * @return totalWorkedHours
   */
  
  @Schema(name = "totalWorkedHours", description = "Total worked hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalWorkedHours")
  public Float getTotalWorkedHours() {
    return totalWorkedHours;
  }

  public void setTotalWorkedHours(Float totalWorkedHours) {
    this.totalWorkedHours = totalWorkedHours;
  }

  public Payslip totalBonusHours(Float totalBonusHours) {
    this.totalBonusHours = totalBonusHours;
    return this;
  }

  /**
   * Total bonus hours
   * @return totalBonusHours
   */
  
  @Schema(name = "totalBonusHours", description = "Total bonus hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalBonusHours")
  public Float getTotalBonusHours() {
    return totalBonusHours;
  }

  public void setTotalBonusHours(Float totalBonusHours) {
    this.totalBonusHours = totalBonusHours;
  }

  public Payslip totalSalary(Float totalSalary) {
    this.totalSalary = totalSalary;
    return this;
  }

  /**
   * Total salary
   * @return totalSalary
   */
  
  @Schema(name = "totalSalary", description = "Total salary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalSalary")
  public Float getTotalSalary() {
    return totalSalary;
  }

  public void setTotalSalary(Float totalSalary) {
    this.totalSalary = totalSalary;
  }

  public Payslip dateGenerated(LocalDate dateGenerated) {
    this.dateGenerated = dateGenerated;
    return this;
  }

  /**
   * Date the payslip was generated
   * @return dateGenerated
   */
  @Valid 
  @Schema(name = "dateGenerated", description = "Date the payslip was generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dateGenerated")
  public LocalDate getDateGenerated() {
    return dateGenerated;
  }

  public void setDateGenerated(LocalDate dateGenerated) {
    this.dateGenerated = dateGenerated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Payslip payslip = (Payslip) o;
    return Objects.equals(this.payslipId, payslip.payslipId) &&
        Objects.equals(this.employee, payslip.employee) &&
        Objects.equals(this.generatedBy, payslip.generatedBy) &&
        Objects.equals(this.totalWorkedHours, payslip.totalWorkedHours) &&
        Objects.equals(this.totalBonusHours, payslip.totalBonusHours) &&
        Objects.equals(this.totalSalary, payslip.totalSalary) &&
        Objects.equals(this.dateGenerated, payslip.dateGenerated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payslipId, employee, generatedBy, totalWorkedHours, totalBonusHours, totalSalary, dateGenerated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Payslip {\n");
    sb.append("    payslipId: ").append(toIndentedString(payslipId)).append("\n");
    sb.append("    employee: ").append(toIndentedString(employee)).append("\n");
    sb.append("    generatedBy: ").append(toIndentedString(generatedBy)).append("\n");
    sb.append("    totalWorkedHours: ").append(toIndentedString(totalWorkedHours)).append("\n");
    sb.append("    totalBonusHours: ").append(toIndentedString(totalBonusHours)).append("\n");
    sb.append("    totalSalary: ").append(toIndentedString(totalSalary)).append("\n");
    sb.append("    dateGenerated: ").append(toIndentedString(dateGenerated)).append("\n");
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

