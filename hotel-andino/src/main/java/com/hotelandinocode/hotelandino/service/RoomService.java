package com.hotelandinocode.hotelandino.service;

import com.hotelandinocode.hotelandino.entities.Room;
import com.hotelandinocode.hotelandino.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
