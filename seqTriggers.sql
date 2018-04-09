create Sequence purchaseSequence start with 100043 increment by 1; -- generates a six digit sequence number
create Sequence suppliesSequence start with 1011 increment by 1; -- generates a four digit sequence number
create Sequence logsSequence start with 10000 increment by 1; -- generates a five digit sequence number

create or replace trigger purchaseSeqtrigger
        before insert on purchases
        for each row
begin
		-- auto increment the logid using the sequence that was created above.
        if(:new.pur# is NULL) then
                :new.pur# := purchaseSequence.nextval;
        end if;
end;
/

create or replace trigger suppliesSeqtrigger
        before insert on supplies
        for each row
begin
		-- auto increment the sup# using the sequence that was created above.
        if(:new.sup# is NULL) then
                :new.sup# := suppliesSequence.nextval;
        end if;
end;
/

create or replace trigger logseqtrigger
        before insert on logs
        for each row
begin
		-- auto incrementing the log# using the sequence that was created above.
        if(:new.log# is NULL) then
                :new.log# := logsSequence.nextval;
        end if;
end;
/






