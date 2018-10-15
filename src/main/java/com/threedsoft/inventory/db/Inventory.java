package com.threedsoft.inventory.db;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Data
@Table(name="INVENTORY")
@EntityListeners(AuditingEntityListener.class)
public class Inventory  implements Serializable{
	@Column(name="ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name="BUS_NAME")
	String busName;

	@Column(name="LOCN_NBR")
	Integer locnNbr;

	@Column(name="COMPANY")
	String company;

	@Column(name="DIVISION")
	String division;

	@Column(name="BUS_UNIT")
	String busUnit;

	@Column(name="LOCN_BRCD")
	String locnBrcd;

	@Column(name="ITEM_BRCD")
	String itemBrcd;

	@Column(name="QTY")
	Integer qty;

	@Column(name="STAT_CODE")
	Integer statCode;

	@Column(name="ILPN")
	String ilpn;

	@Column(name="BATCH_NBR")
	String batchNbr;

	@Column(name="PACKAGE_NBR")
	String packageNbr;

	@Column(name="ORDER_NBR")
	String orderNbr;

	@Column(name="ORDER_LINE_NBR")
	Integer orderLineNbr;

	@Column(name="ORDER_ID")
	Long orderId;
	
	@Column(name="ORDER_LINE_ID")
	Long orderLineId;

	@Column(name="TRANSIT_CONTAINER_NBR")
	String transitContainerNbr;

	@Column(name="SOURCE")
	String source;

	@Column(name="TRANSACTION_NAME")
	String transactionName;

	@Column(name="TRACK_BY_LPN")
	String trackByLPN;

	@Column(name="REF_FIELD_1")
	String refField1;

	@Column(name="REF_FIELD_2")
	String refField2;

	@Column(name="HOST_NAME")
	String hostName;

    @CreatedDate
	@Column(name="CREATED_DTTM", nullable = false, updatable = false)
    LocalDateTime createdDttm;
	
    @Column(name = "UPDATED_DTTM", nullable = false)
    @LastModifiedDate
	LocalDateTime updatedDttm;
	
	@Column(name="CREATED_BY")
	String createdBy;

	@Column(name="UPDATED_BY")
	String updatedBy;

	@Version
 	@Column(name="VERSION")
	Integer version; 	
}
