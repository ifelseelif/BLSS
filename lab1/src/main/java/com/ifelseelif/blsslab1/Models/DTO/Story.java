package com.ifelseelif.blsslab1.Models.DTO;

import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
public class Story {
    private String header;
    private Date travelDate;
    private String briefInformation;
    private String mainText;
    private Set<Long> countries;
}
