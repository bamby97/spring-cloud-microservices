package com.example.roomreservationsservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/room-reservations")
public class RoomReservationWebService {
    private final RoomClient roomClient;
    private final GuestClient guestClient;
    private final ReservationClient reservationClient;
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public RoomReservationWebService(RoomClient roomClient, GuestClient guestClient, ReservationClient reservationClient) {
        this.roomClient = roomClient;
        this.guestClient = guestClient;
        this.reservationClient = reservationClient;
    }

    @GetMapping
    public List<RoomReservation> getRoomReservations(@RequestParam(name = "date", required = false)String dateString){
        Date date = createDatefromDateString(dateString);
        List<Room> rooms = this.roomClient.getAllRooms();
        Map<Long,RoomReservation> roomReservations = new HashMap<>();
        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomNumber(room.getRoomNumber());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomId(room.getId());
            roomReservations.put(room.getId(),roomReservation);
        });
        List<Reservation> reservations = this.reservationClient.getAllReservations(new java.sql.Date(date.getTime()));
        reservations.forEach((reservation ->{
            RoomReservation roomReservation= roomReservations.get(reservation.getId());
            roomReservation.setDate(date);
            Guest guest = this. guestClient.getGuest(reservation.getGuestId());
            roomReservation.setGuestId(guest.getId());
            roomReservation.setFirstName(guest.getFirstName());
            roomReservation.setLastName(guest.getLastName());
        }));
        return  new ArrayList<>(roomReservations.values());
    }
    private Date createDatefromDateString(String dateString){
        Date date= null;
        if(dateString!=null){
            try {
                date= DATE_FORMAT.parse(dateString);
            }catch (ParseException e){
                date= new Date();
            }
        }else {
            date= new Date();
        }
        return date;
    }

}
