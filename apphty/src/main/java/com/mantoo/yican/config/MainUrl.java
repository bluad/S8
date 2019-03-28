package com.mantoo.yican.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/12.
 */

public class MainUrl {

    // 获得地址
    public static final String ADDRESS = "http://yican2017.mantoo.com.cn:8081/YiChanPlatform.asmx/GetAddress";

    public static final String BASEURL = "";

    // 1.登录
    public static final String LOGIN = "/Login";
    // 2.修改密码
    public static final String MODIFYACCOUNTINFORMATION = "/ModifyAccountInformation";
    // 3.查询收件列表
    public static final String ADDRESSEE = "/queryRecieve";
    // 4.获取派单列表
    public static final String PIELIST = "/PieList";
    // 5.任务单详情列表
    public static final String TASKLIST = "/TaskList";
    // 6.派单接单
    public static final String DISPATCHORDER = "/DispatchOrder";
    // 7.派单拒接
    public static final String SENDASINGLEREJECT = "/SendASingleReject";
    // 10.运单详情
    public static final String WAYBILLDETAILS = "/WaybillDetails";
    // 11.任务单发车
    public static final String TASKSHEETDEPARTURE = "/TaskSheetDeparture";
    // 12.图片轮播
    public static final String getPictures = "/getPictures";
    // 14获取快递信息
    public static final String GETMSG = "/gainExpressDetail";
    // 15、已完成列表
    public static final String COMPLETElIST = "/CompletedList";
    // 16、异常运单列表
    public static final String LISTOFABNORMALWAYBILL = "/ListOfAbnormalWaybill";
    // 17.异常运单查看运单详情
    public static final String EXCEPTIONWAYBILLDATEAILS = "/ExceptionWaybillDetails";
    // 18.提报异常
    public static final String ABNORMALREPOTING = "/AbnormalReporting";
    // 19.未结算任务单列表
    public static final String LISTOFOUTSTANDINGTASKLISTS = "/ListOfOutStandingTaskLists";
    // 20.获取财务信息
    public static final String OBTAINFINANCIALINFOMATION = "/ObtainFinancialInformation";
    // 21.获取统计信息
    public static final String GETSTATISTICS = "/GetStatistics";
    // 22.物流跟踪
    public static final String LOGISTICSTRACKIING = "/LogisticsTracking";
    // 23.个人中心
    public static final String toPersonalCenter = "/PersonalCenter";
    // 24.建议反馈
    public static final String toSuggestedFeedback = "/SuggestedFeedback";
    // 25.密码修改
    public static final String MODIFYPASSWORD = "/ModifyPassword";
    // 26.退出登录
    public static final String LOGOUT = "/LogOut";
    // 26.修改上班状态
    public static final String MODIFYWORKSTATUS = "/ModifyWorkStatus";
    // 27、签收
    public static final String UPDATESIGN = "/updateSign";
    // 28、图片上传
    public static final String UPLOADPICTURES = "/UploadPictures";
    // 29、异常信息
    public static final String EXCEPTIONWAYBILLDETAILS = "/ExceptionWaybillDetails";
    // 30、消息列表
    public static final String GETALISTOFMESSAGES = "/GetAListOfMessages";
    // 31、发车任务列表
    public static final String FACHETASKLIST = "/facheTaskList";
    // 32、中转到站签收
    public static final String TASKORDERSIGN = "/TaskOrderSign";
    // 33、上传经纬度
    public static final String UPLOADLBS = "/UploadLatitudeAndLongitude";
    // 33、中英文
    public static final String LANGUAGE = "/setContextLanguage";
    // 34、签名
    public static final String UPDATESIGNPIC = "/updateSignPic";
    // 35、车牌号
    public static final String GETVEHICLENO = "/getVehicleNo";
    // 36、删除
    public static final String DELETEMESSAGES = "/deleteMessages";



    //获取查询待收件等信息
    public static final String getNumbers = "/getNumbers";
    //获取快递二维码与信息
    public static final String getUsersInfo = "/getUsersInfo";

    public static Map<String,String> goodMap = new HashMap<String,String>();

}
