package com.github.blessedsibanda263.reservation.rest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.RestQuery;

import com.github.blessedsibanda263.reservation.inventory.Car;
import com.github.blessedsibanda263.reservation.inventory.InventoryClient;
import com.github.blessedsibanda263.reservation.reservation.Reservation;
import com.github.blessedsibanda263.reservation.reservation.ReservationsRepository;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {
    private final ReservationsRepository reservationsRepository;
    private final InventoryClient inventoryClient;

    public ReservationResource(ReservationsRepository reservations, InventoryClient inventoryClient) {
        this.reservationsRepository = reservations;
        this.inventoryClient = inventoryClient;
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
        var availableCars = inventoryClient.allCars();
        Map<Long, Car> carsById = new HashMap<>();
        for (Car car : availableCars) {
            carsById.put(car.id, car);
        }
        List<Reservation> reservations = reservationsRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.carId);
            }
        }
        return carsById.values();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Reservation make(Reservation reservation) {
        return reservationsRepository.save(reservation);
    }
}
