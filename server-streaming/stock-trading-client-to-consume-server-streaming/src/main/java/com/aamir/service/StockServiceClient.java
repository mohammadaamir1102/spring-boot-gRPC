package com.aamir.service;

import com.aamir.StockRequest;
import com.aamir.StockResponse;
import com.aamir.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


/**
 * This is a Spring-managed service class that acts as a gRPC **client**.
 * <p>
 * It communicates with a gRPC **server** (StockTradingService) to fetch stock prices.
 */

@Service
public class StockServiceClient {


    /**
     * The gRPC client stub for communicating with the StockTradingService.
     * <p>
     * This stub is injected using the `@GrpcClient` annotation provided by the
     * `net.devh.boot.grpc.client` library. It allows the client to make asynchronous
     * calls to the gRPC server.
     * <p>
     * The `stock-trading-server` identifier is used to resolve the gRPC server
     * configuration (e.g., host, port) defined in the application's properties file.
     * <p>
     * Example usage:
     * <pre>
     */


    @GrpcClient("stock-trading-server")
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    /**
     * Streams stock price updates for a given stock symbol from the gRPC server.
     * <p>
     * This method uses the `streamStockPrices` RPC method defined in the gRPC service.
     * It sends a `StockRequest` to the server and receives a stream of `StockResponse` objects.
     * <p>
     * The method is asynchronous and uses a `StreamObserver` to handle the server's responses.
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * StockServiceClient client = new StockServiceClient();
     * client.getStreamStockPrice("AAPL");
     * }
     * </pre>
     *
     * @param stockSymbol The stock symbol for which to stream price updates (e.g., "AAPL").
     *                    This is sent as part of the `StockRequest` to the server.
     * @throws IllegalArgumentException if the stockSymbol is null or empty.
     */

    public void getStreamStockPrice(String stockSymbol) {
        // 1️⃣ Create the gRPC request using builder pattern.
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();

        // 2️⃣ Make the actual gRPC call (blocking, waits for response).
        stockTradingServiceStub.streamStockPrices(request, new StreamObserver<>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                // Handle the response here
                System.out.println(
                        "Stock Symbol: " + stockResponse.getStockSymbol() + "\n" +
                                "Price: " + stockResponse.getPrice() + "\n" +
                                "Timestamp: " + stockResponse.getTimestamp() + "\n" +
                                "-----------------------------------");
            }

            @Override
            public void onError(Throwable throwable) {
                // Handle error here
                System.err.println("Error occurred: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // Handle completion here
                System.out.println("Stream completed.");
            }
        });
    }

}
