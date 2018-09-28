package com.example.inventory.dto.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.inventory.dto.requests.InventoryAllocationRequestDTO;
import com.example.order.dto.events.OrderPlannedEvent;
import com.example.order.dto.responses.OrderDTO;
import com.example.order.dto.responses.OrderLineDTO;

@Component
public class OrderToInventoryDTOConverter {
	
	public List<InventoryAllocationRequestDTO> createInvAllocReq(OrderPlannedEvent orderPlannedEvent) {
		//ObjectMapper mapper = new ObjectMapper();
		//OrderDTO orderDTO = mapper.convertValue(orderCreatedEvent.getRequestObj(), OrderDTO.class);
		OrderDTO orderDTO = orderPlannedEvent.getOrderDTO();
		List<InventoryAllocationRequestDTO> invnResvReqList = new ArrayList();
		for(OrderLineDTO orderLineDTO : orderDTO.getOrderLines()) {
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
