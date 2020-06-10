package ua.javaexternal_shulzhenko.car_repair_agency.services.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;

public interface ReviewDatabaseService {

    void addReview(Review review);

    Page<Review> getPageableReviews(Pageable pageable);
}
