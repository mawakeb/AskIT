package nl.tudelft.oopp.askit.repositories;

import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByid(UUID id);
}
