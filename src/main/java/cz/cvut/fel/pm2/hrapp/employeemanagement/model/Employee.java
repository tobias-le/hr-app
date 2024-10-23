package cz.cvut.fel.pm2.hrapp.employeemanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.ContractType;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.WorkPercentage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.data.annotation.Id;


/**
 * Employee
 */
@Entity
@Data
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer employeeId;

  @ManyToOne
  @JoinColumn(name = "departmentId")
  private Department department;

  private String name;

  @Embedded
  private String homeAddress;

  private String email;

  @OneToOne
  @JoinColumn(name = "userId")
  private User user;

  @Enumerated(EnumType.STRING)
  private ContractType contractType;

  @Enumerated(EnumType.STRING)
  private WorkPercentage workPercentage;

  @ManyToOne
  @JoinColumn(name = "supervisorId")
  private Employee supervisor;

  @Column(nullable = false)
  private Integer contractualHours;
    @jakarta.persistence.Id
    @GeneratedValue
    private Integer id;

  public Employee name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The person's full name
   * @return name
   */

  @Schema(name = "name", description = "The person's full name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Employee homeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  /**
   * Get homeAddress
   * @return homeAddress
   */
  @Valid
  @Schema(name = "homeAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("homeAddress")
  public String getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Employee email(String email) {
    this.email = email;
    return this;
  }

  /**
   * The person's email address
   * @return email
   */
  @Schema(name = "email", description = "The person's email address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Employee employeeId(Integer employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * The ID of the employee
   * @return employeeId
   */

  @Schema(name = "employeeId", description = "The ID of the employee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employeeId")
  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  public Employee department(Department department) {
    this.department = department;
    return this;
  }

  /**
   * Get department
   * @return department
   */
  @Valid
  @Schema(name = "department", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("department")
  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Employee user(User user) {
    this.user = user;
    return this;
  }

  /**
   * Get user
   * @return user
   */
  @Valid
  @Schema(name = "user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Employee contractType(ContractType contractType) {
    this.contractType = contractType;
    return this;
  }

  /**
   * Get contractType
   * @return contractType
   */
  @Valid
  @Schema(name = "contractType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contractType")
  public ContractType getContractType() {
    return contractType;
  }

  public void setContractType(ContractType contractType) {
    this.contractType = contractType;
  }

  public Employee workPercentage(WorkPercentage workPercentage) {
    this.workPercentage = workPercentage;
    return this;
  }

  /**
   * Get workPercentage
   * @return workPercentage
   */
  @Valid
  @Schema(name = "workPercentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("workPercentage")
  public WorkPercentage getWorkPercentage() {
    return workPercentage;
  }

  public void setWorkPercentage(WorkPercentage workPercentage) {
    this.workPercentage = workPercentage;
  }

  public Employee supervisor(Employee supervisor) {
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

  public Employee contractualHours(Integer contractualHours) {
    this.contractualHours = contractualHours;
    return this;
  }

  /**
   * Total hours per week
   * @return contractualHours
   */

  @Schema(name = "contractualHours", description = "Total hours per week", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contractualHours")
  public Integer getContractualHours() {
    return contractualHours;
  }

  public void setContractualHours(Integer contractualHours) {
    this.contractualHours = contractualHours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee employee = (Employee) o;
    return Objects.equals(this.name, employee.name) &&
        Objects.equals(this.homeAddress, employee.homeAddress) &&
        Objects.equals(this.email, employee.email) &&
        Objects.equals(this.employeeId, employee.employeeId) &&
        Objects.equals(this.department, employee.department) &&
        Objects.equals(this.user, employee.user) &&
        Objects.equals(this.contractType, employee.contractType) &&
        Objects.equals(this.workPercentage, employee.workPercentage) &&
        Objects.equals(this.supervisor, employee.supervisor) &&
        Objects.equals(this.contractualHours, employee.contractualHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, homeAddress, email, employeeId, department, user, contractType, workPercentage, supervisor, contractualHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Employee {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    contractType: ").append(toIndentedString(contractType)).append("\n");
    sb.append("    workPercentage: ").append(toIndentedString(workPercentage)).append("\n");
    sb.append("    supervisor: ").append(toIndentedString(supervisor)).append("\n");
    sb.append("    contractualHours: ").append(toIndentedString(contractualHours)).append("\n");
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

