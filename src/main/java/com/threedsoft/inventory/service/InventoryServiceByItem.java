package com.threedsoft.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.threedsoft.inventory.db.Inventory;
import com.threedsoft.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.threedsoft.inventory.dto.requests.InventorySearchRequestDTO;
import com.threedsoft.inventory.dto.requests.InventoryUpdateRequestDTO;
import com.threedsoft.inventory.dto.responses.InventoryResourceDTO;
import com.threedsoft.inventory.exception.InsufficientInventoryException;
import com.threedsoft.inventory.exception.InventoryException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryServiceByItem extends InventoryServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(InventoryServiceByItem.class);

	@Override
	public List<Inventory> getInventoryToBeAllocated(InventoryAllocationRequestDTO invnAllocationRequest) throws InsufficientInventoryException{
		List<Inventory> invnEntityList = null;
		invnEntityList = inventoryDAO.findByBusNameAndLocnNbrAndItemBrcd(invnAllocationRequest.getBusName(), invnAllocationRequest.getLocnNbr(), invnAllocationRequest.getItemBrcd(), InventoryStatus.AVAILABLE.getStatCode());
		int totalQtyReserved = 0;
		int qtyToBeReserved = invnAllocationRequest.getQty();
		List<Inventory> reservedEntityList = new ArrayList();
		if(invnEntityList == null)
			return reservedEntityList;
		for(Inventory invn : invnEntityList) {
			int qtyReserved = 0;
			if(invn.getQty()-qtyToBeReserved<=0) {
				reservedEntityList.add(invn);
				qtyReserved =  invn.getQty();
			}else {
				qtyReserved = qtyToBeReserved;
				// create new inventory record for the reserved inventory
				Inventory newInventory = new Inventory();
				newInventory.setLocnBrcd(invn.getLocnBrcd());
				newInventory.setItemBrcd(invn.getItemBrcd());
				newInventory.setQty(qtyReserved);
				invn.setBusName(invn.getBusName());
				invn.setBusUnit(invn.getBusUnit());
				invn.setLocnNbr(invn.getLocnNbr());
				invn.setIlpn(invn.getIlpn());
				invn.setPackageNbr(invn.getPackageNbr());
				reservedEntityList.add(newInventory);
						
				// update available qty for the current locn/item
				invn.setQty(invn.getQty()-qtyReserved);
				reservedEntityList.add(invn);
				break;
			}
			totalQtyReserved = totalQtyReserved + qtyReserved;
			qtyToBeReserved = qtyToBeReserved - qtyReserved;
		}
		
		// there is not enough quantity availabile in Active to reserve, throw exception or trigger replenishment
		if(qtyToBeReserved>0){
			throw new InsufficientInventoryException("Not Enough Quantity To Reserve for Item:" + invnAllocationRequest.toString());
		}
		return reservedEntityList;
	}

	public List<InventoryResourceDTO> findByBusNameAndLocnNbr(String busName, Integer locnNbr) {
		PageRequest pageRequest = new PageRequest(0, 20);
		List<Inventory> invnEntityList = inventoryDAO.findByBusNameAndLocnNbr(busName, locnNbr, pageRequest);
		List<InventoryResourceDTO> inventoryDTOList = new ArrayList();
		for(Inventory invnEntity : invnEntityList) {
			inventoryDTOList.add(inventoryDTOConverter.getInventoryDTO(invnEntity));
		}
		return inventoryDTOList;
	}

	@Override
	public InventoryResourceDTO updateInventory(InventoryUpdateRequestDTO invnUpdateReq) throws InventoryException{
		Optional<Inventory> optionalInventoryEntity = inventoryDAO.findById(invnUpdateReq.getId());
		if(optionalInventoryEntity.isPresent()) {
			Inventory invnEntity = optionalInventoryEntity.get();
			invnEntity.setLocnBrcd(invnUpdateReq.getLocnBrcd());
			invnEntity.setItemBrcd(invnUpdateReq.getItemBrcd());
			invnEntity.setQty(invnUpdateReq.getQty());
			invnEntity.setIlpn(invnUpdateReq.getIlpn());
			invnEntity.setTrackByLPN(invnUpdateReq.getTrackByLPN());
			invnEntity.setLocked(invnUpdateReq.getLocked());
			Inventory savedInvnEntity = inventoryDAO.save(invnEntity);
			return inventoryDTOConverter.getInventoryDTO(savedInvnEntity);
		}
		return null;
	}

	@Override
	public InventoryResourceDTO deleteInventory(Long id) throws InventoryException{
		Optional<Inventory> optionalInventoryEntity = inventoryDAO.findById(id);
		if(optionalInventoryEntity.isPresent()) {
			Inventory invnEntity = optionalInventoryEntity.get();
			inventoryDAO.delete(invnEntity);
			return inventoryDTOConverter.getInventoryDTO(invnEntity);
		}
		return null;
	}

	@Override
	public List<InventoryResourceDTO> searchInventory(InventorySearchRequestDTO invnSearchReq) throws InventoryException{
		PageRequest pageRequest = new PageRequest(0, 50);
		Inventory searchInventory = inventoryDTOConverter.getInventoryEntityForSearch(invnSearchReq);
		Page<Inventory> invnEntityPage = inventoryDAO.findAll(Example.of(searchInventory), pageRequest);
		List<InventoryResourceDTO> inventoryDTOList = new ArrayList();
		for(Inventory invnEntity : invnEntityPage) {
			inventoryDTOList.add(inventoryDTOConverter.getInventoryDTO(invnEntity));
		}
		return inventoryDTOList;
	}
}
