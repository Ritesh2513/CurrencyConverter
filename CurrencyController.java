package com.Springboot.CurrencyConverter.Controller;

import com.Springboot.CurrencyConverter.Model.ConversionHistory;
import com.Springboot.CurrencyConverter.Service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/convert")
    public double convertCurrency(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        return currencyService.convert(from, to, amount);
    }

    @GetMapping("/history")
    public List<ConversionHistory> getConversionHistory() {
        return currencyService.getHistory();
    }
}
