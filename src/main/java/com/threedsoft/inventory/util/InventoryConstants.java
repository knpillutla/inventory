package com.threedsoft.inventory.util;

import org.springframework.beans.factory.annotation.Value;

public class InventoryConstants {
	@Value("${spring.application.name}")
	public static String INVENTORY_SERVICE_NAME;

}
