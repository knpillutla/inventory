package com.threedsoft.inventory.service;

import java.io.IOException;
import java.util.List;

import com.threedsoft.inventory.dto.events.InventoryReceivedEvent;
import com.threedsoft.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.threedsoft.inventory.dto.requests.InventoryCreationRequestDTO;
import com.threedsoft.inventory.dto.responses.InventoryResourceDTO;
import com.threedsoft.inventory.exception.InventoryException;

public interface InventoryService {
	InventoryResourceDTO findById(Integer locnNbr, Long pickId) throws Exception;
	List<InventoryResourceDTO> allocateInventory(InventoryAllocationRequestDTO invnAllocationReq) throws Exception;
	InventoryResourceDTO createInventory(InventoryCreationRequestDTO invCreationReq) throws InventoryException;
}