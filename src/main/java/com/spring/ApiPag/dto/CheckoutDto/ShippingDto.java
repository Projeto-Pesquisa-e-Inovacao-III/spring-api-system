package com.spring.ApiPag.dto.CheckoutDto;

import com.spring.ApiPag.enums.shipping.ShippingServiceType;
import com.spring.ApiPag.enums.shipping.ShippingType;

import java.io.Serializable;

/**
 * DTO for {@link com.spring.ApiPag.entity.Shipping}
 */
public record ShippingDto(
        ShippingType type,
        ShippingServiceType service_type,
        Boolean address_modifiable,
        Integer amount, AddressDto address,
        BoxDto box) implements Serializable {
}