package com.aamir.controller;

import com.aamir.StockRequest;
import com.aamir.StockResponse;
import com.aamir.StockTradingServiceGrpc;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import net.devh.boot.grpc.client.inject.GrpcClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/stocks")
public class StockStreamingController {

    @GrpcClient("stock-trading-server")
    private StockTradingServiceGrpc.StockTradingServiceStub stockServiceStub;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping(value = "/subscribe/{symbol}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeStockPrice(@PathVariable String symbol) {
        SseEmitter emitter = new SseEmitter();
        executor.execute(() -> {
            StockRequest request = StockRequest.newBuilder().setStockSymbol(symbol).build();

            stockServiceStub.streamStockPrices(request, new StreamObserver<>() {
                @Override
                public void onNext(StockResponse response) {
                    try {
                        String jsonResponse = JsonFormat.printer().print(response);
                        emitter.send(jsonResponse);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    emitter.completeWithError(t);
                }

                @Override
                public void onCompleted() {
                    emitter.complete();
                }
            });
        });
        return emitter;
    }
}
