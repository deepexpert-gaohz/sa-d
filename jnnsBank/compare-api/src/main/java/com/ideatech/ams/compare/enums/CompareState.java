/**
 * 
 */
package com.ideatech.ams.compare.enums;

/**
 * @author zhailiang
 *
 */
public enum CompareState {
	
	/**
	 * 未开始
	 */
	INIT {
		@Override
		public String getName() {
			return "未开始";
		}
	},
	/**
	 * 采集中
	 */
	COLLECTING {
		@Override
		public String getName() {
			return "采集中";
		}
	},
	/**
	 * 采集中
	 */
	COLLECTSUCCESS {
		@Override
		public String getName() {
			return "采集完成";
		}
	},
	/**
	 * 暂停
	 */
	PAUSE {
		@Override
		public String getName() {
			return "暂停";
		}
	},
	/**
	 * 比对中
	 */
	COMPARING {
		@Override
		public String getName() {
			return "比对中";
		}
	},
	/**
	 * 成功
	 */
	SUCCESS {
		@Override
		public String getName() {
			return "完成";
		}
	},
	/**
	 * 失败
	 */
	FAIL {
		@Override
		public String getName() {
			return "失败";
		}
		
	},
	/**
	 * 取消
	 */
	CANCEL {
		@Override
		public String getName() {
			return "取消";
		}

	};

	public abstract String getName();

}
