package com.threedsoft.inventory.dto.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.threedsoft.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.threedsoft.order.dto.events.OrderPlannedEvent;
import com.threedsoft.order.dto.responses.OrderLineResourceDTO;
import com.threedsoft.order.dto.responses.OrderResourceDTO;
import com.threedsoft.util.dto.events.EventResourceConverter;

@Component
public class OrderToInventoryDTOConverter {
	
	public List<InventoryAllocationRequestDTO> createInvAllocReq(OrderPlannedEvent orderPlannedEvent) {
		//ObjectMapper mapper = new ObjectMapper();
		//OrderDTO orderDTO = mapper.convertValue(orderCreatedEvent.getRequestObj(), OrderDTO.class);
		OrderResourceDTO orderDTO = (OrderResourceDTO) EventResourceConverter
				.getObject(orderPlannedEvent.getEventResource(), orderPlannedEvent.getEventResourceClassName());
		List<InventoryAllocationRequestDTO> invnResvReqList = new ArrayList();
		for(OrderLineResourceDTO orderLineDTO : orderDTO.getOrderLines()) {
			InventoryAllocationRequestDTO invAllocReqDTO = new InventoryAllocationRequestDTO();
			invAllocReqDTO.setOrderLineId(orderLineDTO.getId());
			invAllocReqDTO.setOrderId(orderDTO.getId());
			invAllocReqDTO.setBusName(orderDTO.getBusName());
			invAllocReqDTO.setLocnNbr(orderDTO.getLocnNbr());
			invAllocReqDTO.setBusUnit(orderDTO.getBusUnit());
			invAllocReqDTO.setItemBrcd(orderLineDTO.getItemBrcd());
			invAllocReqDTO.setOrderNbr(orderDTO.getOrderNbr());
			invAllocReqDTO.setBatchNbr(orderDTO.getBatchNbr());
			invAllocReqDTO.setOrderLineNbr(orderLineDTO.getOrderLineNbr());
			//invAllocReqDTO.setPackageNbr(orderDTO.getP);	
//			invResvReqDTO.setIlpn(orderLineDTO.get.getIlpn());
//			invResvReqDTO.setTrackByLPN(orderDTO..isTrackByLPN()?"Y":"N");
			invAllocReqDTO.setQty(orderLineDTO.getOrderQty());
			invAllocReqDTO.setUserId(orderDTO.getUpdatedBy());
			invnResvReqList.add(invAllocReqDTO);
		}
		return invnResvReqList;
	}
}
