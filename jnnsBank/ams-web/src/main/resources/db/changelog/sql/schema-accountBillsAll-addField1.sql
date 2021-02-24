update
	yd_account_bills_all ab
set
	ab.yd_depositor_type = (select ap.yd_depositor_type from yd_account_public ap where
	ab.yd_account_id= ap.yd_account_id)

