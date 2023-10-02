package se.umejug.exchangeday.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderRow {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="seller_id", nullable=false)
    private Seller seller;

    public Long getId() {

        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(final Seller seller) {
        this.seller = seller;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
