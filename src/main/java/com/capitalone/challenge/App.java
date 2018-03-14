package com.capitalone.challenge;

import com.capitalone.challenge.config.ConfigModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.capitalone.challenge.Constants.END_DATE;
import static com.capitalone.challenge.Constants.START_DATE;


public class App {
    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        try {
            LOG.info("Capital One coding challenge started");

            Injector injector = Guice.createInjector(new ConfigModule());
            SimpleDateFormat formatter = injector.getInstance(SimpleDateFormat.class);
            Properties properties = injector.getInstance(Properties.class);

            List<String> tickers = Arrays.asList("COF", "GOOGL", "MSFT");
            List<IServiceClient> clients = new ArrayList<>();
            for(String ticker : tickers) {
                IServiceClient client = injector.getInstance(IServiceClient.class);
                client.Setup(ticker,
                        formatter.parse(properties.getProperty(START_DATE)),
                        formatter.parse(properties.getProperty(END_DATE)));
                clients.add(client);

                client.DisplayAverages();

                if(Arrays.asList(args).contains("—max-daily-profit")) {
                    client.DisplayMaxDailyProfit();
                }

                if(Arrays.asList(args).contains("—busy-day")) {
                    client.DisplayBusyDays();
                }
            }


            if(Arrays.asList(args).contains("—biggest-loser")) {
                System.out.println("\n\n*****************************************Biggest Loser************************************************");
                IServiceClient biggestLoser = clients.stream()
                        .max((c1, c2) -> Long.compare(c1.getNumberOfLosingDays(), c2.getNumberOfLosingDays()))
                        .get();
                System.out.println(String.format("Ticker: %s    # of Days: %s",
                        biggestLoser.getTicker(),
                        biggestLoser.getNumberOfLosingDays()));
            }

            LOG.info("Capital One coding challenge ended");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOG.error(e.getMessage(), e);
        }
    }
}
