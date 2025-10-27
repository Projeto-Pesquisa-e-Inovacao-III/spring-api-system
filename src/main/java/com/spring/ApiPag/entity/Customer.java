package com.spring.ApiPag.entity;

import com.spring.ApiPag.validator.LengthElevenOrFourteen.LengthElevenOrFourteen;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 120)
    private String name;

    @NotBlank
    @Email
    @Length(min = 10, max = 60)
    private String email;

    @NotBlank
    @LengthElevenOrFourteen
    private String tax_id;

    @OneToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    public Customer() {
    }

    public Customer(Long id, String name, String email, String tax_id, Phone phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tax_id = tax_id;
        this.phone = phone;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
