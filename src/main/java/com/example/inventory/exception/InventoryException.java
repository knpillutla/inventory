package com.example.inventory.exception;

import com.example.inventory.dto.events.BaseEvent;
import com.example.inventory.dto.events.ExceptionEvent;

import lombok.Data;

@Data
public class InventoryException extends Exception{
	BaseEvent event = null;
	public InventoryException(ExceptionEvent event) {
		super(event.getErrorMsg());
		this.event = event;
	}

}
