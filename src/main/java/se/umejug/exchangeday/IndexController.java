package se.umejug.exchangeday;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import jakarta.servlet.http.HttpServletResponse;
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
    public String getOrder(@PathVariable("exchange_id") Long exchangeId,
                           @PathVariable("order_id") Long orderId,
                           Model model,
                           HttpServletResponse response) throws IOException, WriterException {
        var optionalExchangeDay = exchangeDayService.findById(exchangeId);
        if (optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();
            var optionalOrder = exchangeDay.findOrder(orderId);
            if(optionalOrder.isPresent()) {
                var order = optionalOrder.get();
                var sum = order.getSum();
                model.addAttribute("exchangeDay", exchangeDay);
                model.addAttribute("order", order);
                model.addAttribute("sum", sum);
                model.addAttribute("qr", getQRCode(exchangeDay, exchangeDay.getSwishNumber(), sum));
                response.setHeader("HX-Refresh", "true");
                return "order";
            }
        }
        return "redirect:/exchange_days/"+exchangeId;
    }

    @PostMapping("/exchange_days/{exchange_id}/orders/{order_id}/rows")
    public String createRow(@PathVariable("exchange_id") Long exchangeId,
                                            @PathVariable("order_id") Long orderId,
                                            NewOrderRow newOrderRow,
                                            @RequestHeader("HX-Request") boolean hxRequest,
                                            Model model,
                                            HttpServletResponse response ) {
        var row = exchangeDayService.createOrderRow(exchangeId, orderId, newOrderRow);
        if(hxRequest && row.isPresent()) {
            model.addAttribute("row", row.get());
            response.setHeader("HX-Trigger", "update-sum");
            return "components/orderRow";
        }
        return "redirect:/exchange_days/" + exchangeId + "/orders/" + orderId;
    }

    @GetMapping("/exchange_days/{exchange_id}/orders/{order_id}/sum")
    public String sum(@PathVariable("exchange_id") Long exchangeId,
                      @PathVariable("order_id") Long orderId,
                      Model model) {
        var sum = exchangeDayService.findById(exchangeId)
                .flatMap(exchangeDay -> exchangeDay.findOrder(orderId))
                .map(Order::getSum)
                .orElse(BigDecimal.ZERO);
        model.addAttribute("sum", sum);
        model.addAttribute("exchangeDayId", exchangeId);
        model.addAttribute("orderId", orderId);
        return "components/orderSum";
    }

    @GetMapping("/exchange_days/{exchange_id}/orders/{order_id}/qr")
    public String qr(@PathVariable("exchange_id") Long exchangeId,
                      @PathVariable("order_id") Long orderId,
                      Model model,
                     HttpServletResponse response ) throws IOException, WriterException {
        var optionalExchangeDay = exchangeDayService.findById(exchangeId);
        if (optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();
            var optionalOrder = exchangeDay.findOrder(orderId);
            if (optionalOrder.isPresent()) {
                var order = optionalOrder.get();
                var qr = getQRCode(exchangeDay, exchangeDay.getSwishNumber(), order.getSum());
                model.addAttribute("qr", qr);
                model.addAttribute("exchangeDayId", exchangeId);
                model.addAttribute("orderId", orderId);
                return "components/orderQr";
            }
        }
        response.setHeader("HX-Refresh", "true");
        return "redirect:/exchange_days/" + exchangeId + "/orders/" + orderId;
    }

    @GetMapping("/exchange_days/{exchange_id}/sellers/{seller_id}")
    public String getSeller(@PathVariable("exchange_id") Long exchangeId,
                           @PathVariable("seller_id") Long sellerId,
                           Model model,
                           HttpServletResponse response) throws IOException, WriterException {
        var optionalExchangeDay = exchangeDayService.findById(exchangeId);
        if (optionalExchangeDay.isPresent()) {
            var exchangeDay = optionalExchangeDay.get();
            var optionalSeller = exchangeDay.findSeller(sellerId);
            if(optionalSeller.isPresent()) {
                var seller = optionalSeller.get();
                var sum = exchangeDay.findOrdersBySeller(seller)
                        .stream()
                        .map(order -> order.getSumBySeller(seller))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                model.addAttribute("exchangeDay", exchangeDay);
                model.addAttribute("sum", sum);
                model.addAttribute("seller", seller);
                model.addAttribute("qr", getQRCode(exchangeDay, seller.getSwishNumber(), sum));
                response.setHeader("HX-Refresh", "true");
                return "seller";
            }
        }
        return "redirect:/exchange_days/"+exchangeId;
    }


    private String getQRCode(final ExchangeDay exchangeDay, final String recipient, final BigDecimal sum) throws IOException, WriterException {
        var url = String.format("https://app.swish.nu/1/p/sw/?sw=%s&amt=%s&cur=SEK&msg=%s&src=qr",
                recipient,
                sum,
                UriUtils.encode("Exchange Day: " + exchangeDay.getName(), StandardCharsets.UTF_8));
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
record NewOrderRow(Integer sellerId, BigDecimal price) { }
