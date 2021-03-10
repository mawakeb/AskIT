package nl.tudelft.oopp.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.Test;

import nl.tudelft.oopp.demo.controllers.RoomController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpResponse;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
    @Mock
    RoomRepository roomRepository;

    @Test
    public void checkIfRandomStringGetsCreated(){
        String randString = RoomController.randomString();

        assertEquals(randString.length(), 15);
        assertNotNull(randString);
    }

    @Test
    public void checkIfTwoValidLinksAreReturned(){

        RoomController rc = new RoomController();

        Room testRoom = new Room(1,"TestRoom","1/123","1/456");

        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        List<String> testLinks = rc.createLink("TestRoom");
        assertEquals(testLinks.size(), 2);
    }

}