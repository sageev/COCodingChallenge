This is an IntelliJ Maven Java (SDK 1.8.0_161) solution using Java 8.  Please, open it in IntelliJ IDE, build and run it (either from within IntelliJ itself or command line).
The program will output data on the command line.
The following features have been implemented:
1. Monthly averages;
2. Max daily profit;
3. Busy days;
4. Biggest loser.


Notes:
1. You can modify the API base URL and START/END dates in the config.properties file.  The list of tickers (COF, GOOGL, MSFT) is currently hard-coded, but this can be changed, if needed.
2. Exceptions are being logged into a log file using log4j.
3. Using Guice Dependency Injector to create/inject instances of objects at run-time.
4. Using JBoss' Rest-Easy client library to make REST API calls.
5. Due to the lack of time, no unit tests have been added.  If required, let me know and I'll add them.
