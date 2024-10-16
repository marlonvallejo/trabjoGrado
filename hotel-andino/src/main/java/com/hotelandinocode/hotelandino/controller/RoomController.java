package com.hotelandinocode.hotelandino.controller;

import com.hotelandinocode.hotelandino.constants.HttpConstant;
import com.hotelandinocode.hotelandino.dto.ReservationDto;
import com.hotelandinocode.hotelandino.dto.ResponseDto;
import com.hotelandinocode.hotelandino.entities.Reservation;
import com.hotelandinocode.hotelandino.entities.Room;
import com.hotelandinocode.hotelandino.entities.User;
import com.hotelandinocode.hotelandino.service.ReservationService;
import com.hotelandinocode.hotelandino.service.RoomService;
import com.hotelandinocode.hotelandino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(HttpConstant.PATH_ROOM)
public class RoomController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveRoom(@RequestBody ReservationDto reservationDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User customer = userService.getByUserName(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            boolean isAvailable = reservationService.isRoomAvailable(reservationDto.getRoomId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());

            if (!isAvailable) {
                return new ResponseEntity<>(new ResponseDto(false, "La habitación no está disponible para estas fechas", null), HttpStatus.BAD_REQUEST);
            }

            Reservation reservation = reservationService.createReservation(
                    customer,
                    reservationDto.getRoomId(),
                    reservationDto.getCheckInDate(),
                    reservationDto.getCheckOutDate(),
                    reservationDto.getTotalPrice()
            );

            return new ResponseEntity<>(new ResponseDto(true, "Reserva creada con éxito", reservation), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al reservar la habitación", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<?> getRoomAvailability(@PathVariable Long roomId) {
        try {
            List<LocalDate[]> bookedRanges = reservationService.getRoomAvailability(roomId);

            if (bookedRanges.isEmpty()) {
                return new ResponseEntity<>(new ResponseDto(true, "La habitación está completamente disponible", null), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ResponseDto(true, "Fechas ocupadas encontradas", bookedRanges), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al consultar la disponibilidad", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        try {
            List<Room> availableRooms = reservationService.getAvailableRooms(checkInDate, checkOutDate);

            if (availableRooms.isEmpty()) {
                return new ResponseEntity<>(new ResponseDto(false, "No hay habitaciones disponibles para estas fechas", null), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ResponseDto(true, "Habitaciones disponibles encontradas", availableRooms), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al consultar las habitaciones disponibles", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            return new ResponseEntity<>(new ResponseDto(true, "Habitaciones obtenidas con éxito", rooms), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al obtener habitaciones", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
