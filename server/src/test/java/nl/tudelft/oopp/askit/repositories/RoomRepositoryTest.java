package nl.tudelft.oopp.askit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class RoomRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void findByid() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Room room = new Room(id, "room", "staff", "student", null);
        entityManager.persist(room);
        entityManager.flush();

        Room found = roomRepository.findByid(id);

        assertEquals(room, found);

    }
}