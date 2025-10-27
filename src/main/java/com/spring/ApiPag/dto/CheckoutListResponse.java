package com.spring.ApiPag.dto;

import com.spring.ApiPag.dto.CheckoutDto.LinksDto;

import java.util.List;

public record CheckoutListResponse(
        List<GetAllCheckoutsDto> checkouts,
        List<LinksDto> links
) { }
