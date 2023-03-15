package cn.rmy.common.redisUtils.concasts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 项目通用常量类.
 * 项目名称: 咪咕营销
 * 包: com.migu.redstone.microservice.common.constants
 * 类名称: CommonConst.java
 * 类描述: 本类是一个通用常量类，用于存放通用的常量
 * 创建人: liaolei@asiainfo.com
 * 创建时间: 2017/7/26 19:19
 */
public class CommonConst {

    /**
     * 数据状态常量类.
     * 项目名称: 咪咕营销
     * 包: com.migu.redstone.microservice.common.constants
     * 类名称: CommonConst.java
     * 类描述: 本类是一个标识数据正常与删除状态的类
     * 创建人: liaolei@asiainfo.com
     * 创建时间: 2017/7/26 19:20
     */
    public static class DataStates {
        /**
         * 正常状态数据.
         */
        public static final int NORMAL_STATE = 1;

        /**
         * 删除状态数据.
         */
        public static final int DELETE_STATE = 0;
    }

    /**
     * 定义展示的日期格式.
     */
    public static class DATEFORMAT {
        /**
         * DATETIME_FORMAT.
         */
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * DAY_FORMAT.
         */
        public static final String DAY_FORMAT = "yyyy-MM-dd";

        /**
         * yyyyMMdd
         */
        public static final String YYYYMMDD = "yyyyMMdd";
        /**
         * YYYY_MM
         */
        public static final String YYYY_MM = "yyyy-MM-";

        /**
         * DATE_FORMAT.
         */
        public static final String DATE_FORMAT = "yyyyMMdd";

        /**
         * MINUTE_FORMAT.
         */
        public static final String MINUTE_FORMAT = "yyMMddHHmm";
        
        /**
         * MINUTE_FORMAT.
         */
        public static final String YEAR_HOUR_MINUTE_FORMAT = "yyyyMMddHHmm";

        /**
         * MONTH_FORMAT.
         */
        public static final String MONTH_FORMAT = "yyMM";

        /**
         * TIMESTAMP_FORMAT. yyyyMMddHHmmss
         */
        public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";

        /**
         * TZ_FORMAT
         */
        public static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        /**
         * MON_FORMAT.
         */
        public static final String MON_FORMAT = "yyyyMM";
        /**
         * YEAR_FORMAT.
         */
        public static final String YEAR_FORMAT = "yyyy";

        /**
         * HHMM_FORMAT.
         */
        public static final String HHMM_FORMAT = "HH:mm";

        /**
         * HHMMSS_FORMAT
         */
        public static final String HHMMSS_FORMAT = "HH:mm:ss";

        /**
         * TZ_LAST
         */
        public static final String TZ_LAST = "Z";
    }

    /**
     * 是否的常量定义.
     */
    public static class YesNo {
        /**
         * YES.
         */
        public static final String YES = "Y";

        /**
         * NO.
         */
        public static final String NO = "N";
    }

    /**
     * @author Administrator
     *
     */
    public static class ANDSTATE {
        /**
         * METHOD_NAME.
         */
        public static final String METHOD_NAME = "andStateEqualTo";

        /**
         * METHOD_VAL.
         */
        public static final String METHOD_VAL = "1";
    }

    /**
     *
    * 缓存的key值.
    * 项目名称:  redstone-common
    * 包:      com.migu.redstone.common.constants
    * 类名称:   CACHE_KEY
    * 类描述:   类功能详细描述
    * 创建人:   yi
    * 创建时间:  2017 下午3:02:54
     */
    public static class CacheKey {
        /**
         * 奖品库存余量信息
         */
        public static final String PRIZESPEC = "PRIZESPEC";

		/**
		 * 奖品库存余量信息
		 */
		public static final String PRIZELEFT_CNT = "PRIZELEFT_CNT";
		
		/**
         * 用户日兑换量
         */
        public static final String U_DAYLIMIT = "U_DAYLIMIT_";

        /**
         * 用户周兑换量
         */
        public static final String U_WEEKLIMIT = "U_WEEKLIMIT_";

        /**
         * 用户月兑换量
         */
        public static final String U_MONTHLIMIT = "U_MONTHLIMIT_";

        /**
         * 用户活动期间兑换量
         */
        public static final String U_TOTALLIMIT = "U_TOTALLIMIT_";
		
    }

    /**
     *
     * 表名.
     * 项目名称:  redstone-common
     * 包:      com.migu.redstone.common.constants
     * 类名称:   CACHE_KEY
     * 类描述:   类功能详细描述
     * 创建人:   yi
     * 创建时间:  2017 下午3:02:54
     */
    public static class Tables {
        /**
         * 用户行为表
         */
        public static final String MKT_USER_BEHAVIOR = "mg_mkt_user_behavior";
        /**
         * 用户奖品表
         */
        public static final String MKT_USER_PRIZE_REC = "mg_mkt_user_prize_rec";

        /**
         * 能量积分明细表
         */
        public static final String MKT_SCORE_DETAIL= "mg_mkt_score_detail";

    }

    /**
     * 排序方式.
     */
    public static class OrderWay {
        /**
         * ASC.
         */
        public static final String ASC = "ASC";

        /**
         * DESC.
         */
        public static final String DESC = "DESC";

        /**
         * SPACE.
         */
        public static final String SPACE = " ";
    }

    /**
     * 要排序字段名称，mapper自动生成的查询方法使用.
     */
    public static class FieldName {

        /**
         * SORT.
         */
        public static final String SORT = "sort";

        /**
         * OrderByColumn
         */
        public static final String ORDER_BY_COLUMN = "gmt_modified";
    }

    /**
     * 要排序字段名称，mapper自动生成的查询方法使用.
     */
    public static class OFFLINE {

        /**
         * NOWIN.
         */
        public static final String NOWIN = "资源余量不足";

        /**
         * ERROR.
         */
        public static final String ERROR = "文件下载失败";


        /**
         * ZERO.
         */
        public static final String ZERO = "0";

        /**
         * user.dir.
         */
        public static final String PATH = "user.dir";

        /**
         * .
         */
        public static final String CUT = "/";

        /**
         * S.
         */
        public static final String SUCCESS = "S";
        /**
         * F.
         */
        public static final String FAIL = "F";
        /**
         * failCnt.
         */
        public static final String FAILCNT = "failCnt";
        /**
         * dismatchNum.
         */
        public static final String DISMATCHNUM = "dismatchNum";
        /**
         * allNum.
         */
        public static final String ALLNUM = "allNum";
        /**
         * resIdNull.
         */
        public static final String RESIDNULL = "resIdNull";
        /**
         * resIdNotNull.
         */
        public static final String RESIDNOTNULL = "resIdNotNull";

		public static final String WRONGRESSPECCODE ="奖品编码错误";
    }

    /**
     *
     * 验证response返回结果是否为success.
     */
    public static class CheckResponse {
        /**
         * GET_RESULT.
         */
        public static final String GET_RESULT = "getResult";
    }

    /**
     * 类作用: ftp配置.
     * 项目名称: redstone-common
     * 包: com.migu.redstone.common.constants
     * 类名称: CommonConst.java
     * 类描述: ftp配置
     * 创建人: jianghao
     * 创建时间: 2017/9/16 16:40
     */
    public static class FtpConfig {
        /**
         * ALLOWED_FILE_TYPE.
         */
        public static final String ALLOWED_FILE_TYPE = "allowedFileType";
        /**
         * FTP_PATH_CODE.
         */
        public static final String FTP_PATH_CODE = "ftpPathCode";
        /**
         * COMPANY_ID.
         */
        public static final String COMPANY_ID = "companyId";
        /**
         * 奖品发放ftpPathCode.
         */
        public static final String CAMPAIGN_PRIZE_AWARD = "CAMPAIGN_PRIZE_AWARD";

        /**
         * 会员奖品话单ftpPathCode.
         */
        public static final String MEMBER_DATA = "MEMBER_DATA";

        /**
         * BONUS_DATA.
         */
        public static final String BONUS_DATA = "BONUS_DATA";
        public static final String BONUS_RED_PACKET = "red_packet";
    }

    /**
     *
     * bool值.
     * 项目名称: redstone-common
     * 包: com.migu.redstone.common.constants
     * 类名称: NumberBool
     * 类描述: 类功能详细描述
     * 创建人: yi
     * 创建时间: 2017 上午9:51:35
     */
    public static class NumberBool {
        /**
         * TRUE_NUMBER.
         */
        public static final String TRUE_NUMBER = "1";

        /**
         * FALSE_NUMBER.
         */
        public static final String FALSE_NUMBER = "0";
        
        public static final boolean TEUE_CONST = true;
        
        public static final boolean FALSE_CONST = false;
    }

    /**
     * 工作台活动状态分组.
     */
    /**
     * 待审核的活动状态.
     */
    public static final List<String> AUDIT;
    static {
        List<String> tempList = new ArrayList<String>();
        tempList.add("2");
        tempList.add("8");
        tempList.add("10");
        tempList.add("11");
        tempList.add("12");
        AUDIT = Collections.unmodifiableList(tempList);
    }

    /**
     * 待发布的活动状态.
     */
    public static final List<String> RELEASED;
    static {
        List<String> tempList = new ArrayList<String>();
        tempList.add("3");
        tempList.add("9");
        RELEASED = Collections.unmodifiableList(tempList);
    }

    /**
     * 待维护的活动状态.
     */
    public static final List<String> MAINTAIN;
    static {
        List<String> tempList = new ArrayList<String>();
        tempList.add("1");
        tempList.add("4");
        tempList.add("5");
        tempList.add("6");
        MAINTAIN = Collections.unmodifiableList(tempList);
    }

    /**
     * 线上的活动状态.
     */
    public static final List<String> ACTIVITIES;
    static {
        List<String> tempList = new ArrayList<String>();
        tempList.add("5");
        tempList.add("8");
        tempList.add("9");
        tempList.add("11");
        ACTIVITIES = Collections.unmodifiableList(tempList);
    }

    /**
     *
    * 一句话功能描述.
    * 项目名称:  redstone-common
    * 包:      com.migu.redstone.common.constants
    * 类名称:   RegExp
    * 类描述:   类功能详细描述
    * 创建人:   yi
    * 创建时间:  2017 下午4:08:56
     */
    public static class RegExp {
        /**
         * 手机号码格式正则表达式.
         */
        public static final String MOBILENO_REG =
                "^[1][3,4,5,7,8][0-9]{9}$";

        /**
         * ,分隔符.
         */
        public static final String SPLIT_BY_COMMA = ",";
        /**
         * ;分隔符.
         */
        public static final String SPLIT_BY_SEMICOLON=";";

        /**
         * .分隔符.
         */
        public static final String SPLIT_BY_DOT = ".";
        
        /**
         * .分隔符.
         */
        public static final String BLANK = "";
        /**
         * _分隔符.
         */
        public static final String UNDERLINE = "_";
        /**
         * |.
         */
        public static final String VERTICAL_LINE = "|";
        
        /**
         * \\.
         */
        public static final String DOT = "\\.";
        /**
         * %
         */
        public static final String PER = "%";
        /**
         * -
         */
        public static final String LINEAE = "-";
        
        /**
         * EQUAL_LINE
         */
        public static final String EQUAL_LINE = "=";

        /**
         * VERTICAL_LINE_REGEXP.
         */
        public static final String VERTICAL_LINE_REGEXP = "\\|";

        /**
         * NEWLINE_RETURN_REGEXP.
         */
        public static final String NEWLINE_RETURN_REGEXP = "[\\n\\r]";

        /**
         * SLASH_LINE.
         */
        public static final String SLASH_LINE = "/";

        /**
         * Left parenthesis
         */
        public static final String LEFT_PARENTHESIS = "{";
        
        /**
         * right parenthesis.
         */
        public static final String RIGHT_PARENTHESIS = "}";
        
        /**
         * \r\n 回车换行
         */
        public static final String RETURN_TO_LINE = "\r\n";
        
        /**
         * \t
         */
        public static final String TAB = "\t";
		
        /**
         * \
         */
        public static final String TRANSFERRED = "\\";
    }

    /**
    *〈静态数据code_type值〉
    *〈数据字典中静态数据code_type值〉
    */
    public static class StaticCodeType{
        /**
         * 产品线.
         */
        public static final String PRODUCT_LINE = "PRODUCT_LINE";
        /**
         * 门户类型.
         */
        public static final String PORTAL_TYPE = "PORTAL_TYPE";
        /**
         * 平台标识.
         */
        public static final String PLATFORM = "PLATFORM";
        /**
         * 厂商标识.
         */
        public static final String COMPANY_ID = "COMPANY_ID";
        
        /**
         * PRODUCT_ID.
         */
        public static final String PRODUCT_ID = "PRODUCT_ID";
        /**
         * 子公司编码.
         */
        public static final String MIGU_COMPANY_ID = "MIGU_COMPANY_ID";
        /**
         * 产品线与子公司编码映射关系.
         */
        public static final String PRODUCT_LINE_COMPANY_MAPPING = "PRODUCT_LINE_COMPANY_MAPPING";
        /**
         * PRODUCT_LINE_PORTAL_MAPPING
         */
        public static final String PRODUCT_LINE_PORTAL_MAPPING = "PRODUCT_LINE_PORTAL_MAPPING";

        public static final String SEPARATOR = "_";
        /**
         * 用户组标识.
         */
        public static final String USER_GROUP_ID = "USER_GROUP_ID";
		
        /**
         * 默认厂商编码.
         */
        public static final String COMPANY_ID_15 = "15";
        /**
         * 省份编码.
         */
        public static final String PROVINCE_CODE = "PROVINCE_CODE";
        
        /**
         * 全国编码，静态数据code_type为PROVINCE_CODE
         */
        public static final String COUNTRY_CODE = "000";
        
        /**
         * 手机号省份映射.
         */
        public static final String MOBILE_PROVINCE_MAPPING = "MOBILE_PROVINCE_MAPPING";
        /**
         * 开关，codeValue为0或没值是关，其他值都是开
         */
        public static final String SWITCHW = "SWITCH";
        /**
         * codeName为VERSION_930.
         */
        public static final String VERSION_930 = "VERSION_930";
        /**
         * codeName为SYSN_YJ_FLAG.codeValue为0或没值是关，其他值都是开
         */
        public static final String SYSN_YJ_FLAG = "SYSN_YJ_FLAG";
        /**
         * USER_ACCOUNT_TYPE.
         */
        public static final String USER_ACCOUNT_TYPE = "USER_ACCOUNT_TYPE";

        /**
         * 概要日志操作类型.
         */
        public static final String MG_SUMMARY_LOG_OPER_TYPE = "MG_SUMMARY_LOG_OPER_TYPE";

        /**
         * 详细日志操作类型.
         */
        public static final String MG_DETAIL_LOG_OPER_TYPE = "MG_DETAIL_LOG_OPER_TYPE";

        /**
         * RES_TYPE.
         */
        public static final String RES_TYPE = "RES_TYPE";

        /**
         * RES_SUB_TYPE.
         */
        public static final String RES_SUB_TYPE = "RES_SUB_TYPE";

        /**
         * MSISDN_SECTION.
         */
        public static final String MSISDN_SECTION = "MSISDN_SECTION";

        /**
         * REGEX_MOBILE.
         */
        public static final String REGEX_MOBILE = "REGEX_MOBILE";

        /**
         * 用户类型.
         */
        public static final String USER_TYPE = "USER_TYPE";

        /**
         * LIMIT.
         */
        public static final String LIMIT_NUM = "LIMIT_NUM";

        /**
         * 每天赠送抽奖机会接口赠送上限LNT.
         */
        public static final String LNT = "LNT";

        /**
         * NEED_SPLIT_RES_TYPE_CODE_TYPE.
         */
        public static final String NEED_SPLIT_RES_TYPE_CODE_TYPE = "NEED_SPLIT_RES_TYPE";

        /**
         * NEED_SPLIT_RES_TYPE_CODE_NAME.
         */
        public static final String NEED_SPLIT_RES_TYPE_CODE_NAME = "NEED_SPLIT_RES_TYPE";
        
        public static final String  OFFLINE_CAMPAIGN_QUERYUSER="OFFLINE_CAMPAIGN_QUERYUSER";

        /**
         * SWITCH_CONSTRACT.
         */
        public static final String SWITCH_CONSTRACT = "CONSTRACT";

        /**
         * SUPPLIER_ID.
         */
        public static final String SUPPLIER_ID = "SUPPLIER_ID";

        /**
         * SPU_ID.
         */
        public static final String SPU_ID = "SPU_ID";

        /**
         * MD5KEY.
         */
        public static final String MD5KEY = "MD5KEY";

        /**
         * RES_TYPE_PLATFORM_MAPPING.
         */
        public static final String RES_TYPE_PLATFORM_MAPPING = "RES_TYPE_PLATFORM_MAPPING";

        /**
         * USER_CENTER.
         */
        public static final String USER_CENTER = "USER_CENTER";
        /**
         * USER_PRODUCTCODE_MAPPING.
         */
        public static final String USER_PRODUCTCODE_MAPPING = "USER_PRODUCTCODE_MAPPING";

        /**
         * MEMBER_PRODUCT_CODE_MAPPING.
         */
        public static final String MEMBER_PRODUCT_CODE_MAPPING = "MEMBER_PRODUCT_CODE_MAPPING";

        /**
         * MEMBER_UNIT_CFG.
         */
        public static final String MEMBER_UNIT_CFG = "MEMBER_UNIT_CFG";

        /**
         * 初级会员codeName.
         */
        public static final String JUNIOR_MEMBER = "JUNIOR_MEMBER";

        /**
         * 特级会员codeName.
         */
        public static final String SPECIAL_MEMBER = "SPECIAL_MEMBER";

        /**
         * 超级会员codeName.
         */
        public static final String SUPER_MEMBER = "SUPER_MEMBER";

        /**
         * 体验会员codeName.
         */
        public static final String MEMBER_TRIAL = "MEMBER_TRIAL";

        /**
         * RES_MGR_PLATFOM.
         */
        public static final String RES_MGR_PLATFOM = "RES_MGR_PLATFOM";

        /**
         * 活动类型.
         */
        public static final String CAMPAIGN_TYPE = "CAMPAIGN_TYPE";

        /**
         * 关联对象类型.
         */
        public static final String REL_OBJ_TYPE = "REL_OBJ_TYPE";

        /**
         * 领取状态.
         */
        public static final String RESULT_REC_STATUS = "RESULT_REC_STATUS";

        /**
         * 下发结果.
         */
        public static final String DRAW_RESULT = "DRAW_RESULT";

        /**
         * STATUS_RESULT.
         */
        public static final String STATUS_RESULT = "STATUS_RESULT";

        /**
         * 是否需要稽核.
         */
        public static final String IS_NEED_CHECK = "IS_NEED_CHECK";
        public static final String FLOWPRESENT_TO_HIS_TIMERANGE = "FLOWPRESENT_TO_HIS_TIMERANGE";

        /**
         * MEMBER_DATA_GROUP_ID.
         */
        public static final String MEMBER_DATA_GROUP_ID = "MEMBER_DATA_GROUP_ID";

        /**
         * 流量/咪咕币的bizType静态配置的code_type
         */
        public static final String YJ_RES_RES_TYPE_BUSITYPE_MAPPING = "YJ_RES_RES_TYPE_BUSITYPE_MAPPING";

        /**
         * 校园版会员参与活动后手低登陆code_type
         */
        public static final String CAMPAIGN_ID_FOR_SCHOOL_VIP = "CAMPAIGN_ID_FOR_SCHOOL_VIP";
        
        /**
         * PRODUCTLINE_PRODUCTCODE_MAPPING
         */
        public static final String PRODUCTLINE_PRODUCTCODE_MAPPING = "PRODUCTLINE_PRODUCTCODE_MAPPING";
        
        /**
         * PORTALTYPE_PRODUCTCODE_MAPPING
         */
        public static final String PORTALTYPE_PRODUCTCODE_MAPPING = "PORTALTYPE_PRODUCTCODE_MAPPING";
        
        /**
         * PLATFORM_PRODUCTCODE_MAPPING
         */
        public static final String PLATFORM_PRODUCTCODE_MAPPING = "PLATFORM_PRODUCTCODE_MAPPING";

        /**
         * SMS_SOURCE_ID_CONFIG.
         */
        public static final String SMS_SOURCE_ID_CONFIG = "SMS_SOURCE_ID_CONFIG";

        /**
         * 无可变尾号时alias静态配置
         */
        //public static final String NO_EXPEND_ALIAS = "1";

        /**
         * 有可变尾号时alias静态配置
         */
        //public static final String EXPEND_ALIAS = "2";

        /**
         * SEND_SMS_MMS_LIMIT_CODE_TYPE。
         */
        public static final String SEND_SMS_MMS_LIMIT_CODE_TYPE = "CAMP_SMS_SEND_LIMIT";

        /**
         * CAMP_SMS_SEND_LIMIT_EXPIRE.
         */
        public static final String CAMP_SMS_SEND_LIMIT_EXPIRE = "CAMP_SMS_SEND_LIMIT_EXPIRE";
        /**
         * SEND_SMS_MMS_DAY_UPLIMIT.
         */
        public static final String SEND_SMS_MMS_DAY_UPLIMIT = "DAY_UPLIMIT";

        /**
         * SEND_SMS_MMS_DAY_UPLIMIT_EXPIRE_PREFIX.
         */
        public static final String SEND_SMS_MMS_DAY_UPLIMIT_EXPIRE_PREFIX = "DAY_UPLIMIT_EXPIRE_PREFIX";

        /**
         * SEND_SMS_MMS_MONTH_UPLIMIT.
         */
        public static final String SEND_SMS_MMS_MONTH_UPLIMIT = "MONTH_UP_LIMIT";

        /**
         * SEND_SMS_MMS_MONTH_UPLIMIT_EXPIRE_PREFIX.
         */
        public static final String SEND_SMS_MMS_MONTH_UPLIMIT_EXPIRE_PREFIX = "MONTH_UP_LIMIT_EXPIRE_PREFIX";

        /**
         * PLAY_EVENT_TASK.
         */
        public static final String PLAY_EVENT_TASK = "PLAY_EVENT_TASK";

        /**
         * PLAY_EVENT_INTERVAL.
         */
        public static final String PLAY_EVENT_INTERVAL = "PLAY_EVENT_INTERVAL";
        /**
         * BONUS_END_TIME_CODE_TYPE.
         */
        public static final String BONUS_END_TIME_CODE_TYPE = "BONUS_END_TIME_CODE_TYPE";
        /**
         * BONUS_END_TIME_CODE_NAME.
         */
        public static final String BONUS_END_TIME_CODE_NAME = "BONUS_END_TIME_CODE_NAME";
        
        /**
         * PINKSTONE_SERVER
         */
        public static final String RAINBOW_SERVER = "RAINBOW_SERVER";
        
        /**
         * PINKSTONE_SERVER_USER_RECHARGE.
         */
        public static final String PINKSTONE_SERVER_USER_RECHARGE = "USER_RECHARGE";
        public static final String BONUS_CODE_TYPE = "BONUS_CODE_TYPE";
        public static final String DAY_INTERVAL = "DAY_INTERVAL";
        public static final String BONUS_COMPANY_ID = "13";
    }


    /**
     *
    * 一句话功能描述
    * 项目名称:  redstone-common
    * 包:      com.migu.redstone.common.constants
    * 类名称:   KafkaTopic
    * 类描述:   类功能详细描述
    * 创建人:   yi
    * 创建时间:  2017 下午3:03:28
     */
    public static class KafkaTopic{
        //营销合约类产品同步
        public static final String CAMPAIGN_SYN_PRODUCT = "CAMPAIGN-SYN-PRODUCT";

        //新的产品同步topic
        public static final String PRODUCTSTRATEGY = "productStrategy";

        //营销事件接收
        public static final String CAMPAIGN_EVENT = "CAMPAIGN-EVENT";

        //营销向前台APP的PUSH消息
        public static final String CAMPAIGN_APP_PUSH = "CAMPAIGN-APP-PUSH";

        //动漫平台增量发送活动结果数据同步到营销中心
        public static final String CAMPAIGN_RESULT_INCREMENT = "CAMPAIGN-RESULT-INCREMENT";

        //代码异常发送的消息主题
        public static final String CAMPAIGN_MONITOR = "CAMPAIGN-MONITOR";

        //活动执行结果处理消息主题
        public static final String CAMPAIGN_EXECUTE_HANDLE = "CAMPAIGN_EXECUTE_HANDLE";

        //营销登陆
        public static final String CAMPAIGN_LOGIN_EVENT_REORDER = "CAMPAIGN-LOGIN-EVENT-REORDER";

        //营销订购产品
        public static final String CAMPAIGN_ORDER_EVENT_REORDER = "CAMPAIGN-ORDER-EVENT-REORDER";

        //点赞
        public static final String CAMPAIGN_UPVOTE_EVENT_REORDER = "CAMPAIGN-UPVOTE-EVENT-REORDER";

        //点播
        public static final String CAMPAIGN_VOD_EVENT_REORDER = "CAMPAIGN-VOD-EVENT-REORDER";

        //内部点播
        public static final String CAMPAIGN_PLAY_EVENT_REORDER = "CAMPAIGN-PLAY-EVENT-REORDER";

        //活动执行
        public static final String CAMPAIGN_TO_EXCUTE_EVENT = "CAMPAIGN-TO-EXCUTE-EVENT";

        //外部系统发送的登陆事件
        public static final String CAMPAIGN_LOGIN_EVENT = "CAMPAIGN-LOGIN-EVENT";

        //外部系统发送的创建订单事件
        public static final String CAMPAIGN_ORDER_EVENT = "CAMPAIGN-ORDER-EVENT";

        //外部系统发送的点赞事件
        public static final String CAMPAIGN_UPVOTE_EVENT = "CAMPAIGN-UPVOTE-EVENT";

        //外部系统发送的点播事件
        public static final String CAMPAIGN_VOD_EVENT = "CAMPAIGN-VOD-EVENT";

        //外部系统点播消息
        public static final String CAMPAIGN_PLAY_EVENT = "CAMPAIGN-PLAY-EVENT";

        //活动执行分区数量
        public static final Integer PARTITION_EXCUTE_NUM = 3;

        //登陆分区数
        public static final Integer PARTITION_LOGIN_REORDER_NUM = 8;

        //订单
        public static final Integer PARTITION_ORDER_REORDER_NUM = 3;

        //点赞
        public static final Integer PARTITION_UPVOTE_REORDER_NUM = 5;

        //点播
        public static final Integer PARTITION_VOD_REORDER_NUM = 5;

        /**
         * 扩展同步topic
         */
        public static final String B1_SYSN_MESSAGE_TOPIC = "B1-SYSN-MESSAGE";

        /**
         * 通用topic.
         */
        public static final String CAMPAIGN_COMMON_TOPIC = "CAMPAIGN-COMMON-TOPIC";

        /**
         * 营销能力补充topic.
         */
        public static final String CAMPAIGN_ABILITY_SUPPLEMENT_TOPIC = "campaignAbilitySupplement";
    }

    /**
    * 类作用:    类作用
    * 项目名称:   campaign-common
    * 包:       com.migu.redstone.common.constants
    * 类名称:    KafkaCommonMesageConst
    * 类描述:    kafka通用message
    * 创建人:    jianghao
    * 创建时间:   2018年9月6日 下午7:12:43
    */
    public static class KafkaCommonMesageConst {
        /**
         * 实现类spring bean注解名.
         */
        public static final String PROCESS_CLASS_NAME = "processClassName";

        /**
         * 具体实现类的参数.
         */
        public static final String KAFKA_DATA = "data";
        public static final String OPER_TYPE = "operType";
        public static final String OPER_TYPE_ONE = "1";
        public static final String OPER_TYPE_TWO = "2";

    }

    /**
     * 
    * 业务缓存的key值
    * 项目名称:  redstone-common
    * 包:      com.migu.redstone.common.constants
    * 类名称:   BusinessCacheKeyPrefix
    * 类描述:   类功能详细描述
    * 创建人:   wangqian
    * 创建时间:  2017 下午9:58:56
     */
    public static class BusinessCacheKeyPrefix{
    	public static final String USER_PRIZE_DAYUPLIMIT = "USER_PRIZE_DAYUPLIMIT";

    	public static final String USER_PRIZE_WEEKUPLIMIT = "USER_PRIZE_WEEKUPLIMIT";

    	public static final String USER_PRIZE_MONTHUPLIMIT = "USER_PRIZE_MONTHUPLIMIT";


    	public static final String PRIZE_DAYUPLIMIT = "PRIZE_DAYUPLIMIT";

    	public static final String PRIZE_WEEKUPLIMIT = "PRIZE_WEEKUPLIMIT";

    	public static final String PRIZE_MONTHUPLIMIT = "PRIZE_MONTHUPLIMIT";

    }

    //奖品每次减去的数量：1
    public static final int LOTTY_REDUCE_NUM = 1;

    //一个月时间的秒数
    //public static final int MOTHLY_TIME_SECOND = 60*60*24*30;

    //一天的毫秒数
    public static final long DAY_MILLI_SECOND = 1000*60*60*24;

    //一分钟的毫秒数
    public static final long MINUTE_MILLI_SECOND = 1000*60;
    
    /**
     *  计量单位-万
     */
    public static final Long MYRIAD = 10000L;
    
    /**
     * 奖品有价值类型 
     */
	public static final String LOTTERY_HAVE_VALUED_TYPE = "Y";
	
	/**
     * 奖品无价值类型 
     */
	public static final String LOTTERY_NO_VALUED_TYPE = "N";

    /**
     * USERID_PRE.
     */
    public static final String USERID_PRE = "U";

    /**
     * 字符编码utf8.
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * LONG_THOUSAND.
     */
    public static final long LONG_THOUSAND = 1000L;

    /**
     * HOUR_SECOND.
     */
    public static final long HOUR_SECOND = 3600L;

    /**
     * MONTH_SECOND.
     */
    public static final long MONTH_SECOND = 2592000L;

    /**
     * DEFAULT_AMT_PER_TIME.
     */
    public static final long DEFAULT_AMT_PER_TIME = 1L;

    public static class NumberConst {
        public static final Long ZERO_LONG = 0L;
        
        public static final Long ONE_LONG = 1L;
        
        public static final int ZERO_INT = 0;
        
        public static final int ONE_INT = 1;
    }

    /**
     * 默认分页页码
     */
    public static Integer DEFAULT_PAGE_NUM = 1;
    /**
     * 默认分页每页数据量
     */
    public static Integer DEFAULT_PAGE_SIZE = 10;
}
