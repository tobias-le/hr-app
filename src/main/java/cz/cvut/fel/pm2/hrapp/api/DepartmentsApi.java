/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.9.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package cz.cvut.fel.pm2.hrapp.api;

import cz.cvut.fel.pm2.hrapp.model.*;
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

import java.lang.Error;
import java.util.List;
import java.util.Optional;

@Validated
@Tag(name = "departments", description = "the departments API")
public interface DepartmentsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * DELETE /departments/{departmentId} : Delete a specific department
     *
     * @param departmentId ID of the department (required)
     * @param authorization Bearer token for authorization (required)
     * @return No Content (status code 204)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdDelete",
        summary = "Delete a specific department",
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
        value = "/departments/{departmentId}",
        produces = { "application/json" }
    )
    
    default ResponseEntity<Void> departmentsDepartmentIdDelete(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
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
     * DELETE /departments/{departmentId}/employees/{employeeId} : Remove an employee from a department
     *
     * @param departmentId ID of the department (required)
     * @param employeeId ID of the employee to be removed (required)
     * @param authorization Bearer token for authorization (required)
     * @return Employee removed from the department (status code 204)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdEmployeesEmployeeIdDelete",
        summary = "Remove an employee from a department",
        responses = {
            @ApiResponse(responseCode = "204", description = "Employee removed from the department"),
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
        method = RequestMethod.DELETE,
        value = "/departments/{departmentId}/employees/{employeeId}",
        produces = { "application/json" }
    )
    
    default ResponseEntity<Void> departmentsDepartmentIdEmployeesEmployeeIdDelete(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
        @Parameter(name = "employeeId", description = "ID of the employee to be removed", required = true, in = ParameterIn.PATH) @PathVariable("employeeId") Integer employeeId,
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
     * GET /departments/{departmentId}/employees : Get a list of all employees in a specific department
     *
     * @param departmentId ID of the department (required)
     * @param authorization Bearer token for authorization (required)
     * @return List of employees in the department (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdEmployeesGet",
        summary = "Get a list of all employees in a specific department",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of employees in the department", content = {
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
        value = "/departments/{departmentId}/employees",
        produces = { "application/json" }
    )
    
    default ResponseEntity<List<Employee>> departmentsDepartmentIdEmployeesGet(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
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
     * POST /departments/{departmentId}/employees : Add an employee to a department
     *
     * @param departmentId ID of the department (required)
     * @param authorization Bearer token for authorization (required)
     * @param departmentsDepartmentIdEmployeesPostRequest  (required)
     * @return Employee added to the department (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdEmployeesPost",
        summary = "Add an employee to a department",
        responses = {
            @ApiResponse(responseCode = "201", description = "Employee added to the department"),
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
        method = RequestMethod.POST,
        value = "/departments/{departmentId}/employees",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Void> departmentsDepartmentIdEmployeesPost(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "DepartmentsDepartmentIdEmployeesPostRequest", description = "", required = true) @Valid @RequestBody DepartmentsDepartmentIdEmployeesPostRequest departmentsDepartmentIdEmployeesPostRequest
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
     * GET /departments/{departmentId} : Get details of a specific department
     *
     * @param departmentId ID of the department (required)
     * @param authorization Bearer token for authorization (required)
     * @return OK (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdGet",
        summary = "Get details of a specific department",
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
        value = "/departments/{departmentId}",
        produces = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> departmentsDepartmentIdGet(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
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
     * PUT /departments/{departmentId} : Update an existing department
     *
     * @param departmentId ID of the department (required)
     * @param authorization Bearer token for authorization (required)
     * @param department  (required)
     * @return OK (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Resource not found (status code 404)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsDepartmentIdPut",
        summary = "Update an existing department",
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
        value = "/departments/{departmentId}",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> departmentsDepartmentIdPut(
        @Parameter(name = "departmentId", description = "ID of the department", required = true, in = ParameterIn.PATH) @PathVariable("departmentId") Integer departmentId,
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "Department", description = "", required = true) @Valid @RequestBody Department department
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
     * GET /departments : Get a list of all departments
     *
     * @param authorization Bearer token for authorization (required)
     * @return OK (status code 200)
     *         or Unauthorized access (status code 401)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsGet",
        summary = "Get a list of all departments",
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
        value = "/departments",
        produces = { "application/json" }
    )
    
    default ResponseEntity<PayslipsGet200Response> departmentsGet(
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
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /departments : Create a new department
     *
     * @param authorization Bearer token for authorization (required)
     * @param department  (required)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized access (status code 401)
     *         or Internal server error (status code 500)
     */
    @Operation(
        operationId = "departmentsPost",
        summary = "Create a new department",
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
        value = "/departments",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<PayslipsPost201Response> departmentsPost(
        @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
        @Parameter(name = "Department", description = "", required = true) @Valid @RequestBody Department department
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