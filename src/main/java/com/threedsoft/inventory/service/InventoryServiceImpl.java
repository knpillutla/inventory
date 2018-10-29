package com.threedsoft.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.threedsoft.inventory.db.Inventory;
import com.threedsoft.inventory.db.InventoryRepository;
import com.threedsoft.inventory.dto.converter.InventoryDTOConverter;
import com.threedsoft.inventory.dto.events.InSufficientInventoryEvent;
import com.threedsoft.inventory.dto.events.InventoryAllocatedEvent;
import com.threedsoft.inventory.dto.events.InventoryAllocationFailedEvent;
import com.threedsoft.inventory.dto.events.InventoryCreatedEvent;
import com.threedsoft.inventory.dto.events.InventoryCreationFailedEvent;
import com.threedsoft.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.threedsoft.inventory.dto.requests.InventoryCreationRequestDTO;
import com.threedsoft.inventory.dto.responses.InventoryResourceDTO;
import com.threedsoft.inventory.exception.InventoryException;
import com.threedsoft.inventory.util.InventoryConstants;
import com.threedsoft.util.service.EventPublisher;

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
	public List<InventoryResourceDTO> allocateInventory(InventoryAllocationRequestDTO invnAllocationReq)
			throws InventoryException {
		List<InventoryResourceDTO> invnDTOList = new ArrayList();
		try {
			List<Inventory> invnList = this.getInventoryToBeAllocated(invnAllocationReq);
			for (Inventory invn : invnList) {
				invn.setOrderId(invnAllocationReq.getOrderId());
				invn.setOrderLineId(invnAllocationReq.getOrderLineId());
				invn.setOrderLineNbr(invnAllocationReq.getOrderLineNbr());
				invn.setOrderNbr(invnAllocationReq.getOrderNbr());
				invn.setBatchNbr(invnAllocationReq.getBatchNbr());
				invn.setItemBrcd(invnAllocationReq.getItemBrcd());
				invn.setStatCode(InventoryStatus.ALLOCATED.getStatCode());
				InventoryResourceDTO inventoryDTO = inventoryDTOConverter.getInventoryDTO(inventoryDAO.save(invn));
				invnDTOList.add(inventoryDTO);
				eventPublisher.publish(new InventoryAllocatedEvent(inventoryDTO,InventoryConstants.INVENTORY_SERVICE_NAME));
			}
		} catch (InventoryException ex) {
			InSufficientInventoryEvent event = new InSufficientInventoryEvent(invnAllocationReq, InventoryConstants.INVENTORY_SERVICE_NAME,
					"Insufficient Inventory for Allocation Error:" + ex.getMessage(), ex);
			InventoryException invException = new InventoryException(event);
			eventPublisher.publish(event);
			throw invException;
		} catch (Exception ex) {
			InventoryAllocationFailedEvent event = new InventoryAllocationFailedEvent(invnAllocationReq,InventoryConstants.INVENTORY_SERVICE_NAME,
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
	public InventoryResourceDTO createInventory(InventoryCreationRequestDTO invnCreationReq) throws InventoryException {
		InventoryResourceDTO inventoryDTO = null;
		try {
			Inventory newInventory = inventoryDTOConverter.getInventoryEntity(invnCreationReq);
			newInventory.setStatCode(invnCreationReq.getLocked()!=null && invnCreationReq.getLocked().trim().equals("Y") ? InventoryStatus.LOCKED.getStatCode()
					: InventoryStatus.AVAILABLE.getStatCode());
			Inventory savedInventoryObj = inventoryDAO.save(newInventory);
			inventoryDTO = inventoryDTOConverter.getInventoryDTO(savedInventoryObj);
		} catch (Exception ex) {
			log.error("Inventory Creation Error:" + ex.getMessage(), ex);
			InventoryCreationFailedEvent event = new InventoryCreationFailedEvent(invnCreationReq,InventoryConstants.INVENTORY_SERVICE_NAME,
					"Inventory Creation Error:" + ex.getMessage());
			InventoryException invException = new InventoryException(event);
			eventPublisher.publish(event);
			throw invException;

		}
		eventPublisher.publish(new InventoryCreatedEvent(inventoryDTO,InventoryConstants.INVENTORY_SERVICE_NAME));
		return inventoryDTO;
	}

	@Override
	public InventoryResourceDTO findById(Integer locnNbr, Long id) throws Exception {
		Inventory invnEntity = inventoryDAO.findById(locnNbr, (long) id);
		return inventoryDTOConverter.getInventoryDTO(invnEntity);
	}

}
