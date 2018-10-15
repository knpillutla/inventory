package com.threedsoft.inventory.exception;

import lombok.Data;

@Data
public class InsufficientInventoryException extends Exception{
	public InsufficientInventoryException(String errorMsg) {
		super(errorMsg);
	}
}
