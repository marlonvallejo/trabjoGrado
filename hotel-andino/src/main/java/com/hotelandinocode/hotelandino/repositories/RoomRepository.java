package com.hotelandinocode.hotelandino.repositories;

import com.hotelandinocode.hotelandino.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Override
    List<Room> findAll();
}
