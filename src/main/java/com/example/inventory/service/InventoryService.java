package com.example.inventory.service;

import java.io.IOException;
import java.util.List;

import com.example.inventory.dto.requests.InventoryCreationRequestDTO;
import com.example.inventory.dto.events.ASNUPCReceivedEvent;
import com.example.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.example.inventory.dto.responses.InventoryDTO;
import com.example.inventory.exception.InventoryException;

public interface InventoryService {
	InventoryDTO findById(Integer locnNbr, Long pickId) throws Exception;
	List<InventoryDTO> allocateInventory(InventoryAllocationRequestDTO invnAllocationReq) throws Exception;
	InventoryDTO createInventory(InventoryCreationRequestDTO invCreationReq) throws InventoryException;
}