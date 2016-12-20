package com.lovejob.view.payinfoviews.alipay;

/**
 * ClassType:／／支付宝传递参数实体类
 * User:wenyunzhao
 * ProjectName:AlipayTestDemo
 * Package_Name:com.lovejob.alipaytestdemo.wenyun
 * Created on 2016-10-26 12:04
 */

public class BusenessParmas {
    private String body;//maxLeght=128  对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。//TODO
    private String subject;//maxLeght=256  商品的标题/交易标题/订单标题/订单关键字等。
    private String out_trade_no;//maxLeght=64  商户网站唯一订单号
    private String timeout_express;//maxLeght=6  该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
    private String total_amount;//maxLeght=9  订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    private String seller_id;//maxLeght=16  收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID //TODO
    private String product_code;//maxLeght=64  销售产品码，商家和支付宝签约的产品码，为固定值  //TODO 固定址 QUICK_MSECURITY_PAY


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
