package com.threedsoft.inventory.dto.converter;

import org.springframework.stereotype.Component;

import com.threedsoft.inventory.db.Inventory;
import com.threedsoft.inventory.dto.requests.InventoryCreationRequestDTO;
import com.threedsoft.inventory.dto.responses.InventoryResourceDTO;

@Component
public class InventoryDTOConverter {

	public Inventory getInventoryEntity(InventoryCreationRequestDTO invnCreationReq) {
		Inventory inventoryEntity = new Inventory();
		inventoryEntity.setBusName(invnCreationReq.getBusName());
		inventoryEntity.setLocnNbr(invnCreationReq.getLocnNbr());
		inventoryEntity.setBusUnit(invnCreationReq.getBusUnit());
		inventoryEntity.setLocnBrcd(invnCreationReq.getLocnBrcd());
		inventoryEntity.setItemBrcd(invnCreationReq.getItemBrcd());
		inventoryEntity.setIlpn(invnCreationReq.getIlpn());
		inventoryEntity.setTrackByLPN(invnCreationReq.isTrackByLPN() ? "Y" : "N");
		inventoryEntity.setQty(invnCreationReq.isTrackByLPN() ? 1 : invnCreationReq.getQty());
		inventoryEntity.setCreatedBy(invnCreationReq.getUserId());
		inventoryEntity.setUpdatedBy(invnCreationReq.getUserId());
		return inventoryEntity;
	}

	public InventoryResourceDTO getInventoryDTO(Inventory invnEntity) {
		if (invnEntity != null) {
			InventoryResourceDTO inventoryDTO = new InventoryResourceDTO(invnEntity.getId(), invnEntity.getOrderId(),
					invnEntity.getOrderLineId(), invnEntity.getOrderLineNbr(), invnEntity.getBusName(),
					invnEntity.getLocnNbr(), invnEntity.getCompany(), invnEntity.getDivision(), invnEntity.getBusUnit(), invnEntity.getLocnBrcd(),
					invnEntity.getItemBrcd(), invnEntity.getQty(), invnEntity.getStatCode(), invnEntity.getIlpn(),
					invnEntity.getOrderNbr(), invnEntity.getPackageNbr(),
					invnEntity.getTransitContainerNbr(), invnEntity.getSource(), invnEntity.getTransactionName(),
					invnEntity.getTrackByLPN(), invnEntity.getRefField1(), invnEntity.getRefField2(),
					invnEntity.getUpdatedDttm(), invnEntity.getUpdatedBy(), invnEntity.getBatchNbr(),
					invnEntity.getUpdatedBy());
			return inventoryDTO;
		}
		return null;
	}
}
