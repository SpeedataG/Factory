package common.utils;


public class Contants {
	public static String app_version = "360PETRO V2.4";
	public static boolean debug = false;//给客户的是false
	public static int TIME_OUT_SECOND = 10;
	public static String URL = "172.16.68.1";
	public static int PORT = 50001;
//	public static String URL = "124.207.104.52";
//	public static int PORT = 2009;
	// public static String URL = "172.21.194.1";
	// public static int PORT = 8000;
	// public static String URL = "10.1.2.189";
	// public static int PORT = 2009;
	// public static int PORT = 50001;
	public static String shiftDate = "", shiftNo = "", empNo = "",
			empName = "", deptNo = "", deptName = "", posType = "mPOS";
	public final static String cmd_login = "01";
	public final static String cmd_accredit = "02";
	public final static String cmd_search_no_pay = "03";
	public final static String cmd_payment = "04";
	public final static String cmd_already_pay = "07";
	public final static String cmd_sum = "06";
	public final static String cmd_logout = "08";
	//
	public final static String pay_type_money = "1";
	public final static String pay_type_ic = "2";
	public final static String ic_type_person_card = "0";
	public final static String ic_type_conpany_card = "1";
	public final static String ic_type_gift_card = "6";
	public final static String flag_qrc_no = "3";
	public final static String flag_qrc = "0";// 二维码识别标志 识别
	//
	public final static String field_posTtc = "posTtc";// 终端交易计数器
	public final static String field_cmd = "cmd";
	public final static String field_posId = "posId";// pos编号
	public final static String field_posType = "posType";// 固定值posType
	public final static String field_empNo = "empNo";// 员工号
	public final static String field_password = "password";
	public final static String field_status = "status";// 状态
	public final static String field_empName = "empName";// 员工名
	public final static String field_deptNo = "deptNo";// 站点号
	public final static String field_deptName = "deptName";// 站点名称
	public final static String field_shiftDate = "shiftDate";// 班次日期
	public final static String field_shiftNo = "shiftNo";// 班次号

	//
	public final static String field_payType = "payType";// 支付类型
	public final static String field_QRcode = "QRcode";// 二维码
	public final static String field_carNo = "carNo";// 车牌号
	public final static String field_oilName = "oilName";// 车牌号
	public final static String field_gunNo = "gunNo";// 枪号
	// public final static String field_empNo = "empNo";
	public final static String field_cardType = "cardType";// 卡片类型
	public final static String field_asn = "asn";// 卡号
	public final static String field_cardBalance = "cardBalance";// 卡余额
	public final static String field_cardCarNo = "cardCarNo";// 卡上车牌号
	public final static String field_carFlag = "carFlag";// 二维码识别标志
	public final static String field_authType = "authType";// 加油方式
	public final static String field_authType_quota_money = "1";// 加油定额
	public final static String field_authType_quota_quantify = "2";// 加油定量
	public final static String field_authType_quota_full = "3";// 加满
	public final static String field_amount = "amount";// 加油金额
	public final static String field_count = "count";// 加油次数
	public final static String field_volume = "volume";// 加油量
	//
	public final static String field_recCount = "recCount";// 记录数量
	public final static String field_record = "record";// 记录数量
	public final static String field_tradeTime = "tradeTime";// 交易时间
	// record”:[
	// {“id”:流水号,
	// “price”:价格,
	// “volume”:加油量,
	// “amount”:金额,
	// “datetime”:”加油时间”,
	// “pumpCount”:泵码,
	// },
	// ...
	// ]}
	public final static String field_id = "id";// 流水号
	public final static String field_price = "price";// 价格
	public final static String field_datetime = "datetime";// 加油时间
	public final static String field_pumpCount = "pumpCount";// 泵码

	//
	public final static String field_cardNo = "cardNo";// 积分卡卡号
	public final static String field_point = "point";// 原始积分
	public final static String field_preferAmount = "preferAmount";// 优惠金额
	public final static String field_payAmount = "payAmount";// 支付金额

	// 状态
	public final static String field_state_upload_success = "finished";// 登录成功
	public final static String field_state_upload_failure = "unfinish";// 登录成功

	// 错误报告
	public final static String send_from = "speedatalog@sina.com";
	public final static String send_from_psw = "Geofancismart";
	public final static String send_to = "bairu.xu@speedatagroup.com";

	// YYYY-MM-DD HH:MM:SS
	//
	// ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE))
	// .getDeviceId();

}
