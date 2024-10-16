package com.hotelandinocode.hotelandino.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "habitacion")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero")
    private String number;

    @Column(name = "tipo")
    private String type;

    @Column(name = "estado")
    private String state;


}
