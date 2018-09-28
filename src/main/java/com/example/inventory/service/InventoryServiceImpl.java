package com.example.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.db.Inventory;
import com.example.inventory.db.InventoryRepository;
import com.example.inventory.dto.converter.InventoryDTOConverter;
import com.example.inventory.dto.events.InSufficientInventoryEvent;
import com.example.inventory.dto.events.InventoryAllocatedEvent;
import com.example.inventory.dto.events.InventoryAllocationFailedEvent;
import com.example.inventory.dto.events.InventoryCreatedEvent;
import com.example.inventory.dto.events.InventoryCreationFailedEvent;
import com.example.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.example.inventory.dto.requests.InventoryCreationRequestDTO;
import com.example.inventory.dto.responses.InventoryDTO;
import com.example.inventory.exception.InventoryException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class InventoryServiceImpl implements InventoryService {
	@Autowired
	InventoryRepository inventoryDAO;

	@Autowired
	EventPublisher eventPublisher;

	@Autowired
	InventoryDTOConverter inventoryDTOConverter;

	public enum InventoryStatus {
		LOCKED(100), AVAILABLE(110), ALLOCATED(120), PICKED(130), PACKED(140), CYCLECOUNT(150), SHIPPED(160);
		InventoryStatus(Integer statCode) {
			this.statCode = statCode;
		}

		private Integer statCode;

		public Integer getStatCode() {
			return statCode;
		}
	}

	@Override
	@Transactional
	public List<InventoryDTO> allocateInventory(InventoryAllocationRequestDTO invnAllocationReq)
			throws InventoryException {
		List<InventoryDTO> invnDTOList = new ArrayList();
		try {
			List<Inventory> invnList = this.getInventoryToBeAllocated(invnAllocationReq);
			for (Inventory invn : invnList) {
				invn.setOrderId(invnAllocationReq.getOrderId());
				invn.setOrderLineId(invnAllocationReq.getOrderLineId());
				invn.setOrderLineNbr(invnAllocationReq.getOrderLineNbr());
				invn.setOrderNbr(invnAllocationReq.getOrderNbr());
				invn.setBatchNbr(invnAllocationReq.getBatchNbr());
				invn.setItemBrcd(invnAllocationReq.getItemBrcd());
				invn.setUpdatedDttm(new java.util.Date());
				invn.setStatCode(InventoryStatus.ALLOCATED.getStatCode());
				InventoryDTO inventoryDTO = inventoryDTOConverter.getInventoryDTO(inventoryDAO.save(invn));
				invnDTOList.add(inventoryDTO);
				eventPublisher.publish(new InventoryAllocatedEvent(inventoryDTO));
			}
		} catch (InventoryException ex) {
			InSufficientInventoryEvent event = new InSufficientInventoryEvent(invnAllocationReq,
					"Insufficient Inventory for Allocation Error:" + ex.getMessage());
			InventoryException invException = new InventoryException(event);
			eventPublisher.publish(event);
			throw invException;
		} catch (Exception ex) {
			InventoryAllocationFailedEvent event = new InventoryAllocationFailedEvent(invnAllocationReq,
					"Inventory Allocation Error:" + ex.getMessage());
			InventoryException invException = new InventoryException(event);
			eventPublisher.publish(event);
			throw invException;
		}
		return invnDTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public abstract List<Inventory> getInventoryToBeAllocated(InventoryAllocationRequestDTO invnResvRequest)
			throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	@Transactional
	public InventoryDTO createInventory(InventoryCreationRequestDTO invnCreationReq) throws InventoryException {
		InventoryDTO inventoryDTO = null;
		try {
			Inventory newInventory = inventoryDTOConverter.getInventoryEntity(invnCreationReq);
			newInventory.setStatCode(invnCreationReq.isLocked() ? InventoryStatus.LOCKED.getStatCode()
					: InventoryStatus.AVAILABLE.getStatCode());
			Inventory savedInventoryObj = inventoryDAO.save(newInventory);
			inventoryDTO = inventoryDTOConverter.getInventoryDTO(savedInventoryObj);
		} catch (Exception ex) {
			log.error("Inventory Creation Error:" + ex.getMessage(), ex);
			InventoryCreationFailedEvent event = new InventoryCreationFailedEvent(invnCreationReq,
					"Inventory Creation Error:" + ex.getMessage());
			InventoryException invException = new InventoryException(event);
			eventPublisher.publish(event);
			throw invException;

		}
		eventPublisher.publish(new InventoryCreatedEvent(inventoryDTO));
		return inventoryDTO;
	}

	@Override
	public InventoryDTO findById(Integer locnNbr, Long id) throws Exception {
		Inventory invnEntity = inventoryDAO.findById(locnNbr, (long) id);
		return inventoryDTOConverter.getInventoryDTO(invnEntity);
	}

}
