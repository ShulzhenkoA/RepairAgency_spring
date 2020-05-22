package ua.javaexternal_shulzhenko.repair_agency.entities.review;

import lombok.*;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "reviews")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "review_date")
    private LocalDateTime dateTime;

    @Column(name = "review_content")
    private String reviewContent;
}