package se.umejug.exchangeday;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import se.umejug.exchangeday.models.ExchangeDay;
import se.umejug.exchangeday.models.Order;
import se.umejug.exchangeday.models.OrderRow;
import se.umejug.exchangeday.models.Seller;

@Service
public class ExchangeDayService {

    private final ExchangeDayRepository repository;

    public ExchangeDayService(final ExchangeDayRepository repository) {
        this.repository = repository;
    }


    public ExchangeDay createExchangeDay(NewExchangeDay newExchangeDay) {
        var exchangeDay = new ExchangeDay();
        exchangeDay.setName(newExchangeDay.name());
        exchangeDay.setSwishNumber(newExchangeDay.swishNumber());
        return repository.save(exchangeDay);
    }

    public List<ExchangeDay> findAllExchangeDays() {
       return repository.findAll();
    }

    public Optional<ExchangeDay> findById(final Long id) {
        return repository.findById(id);
    }

    public void addSeller(final Long exchangeId, final NewSeller newSeller) {
        var seller = new Seller();
        seller.setName(newSeller.name());
        seller.setSwishNumber(newSeller.swishNumber());
        repository.findById(exchangeId)
                .ifPresent(exchangeDay -> {
                    exchangeDay.getSellerList().add(seller);
                    repository.save(exchangeDay);
                });
    }

    public Optional<Order> createOrder(final Long exchangeId) {
        return repository.findById(exchangeId)
                .map(exchangeDay -> {
                    var newOrder = new Order();
                    exchangeDay.getOrderList().add(newOrder);
                    repository.save(exchangeDay);
                    return newOrder;
                });
    }

    public void createOrderRow(final Long exchangeId, final Long orderId, final NewOrderRow newOrderRow) {
        var optionalExchangeDay = repository.findById(exchangeId);
        if(optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();

            var seller = exchangeDay.findSeller(newOrderRow.sellerId());
            var order = exchangeDay.findOrder(orderId);
            if(seller.isPresent() && order.isPresent()) {
                var orderRow = new OrderRow();
                orderRow.setPrice(newOrderRow.price());
                orderRow.setSeller(seller.get());
                order.get().getOrderRows().add(orderRow);
                repository.save(exchangeDay);
            }
        }
    }


}
