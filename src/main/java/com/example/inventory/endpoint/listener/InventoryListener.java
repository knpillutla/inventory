package com.example.inventory.endpoint.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.example.inventory.dto.converter.ASNUPCToInventoryConverter;
import com.example.inventory.dto.converter.OrderToInventoryDTOConverter;
import com.example.inventory.dto.events.ASNUPCReceivedEvent;
import com.example.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.example.inventory.service.InventoryService;
import com.example.inventory.streams.InventoryStreams;
import com.example.order.dto.events.OrderPlannedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryListener {
	@Autowired
	InventoryService inventoryService;

	@Autowired
	OrderToInventoryDTOConverter orderToInvnConvertor;

	@StreamListener(target=InventoryStreams.ORDERS_OUTPUT, condition = "headers['eventName']=='OrderPlannedEvent'")
	public void handleIncomingOrders(OrderPlannedEvent orderPlannedEvent) {
		log.info("Received OrderPlannedEvent, Allocation of Inventory Started: {}" + ": at :" + new java.util.Date(), orderPlannedEvent.toString());
		long startTime = System.currentTimeMillis();
		for (InventoryAllocationRequestDTO orderLineAllocationReq : orderToInvnConvertor
				.createInvAllocReq(orderPlannedEvent)) {
			try {
				inventoryService.allocateInventory(orderLineAllocationReq);

			} catch (Exception e) {
				e.printStackTrace();
				log.error("OrderLine Allocation Failed for OrderPlannedEvent, orderLineInfo :" + orderLineAllocationReq + "," + e.getMessage(), e);
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("Completed Allocation of Inventory for OrderPlannedEvent : " + orderPlannedEvent + ": at :"
				+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
	}

	@StreamListener(target=InventoryStreams.INVENTORY_INPUT,condition = "headers['eventName']=='ASNUPCReceivedEvent'")
	public void handleUPCReceipt(ASNUPCReceivedEvent upcReceivedEvent) {
		log.info("ASNUPC Received: {}" + ": at :" + new java.util.Date(),
				upcReceivedEvent);
		long startTime = System.currentTimeMillis();
		try {
			inventoryService.createInventory(ASNUPCToInventoryConverter.getInventoryCreationRequestDTO(upcReceivedEvent));
			long endTime = System.currentTimeMillis();
			log.info("Completed ASNUPC for : " + upcReceivedEvent + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Completing ASNUPC for : " + upcReceivedEvent + ", Error:" + e.getMessage(), e);
		}
	}
/*
	@StreamListener(target=InventoryStreams.INVENTORY_INPUT,condition = "headers['eventName']=='ASNUPCReceivedEvent'")
	public void handleNewInventory(InventoryCreationRequestDTO invnCreationReq) {
		log.info("InventoryCreationRequest Received: {}" + ": at :" + new java.util.Date(),
				invnCreationReq);
		long startTime = System.currentTimeMillis();
		try {
			inventoryService.createInventory(invnCreationReq);
			long endTime = System.currentTimeMillis();
			log.info("Completed InventoryCreationRequest for : " + invnCreationReq + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Completing InventoryCreationRequest for : " + invnCreationReq + ", Error:" + e.getMessage(), e);
		}
	}
*/}
