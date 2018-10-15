package com.threedsoft.inventory.dto.converter;

import java.util.List;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.threedsoft.inventory.dto.events.InventoryReceivedEvent;
import com.threedsoft.inventory.dto.requests.InventoryCreationRequestDTO;
import com.threedsoft.order.dto.responses.OrderResourceDTO;
import com.threedsoft.util.dto.events.WMSEventDeserializer;

@Component
public class UPCReceivedEventToInventoryConverter {

	public static InventoryCreationRequestDTO getInventoryCreationRequestDTO(InventoryReceivedEvent upcReceivedEvent) throws ClassNotFoundException {
/*		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
        .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
        .modules(new JSR310Module())
        .build();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		InventoryCreationRequestDTO invnCreationReqDTO = mapper.convertValue(upcReceivedEvent.getEventResource(), InventoryCreationRequestDTO.class);
*/
		InventoryCreationRequestDTO invnCreationReqDTO =  upcReceivedEvent.getInvnCreationReq();
				//(InventoryCreationRequestDTO) WMSEventDeserializer.deserialize(upcReceivedEvent.getEventResource(), upcReceivedEvent.getEventResource().getClass());
		/*		InventoryCreationRequestDTO invnCreationReqDTO = new InventoryCreationRequestDTO();
		invnCreationReqDTO.setBusName(upcReceivedEvent.getBusName());
		invnCreationReqDTO.setLocnNbr(upcReceivedEvent.getLocnNbr());
		invnCreationReqDTO.setItemBrcd(upcReceivedEvent.getItemBrcd());
		invnCreationReqDTO.setCompany(upcReceivedEvent.getCompany());
		invnCreationReqDTO.setDivision(upcReceivedEvent.getDivision());
		invnCreationReqDTO.setLocked(false);
		invnCreationReqDTO.setQty(upcReceivedEvent.getQty());
		invnCreationReqDTO.setBusUnit(upcReceivedEvent.getBusUnit());
		invnCreationReqDTO.setCompany("");
		invnCreationReqDTO.setDivision("");
		invnCreationReqDTO.setUserId("Krishna");*/
		return invnCreationReqDTO;
	}
}
