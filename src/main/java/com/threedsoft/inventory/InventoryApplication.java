package com.threedsoft.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.threedsoft.inventory.streams.InventoryStreams;
import com.threedsoft.util.service.EventPublisher;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableBinding(InventoryStreams.class)
@EnableAutoConfiguration
@EnableScheduling
@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {InventoryApplication.class, Jsr310JpaConverters.class}
)
@Slf4j
public class InventoryApplication {
	@Autowired
	InventoryStreams inventoryStreams;
	
	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}
	@Bean
	public EventPublisher eventPublisher() {
		return new EventPublisher(inventoryStreams.outboundInventory());
	}	
	@Bean
	public CorsFilter corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true); 
	    config.addAllowedOrigin("http://*the3dsoft.com");
	    config.addAllowedOrigin("http://localhost");
	    config.addAllowedOrigin("https://localhost:5000");
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
		
/*	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
	public MessageSource<String> timerMessageSource() {
		return () -> MessageBuilder.withPayload("hello").build();
	}	
*/	
}
