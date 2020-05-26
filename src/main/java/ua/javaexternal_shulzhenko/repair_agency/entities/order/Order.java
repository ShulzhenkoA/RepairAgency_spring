package ua.javaexternal_shulzhenko.repair_agency.entities.order;

import lombok.*;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "creation_date")
    private LocalDateTime date;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "repair_type")
    @Enumerated(EnumType.STRING)
    private RepairType repairType;

    @Column(name = "repair_description")
    private String repairDescription;

    @Column(name = "price")
    private double price;

    @OneToOne
    @JoinColumn(name = "master_id")
    private User master;

    @Column(name = "repair_completion_date")
    private LocalDateTime repairCompletionDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "manager_comment")
    private String managerComment;

    @Entity
    @Table(name = "cars")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Car{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "brand")
        private String carBrand;

        @Column(name = "model")
        private String carModel;

        @Column(name = "year")
        private String carYear;
    }
}
