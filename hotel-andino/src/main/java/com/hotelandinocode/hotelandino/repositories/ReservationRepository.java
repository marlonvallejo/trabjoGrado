package com.hotelandinocode.hotelandino.repositories;

import com.hotelandinocode.hotelandino.entities.Reservation;
import com.hotelandinocode.hotelandino.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(Room room, LocalDate checkInDate, LocalDate checkOutDate);
    List<Reservation> findByRoomAndCheckOutDateAfter(Room room, LocalDate today);

    // Encuentra todas las reservas en un rango de fechas para todas las habitaciones
    List<Reservation> findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(LocalDate checkOutDate, LocalDate checkInDate);


}
