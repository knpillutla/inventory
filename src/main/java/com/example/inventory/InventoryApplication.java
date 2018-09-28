package com.example.inventory;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.inventory.streams.InventoryStreams;

@SpringBootApplication
@EnableBinding(InventoryStreams.class)
@EnableAutoConfiguration
@EnableScheduling
public class InventoryApplication {
	private Random random = new Random();
	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}
	
/*	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
	public MessageSource<String> timerMessageSource() {
		return () -> MessageBuilder.withPayload("hello").build();
	}	
*/	
}
