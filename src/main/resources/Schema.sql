CREATE TABLE INVENTORY
(
    ID serial primary key,
    BUS_NAME character varying(50),
	LOCN_NBR integer not null default 0,
	BUS_UNIT  character varying(3) NOT NULL,
    LOCN_BRCD character varying(25) NOT NULL,
    LOCN_CLS character varying(1) NOT NULL,
    ITEM_BRCD character varying(25) NULL,
    QTY integer NOT NULL DEFAULT 0,
    STAT_CODE integer DEFAULT 0,
	ILPN  character varying(50),
	BATCH_NBR character varying(50),
	PACKAGE_NBR character varying(50),
    ORDER_NBR character varying(50),
    ORDER_LINE_NBR integer,
    ORDER_ID integer,
	TRANSIT_CONTAINER_NBR  character varying(50),
	SOURCE character varying(50),
	TRANSACTION_NAME character varying(50),
	TRACK_BY_LPN character varying(1),
	REF_FIELD_1  character varying(50),
	REF_FIELD_2  character varying(50),
	HOST_NAME  character varying(50),
    CREATED_DTTM  timestamp not null default NOW(),
    UPDATED_DTTM  timestamp not null default NOW(),
    CREATED_BY character varying(25),
    UPDATED_BY character varying(25)
);


