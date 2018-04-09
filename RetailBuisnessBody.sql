CREATE or REPLACE PACKAGE BODY "RETAILBUISNESS" AS
    procedure spShowCustomers (cust_cursor OUT SYS_REFCURSOR) is
        begin
          open cust_cursor for
          select * from CUSTOMERS order by CID;        
        end;
    
    procedure spShowDiscounts (disc_cursor OUT SYS_REFCURSOR) is     
        begin
          open disc_cursor for
          select * from DISCOUNTS order by DISCNT_CATEGORY;
        end;  
        
    procedure spShowEmployees (employee_cursor OUT SYS_REFCURSOR) is  
        begin
            open employee_cursor for
            select * from EMPLOYEES order by EID;
        end;    
        
    procedure spShowLogs (logs_cursor OUT SYS_REFCURSOR) is   
        begin
            open logs_cursor for
            select * from LOGS order by LOG#;
        end; 
    
    procedure spShowProducts (products_cursor OUT SYS_REFCURSOR) is   
        begin
            open products_cursor for
            select * from PRODUCTS order by PID;
        end;     
        
    procedure spShowPurchases (purchases_cursor OUT SYS_REFCURSOR) is
        begin
            open purchases_cursor for
            select * from PURCHASES order by PUR#;
        end;
        
    procedure spShowSuppliers (suppliers_cursor OUT SYS_REFCURSOR) is
        begin
            open suppliers_cursor for
            select * from SUPPLIERS order by SID;
        end;
    
    procedure spShowSupplies (supplies_cursor OUT SYS_REFCURSOR) is
        begin
            open supplies_cursor for
            select * from SUPPLIES order by SUP#;
        end;
        
    -- input parameter eidIn should be in small letters check while running from java application
    procedure monthly_sale_activities (eidIn IN EMPLOYEES.EID %TYPE, monthly_sale_cursor OUT SYS_REFCURSOR) 
    is
    begin
        --add application error here
        open monthly_sale_cursor for
        select p.eid, e.name, to_char(p.ptime, 'mon') as month, to_char(p.ptime, 'yyyy') as year, count(*) as employee_sales, SUM(QTY) as total_quantity, SUM(total_price) as tatal_amount_sold
        from PURCHASES p
        join EMPLOYEES e
        on p.EID = e.EID
        where p.EID = eidIn
        group by p.eid, e.name, to_char(p.ptime, 'yyyy'), to_char(p.ptime, 'mon')
        order by year, month;
    end;
    
    procedure add_customer(c_idIn CUSTOMERS.CID %type, c_nameIn CUSTOMERS.NAME %type, c_telephone CUSTOMERS.TELEPHONE# %type)
    is
    begin
        insert into CUSTOMERS values (c_idIn, c_nameIn, c_telephone, 1, SYSDATE);
        commit;
    end;
    
    procedure add_purchase (e_idIn PURCHASES.EID %type, p_idIn PURCHASES.PID %type, c_idIn PURCHASES.CID %TYPE, qtyIn purchases.qty %type, error_msg out varchar2) 
    is
    varOriginalPrice PRODUCTS.ORIGINAL_PRICE %TYPE;
    varDiscntRate DISCOUNTS.DISCNT_RATE %TYPE;
    varQuantity PRODUCTS.QOH %TYPE;
    varQohThreshold PRODUCTS.QOH_THRESHOLD %TYPE;
    varPricePerItem number;
    varTotalPrice number;
    begin
        select products.ORIGINAL_PRICE , discounts.DISCNT_RATE, products.QOH, products.QOH_THRESHOLD
        into varOriginalPrice, varDiscntRate, varQuantity, varQohThreshold
        from products 
        join discounts
        on products.DISCNT_CATEGORY = discounts.DISCNT_CATEGORY
        where products.PID = p_idIn;
        --exception for no. of rows of above select statement = 0
        
        if(qtyIn <= varQuantity) then 
            varTotalPrice := (varOriginalPrice * (1-varDiscntRate)) * qtyIn;
            if((varQuantity - qtyIn) < varQohThreshold) then
                error_msg := 'ONE'; -- qty below threshold
            else
                error_msg := 'TWO'; -- purchase completed
            end if;
            insert into purchases(eid, pid, cid, qty, ptime, total_price) values(e_idIn, p_idIn, c_idIn, qtyIn, SYSDATE, varTotalPrice);  
            commit;
        else
            error_msg := 'THREE'; -- cannot buy this product
        end if;
    end;
    
    procedure delete_purchase (purIn PURCHASES.PUR# %TYPE)
    is
    begin
        delete from PURCHASES where PUR# = purIn;
        commit;
    end; 
    
    procedure get_qoh(pidIn products.pid %type, qohOut out products.qoh %type) 
    is
    begin
        select qoh into qohOut from products where pid = pidIn;
    end;
    
   function fPurchase_saving (purchase_id in purchases.pur# %type) return  number is saving number;
    begin
        select (pr.qty*p.original_price)- pr.total_price into saving 
        from purchases pr 
        join products p  
        on p.pid = pr.pid
        where pr.pur# = purchase_id;
        return saving;
    end;
END;    