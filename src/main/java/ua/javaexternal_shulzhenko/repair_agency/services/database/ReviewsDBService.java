package ua.javaexternal_shulzhenko.repair_agency.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.repair_agency.services.database.repository.ReviewsRepository;

@Service
public final class ReviewsDBService {

    private static ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsDBService(ReviewsRepository reviewsRepository) {
        ReviewsDBService.reviewsRepository = reviewsRepository;
    }

    public static void addReview(Review review){
        reviewsRepository.save(review);
    }

    public static Page<Review> getPageableReviews(Pageable pageable){
        return reviewsRepository.findAll(pageable);
    }
}
