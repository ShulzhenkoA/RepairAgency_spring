package ua.javaexternal_shulzhenko.car_repair_agency.services.database.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.repository.ReviewsRepository;

import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewsDatabaseInteractionServiceTest {

    @Autowired
    private ReviewsDatabaseInteractionService reviewsDatabaseInteractionService;

    @MockBean
    private ReviewsRepository reviewsRepository;

    @Test
    public void addingReview_savesReview(){
        Review review = mock(Review.class);
        reviewsDatabaseInteractionService.addReview(review);
        verify(reviewsRepository, times(1)).save(review);
    }

    @Test
    public void gettingPageableReviews_findsAllReviews(){
        Pageable pageable = Pageable.unpaged();
        reviewsDatabaseInteractionService.getPageableReviews(pageable);
        verify(reviewsRepository, times(1)).findAll(pageable);
    }
}