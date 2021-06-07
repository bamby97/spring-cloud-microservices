package com.example.roomreservationsservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("guestservices")
public interface GuestClient {
    @GetMapping("/guests")
    Iterable<Guest> getAllGuests();
    @GetMapping("/guests/{id}")
    Guest getGuest(@PathVariable("id")long id);
}
