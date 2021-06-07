package com.frankmoley.lil.reservationservices;

import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/reservations")
public class ReservationWebServices {
    private final ReservationRepository repository;

    public ReservationWebServices(ReservationRepository repository){
        super();
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Reservation> getAllReservations(@RequestParam(name = "date",required = false)Date date){
        if(date!=null){
            return this.repository.findAllByDate(date);
        }
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable("id") long id){
        return this.repository.findById(id).get();
    }
}
