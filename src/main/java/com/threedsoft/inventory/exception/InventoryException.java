package com.threedsoft.inventory.exception;

import com.threedsoft.util.dto.events.ExceptionEvent;
import com.threedsoft.util.dto.events.WMSEvent;

import lombok.Data;

@Data
public class InventoryException extends Exception{
	WMSEvent event = null;
	public InventoryException(ExceptionEvent event) {
		super(event.getErrorMsg());
		this.event = event;
	}

}
