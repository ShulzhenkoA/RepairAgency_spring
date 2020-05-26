package ua.javaexternal_shulzhenko.repair_agency.services.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Integer> {
}
