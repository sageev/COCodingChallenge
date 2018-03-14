package com.capitalone.challenge;

import com.capitalone.challenge.Models.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

import static com.capitalone.challenge.Constants.*;

public class ServiceClient implements IServiceClient {
    @Inject
    private Properties properties;

    @Inject
    private Client client;

    @Inject
    private SimpleDateFormat formatter;

    @Inject
    private ObjectMapper mapper;

    private Data data;
    private String ticker;
    private Date startDate;
    private Date endDate;

    public void Setup(String ticker, Date startDate, Date endDate) throws IOException {
        if(StringUtils.isEmpty(ticker)) {
            throw new IllegalArgumentException("Ticker");
        }

        if(startDate == null) {
            throw new IllegalArgumentException("Start Date");
        }

        if(endDate == null) {
            throw new IllegalArgumentException("End Date");
        }

        this.ticker = ticker;
        this.startDate = startDate;
        this.endDate = endDate;

        String url = String.format(properties.getProperty(API_BASE_URL),
                this.ticker,
                formatter.format(this.startDate),
                formatter.format(this.endDate));
        Response response = client.target(url).request().get();
        data = mapper.readValue(response.readEntity(String.class), Data.class);
    }

    public void DisplayAverages() {
        Map<String, Tuple2<Double, Double>> map = data.dataset.data.stream().collect(groupingBy(
                d -> ((String)d.get(0)).substring(0, 7),
                Tuple.collectors(
                        Collectors.averagingDouble(d -> (Double)d.get(1)), //average Open price
                        Collectors.averagingDouble(d -> (Double)d.get(4))  //average Close price
                )
        ));

        System.out.println("\n\n*****************************************Monthly Averages****************************************");
        System.out.println(String.format("Ticker: %s", data.dataset.dataset_code));
        map.entrySet().stream()
                .sorted((m1, m2) -> m1.getKey().compareTo(m2.getKey()))
                .forEach(month -> System.out.println(String.format("Month: %s   Average Open: $%.2f      Average Close: $%.2f",
                        month.getKey(),
                        month.getValue().v1,
                        month.getValue().v2)));
    }

    public void DisplayMaxDailyProfit() {
        List<Object> tradingDay = data.dataset.data.stream()
                .max((m1, m2) -> Double.compare((Double)m1.get(4) - (Double)m1.get(1), (Double)m2.get(4) - (Double)m2.get(1)))
                .get();

        System.out.println("\n\n*****************************************Max Daily Profit*****************************************");
        System.out.println(String.format("Ticker: %s", data.dataset.dataset_code));
        System.out.println(String.format("Date: %s    Amount of Profit: $%.2f",
                tradingDay.get(0),
                (Double)tradingDay.get(4) - (Double)tradingDay.get(1)));
    }

    public void DisplayBusyDays() {
        Double averageVolume = data.dataset.data.stream()
                .mapToDouble(d -> (Double)d.get(5))
                .summaryStatistics().getAverage();

        System.out.println("\n\n*****************************************Busy Days************************************************");
        System.out.println(String.format("Ticker: %s", data.dataset.dataset_code));
        System.out.println(String.format("Average Volume: %.0f", averageVolume));
        data.dataset.data.stream()
                .filter(d -> ((Double)d.get(5)-averageVolume)*100/averageVolume > 10)
                .sorted((d1, d2) -> ((String)d1.get(0)).compareTo((String)d2.get(0)))
                .forEach(d -> System.out.println(String.format("Date: %s    Volume: %.0f", d.get(0), (Double)d.get(5))));

    }

    public long getNumberOfLosingDays() {
        return data.dataset.data.stream()
                .filter(d -> (Double)d.get(4) - (Double)d.get(1) < 0)
                .count();
    }

    public String getTicker() {
        return data.dataset.dataset_code;
    }
}
