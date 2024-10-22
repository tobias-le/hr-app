package cz.cvut.fel.pm2.hrapp.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * DepartmentsDepartmentIdEmployeesPostRequest
 */

@JsonTypeName("_departments__departmentId__employees_post_request")
public class DepartmentsDepartmentIdEmployeesPostRequest {

  private Integer employeeId;

  public DepartmentsDepartmentIdEmployeesPostRequest employeeId(Integer employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * The ID of the employee to add
   * @return employeeId
   */
  
  @Schema(name = "employeeId", description = "The ID of the employee to add", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employeeId")
  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DepartmentsDepartmentIdEmployeesPostRequest departmentsDepartmentIdEmployeesPostRequest = (DepartmentsDepartmentIdEmployeesPostRequest) o;
    return Objects.equals(this.employeeId, departmentsDepartmentIdEmployeesPostRequest.employeeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DepartmentsDepartmentIdEmployeesPostRequest {\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
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

