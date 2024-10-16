package com.hotelandinocode.hotelandino.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reserva")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Room room;

    @NotNull
    @Column(name = "fecha_ingreso")
    private LocalDate checkInDate;

    @NotNull
    @Column(name = "fecha_salida")
    private LocalDate checkOutDate;

    @NotNull
    @Column(name = "precio_total")
    private Double totalPrice;

}
