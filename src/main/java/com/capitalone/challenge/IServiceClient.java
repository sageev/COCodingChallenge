package com.capitalone.challenge;

import java.io.IOException;
import java.util.Date;

public interface IServiceClient {
    void Setup(String ticker, Date startDate, Date endDate) throws IOException;
    void DisplayAverages();
    void DisplayMaxDailyProfit();
    void DisplayBusyDays();
    long getNumberOfLosingDays();
    String getTicker();
}
