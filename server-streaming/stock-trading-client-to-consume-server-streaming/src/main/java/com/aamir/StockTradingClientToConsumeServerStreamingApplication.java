package com.aamir;

import com.aamir.service.StockServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockTradingClientToConsumeServerStreamingApplication implements CommandLineRunner {

	@Autowired
	private StockServiceClient stockServiceClient;

	public static void main(String[] args) {
		SpringApplication.run(StockTradingClientToConsumeServerStreamingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Client Response: " + stockServiceClient.getStockPrice("AAPL"));
	}
}
