package com.lovejob.model;

/**
 * ClassType://
 * User:wenyunzhao
 * ProjectName:Lovejob_Android
 * Package_Name:com.lovejob_android.modle
 * Created on 2016-10-05 11:46
 */


import java.io.Serializable;
import java.util.List;

/**
 * Created by ice on 16/10/3.
 */

public class ThePerfectGirl implements Serializable {
    @Override
    public String toString() {
        return "ThePerfectGirl{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", success=" + success +
                ", data=" + data.toString() +
                '}';
    }

    /**
     * 错误代码
     */
    private int errcode;

    /**
     * 错误消息
     */
    private String errmsg;

    /**
     * 消息成功标志
     */
    private boolean success;

    /**
     * 数据信息
     */
    private Data data;

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类1*********************************************
    //TODO *********************************************华丽的分割线*********************************************


    public class Data implements Serializable {
        private String workPid;
        private String level;
        private String userId;
        private String userType;
        private String privateKey_SERVICE;
        private WorkInfoDTO workInfoDTO;
        private List<WorkInfoDTO> workInfoDTOs;
        private String uploadToken;
        private double amount;
        private AccountDTO accountDTO;
        private List<AccountDTO> accountDTOs;
        private List<UserInfoDTO> userInfoDTOs;
        private UserInfoDTO userInfoDTO;
        private List<PositionTypeInfoDTO> positionTypeInfoDTOs;

        private List<InformationInfo> informationInfos;
        private InformationInfo informationInfo;
        private String portraitId;

        public String getPortraitId() {
            return portraitId;
        }

        public void setPortraitId(String portraitId) {
            this.portraitId = portraitId;
        }

        public InformationInfo getInformationInfo() {
            return informationInfo;
        }

        public void setInformationInfo(InformationInfo informationInfo) {
            this.informationInfo = informationInfo;
        }

        private ApliPayDTO apliPayDTO;

        private WeChatPayDTO weChatPayDTO;
        private ServerDTO serverDTO;
        private List<ServerDTO> serverDTOList;
        private DynamicDTO dynamicDTO;
        private List<DynamicDTO> dynamicDTOList;
        private List<DynamicDTO> dynamicInfos;
        private ResumeDTO resumeDTO;
        private int points;
        private int count;
        private List<WorkEvaluateView> workEvaluateViews;
        private WorkEvaluateView workEvaluateView;

        private String pid;
        private String publicKey;
        private int tokenCount;
        private String username;
        private String localToken;
        private String realName;
        private String rongCloudToken;
        private List<DynamicCommentDTO> list;
        private DynamicCommentDTO dynamicCommentDTO;
        private UserImpressionInfo userImpressionInfo;
        private List<UserImpressionInfo> userImpressionInfoList;
        private orderDetail orderDetail;
        private List<orderDetail> orderDetails;
        private List<WorkTokenDTO> userTokenList;
        private List<ViewDynamic> viewDynamics;
        private List<DynamicPointPraiseInfo> dynamicGoodPointPraiseInfos;
        private List<workPushDTO> workPushDTO;
        private List<AdviceInfor> adviceInforList;
        private String account;
        private String systemVersionUpdate;
        private boolean boundQQ;
        private boolean boundWeChat;
        private boolean identify;

        private AboutMe aboutusDTO;


        public AboutMe getAboutusDTO() {
            return aboutusDTO;
        }

        public void setAboutusDTO(AboutMe aboutusDTO) {
            this.aboutusDTO = aboutusDTO;
        }

        public boolean isIdentify() {
            return identify;
        }

        public void setIdentify(boolean identify) {
            this.identify = identify;
        }

        public boolean isBoundQQ() {
            return boundQQ;
        }

        public void setBoundQQ(boolean boundQQ) {
            this.boundQQ = boundQQ;
        }

        public boolean isBoundWeChat() {
            return boundWeChat;
        }

        public void setBoundWeChat(boolean boundWeChat) {
            this.boundWeChat = boundWeChat;
        }

        public String getSystemVersionUpdate() {
            return systemVersionUpdate;
        }

        public void setSystemVersionUpdate(String systemVersionUpdate) {
            this.systemVersionUpdate = systemVersionUpdate;
        }


        //        private List<DynamicCommentDetailDTO> replyList;

//        public List<DynamicCommentDetailDTO> getReplyList() {
//            return replyList;
//        }
//
//        public void setReplyList(List<DynamicCommentDetailDTO> replyList) {
//            this.replyList = replyList;
//        }

        @Override
        public String toString() {
            return "Data{" +
                    "workPid='" + workPid + '\'' +
                    ", level='" + level + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userType='" + userType + '\'' +
                    ", privateKey_SERVICE='" + privateKey_SERVICE + '\'' +
                    ", workInfoDTO=" + workInfoDTO +
                    ", workInfoDTOs=" + workInfoDTOs +
                    ", uploadToken='" + uploadToken + '\'' +
                    ", amount=" + amount +
                    ", accountDTO=" + accountDTO +
                    ", accountDTOs=" + accountDTOs +
                    ", userInfoDTOs=" + userInfoDTOs +
                    ", userInfoDTO=" + userInfoDTO +
                    ", positionTypeInfoDTOs=" + positionTypeInfoDTOs +
                    ", informationInfos=" + informationInfos +
                    ", apliPayDTO=" + apliPayDTO +
                    ", weChatPayDTO=" + weChatPayDTO +
                    ", serverDTO=" + serverDTO +
                    ", serverDTOList=" + serverDTOList +
                    ", dynamicDTO=" + dynamicDTO +
                    ", dynamicDTOList=" + dynamicDTOList +
                    ", resumeDTO=" + resumeDTO +
                    ", points=" + points +
                    ", count=" + count +
                    ", pid='" + pid + '\'' +
                    ", publicKey='" + publicKey + '\'' +
                    ", tokenCount=" + tokenCount +
                    ", username='" + username + '\'' +
                    ", localToken='" + localToken + '\'' +
                    ", realName='" + realName + '\'' +
                    ", rongCloudToken='" + rongCloudToken + '\'' +
                    '}';
        }

        public List<AdviceInfor> getAdviceInforList() {
            return adviceInforList;
        }

        public void setAdviceInforList(List<AdviceInfor> adviceInforList) {
            this.adviceInforList = adviceInforList;
        }

        public List<ThePerfectGirl.workPushDTO> getWorkPushDTO() {
            return workPushDTO;
        }

        public void setWorkPushDTO(List<ThePerfectGirl.workPushDTO> workPushDTO) {
            this.workPushDTO = workPushDTO;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public List<DynamicPointPraiseInfo> getDynamicGoodPointPraiseInfos() {
            return dynamicGoodPointPraiseInfos;
        }

        public void setDynamicGoodPointPraiseInfos(List<DynamicPointPraiseInfo> dynamicGoodPointPraiseInfos) {
            this.dynamicGoodPointPraiseInfos = dynamicGoodPointPraiseInfos;
        }

        public List<ViewDynamic> getViewDynamics() {
            return viewDynamics;
        }

        public void setViewDynamics(List<ViewDynamic> viewDynamics) {
            this.viewDynamics = viewDynamics;
        }

        public List<WorkTokenDTO> getUserTokenList() {
            return userTokenList;
        }

        public void setUserTokenList(List<WorkTokenDTO> userTokenList) {
            this.userTokenList = userTokenList;
        }

        public List<DynamicDTO> getDynamicInfos() {
            return dynamicInfos;
        }

        public void setDynamicInfos(List<DynamicDTO> dynamicInfos) {
            this.dynamicInfos = dynamicInfos;
        }

        public ThePerfectGirl.orderDetail getOrderDetail() {
            return orderDetail;
        }

        public void setOrderDetail(ThePerfectGirl.orderDetail orderDetail) {
            this.orderDetail = orderDetail;
        }

        public List<ThePerfectGirl.orderDetail> getOrderDetails() {
            return orderDetails;
        }

        public void setOrderDetails(List<ThePerfectGirl.orderDetail> orderDetails) {
            this.orderDetails = orderDetails;
        }

        public UserImpressionInfo getUserImpressionInfo() {
            return userImpressionInfo;
        }

        public void setUserImpressionInfo(UserImpressionInfo userImpressionInfo) {
            this.userImpressionInfo = userImpressionInfo;
        }

        public List<UserImpressionInfo> getUserImpressionInfoList() {
            return userImpressionInfoList;
        }

        public void setUserImpressionInfoList(List<UserImpressionInfo> userImpressionInfoList) {
            this.userImpressionInfoList = userImpressionInfoList;
        }

        public DynamicCommentDTO getDynamicCommentDTO() {
            return dynamicCommentDTO;
        }

        public void setDynamicCommentDTO(DynamicCommentDTO dynamicCommentDTO) {
            this.dynamicCommentDTO = dynamicCommentDTO;
        }

        public List<DynamicCommentDTO> getList() {
            return list;
        }

        public void setList(List<DynamicCommentDTO> list) {
            this.list = list;
        }

        public List<WorkEvaluateView> getWorkEvaluateViews() {
            return workEvaluateViews;
        }

        public void setWorkEvaluateViews(List<WorkEvaluateView> workEvaluateViews) {
            this.workEvaluateViews = workEvaluateViews;
        }

        public WorkEvaluateView getWorkEvaluateView() {
            return workEvaluateView;
        }

        public void setWorkEvaluateView(WorkEvaluateView workEvaluateView) {
            this.workEvaluateView = workEvaluateView;
        }

        public AccountDTO getAccountDTO() {
            return accountDTO;
        }

        public void setAccountDTO(AccountDTO accountDTO) {
            this.accountDTO = accountDTO;
        }

        public List<AccountDTO> getAccountDTOs() {
            return accountDTOs;
        }

        public void setAccountDTOs(List<AccountDTO> accountDTOs) {
            this.accountDTOs = accountDTOs;
        }

        public int getTokenCount() {
            return tokenCount;
        }

        public void setTokenCount(int tokenCount) {
            this.tokenCount = tokenCount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public ResumeDTO getResumeDTO() {
            return resumeDTO;
        }

        public void setResumeDTO(ResumeDTO resumeDTO) {
            this.resumeDTO = resumeDTO;
        }

        public String getRongCloudToken() {
            return rongCloudToken;
        }

        public void setRongCloudToken(String rongCloudToken) {
            this.rongCloudToken = rongCloudToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getLocalToken() {
            return localToken;
        }

        public void setLocalToken(String localToken) {
            this.localToken = localToken;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }


        public List<DynamicDTO> getDynamicDTOList() {
            return dynamicDTOList;
        }

        public void setDynamicDTOList(List<DynamicDTO> dynamicDTOList) {
            this.dynamicDTOList = dynamicDTOList;
        }

        public List<ServerDTO> getServerDTOList() {
            return serverDTOList;
        }

        public void setServerDTOList(List<ServerDTO> serverDTOList) {
            this.serverDTOList = serverDTOList;
        }

        public DynamicDTO getDynamicDTO() {
            return dynamicDTO;
        }

        public void setDynamicDTO(DynamicDTO dynamicDTO) {
            this.dynamicDTO = dynamicDTO;
        }

        public List<ServerDTO> getServerDTOs() {
            return serverDTOList;
        }

        public void setServerDTOs(List<ServerDTO> serverDTOs) {
            this.serverDTOList = serverDTOs;
        }

        public ServerDTO getServerDTO() {
            return serverDTO;
        }

        public void setServerDTO(ServerDTO serverDTO) {
            this.serverDTO = serverDTO;
        }

        public String getWorkPid() {
            return workPid;
        }

        public void setWorkPid(String workPid) {
            this.workPid = workPid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public ApliPayDTO getApliPayDTO() {
            return apliPayDTO;
        }

        public void setApliPayDTO(ApliPayDTO apliPayDTO) {
            this.apliPayDTO = apliPayDTO;
        }

        public WeChatPayDTO getWeChatPayDTO() {
            return weChatPayDTO;
        }

        public void setWeChatPayDTO(WeChatPayDTO weChatPayDTO) {
            this.weChatPayDTO = weChatPayDTO;
        }

        public String getPrivateKey_SERVICE() {
            return privateKey_SERVICE;
        }

        public void setPrivateKey_SERVICE(String privateKey_SERVICE) {
            this.privateKey_SERVICE = privateKey_SERVICE;
        }

        public List<InformationInfo> getInformationInfos() {
            return informationInfos;
        }

        public void setInformationInfos(List<InformationInfo> informationInfos) {
            this.informationInfos = informationInfos;
        }

        public WorkInfoDTO getWorkInfoDTO() {
            return workInfoDTO;
        }

        public List<PositionTypeInfoDTO> getPositionTypeInfoDTOs() {
            return positionTypeInfoDTOs;
        }

        public void setPositionTypeInfoDTOs(List<PositionTypeInfoDTO> positionTypeInfoDTOs) {
            this.positionTypeInfoDTOs = positionTypeInfoDTOs;
        }

        public List<UserInfoDTO> getUserInfoDTOs() {
            return userInfoDTOs;
        }

        public void setUserInfoDTOs(List<UserInfoDTO> userInfoDTOs) {
            this.userInfoDTOs = userInfoDTOs;
        }

        public UserInfoDTO getUserInfoDTO() {
            return userInfoDTO;
        }

        public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
            this.userInfoDTO = userInfoDTO;
        }

        public void setWorkInfoDTO(WorkInfoDTO workInfoDTO) {
            this.workInfoDTO = workInfoDTO;
        }

        public List<WorkInfoDTO> getWorkInfoDTOs() {
            return workInfoDTOs;
        }

        public void setWorkInfoDTOs(List<WorkInfoDTO> workInfoDTOs) {
            this.workInfoDTOs = workInfoDTOs;
        }

        public String getUploadToken() {
            return uploadToken;
        }

        public void setUploadToken(String uploadToken) {
            this.uploadToken = uploadToken;
        }
    }


    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {

        this.data = data;
    }

    public class AboutMe implements Serializable {


        /**
         * pid : null
         * version : 1.0.1
         * about_us : 【爱上工作】 是一个立体招聘平台，包含招聘求职，实习兼职，个人技能或服务交易，同时，也可以通过分享信息或者推荐好友获得丰厚报酬，解决整个社会的用人需求，帮助个人实现劳动价值，构建全方位立体真实的劳动生态价值体系。||应用特点：|【工作方式任你选】 全职、实习、兼职、自由职业、总能找到适合你的工作；|【实名认证有保障】 所有的用户都已实名认证，放心无忧找工作；|【提问点名不怕坑】 查看职位问答、面试评价、HR风格、办公环境、提前知晓；|【及时沟通不用等】 与HR或与未来上级直接沟通，当场拍板，闪电入职；|【一对一专家指导】 为求职者提供从职业规划、建立编制、面试技巧等专业的指导；|【推荐人才赢奖金】 推荐给企业的人才入职后，最高可获得数千元的推荐奖金！|【转发信息得红包】 随手帮别人转发信息一条，根据相应身份等级可获得现金红包；|【技能服务可出售】 技能也是可以拿来出售滴，不仅帮助了别人，也能交到很多朋友；【成为劳动界的网红】 随时随地的秀出你的工作状态，或许下一个网红就是你哦；|我们还在努力给您带来更棒的用户体验！如果使用过程中有任何问题或是建议，请联系：|官方网站：www.congxinwl.com |官方微博：@爱上工作 |微信订阅号：爱上工作APP |微信服务号：赚点现钱 |QQ群：414083738
         * create_date : 1480230851000
         * update_date : 1480230857000
         * title : 刘超
         * sort : 4
         */

        private Object pid;
        private String version;
        private String about_us;
        private long create_date;
        private long update_date;
        private String title;
        private String sort;

        public Object getPid() {
            return pid;
        }

        public void setPid(Object pid) {
            this.pid = pid;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAbout_us() {
            return about_us;
        }

        public void setAbout_us(String about_us) {
            this.about_us = about_us;
        }

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public long getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(long update_date) {
            this.update_date = update_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类2*********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class WorkInfoDTO implements Serializable {
        @Override
        public String toString() {
            return "WorkInfoDTO{" +
                    "address='" + address + '\'' +
                    ", pid='" + pid + '\'' +
                    ", title='" + title + '\'' +
                    ", salary='" + salary + '\'' +
                    ", payment=" + payment +
                    ", releaseDate=" + releaseDate +
                    ", deadline=" + deadline +
                    ", state=" + state +
                    ", type=" + type +
                    ", positionType=" + positionType +
                    ", sex=" + sex +
                    ", experience=" + experience +
                    ", number=" + number +
                    ", education=" + education +
                    ", age=" + age +
                    ", skill=" + skill +
                    ", content='" + content + '\'' +
                    ", contact='" + contact + '\'' +
                    ", contactPhone='" + contactPhone + '\'' +
                    ", pictrueId='" + pictrueId + '\'' +
                    ", firstRefreshTime=" + firstRefreshTime +
                    ", releasePid='" + releasePid + '\'' +
                    ", releaseInfo=" + releaseInfo +
                    ", employeeInfo=" + employeeInfo +
                    ", applyCount=" + applyCount +
                    ", count=" + count +
                    ", relationState=" + relationState +
                    ", workHours='" + workHours + '\'' +
                    ", showApplyBtn=" + showApplyBtn +
                    ", deadlineDec='" + deadlineDec + '\'' +
                    ", paymentDec='" + paymentDec + '\'' +
                    ", experienceDec='" + experienceDec + '\'' +
                    ", educationDec='" + educationDec + '\'' +
                    ", questionPid='" + questionPid + '\'' +
                    ", observerPid='" + observerPid + '\'' +
                    ", observerName='" + observerName + '\'' +
                    ", observerContent='" + observerContent + '\'' +
                    ", answerPid='" + answerPid + '\'' +
                    ", answerName='" + answerName + '\'' +
                    ", answerContent='" + answerContent + '\'' +
                    ", admittedCount=" + admittedCount +
                    ", confirmedCount=" + confirmedCount +
                    ", evaluateCount=" + evaluateCount +
                    ", applyEvaluateCount=" + applyEvaluateCount +
                    ", signUpCount=" + signUpCount +
                    ", recordedCount=" + recordedCount +
                    ", buyConfirmedCount=" + buyConfirmedCount +
                    ", buyEvaluateCount=" + buyEvaluateCount +
                    ", soldConfirmedCount=" + soldConfirmedCount +
                    ", soldEvaluateCount=" + soldEvaluateCount +
                    ", list=" + list +
                    ", sexDec='" + sexDec + '\'' +
                    '}';
        }

        private String pid;
        private String title;
        private String salary;
        private int payment;
        private long releaseDate;
        private long deadline;
        private int state;
        private int type;
        private int positionType;
        private int sex;
        private int experience;
        private int number;
        private int education;
        private String age;
        private String skill;
        private String address;
        private String content;
        private String contact;
        private String contactPhone;
        private String pictrueId;
        private long firstRefreshTime;

        private String releasePid;
        //发布人信息
        private UserInfoDTO releaseInfo;
        //申请人员信息
        private List<UserInfoDTO> employeeInfo;
        private int applyCount;
        private int count;
        private int relationState;
        private String workHours;
        private int showApplyBtn;

        private String deadlineDec;

        private String paymentDec;

        private String experienceDec;

        private String educationDec;

        private String questionPid;

        private String observerPid;

        private String observerName;

        private String observerContent;

        private String answerPid;

        private String answerName;

        private String answerContent;

        //待录取数
        private int admittedCount = 0;
        //待确认数
        private int confirmedCount = 0;
        //发布待评价数
        private int evaluateCount = 0;
        //申请待评价数
        private int applyEvaluateCount = 0;
        //已报名数
        private int signUpCount = 0;
        //已录用数
        private int recordedCount = 0;
        //我购买的服务待确认数
        private int buyConfirmedCount = 0;
        //我购买的服务待评价数
        private int buyEvaluateCount = 0;
        //我卖出的服务待交易数
        private int soldConfirmedCount = 0;
        //我卖出的服务待评价数
        private int soldEvaluateCount = 0;

        List<WorkTokenDTO> list;
        //江湖令
        private List<Integer> riveToken;
        //悬赏令
        private List<WorkTokenDTO> rewardToken;
        private boolean phone;

        public boolean isPhone() {
            return phone;
        }

        public void setPhone(boolean phone) {
            this.phone = phone;
        }

        public List<WorkTokenDTO> getRewardToken() {
            return rewardToken;
        }

        public void setRewardToken(List<WorkTokenDTO> rewardToken) {
            this.rewardToken = rewardToken;
        }

        public List<Integer> getRiveToken() {
            return riveToken;
        }

        public void setRiveToken(List<Integer> riveToken) {
            this.riveToken = riveToken;
        }

        public int getBuyConfirmedCount() {
            return buyConfirmedCount;
        }

        public void setBuyConfirmedCount(int buyConfirmedCount) {
            this.buyConfirmedCount = buyConfirmedCount;
        }

        public int getBuyEvaluateCount() {
            return buyEvaluateCount;
        }

        public void setBuyEvaluateCount(int buyEvaluateCount) {
            this.buyEvaluateCount = buyEvaluateCount;
        }

        public int getSoldConfirmedCount() {
            return soldConfirmedCount;
        }

        public void setSoldConfirmedCount(int soldConfirmedCount) {
            this.soldConfirmedCount = soldConfirmedCount;
        }

        public int getSoldEvaluateCount() {
            return soldEvaluateCount;
        }

        public void setSoldEvaluateCount(int soldEvaluateCount) {
            this.soldEvaluateCount = soldEvaluateCount;
        }

        public String getSexDec() {
            return sexDec;
        }

        public void setSexDec(String sexDec) {
            this.sexDec = sexDec;
        }

        private String sexDec;

        public List<WorkTokenDTO> getList() {
            return list;
        }

        public void setList(List<WorkTokenDTO> list) {
            this.list = list;
        }

        public int getAdmittedCount() {
            return admittedCount;
        }

        public void setAdmittedCount(int admittedCount) {
            this.admittedCount = admittedCount;
        }

        public int getConfirmedCount() {
            return confirmedCount;
        }

        public void setConfirmedCount(int confirmedCount) {
            this.confirmedCount = confirmedCount;
        }

        public int getEvaluateCount() {
            return evaluateCount;
        }

        public void setEvaluateCount(int evaluateCount) {
            this.evaluateCount = evaluateCount;
        }

        public int getApplyEvaluateCount() {
            return applyEvaluateCount;
        }

        public void setApplyEvaluateCount(int applyEvaluateCount) {
            this.applyEvaluateCount = applyEvaluateCount;
        }

        public int getSignUpCount() {
            return signUpCount;
        }

        public void setSignUpCount(int signUpCount) {
            this.signUpCount = signUpCount;
        }

        public int getRecordedCount() {
            return recordedCount;
        }

        public void setRecordedCount(int recordedCount) {
            this.recordedCount = recordedCount;
        }

        public String getQuestionPid() {
            return questionPid;
        }

        public void setQuestionPid(String questionPid) {
            this.questionPid = questionPid;
        }

        public String getObserverPid() {

            return observerPid;
        }

        public void setObserverPid(String observerPid) {
            this.observerPid = observerPid;
        }

        public String getObserverName() {
            return observerName;
        }

        public void setObserverName(String observerName) {
            this.observerName = observerName;
        }

        public String getObserverContent() {
            return observerContent;
        }

        public void setObserverContent(String observerContent) {
            this.observerContent = observerContent;
        }

        public String getAnswerPid() {
            return answerPid;
        }

        public void setAnswerPid(String answerPid) {
            this.answerPid = answerPid;
        }

        public String getAnswerName() {
            return answerName;
        }

        public void setAnswerName(String answerName) {
            this.answerName = answerName;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public int getPayment() {
            return payment;
        }

        public void setPayment(int payment) {
            this.payment = payment;
        }

        public long getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(long releaseDate) {
            this.releaseDate = releaseDate;
        }

        public long getDeadline() {
            return deadline;
        }

        public void setDeadline(long deadline) {
            this.deadline = deadline;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPositionType() {
            return positionType;
        }

        public void setPositionType(int positionType) {
            this.positionType = positionType;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getEducation() {
            return education;
        }

        public void setEducation(int education) {
            this.education = education;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getPictrueId() {
            return pictrueId;
        }

        public void setPictrueId(String pictrueId) {
            this.pictrueId = pictrueId;
        }

        public long getFirstRefreshTime() {
            return firstRefreshTime;
        }

        public void setFirstRefreshTime(long firstRefreshTime) {
            this.firstRefreshTime = firstRefreshTime;
        }

        public String getReleasePid() {
            return releasePid;
        }

        public void setReleasePid(String releasePid) {
            this.releasePid = releasePid;
        }

        public UserInfoDTO getReleaseInfo() {
            return releaseInfo;
        }

        public void setReleaseInfo(UserInfoDTO releaseInfo) {
            this.releaseInfo = releaseInfo;
        }

        public List<UserInfoDTO> getEmployeeInfo() {
            return employeeInfo;
        }

        public void setEmployeeInfo(List<UserInfoDTO> employeeInfo) {
            this.employeeInfo = employeeInfo;
        }

        public int getApplyCount() {
            return applyCount;
        }

        public void setApplyCount(int applyCount) {
            this.applyCount = applyCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRelationState() {
            return relationState;
        }

        public void setRelationState(int relationState) {
            this.relationState = relationState;
        }

        public String getWorkHours() {
            return workHours;
        }

        public void setWorkHours(String workHours) {
            this.workHours = workHours;
        }

        public int getShowApplyBtn() {
            return showApplyBtn;
        }

        public void setShowApplyBtn(int showApplyBtn) {
            this.showApplyBtn = showApplyBtn;
        }

        public String getDeadlineDec() {
            return deadlineDec;
        }

        public void setDeadlineDec(String deadlineDec) {
            this.deadlineDec = deadlineDec;
        }

        public String getPaymentDec() {
            return paymentDec;
        }

        public void setPaymentDec(String paymentDec) {
            this.paymentDec = paymentDec;
        }

        public String getExperienceDec() {
            return experienceDec;
        }

        public void setExperienceDec(String experienceDec) {
            this.experienceDec = experienceDec;
        }

        public String getEducationDec() {
            return educationDec;
        }

        public void setEducationDec(String educationDec) {
            this.educationDec = educationDec;
        }


    }


    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类3*********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public static class UserInfoDTO implements Serializable {

        private String userId;

        private String portraitId;

        private String realName;

        private String company;

        private String position;

        private int level;

        private int userSex;

        private int applaySate;

        private String address;

        private List<ImpressionDTO> userImpression;

        private int repliesNumber;

        private int postNumber;

        private double improveDegree = 0;

        private int messageCount = 0;

        private int informCount = 0;

        private int praiseCount = 0;

        private String sexDec;
        private int count;
        private String jobState;
        private String background;
        private long lastTime;
        private int dynamicCount;
        private long dynamicNewTime;
        private String phoneNumber;
        private String type;
        private int commCount;
        private int goodCount;
        private int badCount;
        private String state;


        public UserInfoDTO(String address, int applaySate, String background, int badCount, int commCount, String company, int count, int dynamicCount, long dynamicNewTime, int goodCount, double improveDegree, int informCount, String jobState, long lastTime, int level, int messageCount, String phoneNumber, String portraitId, String position, int postNumber, int praiseCount, String realName, int repliesNumber, String sexDec, String state, String type, String userId, List<ImpressionDTO> userImpression, int userSex) {
            this.address = address;
            this.applaySate = applaySate;
            this.background = background;
            this.badCount = badCount;
            this.commCount = commCount;
            this.company = company;
            this.count = count;
            this.dynamicCount = dynamicCount;
            this.dynamicNewTime = dynamicNewTime;
            this.goodCount = goodCount;
            this.improveDegree = improveDegree;
            this.informCount = informCount;
            this.jobState = jobState;
            this.lastTime = lastTime;
            this.level = level;
            this.messageCount = messageCount;
            this.phoneNumber = phoneNumber;
            this.portraitId = portraitId;
            this.position = position;
            this.postNumber = postNumber;
            this.praiseCount = praiseCount;
            this.realName = realName;
            this.repliesNumber = repliesNumber;
            this.sexDec = sexDec;
            this.state = state;
            this.type = type;
            this.userId = userId;
            this.userImpression = userImpression;
            this.userSex = userSex;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getCommCount() {
            return commCount;
        }

        public void setCommCount(int commCount) {
            this.commCount = commCount;
        }

        public int getGoodCount() {
            return goodCount;
        }

        public void setGoodCount(int goodCount) {
            this.goodCount = goodCount;
        }

        public int getBadCount() {
            return badCount;
        }

        public void setBadCount(int badCount) {
            this.badCount = badCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public int getDynamicCount() {
            return dynamicCount;
        }

        public void setDynamicCount(int dynamicCount) {
            this.dynamicCount = dynamicCount;
        }

        public long getDynamicNewTime() {
            return dynamicNewTime;
        }

        public void setDynamicNewTime(long dynamicNewTime) {
            this.dynamicNewTime = dynamicNewTime;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getJobState() {
            return jobState;
        }

        public void setJobState(String jobState) {
            this.jobState = jobState;
        }

        public String getSexDec() {
            return sexDec;
        }

        public void setSexDec(String sexDec) {
            this.sexDec = sexDec;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPortraitId() {
            return portraitId;
        }

        public void setPortraitId(String portraitId) {
            this.portraitId = portraitId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }

        public int getApplaySate() {
            return applaySate;
        }

        public void setApplaySate(int applaySate) {
            this.applaySate = applaySate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<ImpressionDTO> getUserImpression() {
            return userImpression;
        }

        public void setUserImpression(List<ImpressionDTO> userImpression) {
            this.userImpression = userImpression;
        }

        public int getRepliesNumber() {
            return repliesNumber;
        }

        public void setRepliesNumber(int repliesNumber) {
            this.repliesNumber = repliesNumber;
        }

        public int getPostNumber() {
            return postNumber;
        }

        public void setPostNumber(int postNumber) {
            this.postNumber = postNumber;
        }

        public double getImproveDegree() {
            return improveDegree;
        }

        public void setImproveDegree(double improveDegree) {
            this.improveDegree = improveDegree;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public int getInformCount() {
            return informCount;
        }

        public void setInformCount(int informCount) {
            this.informCount = informCount;
        }

        public int getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(int praiseCount) {
            this.praiseCount = praiseCount;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************4********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class ImpressionDTO implements Serializable {
        private String pid;

        private String impression;

        private String count;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getImpression() {
            return impression;
        }

        public void setImpression(String impression) {
            this.impression = impression;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public class PositionTypeInfoDTO implements Serializable {
        private String positionNumber;
        private String positionName;
        private String parentNumber;
        private List<PositionTypeInfo> list;

        public String getPositionNumber() {
            return positionNumber;
        }

        public void setPositionNumber(String positionNumber) {
            this.positionNumber = positionNumber;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getParentNumber() {
            return parentNumber;
        }

        public void setParentNumber(String parentNumber) {
            this.parentNumber = parentNumber;
        }

        public List<PositionTypeInfo> getList() {
            return list;
        }

        public void setList(List<PositionTypeInfo> list) {
            this.list = list;
        }
    }

    public class PositionTypeInfo implements Serializable {
        private String positionNumber;
        private String positionName;
        private String parentNumber;

        public String getPositionNumber() {
            return positionNumber;
        }

        public void setPositionNumber(String positionNumber) {
            this.positionNumber = positionNumber;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getParentNumber() {
            return parentNumber;
        }

        public void setParentNumber(String parentNumber) {
            this.parentNumber = parentNumber;
        }
    }

    public class NewsContentDetails {
        private String content;
        private String pictrueid;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPictrueid() {
            return pictrueid;
        }

        public void setPictrueid(String pictrueid) {
            this.pictrueid = pictrueid;
        }
    }

    public class InformationInfo implements Serializable {
        private String pid;
        private String title;
        private String subTitle;
        private String content;
        private String pictrueid;
        private String issuser;
        private long releaseTime;
        private int count;
        private List<NewsContentDetails> informationInfoList;

        public List<NewsContentDetails> getInformationInfoList() {
            return informationInfoList;
        }

        public void setInformationInfoList(List<NewsContentDetails> informationInfoList) {
            this.informationInfoList = informationInfoList;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPictrueid() {
            return pictrueid;
        }

        public void setPictrueid(String pictrueid) {
            this.pictrueid = pictrueid;
        }

        public String getIssuser() {
            return issuser;
        }

        public void setIssuser(String issuser) {
            this.issuser = issuser;
        }

        public long getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(long releaseTime) {
            this.releaseTime = releaseTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public class WorkTokenDTO implements Serializable {
        private String type;
        private String money;
        private String count;
        private String position;
        private String pid;
        private String enoughDay;
        private String workPid;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getWorkPid() {
            return workPid;
        }

        public void setWorkPid(String workPid) {
            this.workPid = workPid;
        }

        public String getEnoughDay() {
            return enoughDay;
        }

        public void setEnoughDay(String enoughDay) {
            this.enoughDay = enoughDay;
        }


        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public class ApliPayDTO implements Serializable {
        private String alipaySign;

        public String getAlipaySign() {
            return alipaySign;
        }

        public void setAlipaySign(String alipaySign) {
            this.alipaySign = alipaySign;
        }
    }

    public class WeChatPayDTO implements Serializable {
        private String appid;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String sign;
        private String timestamp;

        @Override
        public String toString() {
            return "WeChatPayDTO{" +
                    "appid='" + appid + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", sign='" + sign + '\'' +
                    ", timeStamp='" + timestamp + '\'' +
                    '}';
        }

        public String getTimeStamp() {
            return timestamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timestamp = timeStamp;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类5********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class ServerDTO implements Serializable {
        private String serverPid;
        private String title;
        private String payment;
        private String pictrueId;
        private String content;
        private Double money;
        private int serviceType;
        private long createDate;
        private String userPid;
        private String state;
        private String paymentDec;
        private String soldCount;
        private String level;
        private UserInfoDTO userInfo;
        private String serverRelationPid;
        private List<EvaluateInfoDTO> evaluateInfoDTOList;

        @Override
        public String toString() {
            return "ServerDTO{" +
                    "serverPid='" + serverPid + '\'' +
                    ", title='" + title + '\'' +
                    ", payment='" + payment + '\'' +
                    ", pictrueId='" + pictrueId + '\'' +
                    ", content='" + content + '\'' +
                    ", money=" + money +
                    ", serviceType=" + serviceType +
                    ", createDate=" + createDate +
                    ", userPid='" + userPid + '\'' +
                    ", state='" + state + '\'' +
                    ", paymentDec='" + paymentDec + '\'' +
                    ", soldCount='" + soldCount + '\'' +
                    ", level='" + level + '\'' +
                    ", userInfo=" + userInfo +
                    ", serverRelationPid='" + serverRelationPid + '\'' +
                    '}';
        }

        public List<EvaluateInfoDTO> getEvaluateInfoDTOList() {
            return evaluateInfoDTOList;
        }

        public void setEvaluateInfoDTOList(List<EvaluateInfoDTO> evaluateInfoDTOList) {
            this.evaluateInfoDTOList = evaluateInfoDTOList;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getServerRelationPid() {
            return serverRelationPid;
        }

        public void setServerRelationPid(String serverRelationPid) {
            this.serverRelationPid = serverRelationPid;
        }

        public UserInfoDTO getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSoldCount() {
            return soldCount;
        }

        public void setSoldCount(String soldCount) {
            this.soldCount = soldCount;
        }

        public String getPaymentDec() {
            return paymentDec;
        }

        public void setPaymentDec(String paymentDec) {
            this.paymentDec = paymentDec;
        }

        public String getServerPid() {
            return serverPid;
        }

        public void setServerPid(String serverPid) {
            this.serverPid = serverPid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getPictrueId() {
            return pictrueId;
        }

        public void setPictrueId(String pictrueId) {
            this.pictrueId = pictrueId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public int getServiceType() {
            return serviceType;
        }

        public void setServiceType(int serviceType) {
            this.serviceType = serviceType;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类6********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public static class DynamicDTO implements Serializable {
        String distance;
        private String pid;
        private String pictrueid;//图片
        private String createBy;//创建人
        private String dynamicPid;
        private String content;//内容
        private UserInfoDTO releaseInfo;
        private Double lng;//精度
        private Double lat;//纬度
        private long createTime;//创建时间
        private String firstRefreshTime;
        private int isPointGood;
        private int goodCount;//点赞
        private int badCount;//差评
        private int commentCount;//评论
        private String createTimeDec;
        private boolean isComment;
        private String timeDec;

        public DynamicDTO(int badCount, int commentCount, String content, String createBy, long createTime, String createTimeDec, String distance, String dynamicPid, String firstRefreshTime, int goodCount, boolean isComment, int isPointGood, Double lat, Double lng, String pictrueid, String pid, UserInfoDTO releaseInfo, String timeDec) {
            this.badCount = badCount;
            this.commentCount = commentCount;
            this.content = content;
            this.createBy = createBy;
            this.createTime = createTime;
            this.createTimeDec = createTimeDec;
            this.distance = distance;
            this.dynamicPid = dynamicPid;
            this.firstRefreshTime = firstRefreshTime;
            this.goodCount = goodCount;
            this.isComment = isComment;
            this.isPointGood = isPointGood;
            this.lat = lat;
            this.lng = lng;
            this.pictrueid = pictrueid;
            this.pid = pid;
            this.releaseInfo = releaseInfo;
            this.timeDec = timeDec;
        }

        @Override
        public String toString() {
            return "DynamicDTO{" +
                    "badCount=" + badCount +
                    ", distance='" + distance + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pictrueid='" + pictrueid + '\'' +
                    ", createBy='" + createBy + '\'' +
                    ", dynamicPid='" + dynamicPid + '\'' +
                    ", content='" + content + '\'' +
                    ", releaseInfo=" + releaseInfo +
                    ", lng=" + lng +
                    ", lat=" + lat +
                    ", createTime=" + createTime +
                    ", firstRefreshTime='" + firstRefreshTime + '\'' +
                    ", isPointGood=" + isPointGood +
                    ", goodCount=" + goodCount +
                    ", commentCount=" + commentCount +
                    ", createTimeDec='" + createTimeDec + '\'' +
                    ", isComment=" + isComment +
                    ", timeDec='" + timeDec + '\'' +
                    '}';
        }

        public String getTimeDec() {
            return timeDec;
        }

        public void setTimeDec(String timeDec) {
            this.timeDec = timeDec;
        }

        public boolean isComment() {
            return isComment;
        }

        public void setComment(boolean comment) {
            isComment = comment;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCreateTimeDec() {
            return createTimeDec;
        }

        public void setCreateTimeDec(String createTimeDec) {
            this.createTimeDec = createTimeDec;
        }

        public int getIsPointGood() {
            return isPointGood;
        }

        public void setIsPointGood(int isPointGood) {
            this.isPointGood = isPointGood;
        }

        public int getBadCount() {
            return badCount;
        }

        public void setBadCount(int badCount) {
            this.badCount = badCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDynamicPid() {
            return dynamicPid;
        }

        public void setDynamicPid(String dynamicPid) {
            this.dynamicPid = dynamicPid;
        }

        public String getFirstRefreshTime() {
            return firstRefreshTime;
        }

        public void setFirstRefreshTime(String firstRefreshTime) {
            this.firstRefreshTime = firstRefreshTime;
        }

        public int getGoodCount() {
            return goodCount;
        }

        public void setGoodCount(int goodCount) {
            this.goodCount = goodCount;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public String getPictrueid() {
            return pictrueid;
        }

        public void setPictrueid(String pictrueid) {
            this.pictrueid = pictrueid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public UserInfoDTO getReleaseInfo() {
            return releaseInfo;
        }

        public void setReleaseInfo(UserInfoDTO releaseInfo) {
            this.releaseInfo = releaseInfo;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类7********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class ResumeDTO implements Serializable {
        //基本信息
        private String pid;
        private String userPid;
        private String name;
        private int sex;
        private int height;
        private int age;
        private String address;   //现住址
        private String education; //学历
        private String major; //专业
        private String school;   //毕业院校
        private String educationExperience; //教育经历
        private String industryDirection;  //行业方向
        private String experience; //工作经验
        private String skill;   //技能
        private long updateDate;
        private long createDate;
        private String personalEvaluation;
        //企业用户
        private String scale;
        private String organizationCode;
        private String website;
        private String mainBusiness;
        private String portraitId;
        private String businessLicense;

        @Override
        public String toString() {
            return "ResumeDTO{" +
                    "pid='" + pid + '\'' +
                    ", userPid='" + userPid + '\'' +
                    ", name='" + name + '\'' +
                    ", sex=" + sex +
                    ", height=" + height +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", education='" + education + '\'' +
                    ", major='" + major + '\'' +
                    ", school='" + school + '\'' +
                    ", educationExperience='" + educationExperience + '\'' +
                    ", industryDirection='" + industryDirection + '\'' +
                    ", experience='" + experience + '\'' +
                    ", skill='" + skill + '\'' +
                    ", updateDate=" + updateDate +
                    ", createDate=" + createDate +
                    ", scale='" + scale + '\'' +
                    ", organizationCode='" + organizationCode + '\'' +
                    ", website='" + website + '\'' +
                    ", mainBusiness='" + mainBusiness + '\'' +
                    ", businessLicense='" + businessLicense + '\'' +
                    '}';
        }

        public String getPortraitId() {
            return portraitId;
        }

        public void setPortraitId(String portraitId) {
            this.portraitId = portraitId;
        }

        public String getPersonalEvaluation() {
            return personalEvaluation;
        }

        public void setPersonalEvaluation(String personalEvaluation) {
            this.personalEvaluation = personalEvaluation;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getEducationExperience() {
            return educationExperience;
        }

        public void setEducationExperience(String educationExperience) {
            this.educationExperience = educationExperience;
        }

        public String getIndustryDirection() {
            return industryDirection;
        }

        public void setIndustryDirection(String industryDirection) {
            this.industryDirection = industryDirection;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public long getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(long updateDate) {
            this.updateDate = updateDate;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getOrganizationCode() {
            return organizationCode;
        }

        public void setOrganizationCode(String organizationCode) {
            this.organizationCode = organizationCode;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getMainBusiness() {
            return mainBusiness;
        }

        public void setMainBusiness(String mainBusiness) {
            this.mainBusiness = mainBusiness;
        }

        public String getBusinessLicense() {
            return businessLicense;
        }

        public void setBusinessLicense(String businessLicense) {
            this.businessLicense = businessLicense;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类7********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class AccountDTO implements Serializable {
        private String userPid;
        private double amount;

        //个人账单明细
        private double money;
        private String description;
        private String adminAccountPid;
        private String type;
        private long updatedate;
        private long createdate;
        private String createDateDec;

        @Override
        public String toString() {
            return "AccountDTO{" +
                    "userPid='" + userPid + '\'' +
                    ", amount=" + amount +
                    ", money=" + money +
                    ", description='" + description + '\'' +
                    ", adminAccountPid='" + adminAccountPid + '\'' +
                    ", type='" + type + '\'' +
                    ", updatedate=" + updatedate +
                    ", createdate=" + createdate +
                    ", createDateDec='" + createDateDec + '\'' +
                    '}';
        }

        public String getCreateDateDec() {
            return createDateDec;
        }

        public void setCreateDateDec(String createDateDec) {
            this.createDateDec = createDateDec;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdminAccountPid() {
            return adminAccountPid;
        }

        public void setAdminAccountPid(String adminAccountPid) {
            this.adminAccountPid = adminAccountPid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getUpdatedate() {
            return updatedate;
        }

        public void setUpdatedate(long updatedate) {
            this.updatedate = updatedate;
        }

        public long getCreatedate() {
            return createdate;
        }

        public void setCreatedate(long createdate) {
            this.createdate = createdate;
        }
    }

    //TODO *********************************************华丽的分割线*********************************************
    //TODO *********************************************类7********************************************
    //TODO *********************************************华丽的分割线*********************************************
    public class WorkEvaluateView implements Serializable {
        private String pid;
        private String workId;
        private String title;
        private UserInfoDTO criticInfo;        //评论人的信息
        private String content;     //评论内容
        private int level;      //评论等级
        private String criticId;      //评论人的id
        private String arguedId;   //回复人的id
        private String content2;     //回复内容
        private int level2;      //回复等级
        private long creatDate;
        private UserInfoDTO arguedInfo;         //本人的信息

        @Override
        public String toString() {
            return "WorkEvaluateView{" +
                    "pid='" + pid + '\'' +
                    ", workId='" + workId + '\'' +
                    ", title='" + title + '\'' +
                    ", criticInfo=" + criticInfo +
                    ", content='" + content + '\'' +
                    ", level=" + level +
                    ", criticId='" + criticId + '\'' +
                    ", arguedId='" + arguedId + '\'' +
                    ", content2='" + content2 + '\'' +
                    ", level2=" + level2 +
                    ", arguedInfo=" + arguedInfo +
                    '}';
        }

        public long getCreatDate() {
            return creatDate;
        }

        public void setCreatDate(long creatDate) {
            this.creatDate = creatDate;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getWorkId() {
            return workId;
        }

        public void setWorkId(String workId) {
            this.workId = workId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public UserInfoDTO getCriticInfo() {
            return criticInfo;
        }

        public void setCriticInfo(UserInfoDTO criticInfo) {
            this.criticInfo = criticInfo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getCriticId() {
            return criticId;
        }

        public void setCriticId(String criticId) {
            this.criticId = criticId;
        }

        public String getArguedId() {
            return arguedId;
        }

        public void setArguedId(String arguedId) {
            this.arguedId = arguedId;
        }

        public String getContent2() {
            return content2;
        }

        public void setContent2(String content2) {
            this.content2 = content2;
        }

        public int getLevel2() {
            return level2;
        }

        public void setLevel2(int level2) {
            this.level2 = level2;
        }

        public UserInfoDTO getArguedInfo() {
            return arguedInfo;
        }

        public void setArguedInfo(UserInfoDTO arguedInfo) {
            this.arguedInfo = arguedInfo;
        }

    }

    public class orderDetail implements Serializable {
        private String tradeNo;
        private String subject;
        private Double amount;
        private String appId;
        private String buyerLoginId;
        private String sellerId;
        private String sellerEmail;
        private long gmtPayment;
        private long gmtCreate;
        private String buyerId;
        private String body;
        private Double invoiceAmount;
        private String notifyId;
        private String notifyType;
        private long notifyTime;
        private Double totalAmount;
        private int tradeStatus;
        private Double receiptAmount;
        private Double pointAmount;
        private Double buyerPayAmount;
        private String userPid;
        private long createDate;
        private String spbillCreateIp;
        private String tradeType;
        private String mchId;
        private String timeStart;
        private String timeExpire;
        private String openid;
        private String isSubscribe;
        private String transactionId;
        private String timeEnd;
        private String type;
        private String workPid;
        private String accounted;
        private String buyerEmail;
        private String title;
        private String outTradeNo;

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getBuyerLoginId() {
            return buyerLoginId;
        }

        public void setBuyerLoginId(String buyerLoginId) {
            this.buyerLoginId = buyerLoginId;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerEmail() {
            return sellerEmail;
        }

        public void setSellerEmail(String sellerEmail) {
            this.sellerEmail = sellerEmail;
        }

        public long getGmtPayment() {
            return gmtPayment;
        }

        public void setGmtPayment(long gmtPayment) {
            this.gmtPayment = gmtPayment;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Double getInvoiceAmount() {
            return invoiceAmount;
        }

        public void setInvoiceAmount(Double invoiceAmount) {
            this.invoiceAmount = invoiceAmount;
        }

        public String getNotifyId() {
            return notifyId;
        }

        public void setNotifyId(String notifyId) {
            this.notifyId = notifyId;
        }

        public String getNotifyType() {
            return notifyType;
        }

        public void setNotifyType(String notifyType) {
            this.notifyType = notifyType;
        }

        public long getNotifyTime() {
            return notifyTime;
        }

        public void setNotifyTime(long notifyTime) {
            this.notifyTime = notifyTime;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(int tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public Double getReceiptAmount() {
            return receiptAmount;
        }

        public void setReceiptAmount(Double receiptAmount) {
            this.receiptAmount = receiptAmount;
        }

        public Double getPointAmount() {
            return pointAmount;
        }

        public void setPointAmount(Double pointAmount) {
            this.pointAmount = pointAmount;
        }

        public Double getBuyerPayAmount() {
            return buyerPayAmount;
        }

        public void setBuyerPayAmount(Double buyerPayAmount) {
            this.buyerPayAmount = buyerPayAmount;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getSpbillCreateIp() {
            return spbillCreateIp;
        }

        public void setSpbillCreateIp(String spbillCreateIp) {
            this.spbillCreateIp = spbillCreateIp;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeExpire() {
            return timeExpire;
        }

        public void setTimeExpire(String timeExpire) {
            this.timeExpire = timeExpire;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getIsSubscribe() {
            return isSubscribe;
        }

        public void setIsSubscribe(String isSubscribe) {
            this.isSubscribe = isSubscribe;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWorkPid() {
            return workPid;
        }

        public void setWorkPid(String workPid) {
            this.workPid = workPid;
        }

        public String getAccounted() {
            return accounted;
        }

        public void setAccounted(String accounted) {
            this.accounted = accounted;
        }

        public String getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class DynamicCommentDTO implements Serializable {
        private String dynamicPid;
        private String dynamicCommentPid;
        private String commentContent;
        private long createTime;
        private UserInfoDTO releaseInfo;
        private List<UserInfoDTO> pointUserInfoList;
        private boolean isComment;
        private String createDateDec;
        private boolean isPointGood;
        private int count;
        private String userPid;
        private String detailCount;

        public String getDetailCount() {
            return detailCount;
        }

        public void setDetailCount(String detailCount) {
            this.detailCount = detailCount;
        }

        private List<DynamicCommentDetailDTO> replyList;

        public List<DynamicCommentDetailDTO> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<DynamicCommentDetailDTO> replyList) {
            this.replyList = replyList;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isPointGood() {
            return isPointGood;
        }

        public void setPointGood(boolean pointGood) {
            isPointGood = pointGood;
        }

        public boolean isComment() {
            return isComment;
        }

        public void setComment(boolean comment) {
            isComment = comment;
        }

        public String getCreateDateDec() {
            return createDateDec;
        }

        public void setCreateDateDec(String createDateDec) {
            this.createDateDec = createDateDec;
        }

        public String getDynamicPid() {
            return dynamicPid;
        }

        public void setDynamicPid(String dynamicPid) {
            this.dynamicPid = dynamicPid;
        }

        public String getDynamicCommentPid() {
            return dynamicCommentPid;
        }

        public void setDynamicCommentPid(String dynamicCommentPid) {
            this.dynamicCommentPid = dynamicCommentPid;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public UserInfoDTO getReleaseInfo() {
            return releaseInfo;
        }

        public void setReleaseInfo(UserInfoDTO releaseInfo) {
            this.releaseInfo = releaseInfo;
        }

        public List<UserInfoDTO> getPointUserInfoList() {
            return pointUserInfoList;
        }

        public void setPointUserInfoList(List<UserInfoDTO> pointUserInfoList) {
            this.pointUserInfoList = pointUserInfoList;
        }
    }

    public class DynamicCommentDetailDTO implements Serializable {
        private int count;

        private String commentDetailPid;

        private String commentPid;

        private String userPid;

        private String realName;

        private String portraitid;

        private String reUserPid;

        private String reRealName;

        private String detailContent;

        private long createTime;

        private boolean isPointGood;

        private String createTimeDec;


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCommentDetailPid() {
            return commentDetailPid;
        }

        public void setCommentDetailPid(String commentDetailPid) {
            this.commentDetailPid = commentDetailPid;
        }

        public String getCommentPid() {
            return commentPid;
        }

        public void setCommentPid(String commentPid) {
            this.commentPid = commentPid;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPortraitid() {
            return portraitid;
        }

        public void setPortraitid(String portraitid) {
            this.portraitid = portraitid;
        }

        public String getReUserPid() {
            return reUserPid;
        }

        public void setReUserPid(String reUserPid) {
            this.reUserPid = reUserPid;
        }

        public String getReRealName() {
            return reRealName;
        }

        public void setReRealName(String reRealName) {
            this.reRealName = reRealName;
        }

        public String getDetailContent() {
            return detailContent;
        }

        public void setDetailContent(String detailContent) {
            this.detailContent = detailContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean getIsPointGood() {
            return isPointGood;
        }

        public void setIsPointGood(boolean isPointGood) {
            this.isPointGood = isPointGood;
        }

        public String getCreateTimeDec() {
            return createTimeDec;
        }

        public void setCreateTimeDec(String createTimeDec) {
            this.createTimeDec = createTimeDec;
        }
    }

    public class UserImpressionInfo implements Serializable {
        private String content;   //对用户的评价

        private int count;  //对评论的点赞数

        private String evaluationuser;  //评论人的id

        private String type;        //判断0是标签，1是点评;

        private UserInfoDTO userInfo;
        private String pid;
        private int state;

        private String impression;

        public String getImpression() {
            return impression;
        }

        public void setImpression(String impression) {
            this.impression = impression;
        }

        @Override
        public String toString() {
            return "userImpressionInfo{" +
                    "content='" + content + '\'' +
                    ", count=" + count +
                    ", evaluationuser='" + evaluationuser + '\'' +
                    ", type='" + type + '\'' +
                    ", userInfo=" + userInfo +
                    '}';
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getEvaluationuser() {
            return evaluationuser;
        }

        public void setEvaluationuser(String evaluationuser) {
            this.evaluationuser = evaluationuser;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public UserInfoDTO getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
        }
    }

    public class ViewDynamic implements Serializable {
        private String dynamicId;   //动态id
        private String criticId;    //评论人或者点赞人的id
        private String content;     //内容
        private long createDate;    //
        private UserInfoDTO userInfoDTO;   //用户信息
        private String fContent;    //评论或动态内容
        private String fPicture;       //评论或者动态的图像
        private String type;    //判断是对评论还是对动态做的操作信息 1是对动态，2是对评论
        private String createDateDec;    //日期格式话

        public String getDynamicId() {
            return dynamicId;
        }

        public void setDynamicId(String dynamicId) {
            this.dynamicId = dynamicId;
        }

        public String getCriticId() {
            return criticId;
        }

        public void setCriticId(String criticId) {
            this.criticId = criticId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public UserInfoDTO getUserInfoDTO() {
            return userInfoDTO;
        }

        public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
            this.userInfoDTO = userInfoDTO;
        }

        public String getfContent() {
            return fContent;
        }

        public void setfContent(String fContent) {
            this.fContent = fContent;
        }

        public String getfPicture() {
            return fPicture;
        }

        public void setfPicture(String fPicture) {
            this.fPicture = fPicture;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDateDec() {
            return createDateDec;
        }

        public void setCreateDateDec(String createDateDec) {
            this.createDateDec = createDateDec;
        }
    }

    public class DynamicPointPraiseInfo implements Serializable {
        private String pid;
        private String dynamicPid;      //动态id
        private String pointPraise;
        private Integer state;
        private long createTime;
        private UserInfoDTO userInfo;   //用户信息
        private String fContent;    //评论或动态内容
        private String fPicture;       //评论或者动态的图像
        private String type;    //判断是对评论还是对动态做的操作信息 1是对动态，2是对评论
        private String createTimeDec;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDynamicPid() {
            return dynamicPid;
        }

        public void setDynamicPid(String dynamicPid) {
            this.dynamicPid = dynamicPid;
        }

        public String getPointPraise() {
            return pointPraise;
        }

        public void setPointPraise(String pointPraise) {
            this.pointPraise = pointPraise;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public UserInfoDTO getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
        }

        public String getfContent() {
            return fContent;
        }

        public void setfContent(String fContent) {
            this.fContent = fContent;
        }

        public String getfPicture() {
            return fPicture;
        }

        public void setfPicture(String fPicture) {
            this.fPicture = fPicture;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateTimeDec() {
            return createTimeDec;
        }

        public void setCreateTimeDec(String createTimeDec) {
            this.createTimeDec = createTimeDec;
        }
    }

    public class EvaluateInfoDTO implements Serializable {

        private String userPid;
        private String portraitId;
        private String realName;
        private int level;
        private String content;
        private long createDate;
        private String myUserPid;
        private String myPortraitId;
        private String muRealName;
        private String myContent;
        private long myCreateDate;

        @Override
        public String toString() {
            return "EvaluateInfoDTO{" +
                    "userPid='" + userPid + '\'' +
                    ", portraitId='" + portraitId + '\'' +
                    ", realName='" + realName + '\'' +
                    ", level=" + level +
                    ", content='" + content + '\'' +
                    ", createDate=" + createDate +
                    ", myUserPid='" + myUserPid + '\'' +
                    ", myPortraitId='" + myPortraitId + '\'' +
                    ", muRealName='" + muRealName + '\'' +
                    ", myContent='" + myContent + '\'' +
                    ", myCreateDate=" + myCreateDate +
                    '}';
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getPortraitId() {
            return portraitId;
        }

        public void setPortraitId(String portraitId) {
            this.portraitId = portraitId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getMyUserPid() {
            return myUserPid;
        }

        public void setMyUserPid(String myUserPid) {
            this.myUserPid = myUserPid;
        }

        public String getMyPortraitId() {
            return myPortraitId;
        }

        public void setMyPortraitId(String myPortraitId) {
            this.myPortraitId = myPortraitId;
        }

        public String getMuRealName() {
            return muRealName;
        }

        public void setMuRealName(String muRealName) {
            this.muRealName = muRealName;
        }

        public String getMyContent() {
            return myContent;
        }

        public void setMyContent(String myContent) {
            this.myContent = myContent;
        }

        public long getMyCreateDate() {
            return myCreateDate;
        }

        public void setMyCreateDate(long myCreateDate) {
            this.myCreateDate = myCreateDate;
        }
    }

    public class workPushDTO implements Serializable {
        private String pid;
        private String workPid;

        private String userPid;
        private String userName;
        private String workType;

        private String title;

        private int type;

        private String state;

        private String beUserPid;

        private String beUserName;
        private String typeDec;
        private long createDate;

        @Override
        public String toString() {
            return "workPushDTO{" +
                    "pid='" + pid + '\'' +
                    ", workPid='" + workPid + '\'' +
                    ", userPid='" + userPid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", state='" + state + '\'' +
                    ", beUserPid='" + beUserPid + '\'' +
                    ", beUserName='" + beUserName + '\'' +
                    '}';
        }

        public String getWorkType() {
            return workType;
        }

        public void setWorkType(String workType) {
            this.workType = workType;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getTypeDec() {
            return typeDec;
        }

        public void setTypeDec(String typeDec) {
            this.typeDec = typeDec;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getWorkPid() {
            return workPid;
        }

        public void setWorkPid(String workPid) {
            this.workPid = workPid;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getBeUserPid() {
            return beUserPid;
        }

        public void setBeUserPid(String beUserPid) {
            this.beUserPid = beUserPid;
        }

        public String getBeUserName() {
            return beUserName;
        }

        public void setBeUserName(String beUserName) {
            this.beUserName = beUserName;
        }
    }

    public class AdviceInfor implements Serializable {
        private String pid;
        private String content;
        private long createdate;
        private String pictrueid;
        private String phone;
        private String state;
        private int count;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreatedate() {
            return createdate;
        }

        public void setCreatedate(long createdate) {
            this.createdate = createdate;
        }

        public String getPictrueid() {
            return pictrueid;
        }

        public void setPictrueid(String pictrueid) {
            this.pictrueid = pictrueid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
