package com.spring.ApiPag.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkouts")
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkout_id;

    @NotBlank
    @Column(nullable = false)
    private String id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String reference_id; // transformar isso em um UIID?

    @Column(name = "expiration_date")
    private Timestamp expiration_date;

    @NotBlank
    @Column(name = "created_at", nullable = false)
    private String created_at;

    @NotBlank
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "customer_modifiable")
    private Boolean customer_modifiable = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private List<Item> items;

    @Column(name = "additional_amount")
    @Min(0)
    private Integer additional_amount;

    @Column(name = "discount_amount")
    @Min(0)
    private Integer discount_amount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private Shipping shipping;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private List<PaymentMethod> payment_methods;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private List<PaymentMethodsConfigs> payment_methods_config;

    @Column(name = "soft_descriptor")
    @Size(max = 100)
    private String soft_descriptor;

    @Column(name = "redirect_url")
    private String redirect_url;

    @Column(name = "return_url")
    private String return_url;

    @ElementCollection
    @CollectionTable(name = "checkout_notification_urls", joinColumns = @JoinColumn(name = "checkout_id"))
    @Column(name = "notification_url")
    private List<String> notification_urls;

    @ElementCollection
    @CollectionTable(name = "checkout_payment_notification_urls", joinColumns = @JoinColumn(name = "checkout_id"))
    @Column(name = "payment_notification_url")
    private List<String> payment_notification_urls;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recurrence_plan_id")
    private RecurrencePlan recurrencePlan;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private List<Links> links;

    @NotBlank
    @Column(name = "origin")
    private String origin;

    public Checkout() {}

    public Checkout(Long checkout_id, String id, String reference_id, Timestamp expiration_date, String created_at, String status, Customer customer, Boolean customer_modifiable, List<Item> items, Integer additional_amount, Integer discount_amount, Shipping shipping, List<PaymentMethod> payment_methods, List<PaymentMethodsConfigs> payment_methods_config, String soft_descriptor, String redirect_url, String return_url, List<String> notification_urls, List<String> payment_notification_urls, RecurrencePlan recurrencePlan, List<Links> links, String origin) {
        this.checkout_id = checkout_id;
        this.id = id;
        this.reference_id = reference_id;
        this.expiration_date = expiration_date;
        this.created_at = created_at;
        this.status = status;
        this.customer = customer;
        this.customer_modifiable = customer_modifiable;
        this.items = items;
        this.additional_amount = additional_amount;
        this.discount_amount = discount_amount;
        this.shipping = shipping;
        this.payment_methods = payment_methods;
        this.payment_methods_config = payment_methods_config;
        this.soft_descriptor = soft_descriptor;
        this.redirect_url = redirect_url;
        this.return_url = return_url;
        this.notification_urls = notification_urls;
        this.payment_notification_urls = payment_notification_urls;
        this.recurrencePlan = recurrencePlan;
        this.links = links;
        this.origin = origin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCheckout_id() {
        return checkout_id;
    }

    public void setCheckout_id(Long checkout_id) {
        this.checkout_id = checkout_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public Timestamp getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Timestamp expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getCustomer_modifiable() {
        return customer_modifiable;
    }

    public void setCustomer_modifiable(Boolean customer_modifiable) {
        this.customer_modifiable = customer_modifiable;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getAdditional_amount() {
        return additional_amount;
    }

    public void setAdditional_amount(Integer additional_amount) {
        this.additional_amount = additional_amount;
    }

    public Integer getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Integer discount_amount) {
        this.discount_amount = discount_amount;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<PaymentMethod> getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(List<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public List<PaymentMethodsConfigs> getPayment_methods_config() {
        return payment_methods_config;
    }

    public void setPayment_methods_config(List<PaymentMethodsConfigs> payment_methods_config) {
        this.payment_methods_config = payment_methods_config;
    }

    public String getSoft_descriptor() {
        return soft_descriptor;
    }

    public void setSoft_descriptor(String soft_descriptor) {
        this.soft_descriptor = soft_descriptor;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public List<String> getNotification_urls() {
        return notification_urls;
    }

    public void setNotification_urls(List<String> notification_urls) {
        this.notification_urls = notification_urls;
    }

    public List<String> getPayment_notification_urls() {
        return payment_notification_urls;
    }

    public void setPayment_notification_urls(List<String> payment_notification_urls) {
        this.payment_notification_urls = payment_notification_urls;
    }

    public RecurrencePlan getRecurrencePlan() {
        return recurrencePlan;
    }

    public void setRecurrencePlan(RecurrencePlan recurrencePlan) {
        this.recurrencePlan = recurrencePlan;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
