package com.spring.ApiPag.entity;
import com.spring.ApiPag.enums.shipping.ShippingServiceType;
import com.spring.ApiPag.enums.shipping.ShippingType;
import com.spring.ApiPag.validator.ConditionalValidation.ConditionalValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@ConditionalValidation(
        conditionalField = "address_modifiable",
        conditionalValue = "false",
        requiredField = "address"
)

@ConditionalValidation(
        conditionalField = "type",
        conditionalValue = "FIXED",
        requiredField = "amount"
)

@ConditionalValidation(
        conditionalField = "service_type",
        conditionalValue = "CALCULATED",
        requiredField = "box"
)

@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long shipping_id;

    private ShippingType type;

    private ShippingServiceType service_type;

    private Boolean address_modifiable;

    private Integer amount;

    @NotBlank
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "box_id")
    private Box box;

    public Shipping() {
    }

    public Shipping(Long shipping_id, ShippingType type, ShippingServiceType service_type, Boolean address_modifiable, Integer amount, Address address, Box box) {
        this.shipping_id = shipping_id;
        this.type = type;
        this.service_type = service_type;
        this.address_modifiable = address_modifiable;
        this.amount = amount;
        this.address = address;
        this.box = box;
    }

    public ShippingType getType() {
        return type;
    }

    public void setType(ShippingType type) {
        this.type = type;
    }

    public ShippingServiceType getService_type() {
        return service_type;
    }

    public void setService_type(ShippingServiceType service_type) {
        this.service_type = service_type;
    }

    public Boolean getAddress_modifiable() {
        return address_modifiable;
    }

    public void setAddress_modifiable(Boolean address_modifiable) {
        this.address_modifiable = address_modifiable;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Long getShipping_id() {
        return shipping_id;
    }

    public void setshipping_id(Long shipping_id) {
        this.shipping_id = shipping_id;
    }
}
