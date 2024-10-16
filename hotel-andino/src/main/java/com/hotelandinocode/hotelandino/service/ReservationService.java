package com.hotelandinocode.hotelandino.service;

import com.hotelandinocode.hotelandino.entities.Reservation;
import com.hotelandinocode.hotelandino.entities.Room;
import com.hotelandinocode.hotelandino.entities.User;
import com.hotelandinocode.hotelandino.repositories.ReservationRepository;
import com.hotelandinocode.hotelandino.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Reservation> reservations = reservationRepository.findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(room, checkOutDate, checkInDate);
        return reservations.isEmpty(); // Si no hay reservas, la habitación está disponible
    }

    public Reservation createReservation(User customer, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Double totalPrice) {
        if (!isRoomAvailable(roomId, checkInDate, checkOutDate)) {
            throw new IllegalStateException("La habitación no está disponible para estas fechas.");
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .room(room)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .totalPrice(totalPrice)
                .build();

        return reservationRepository.save(reservation);
    }

    /**
     * Devuelve las fechas en las que la habitación está reservada, desde hoy en adelante.
     */
    public List<LocalDate[]> getRoomAvailability(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));

        // Obtener todas las reservas futuras
        List<Reservation> reservations = reservationRepository.findByRoomAndCheckOutDateAfter(room, LocalDate.now());

        // Lista para almacenar los rangos de fechas reservadas
        List<LocalDate[]> bookedRanges = new ArrayList<>();

        for (Reservation reservation : reservations) {
            LocalDate[] range = new LocalDate[]{reservation.getCheckInDate(), reservation.getCheckOutDate()};
            bookedRanges.add(range);
        }

        return bookedRanges;
    }

    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        // Obtener todas las habitaciones
        List<Room> allRooms = roomRepository.findAll();

        // Obtener todas las reservas en el rango de fechas proporcionado
        List<Reservation> conflictingReservations = reservationRepository.findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(checkOutDate, checkInDate);

        // Crear un set de habitations ocupadas en el rango de fechas proporcionado
        Set<Room> bookedRooms = conflictingReservations.stream()
                .map(Reservation::getRoom)
                .collect(Collectors.toSet());

        // Filtrar las habitaciones que no están reservadas en esas fechas
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> !bookedRooms.contains(room))
                .collect(Collectors.toList());

        return availableRooms;
    }
}
