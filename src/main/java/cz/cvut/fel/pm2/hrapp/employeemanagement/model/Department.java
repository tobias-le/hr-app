package cz.cvut.fel.pm2.hrapp.employeemanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Department
 */
@Entity
@Data
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer departmentId;

  @Column(nullable = false)
  private String departmentName;
    @jakarta.persistence.Id
    @GeneratedValue
    private Integer id;

  public Department departmentId(Integer departmentId) {
    this.departmentId = departmentId;
    return this;
  }

  /**
   * Get departmentId
   * @return departmentId
   */
  
  @Schema(name = "departmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departmentId")
  public Integer getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
  }

  public Department departmentName(String departmentName) {
    this.departmentName = departmentName;
    return this;
  }

  /**
   * The name of the department
   * @return departmentName
   */
  
  @Schema(name = "departmentName", description = "The name of the department", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departmentName")
  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Department department = (Department) o;
    return Objects.equals(this.departmentId, department.departmentId) &&
        Objects.equals(this.departmentName, department.departmentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departmentId, departmentName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Department {\n");
    sb.append("    departmentId: ").append(toIndentedString(departmentId)).append("\n");
    sb.append("    departmentName: ").append(toIndentedString(departmentName)).append("\n");
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

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}

