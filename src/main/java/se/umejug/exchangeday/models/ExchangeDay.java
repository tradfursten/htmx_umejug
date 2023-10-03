package se.umejug.exchangeday.models;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "exchange_day")
public class ExchangeDay {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String swishNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Seller> sellerList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> orderList;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSwishNumber() {
        return swishNumber;
    }

    public void setSwishNumber(final String swishNumber) {
        this.swishNumber = swishNumber;
    }

    public List<Seller> getSellerList() {
        return sellerList;
    }

    public void setSellerList(final List<Seller> sellerList) {
        this.sellerList = sellerList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public Optional<Order> findOrder(final Long orderId) {
        return orderList
                .stream()
                .filter(it -> Objects.equals(it.getId(), orderId))
                .findFirst();
    }

    public Optional<Seller> findSeller(final Long sellerId) {
        return sellerList
                .stream()
                .filter(it -> Objects.equals(it.getId(), sellerId))
                .findFirst();
    }

    public BigDecimal getSum() {
        return orderList
                .stream()
                .map(Order::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Order> findOrdersBySeller(final Seller seller) {
        return orderList.stream()
                .filter(order -> order.getOrderRows()
                        .stream()
                        .anyMatch(orderRow -> orderRow.getSeller().getId().equals(seller.getId())))
                .toList();
    }

    public Optional<Seller> findSellerByNumber(final Integer sellerNumber) {
        return sellerList
                .stream()
                .filter(it -> Objects.equals(it.getSellerNumber(), sellerNumber))
                .findFirst();
    }
}
