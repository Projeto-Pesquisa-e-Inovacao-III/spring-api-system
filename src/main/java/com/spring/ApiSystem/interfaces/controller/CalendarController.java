package com.spring.ApiSystem.interfaces.controller;

import com.spring.ApiSystem.application.dto.CalendarDTO;
import com.spring.ApiSystem.application.service.CalendarService;
import com.spring.ApiSystem.domain.entity.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping
    private ResponseEntity<CalendarDTO> createEvent(@RequestBody CalendarDTO dto) {
        CalendarDTO calendarDTO = calendarService.createEvent(dto);
        return ResponseEntity.ok(calendarDTO);
    }

    @GetMapping
    private ResponseEntity<Page<CalendarDTO>> getAllEvents(Pageable pageable) {
        Page<CalendarDTO> calendarDTO = calendarService.getAllEvents(pageable);
        return ResponseEntity.ok(calendarDTO);
    }
}
