package com.spring.ApiPag.dto;

import com.spring.ApiPag.dto.CheckoutDto.LinksDto;

import java.util.List;

public record GetAllCheckoutsDto(
        String id,
        List<LinksDto> links
) { }
