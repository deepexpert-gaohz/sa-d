UPDATE yd_sys_permission
SET yd_description = '客户信息管理',
yd_order_num = '100',
yd_parent_id = ( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'customerSmart_manager' ),
yd_title = '客户信息管理',
yd_icon = 'fa-handshake-o'
WHERE
	yd_code = 'customer_manager'
	AND yd_parent_id = '-1'