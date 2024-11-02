package dmit2015.dto;

import lombok.Data;

@Data
public class JobDto {

    private String jobId;
    private String jobTitle;
    private Integer minSalary;
    private Integer maxSalary;

}
