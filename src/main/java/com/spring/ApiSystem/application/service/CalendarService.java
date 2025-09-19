package com.spring.ApiSystem.application.service;

import com.spring.ApiSystem.application.dto.CalendarDTO;
import com.spring.ApiSystem.domain.entity.Calendar;
import com.spring.ApiSystem.infrastructure.repository.CalendarRepository;
import com.spring.ApiSystem.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    public CalendarDTO createEvent(CalendarDTO dto) {
        Calendar calendar = new Calendar();

        calendar.setTitle(dto.getTitle());
        calendar.setDateTime(dto.getDateTime());

        calendar = calendarRepository.save(calendar);

        return new CalendarDTO(calendar);
    }

    public Page<CalendarDTO> getAllEvents(Pageable pageable) {
        return calendarRepository.findAll(pageable).map(CalendarDTO::new);
    }
}
