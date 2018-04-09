create or replace TRIGGER insertCustomer
AFTER INSERT ON customers
for each row
declare 
currentUSER VARCHAR2(100); 
currentDate DATE;
nextLid number;
BEGIN
    select USER into currentUSER from dual; --store current user name into currentUSER local variable.
    --insert log entry into log table after a customer is inserted
    insert into logs(USER_NAME, OPERATION, OP_TIME, TABLE_NAME, TUPLE_PKEY) values(currentUSER, 'INSERT', SYSDATE, 'CUSTOMERS', :new.cid);
END;
/

CREATE OR REPLACE TRIGGER updateCustomer
after update of visits_made on customers
for each row
DECLARE
	currentUSER VARCHAR2(100);
BEGIN
	select USER into currentUSER from dual; --store current user name into currentUSER local variable.
	insert into logs(USER_NAME, OPERATION, OP_TIME, TABLE_NAME, TUPLE_PKEY) values(currentUSER, 'UPDATE', SYSDATE, 'CUSTOMERS', :new.cid);
END;
/

CREATE OR REPLACE TRIGGER insertPurchases
after insert on purchases
for each row
DECLARE
	currentUSER VARCHAR2(100);
BEGIN
	select USER into currentUSER from dual; --store current user name into currentUSER local variable.
    insert into logs(USER_NAME, OPERATION, OP_TIME, TABLE_NAME, TUPLE_PKEY) values(currentUSER, 'INSERT', SYSDATE, 'PURCHASES', :new.pur#);
END;
/

CREATE OR REPLACE TRIGGER updateProducts
after update of qoh on products
for each row
DECLARE
	currentUSER VARCHAR2(100);
BEGIN
	select USER into currentUSER from dual; --store current user name into currentUSER local variable.
	insert into logs(USER_NAME, OPERATION, OP_TIME, TABLE_NAME, TUPLE_PKEY) values(currentUSER, 'UPDATE', SYSDATE, 'PRODUCTS', :new.pid);
END;
/

CREATE OR REPLACE TRIGGER insertSupplies
after insert on supplies
for each row
DECLARE
	currentUSER VARCHAR2(100);
BEGIN
	select USER into currentUSER from dual; --store current user name into currentUSER local variable.
    insert into logs(USER_NAME, OPERATION, OP_TIME, TABLE_NAME, TUPLE_PKEY) values(currentUSER, 'INSERT', SYSDATE, 'SUPPLIES', :new.sup#);
END;
/

create or replace trigger check_qoh_insertPurchases
after insert on purchases
for each row
declare 
	new_qoh number;
 	threshold number;
	sid_supply char(2);
	req_quantity number;
begin
	--Reduce qoh for pid in the product table by the quantity purchased
	update products set qoh = qoh - :new.qty where pid = :new.pid;
	select qoh, qoh_threshold into new_qoh, threshold from products where pid = :new.pid;
	dbms_output.put_line(new_qoh);
    dbms_output.put_line(threshold);
 
	IF (new_qoh < threshold) THEN
		select sid into sid_supply from supplies where sup# in (select min(sup#) from supplies where pid = :new.pid);
		--req_quantity holds the quantity required to supply
		req_quantity := 10 + ( threshold - new_qoh ) + 1 + new_qoh;
        dbms_output.put_line(sid_supply);
        
		insert into supplies(PID, SID, SDATE, QUANTITY) values(:new.pid, sid_supply, sysdate, req_quantity);
		req_quantity := req_quantity + new_qoh;
		--Update qoh in the product table with new qoh for pid
		update products set qoh = req_quantity where pid = :new.pid;
	END IF;
    
    --Update customers entry in the customers table
	update customers set visits_made = visits_made + 1, last_visit_date = :new.ptime where cid = :new.cid and last_visit_date <> :new.ptime;
end;
/

create or replace trigger deletePurchases
after delete on purchases
for each row
begin
	--Reduce qoh for pid in the product table by the quantity purchased
	update products set qoh = qoh + :old.qty where pid = :old.pid;
	 	
	--Update customers entry in the customers table
	update customers set visits_made = visits_made + 1, last_visit_date = SYSDATE where cid = :old.cid;
end;
/










