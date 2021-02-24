UPDATE yd_sys_permission a,
( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'customerSmart_manager' ) b
SET a.yd_description = '客户信息管理',
a.yd_order_num = '100',
a.yd_parent_id = b.yd_id,
a.yd_title = '客户信息管理',
a.yd_icon = 'fa-handshake-o'
WHERE
	a.yd_code = 'customer_manager'
	AND a.yd_parent_id = '-1'