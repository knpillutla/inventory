package com.example.inventory.endpoint.rest;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.esotericsoftware.minlog.Log;
import com.example.inventory.dto.events.InventoryCreationFailedEvent;
import com.example.inventory.dto.requests.InventoryCreationRequestDTO;
import com.example.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.example.inventory.exception.InventoryException;
import com.example.inventory.service.InventoryServiceByItem;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/inventory/v1")
@Api(value="Inventory Service", description="Operations pertaining to Inventory")
@RefreshScope
@Slf4j
public class InventoryRestEndPoint {

    @Value("${message: Inventory Service - Config Server is not working..pelase check}")
    private String msg;
    
    @Autowired
    InventoryServiceByItem invnService;
	
	@GetMapping("/")
	public ResponseEntity hello() throws Exception {
		return ResponseEntity.ok(msg);
	}
	
	@GetMapping("/{locnNbr}/inventory/{id}")
	public ResponseEntity getById(@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long id) throws IOException {
		try {
			return ResponseEntity.ok(invnService.findById(locnNbr, id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@PostMapping("/{locnNbr}/picks/{id}")
	public ResponseEntity reserveInventory(@PathVariable("locnNbr") Integer locnNbr, @RequestBody InventoryAllocationRequestDTO invnAllocationReq) throws IOException {
		try {
			return ResponseEntity.ok(invnService.allocateInventory(invnAllocationReq));
		}catch (InventoryException ex) {
			log.error("ReserveInventory Error:", ex.getEvent(), ex);
			return ResponseEntity.badRequest().body(ex.getEvent());
		} 
	}	
	
	@PutMapping("/{locnNbr}/inventory")
	public ResponseEntity createInventory(@PathVariable("locnNbr") Integer locnNbr, @RequestBody InventoryCreationRequestDTO invnCreationReq) throws IOException {
		long startTime = System.currentTimeMillis();
		log.info("Received Inventory Create request for : " + invnCreationReq.toString() + ": at :" + new java.util.Date());
		ResponseEntity resEntity = null;
		try {
			resEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(invnService.createInventory(invnCreationReq));
		} catch (InventoryException ex) {
			log.error("CreateInventory Error:", ex.getEvent(), ex);
			resEntity = ResponseEntity.badRequest().body(ex.getEvent());
		}
		long endTime = System.currentTimeMillis();
		log.info("Completed Inventory Create request for : " + invnCreationReq.toString() + ": at :" + new java.util.Date() + " : total time:" + (endTime-startTime)/1000.00 + " secs");
		return resEntity;
	}	
}
