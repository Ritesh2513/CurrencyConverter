package com.Springboot.CurrencyConverter.Service;

import com.Springboot.CurrencyConverter.Model.ConversionHistory;
import com.Springboot.CurrencyConverter.Repository.ConversionHistoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.List;

@Service
public class CurrencyService {

    private static final String API_KEY = "d7c81f0b3b9055331c0711c0";  // Replace with actual key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";
    
    private final ConversionHistoryRepo historyRepo;

    public CurrencyService(ConversionHistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public double convert(String from, String to, double amount) {
        try {
            String url = API_URL + from;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            
            JSONObject jsonObject = new JSONObject(response);
            JSONObject rates = jsonObject.getJSONObject("conversion_rates");
            double rate = rates.getDouble(to);
            double convertedAmount = amount * rate;
            
            // Save to database
            ConversionHistory history = new ConversionHistory(from, to, amount, convertedAmount);
            historyRepo.save(history);

            return convertedAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<ConversionHistory> getHistory() {
        return historyRepo.findAll();
    }
}
