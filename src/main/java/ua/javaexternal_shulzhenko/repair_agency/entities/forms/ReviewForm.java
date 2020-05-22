package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewForm implements Form{

    private String customerID;
    @NotBlank
    private String reviewContent;
    private LocalDateTime dateTime;
}
