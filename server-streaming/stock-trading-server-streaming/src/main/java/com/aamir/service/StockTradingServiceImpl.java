package com.aamir.service;

import com.aamir.StockRequest;
import com.aamir.StockResponse;
import com.aamir.StockTradingServiceGrpc;
import com.aamir.entity.Stock;
import com.aamir.repo.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This is the implementation of the gRPC service defined in your proto file.
 *
 * It extends the auto-generated `StockTradingServiceGrpc.StockTradingServiceImplBase`,
 * which gives you the method signatures to override (like a controller in REST).
 */

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    // Injecting the JPA repository to fetch stock data from the database.
    @Autowired
    private StockRepository stockRepository;

    /*
     * 🚨 The gRPC method's return type is void because:
     *
     * - gRPC is built on **streaming** principles. Instead of returning a value directly,
     *   you **send the response asynchronously** using the `StreamObserver`.
     *
     * ✅ Even though the method is void, data is still returned to the client.
     * The `StreamObserver` object is responsible for **pushing the data** back to the client.
     *
     * Here’s how it works:
     *
     * - `responseObserver.onNext(stockResponse);` sends the response back.
     * - `responseObserver.onCompleted();` signals that the response is finished (like closing a stream).
     *
     * ➡️ So when you call this service from a client (even Postman via gRPC plugin or another gRPC client),
     *    the client **receives the StockResponse** as part of the gRPC protocol handshake,
     *    even though the method looks like it returns void.
     */
    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {

        // Extract the stock symbol from the request.
        String stockSymbol = request.getStockSymbol();

        // Fetch stock data from the database using the repository.
        Stock stockData = stockRepository.findByStockSymbol(stockSymbol);

        // Build the response object using the fetched data.
        StockResponse stockResponse = StockResponse.newBuilder()
                .setStockSymbol(stockData.getStockSymbol()) // set the symbol
                .setPrice(stockData.getPrice())             // set the current price
                .setTimestamp(stockData.getLastUpdated().toString()) // set the last updated time
                .build();

        // ✅ Send the response back to the client.
        responseObserver.onNext(stockResponse);

        // ✅ Indicate that the response is complete.
        responseObserver.onCompleted();
    }

    @Override
    public void streamStockPrices(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbol = request.getStockSymbol();
        try {
            for (int i = 0; i <= 25; i++) { // simulate streaming 10 prices
                StockResponse stockResponse = StockResponse.newBuilder()
                        .setStockSymbol(symbol)
                        .setPrice(new Random().nextDouble(200)) // set a random price
                        .setTimestamp(Instant.now().toString()) // set the current timestamp
                        .build();
                responseObserver.onNext(stockResponse);
                TimeUnit.SECONDS.sleep(1); // wait for 1 second before sending the next price
            }
            responseObserver.onCompleted(); // indicate that the stream is complete
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }
}
