UPDATE yd_sys_permission a,
( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'annual_manager_menu' ) b
SET a.yd_description = '年检任务',
a.yd_order_num = '0',
a.yd_parent_id = b.yd_id,
a.yd_title = '年检任务',
a.yd_icon = 'fa fa-tasks'
WHERE
	a.yd_code = 'annual_manager'
	AND a.yd_parent_id = '-1'