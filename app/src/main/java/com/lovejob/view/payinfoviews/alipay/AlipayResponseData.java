package com.lovejob.view.payinfoviews.alipay;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:AlipayTestDemo
 * Package_Name:com.lovejob.alipaytestdemo.wenyun
 * Created on 2016-10-26 21:18
 */

public class AlipayResponseData {

    /**
     * code : 10000
     * msg : Success
     * app_id : 2016011101084047
     * auth_app_id : 2016011101084047
     * charset : utf-8
     * timestamp : 2016-10-26 21:17:18
     * total_amount : 0.01
     * trade_no : 2016102621001004770299811819
     * seller_id : 2088121772697011
     * out_trade_no : 1477487813206
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2016011101084047","auth_app_id":"2016011101084047","charset":"utf-8","timestamp":"2016-10-26 21:17:18","total_amount":"0.01","trade_no":"2016102621001004770299811819","seller_id":"2088121772697011","out_trade_no":"1477487813206"}
     * sign : kZoiQRIqF+MP/F30oFqHFQT8fmNBO3XFFrJgPXPojL2FgJfVRmFhXysaDlYLJd/zqHytFcoIgBb+L/tmGFNKlqXZLdEPgH+48BOfp86XqWGoJbSeer0gq9xA8VlYiHXLAvPnfqYtVh2lMD0i3ljrJF/YYIDizTt7JsO6k6wjy9k=
     * sign_type : RSA
     */

    private String sign;
    private String sign_type;

    @Override
    public String toString() {
        return "AlipayResponseData{" +
                "alipay_trade_app_pay_response=" + alipay_trade_app_pay_response +
                ", sign='" + sign + '\'' +
                ", sign_type='" + sign_type + '\'' +
                '}';
    }

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean {
        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String total_amount;
        private String trade_no;
        private String seller_id;
        private String out_trade_no;

        @Override
        public String toString() {
            return "AlipayTradeAppPayResponseBean{" +
                    "app_id='" + app_id + '\'' +
                    ", code='" + code + '\'' +
                    ", msg='" + msg + '\'' +
                    ", auth_app_id='" + auth_app_id + '\'' +
                    ", charset='" + charset + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    ", trade_no='" + trade_no + '\'' +
                    ", seller_id='" + seller_id + '\'' +
                    ", out_trade_no='" + out_trade_no + '\'' +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }
    }
}
