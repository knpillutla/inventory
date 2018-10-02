package com.example.inventory.dto.converter;

import org.springframework.stereotype.Component;

import com.example.inventory.dto.events.UPCReceivedEvent;
import com.example.inventory.dto.requests.InventoryCreationRequestDTO;
import com.example.inventory.dto.responses.InventoryDTO;

@Component
public class UPCReceivedEventToInventoryConverter {

	public static InventoryCreationRequestDTO getInventoryCreationRequestDTO(UPCReceivedEvent upcReceivedEvent) {
		InventoryCreationRequestDTO invnCreationReqDTO = new InventoryCreationRequestDTO();
		invnCreationReqDTO.setBusName(upcReceivedEvent.getBusName());
		invnCreationReqDTO.setLocnNbr(upcReceivedEvent.getLocnNbr());
		invnCreationReqDTO.setItemBrcd(upcReceivedEvent.getItemBrcd());
		invnCreationReqDTO.setLocked(false);
		invnCreationReqDTO.setQty(upcReceivedEvent.getQty());
		invnCreationReqDTO.setBusUnit(upcReceivedEvent.getBusUnit());
		invnCreationReqDTO.setCompany("");
		invnCreationReqDTO.setDivision("");
		invnCreationReqDTO.setUserId("Krishna");
		return invnCreationReqDTO;
	}
}
