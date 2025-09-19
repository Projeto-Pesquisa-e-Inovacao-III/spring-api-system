package com.spring.ApiSystem.application.dto;

import com.spring.ApiSystem.domain.entity.Calendar;

import java.time.LocalDateTime;

public class CalendarDTO {
    private Long id;
    private String title;
    private LocalDateTime dateTime;

    public CalendarDTO(Long id, String title, LocalDateTime dateTime) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
    }

    public CalendarDTO() {
    }

    public CalendarDTO(Calendar calendar) {
        this.id = calendar.getId();
        this.title = calendar.getTitle();
        this.dateTime = calendar.getDateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
