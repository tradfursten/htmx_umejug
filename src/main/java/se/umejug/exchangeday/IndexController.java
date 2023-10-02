package se.umejug.exchangeday;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import se.umejug.exchangeday.models.ExchangeDay;
import se.umejug.exchangeday.models.Order;
import se.umejug.exchangeday.models.OrderRow;

@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final ExchangeDayService exchangeDayService;

    public IndexController(final ExchangeDayService exchangeDayService) {
        this.exchangeDayService = exchangeDayService;
    }

    @GetMapping("/")
    public String index(Model model) {
        logger.info("Index request");
        var exchangeDays = exchangeDayService.findAllExchangeDays();
        model.addAttribute("exchangeDays", exchangeDays);
        return "index";
    }

    @PostMapping(value = "/exchange_days")
    public String createExchangeDay(NewExchangeDay newExchangeDay, Model model) {
        logger.info("Post request: {}", newExchangeDay);
        exchangeDayService.createExchangeDay(newExchangeDay);
        return "redirect:/";
    }

    @GetMapping("/exchange_days/{id}")
    public String getExchangeDay(@PathVariable("id") Long id,
                                 @RequestParam(value = "display_sellers", required = false) boolean displaySellers,
                                 @RequestParam(value = "display_orders", required = false) boolean displayOrders,
                                 Model model) {
        var optionalExchangeDay = exchangeDayService.findById(id);
        if (optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();
            model.addAttribute("exchangeDay", exchangeDay);
            model.addAttribute("displaySellers", displaySellers);
            model.addAttribute("displayOrders", displayOrders);
            return "exchangeDay";
        }
        return "redirect:/";

    }

    @PostMapping(value = "/exchange_days/{id}/sellers")
    public String createExchangeDay(@PathVariable("id") Long exchangeId,
                                    @RequestParam(value = "display_sellers", required = false) boolean displaySellers,
                                    @RequestParam(value = "display_orders", required = false) boolean displayOrders,
                                    NewSeller newSeller,
                                    Model model) {
        exchangeDayService.addSeller(exchangeId, newSeller);
        return "redirect:/exchange_days/"+exchangeId + "?display_sellers=" + displaySellers + "&display_orders="+ displayOrders;
    }

    @PostMapping("/exchange_days/{exchange_id}/orders")
    public String createOrder(@PathVariable("exchange_id") Long exchangeId) {
        var order = exchangeDayService.createOrder(exchangeId);
        return order.map(value -> "redirect:/exchange_days/" + exchangeId + "/orders/" + value.getId())
                .orElseGet(() -> "redirect:/exchange_days/" + exchangeId);
    }

    @GetMapping("/exchange_days/{exchange_id}/orders/{order_id}")
    public String getOrder(@PathVariable("exchange_id") Long exchangeId, @PathVariable("order_id") Long orderId, Model model) throws IOException, WriterException {
        var optionalExchangeDay = exchangeDayService.findById(exchangeId);
        if (optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();
            var optionalOrder = exchangeDay.findOrder(orderId);
            if(optionalOrder.isPresent()) {
                var order = optionalOrder.get();
                var sum = order.getOrderRows().stream()
                        .map(OrderRow::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                model.addAttribute("exchangeDay", exchangeDay);
                model.addAttribute("order", order);
                model.addAttribute("sum", sum);
                model.addAttribute("qr", getQRCode(exchangeDay, order));
                return "order";
            }
        }
        return "redirect:/exchange_days/"+exchangeId;
    }

    @PostMapping("/exchange_days/{exchange_id}/orders/{order_id}/rows")
    public String createRow(@PathVariable("exchange_id") Long exchangeId, @PathVariable("order_id") Long orderId, NewOrderRow newOrderRow) {
        exchangeDayService.createOrderRow(exchangeId, orderId, newOrderRow);
        return "redirect:/exchange_days/" + exchangeId + "/orders/" + orderId;
    }


    private String getQRCode(final ExchangeDay exchangeDay, final Order order) throws IOException, WriterException {
        var sum = order.getSum().setScale(2, BigDecimal.ROUND_HALF_UP);
        var url = String.format("https://app.swish.nu/1/p/sw/?sw=%s&amt=%s&cur=SEK&msg=%s&src=qr",
                exchangeDay.getSwishNumber(),
                sum,
                URLEncoder.encode("Exchange Day: " + exchangeDay.getName(), StandardCharsets.UTF_8));
        var imageSize = 200;
        var matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
                imageSize, imageSize);
        var bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        var image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode

        return "<img src=\"data:image/png;base64, " + image + "\"/>";
    }
}

record NewExchangeDay(String name, String swishNumber) { }
record NewSeller(String name, String swishNumber) { }
record NewOrderRow(Long sellerId, BigDecimal price) { }
