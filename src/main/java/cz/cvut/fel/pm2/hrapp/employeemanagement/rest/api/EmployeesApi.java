/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.9.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package cz.cvut.fel.pm2.hrapp.employeemanagement.rest.api;

import cz.cvut.fel.pm2.hrapp.configuration.ApiUtil;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.utilobjects.PayslipsGet200Response;
import cz.cvut.fel.pm2.hrapp.utilobjects.PayslipsPost201Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.List;
import java.util.Optional;

@Validated
@Tag(name = "employees", description = "the employees API")
public interface EmployeesApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * DELETE /employees/{employeeId} : Delete a specific employee
     *
     * @param employeeId ID of the employee (required)
     * @param authorization Bearer token for authorization (required)
     * @return No Content (status code 204)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesEmployeeIdDelete",
        summary = "Delete a specific employee",
        responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/employees/{employeeId}",
        produces = { "application/json" }
    )
    
    default ResponseEntity<Void> employeesEmployeeIdDelete(
        @Parameter(name = "employeeId", description = "ID of the employee", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /employees/{employeeId} : Get details of a specific employee
     *
     * @param employeeId ID of the employee (required)
     * @param authorization Bearer token for authorization (required)
     * @return OK (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesEmployeeIdGet",
        summary = "Get details of a specific employee",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = PayslipsGet200Response.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/employees/{employeeId}",
        produces = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> employeesEmployeeIdGet(
        @Parameter(name = "employeeId", description = "ID of the employee", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Success\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /employees/{employeeId} : Update an existing employee
     *
     * @param employeeId ID of the employee (required)
     * @param authorization Bearer token for authorization (required)
     * @param employee  (required)
     * @return OK (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesEmployeeIdPut",
        summary = "Update an existing employee",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = PayslipsGet200Response.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/employees/{employeeId}",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> employeesEmployeeIdPut(
        @Parameter(name = "employeeId", description = "ID of the employee", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "Employee", description = "", required = true) @Valid @RequestBody Employee employee
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Success\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /employees/{employeeId}/subordinates : Get a list of all subordinates for a specific supervisor
     *
     * @param employeeId ID of the supervisor (required)
     * @param authorization Bearer token for authorization (required)
     * @return List of subordinates (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesEmployeeIdSubordinatesGet",
        summary = "Get a list of all subordinates for a specific supervisor",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of subordinates", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/employees/{employeeId}/subordinates",
        produces = { "application/json" }
    )
    
    default ResponseEntity<List<Employee>> employeesEmployeeIdSubordinatesGet(
        @Parameter(name = "employeeId", description = "ID of the supervisor", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"workPercentage\" : \"FULL_TIME\", \"contractualHours\" : 5, \"contractType\" : \"FULL_TIME\", \"name\" : \"name\", \"employeeId\" : 6, \"department\" : { \"departmentName\" : \"departmentName\", \"departmentId\" : 1 }, \"user\" : { \"password\" : \"password\", \"role\" : \"EMPLOYEE\", \"userId\" : 5, \"username\" : \"username\" }, \"email\" : \"email\", \"homeAddress\" : { \"country\" : \"country\", \"city\" : \"city\", \"street\" : \"street\", \"postalCode\" : \"postalCode\" } }, { \"workPercentage\" : \"FULL_TIME\", \"contractualHours\" : 5, \"contractType\" : \"FULL_TIME\", \"name\" : \"name\", \"employeeId\" : 6, \"department\" : { \"departmentName\" : \"departmentName\", \"departmentId\" : 1 }, \"user\" : { \"password\" : \"password\", \"role\" : \"EMPLOYEE\", \"userId\" : 5, \"username\" : \"username\" }, \"email\" : \"email\", \"homeAddress\" : { \"country\" : \"country\", \"city\" : \"city\", \"street\" : \"street\", \"postalCode\" : \"postalCode\" } } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /employees/{employeeId}/supervisor : Get supervisor details for a specific employee
     *
     * @param employeeId ID of the employee (required)
     * @param authorization Bearer token for authorization (required)
     * @return Supervisor details (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesEmployeeIdSupervisorGet",
        summary = "Get supervisor details for a specific employee",
        responses = {
            @ApiResponse(responseCode = "200", description = "Supervisor details", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/employees/{employeeId}/supervisor",
        produces = { "application/json" }
    )
    
    default ResponseEntity<Employee> employeesEmployeeIdSupervisorGet(
        @Parameter(name = "employeeId", description = "ID of the employee", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"workPercentage\" : \"FULL_TIME\", \"contractualHours\" : 5, \"contractType\" : \"FULL_TIME\", \"name\" : \"name\", \"employeeId\" : 6, \"department\" : { \"departmentName\" : \"departmentName\", \"departmentId\" : 1 }, \"user\" : { \"password\" : \"password\", \"role\" : \"EMPLOYEE\", \"userId\" : 5, \"username\" : \"username\" }, \"email\" : \"email\", \"homeAddress\" : { \"country\" : \"country\", \"city\" : \"city\", \"street\" : \"street\", \"postalCode\" : \"postalCode\" } }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /employees : Get a list of all employees
     *
     * @param authorization Bearer token for authorization (required)
     * @param departmentId Filter employees by department ID (optional)
     * @param role  (optional)
     * @return OK (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesGet",
        summary = "Get a list of all employees",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = PayslipsGet200Response.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/employees",
        produces = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> employeesGet(
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "departmentId", description = "Filter employees by department ID", in = ParameterIn.QUERY) @Valid @RequestParam(value = "departmentId", required = false) Integer departmentId,
        @Parameter(name = "role", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "role", required = false) String role
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Success\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /employees : Create a new employee
     *
     * @param authorization Bearer token for authorization (required)
     * @param employee  (required)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "employeesPost",
        summary = "Create a new employee",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = PayslipsPost201Response.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/employees",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<PayslipsPost201Response> employeesPost(
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "Employee", description = "", required = true) @Valid @RequestBody Employee employee
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Resource created successfully\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Error message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}