package com.lovejob.qiniuyun.common;

import com.qiniu.android.dns.DnsManager;

/**
 * Created by bailong on 15/10/10.
 */
public abstract class Zone {
    public static final Zone zone0 =
            createZone("upload.qiniu.com", "up.qiniu.com", "183.136.139.10", "115.231.182.136");
    public static final Zone zone1 =
            createZone("upload-z1.qiniu.com", "up-z1.qiniu.com", "106.38.227.27", "106.38.227.28");

    public static final Zone zone2 =
            createZone("upload-z2.qiniu.com", "up-z2.qiniu.com", "183.60.214.197", "14.152.37.7");

    /**
     * 默认上传服务器
     */
    public abstract ServiceAddress upHost(String token);

    /**
     * 备用上传服务器，当默认服务器网络连接失败时使用
     */
    public abstract ServiceAddress upHostBackup(String token);

    private static Zone createZone(String upHost, String upHostBackup, String upIp, String upIp2) {
        String[] upIps = {upIp, upIp2};
        ServiceAddress up = new ServiceAddress("http://" + upHost, upIps);
        ServiceAddress upBackup = new ServiceAddress("http://" + upHostBackup, upIps);
        return new FixedZone(up, upBackup);
    }

    public static void addDnsIp(DnsManager dns){
        zone0.upHost("").addIpToDns(dns);
        zone0.upHostBackup("").addIpToDns(dns);

        zone1.upHost("").addIpToDns(dns);
        zone1.upHostBackup("").addIpToDns(dns);

        zone2.upHost("").addIpToDns(dns);
        zone2.upHostBackup("").addIpToDns(dns);
    }

    public interface QueryHandler{
        void onSuccess();
        void onFailure(int reason);
    }

    public abstract void preQuery(String token, QueryHandler complete);
}
