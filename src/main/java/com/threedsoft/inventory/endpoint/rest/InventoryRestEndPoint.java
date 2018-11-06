package com.threedsoft.inventory.endpoint.rest;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.threedsoft.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.threedsoft.inventory.dto.requests.InventoryCreationRequestDTO;
import com.threedsoft.inventory.dto.requests.InventorySearchRequestDTO;
import com.threedsoft.inventory.dto.requests.InventoryUpdateRequestDTO;
import com.threedsoft.inventory.exception.InventoryException;
import com.threedsoft.inventory.service.InventoryServiceByItem;
import com.threedsoft.util.dto.ErrorResourceDTO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/inventory/v1")
@Api(value = "Inventory Service", description = "Operations pertaining to Inventory")
@RefreshScope
@Slf4j
public class InventoryRestEndPoint {

	@Autowired
	InventoryServiceByItem invnService;

	@Value("${wms.service.health.msg: Inventory Service - Config Server is not working..please check}")
	private String healthMsg;

	@Value("${wms.service.ready.msg: Inventory Service - Not ready yet}")
	private String readyMsg;

	@GetMapping("/ready")
	public ResponseEntity ready() throws Exception {
		return ResponseEntity.ok(readyMsg);
	}

	@GetMapping("/health")
	public ResponseEntity health() throws Exception {
		return ResponseEntity.ok(healthMsg);
	}

	@GetMapping("/{busName}/{locnNbr}/inventory/{id}")
	public ResponseEntity getById(@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long id)
			throws IOException {
		try {
			return ResponseEntity.ok(invnService.findById(locnNbr, id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting inventory for id:" + id + ":" + e.getMessage()));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/inventory")
	public ResponseEntity getInventoryList(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr) throws IOException {
		try {
			return ResponseEntity.ok(invnService.findByBusNameAndLocnNbr(busName, locnNbr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error occured while getting inventory  list for busName:" + busName + ",locnNbr:" + locnNbr
									+ ":" + e.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/inventory/reserve/{id}")
	public ResponseEntity reserveInventory(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @RequestBody InventoryAllocationRequestDTO invnAllocationReq)
			throws IOException {
		try {
			return ResponseEntity.ok(invnService.allocateInventory(invnAllocationReq));
		} catch (InventoryException ex) {
			log.error("ReserveInventory Error:", ex.getEvent(), ex);
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error while reserving inventory for busName:" + busName + ",locnNbr:" + locnNbr
					+ ":" + ex.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/inventory/{id}")
	public ResponseEntity updateInventory(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @RequestBody InventoryUpdateRequestDTO invnUpdateReq)
			throws IOException {
		try {
			return ResponseEntity.ok(invnService.updateInventory(invnUpdateReq));
		} catch (InventoryException ex) {
			log.error("Inventory Update Error:", ex.getEvent(), ex);
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while updating inventory:" + ex.getMessage(), invnUpdateReq));
		}
	}

	@DeleteMapping("/{busName}/{locnNbr}/inventory/{id}")
	public ResponseEntity deleteInventory(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long id) throws IOException {
		try {
			return ResponseEntity.ok(invnService.deleteInventory(id));
		} catch (InventoryException ex) {
			log.error("Inventory Delete Error:", ex.getEvent(), ex);
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while deleting inventory:" + ex.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/inventory")
	public ResponseEntity createInventory(@PathVariable("locnNbr") Integer locnNbr,
			@RequestBody InventoryCreationRequestDTO invnCreationReq) throws IOException {
		long startTime = System.currentTimeMillis();
		log.info("Received Inventory Create request for : " + invnCreationReq.toString() + ": at :"
				+ LocalDateTime.now());
		ResponseEntity resEntity = null;
		try {
			resEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.body(invnService.createInventory(invnCreationReq));
		} catch (InventoryException ex) {
			log.error("CreateInventory Error:", ex.getEvent(), ex);
			resEntity = ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while deleting inventory:" + ex.getMessage(), invnCreationReq));
		}
		long endTime = System.currentTimeMillis();
		log.info("Completed Inventory Create request for : " + invnCreationReq.toString() + ": at :"
				+ LocalDateTime.now() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		return resEntity;
	}
	
	@PostMapping("/{busName}/{locnNbr}/inventory/search")
	public ResponseEntity searchInventory(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr,
			@RequestBody InventorySearchRequestDTO invnSearchReq) throws IOException {
		long startTime = System.currentTimeMillis();
		log.info("Received Inventory search request for : " + invnSearchReq.toString() + ": at :" + LocalDateTime.now());
		ResponseEntity resEntity = null;
		try {
			resEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.body(invnService.searchInventory(invnSearchReq));
		} catch (Exception e) {
			e.printStackTrace();
			resEntity = ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error occured while searching for inventory:" + e.getMessage(), invnSearchReq));
		}
		long endTime = System.currentTimeMillis();
		log.info("Completed Inventory search request for : " + invnSearchReq.toString() + ": at :" + LocalDateTime.now()
				+ " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		return resEntity;
	}
	
}
