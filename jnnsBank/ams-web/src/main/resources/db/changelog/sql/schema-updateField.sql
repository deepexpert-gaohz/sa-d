update
	yd_account_public ap
set
	ap.yd_organ_full_id = (select yd_organ_full_id from yd_accounts_all where
	ap.yd_account_id= yd_id)

