package com.aamir.service;

import com.aamir.StockRequest;
import com.aamir.StockResponse;
import com.aamir.StockTradingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


/**
 * This is a Spring-managed service class that acts as a gRPC **client**.
 *
 * It communicates with a gRPC **server** (StockTradingService) to fetch stock prices.
 */

@Service
public class StockServiceClient {

    /**
     * Injects the gRPC **Blocking Stub** of the StockTradingService.
     *
     * - The string "stock-trading-server" refers to the name of the gRPC server you have defined
     *   (e.g., in your `application.yml` or `application.properties`).
     *
     * - This is a **blocking stub**, meaning it will make **synchronous** calls (waits for the response before moving on).
     *
     * The `StockTradingServiceGrpc` class and the stub are auto-generated from your `.proto` file.
     */

    @GrpcClient("stock-trading-server")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;


    /**
     * This method fetches the stock price from the remote gRPC service.
     *
     * @param stockSymbol The symbol of the stock (e.g., "AAPL", "GOOG").
     * @return StockResponse containing details like stock symbol, price, etc.
     *
     * How it works:
     * - Builds a `StockRequest` object with the stock symbol.
     * - Calls the `getStockPrice()` method on the **blocking stub** to fetch data from the gRPC server.
     * - Returns the response back to the caller.
     */
    public StockResponse getStockPrice(String stockSymbol) {
        // 1️⃣ Create the gRPC request using builder pattern.
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build();

        // 2️⃣ Make the actual gRPC call (blocking, waits for response).
        StockResponse response = stockTradingServiceBlockingStub.getStockPrice(request);

        // 3️⃣ Return the gRPC response to the caller.
        return response;
    }
}
