UPDATE yd_sys_permission
SET
yd_order_num = '100',
yd_icon = 'fa-search-plus',
yd_description = '批量违法采集',
yd_title = '批量违法采集',
yd_parent_id = ( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'batchCollectTask' )
WHERE
	yd_code = 'illegal_manager'