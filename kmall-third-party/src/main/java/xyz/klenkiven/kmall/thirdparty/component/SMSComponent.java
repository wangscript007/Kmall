package xyz.klenkiven.kmall.thirdparty.component;

import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import xyz.klenkiven.kmall.thirdparty.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS Component For Send Code
 * @author klenkiven
 */
@Component
@Data
@ConfigurationProperties(prefix = "alibaba.cloud.sms")
public class SMSComponent {
    /** SMS Service Host */
    private String host;

    /** SMS Service Path */
    private String path;

    /** SMS Service Method */
    private String method;

    /** SMS Service Application Code */
    private String appcode;

    /** SMS Sign ID */
    private String smsSignId;

    /** SMS Template ID */
    private String templateId;

    /**
     * Send Mobile Code
     */
    public void sendSMSCode(String mobile, String code) {
            Map<String, String> headers = new HashMap<>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<>();
            querys.put("mobile", mobile);
            querys.put("param", "**code**:" + code + ",**minute**:5");
            querys.put("smsSignId", smsSignId);
            querys.put("templateId", templateId);
            Map<String, String> bodys = new HashMap<>();

            try {
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                System.out.println(response.toString());
                //获取response的body
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
