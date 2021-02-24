update
	yd_customer_public cp
set
	cp.yd_organ_full_id = (select yd_organ_full_id from yd_customers_all where
	cp.yd_id= yd_id);

update
	yd_customer_public cp
set
	cp.yd_depositor_name = (select yd_depositor_name from yd_customers_all where
	cp.yd_id= yd_id);

