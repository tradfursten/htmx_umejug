package se.umejug.exchangeday.models;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderRow> orderRows;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<OrderRow> getOrderRows() {
        return orderRows;
    }

    public void setOrderRows(final List<OrderRow> orderRows) {
        this.orderRows = orderRows;
    }

    public BigDecimal getSum() {
        return orderRows
                .stream()
                .map(OrderRow::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
