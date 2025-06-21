package com.example.lab9.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String person;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "HotelRoomId", nullable = false)
    private HotelRoom hotelRoom;

    @Column(nullable = false)
    private LocalDate checkInDate = LocalDate.now();

    @Column(nullable = false)
    private LocalDate checkOutDate = LocalDate.now();

    @Column(nullable = false)

    private Integer numberOfGuests;

    @Column(nullable = false)
    private Integer totalPrice;

    // @ManyToOne
    // @JoinColumn(name = "document_id", nullable = false)
    // private Document document;

}
