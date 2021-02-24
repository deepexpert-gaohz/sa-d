UPDATE yd_sys_permission
SET yd_description = '年检任务',
yd_order_num = '0',
yd_parent_id = ( SELECT ysp2.yd_id FROM yd_sys_permission ysp2 WHERE ysp2.yd_code = 'annual_manager_menu' ),
yd_title = '年检任务',
yd_icon = 'fa fa-tasks'
WHERE
	yd_code = 'annual_manager'
	AND yd_parent_id = '-1'