package com.spring.ApiPag.entity;

import com.spring.ApiPag.validator.Iso3166Alpha3.ValidIso3166Alpha3;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long adress_id;

    @NotBlank
    @Length(max = 160)
    private String street;

    @NotBlank
    @Length(max = 20)
    private String number;

    private String complement;

    @NotBlank
    @Length(max = 60)
    private String locality;

    @NotBlank
    @Length(max = 60)
    private String city;

    @NotBlank
    @ValidIso3166Alpha3
    private String region_code;

    @NotBlank
    @ValidIso3166Alpha3
    private String country;

    @NotBlank
    @Length(min = 8, max = 8)
    private String postal_code;

    public Address() {
    }

    public Address(Long adress_id, String street, String number, String complement, String locality, String city, String region_code, String country, String postal_code) {
        this.adress_id = adress_id;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.locality = locality;
        this.city = city;
        this.region_code = region_code;
        this.country = country;
        this.postal_code = postal_code;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getLocality() {
        return locality;
    }

    public Long getId() {
        return adress_id;
    }

    public void setId(Long adress_id) {
        this.adress_id = adress_id;
    }
}
