package com.example.inventory.dto.converter;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.inventory.db.Inventory;
import com.example.inventory.dto.requests.InventoryCreationRequestDTO;
import com.example.inventory.dto.responses.InventoryDTO;

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

	public InventoryDTO getInventoryDTO(Inventory invnEntity) {
		if (invnEntity != null) {
			InventoryDTO inventoryDTO = new InventoryDTO(invnEntity.getId(), invnEntity.getOrderId(),
					invnEntity.getOrderLineId(), invnEntity.getOrderLineNbr(), invnEntity.getBusName(),
					invnEntity.getLocnNbr(), invnEntity.getBusUnit(), invnEntity.getLocnBrcd(),
					invnEntity.getItemBrcd(), invnEntity.getQty(), invnEntity.getStatCode(), invnEntity.getIlpn(),
					invnEntity.getBatchNbr(), invnEntity.getOrderNbr(), invnEntity.getPackageNbr(),
					invnEntity.getTransitContainerNbr(), invnEntity.getSource(), invnEntity.getTransactionName(),
					invnEntity.getTrackByLPN(), invnEntity.getRefField1(), invnEntity.getRefField2(),
					invnEntity.getUpdatedDttm(), invnEntity.getUpdatedBy(), invnEntity.getBatchNbr(),
					invnEntity.getUpdatedBy());
			return inventoryDTO;
		}
		return null;
	}
}
