package ua.javaexternal_shulzhenko.car_repair_agency.services.database.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.ReviewDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.repository.ReviewsRepository;

@Service
public class ReviewsDatabaseInteractionService implements ReviewDatabaseService {

    private static ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsDatabaseInteractionService(ReviewsRepository reviewsRepository) {
        ReviewsDatabaseInteractionService.reviewsRepository = reviewsRepository;
    }

    public void addReview(Review review){
        reviewsRepository.save(review);
    }

    public Page<Review> getPageableReviews(Pageable pageable){
        return reviewsRepository.findAll(pageable);
    }
}
