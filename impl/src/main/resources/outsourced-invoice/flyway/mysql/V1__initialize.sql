create table osdi_outsourced_invoice (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	invoice_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	project_id binary(16),
	receive_address_detail varchar(50),
	receive_address_postal_code varchar(10),
	receive_address_street varchar(50),
	receiver_id varchar(50),
	remark varchar(50),
	status varchar(20),
	supplier_id varchar(50),
	primary key (id)
) engine=InnoDB;

create table osdi_outsourced_invoice_item (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	invoice_id binary(16),
	invoice_item_id binary(16),
	item_id binary(16),
	item_spec_code varchar(20),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	quantity decimal(19,2),
	remark varchar(50),
	unit varchar(20),
	primary key (id)
) engine=InnoDB;

alter table osdi_outsourced_invoice
	add constraint UKt0gobbk7kbtr51i3i768078tm unique (invoice_id);

create index IDXgt1rchhjlq7cdxxlul6s2h9mo
	on osdi_outsourced_invoice_item (invoice_id);
