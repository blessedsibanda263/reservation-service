package com.github.blessedsibanda263.reservation.reservation;

import java.util.List;

public interface ReservationsRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);
}
