package com.example.inventory.exception;

import com.example.inventory.dto.events.BaseEvent;

import lombok.Data;

@Data
public class InsufficientInventoryException extends Exception{
	public InsufficientInventoryException(String errorMsg) {
		super(errorMsg);
	}
}
