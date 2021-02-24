UPDATE yd_sys_permission a,
( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'batchCollectTask' ) b
SET
a.yd_order_num = '100',
a.yd_icon = 'fa-search-plus',
a.yd_title = '批量违法采集',
a.yd_description = '批量违法采集',
a.yd_parent_id = b.yd_id
WHERE
	a.yd_code = 'illegal_manager'