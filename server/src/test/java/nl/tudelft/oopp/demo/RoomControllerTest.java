package nl.tudelft.oopp.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.oopp.demo.controllers.RoomController;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
    @Mock
    RoomRepository roomRepository;

    @Test
    public void checkIfRandomStringGetsCreated() {
        String randString = RoomController.randomString();

        assertEquals(randString.length(), 15);
        assertNotNull(randString);
    }


}