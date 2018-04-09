create or replace PACKAGE RETAILBUISNESS as
        procedure spShowCustomers (cust_cursor OUT SYS_REFCURSOR);
        procedure spShowDiscounts (disc_cursor OUT SYS_REFCURSOR);
        procedure spShowEmployees (employee_cursor OUT SYS_REFCURSOR);
        procedure spShowLogs (logs_cursor OUT SYS_REFCURSOR);
        procedure spShowProducts (products_cursor OUT SYS_REFCURSOR);
        procedure spShowPurchases (purchases_cursor OUT SYS_REFCURSOR);
        procedure spShowSuppliers (suppliers_cursor OUT SYS_REFCURSOR);
        procedure spShowSupplies (supplies_cursor OUT SYS_REFCURSOR);
        procedure monthly_sale_activities (eidIn IN EMPLOYEES.EID %TYPE, monthly_sale_cursor OUT SYS_REFCURSOR);
        procedure add_customer(c_idIn CUSTOMERS.CID %type, c_nameIn CUSTOMERS.NAME %type, c_telephone CUSTOMERS.TELEPHONE# %type);
        procedure add_purchase (e_idIn PURCHASES.EID %type, p_idIn PURCHASES.PID %type, c_idIn PURCHASES.CID %TYPE, qtyIn purchases.qty %type, error_msg out varchar2);
        procedure delete_purchase (purIn PURCHASES.PUR# %TYPE);
        procedure get_qoh(pidIn products.pid %type, qohOut out products.qoh %type) ;
        function fPurchase_saving(purchase_id in purchases.pur#%type)
        return number;
end;