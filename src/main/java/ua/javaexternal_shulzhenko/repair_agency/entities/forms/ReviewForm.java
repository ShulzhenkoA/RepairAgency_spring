package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;

import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewForm {

    @NotBlank
    private String reviewContent;

    public Review extractReview(){
        return Review
                .builder()
                .reviewContent(reviewContent)
                .dateTime(LocalDateTime.now())
                .build();
    }
}
