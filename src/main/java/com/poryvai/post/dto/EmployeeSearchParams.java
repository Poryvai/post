package com.poryvai.post.dto;

import com.poryvai.post.model.EmployeePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for filtering employees based on dynamic search criteria.
 * This class encapsulates various optional parameters that can be used to construct
 * a dynamic query for retrieving a list of employee entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchParams {
    /**
     * The unique identifier for the employee.
     */
    private Long employeeId;

    /**
     * The first name of the employee, supporting partial matches.
     */
    private String firstName;

    /**
     * The last name of the employee, supporting partial matches.
     */
    private String lastName;

    /**
     * The position or role of the employee.
     */
    private EmployeePosition position;

    /**
     * The unique identifier of the post office where the employee works.
     */
    private Long postOfficeId;

    /**
     * The minimum number of parcels an employee has processed (received or sent).
     */
    private Integer minProcessedParcels;

    /**
     * The maximum number of parcels an employee has processed (received or sent).
     */
    private Integer maxProcessedParcels;

    /**
     * The start of the time range for an employee's last activity.
     */
    private LocalDateTime lastActivityAfter;

    /**
     * The end of the time range for an employee's last activity.
     */
    private LocalDateTime lastActivityBefore;
}
