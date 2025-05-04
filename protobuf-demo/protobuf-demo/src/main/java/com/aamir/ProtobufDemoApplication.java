package com.aamir;

import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProtobufDemoApplication {

	public static void main(String[] args) {

//		Employee.newBuilder()
//						.setId(Int64Value.of(1))
//						.setName(StringValue.of("Aamir"));
		SpringApplication.run(ProtobufDemoApplication.class, args);
	}

}
