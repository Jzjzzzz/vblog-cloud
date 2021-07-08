package team.ark.core.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * 短信工具类
 *
 * @author Ashinch
 * @date 2020/12/15
 */
@Slf4j
public class SmsUtils {
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    public static String TEMPLATE_ = "SMS_";

    /**
     * 发送短信并返回发送结果
     *
     * @param phones       接收者手机号, 多个手机号用逗号分割
     * @param templateCode 短信内容模板ID
     * @param jsonParam    短信内容参数JSON字符串
     */
    public static boolean sendSms(String phones, String jsonParam, String templateCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phones);
        request.putQueryParameter("SignName", "");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", jsonParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("发送短信: [{}]", response.getData());
            return true;
        } catch (Exception e) {
            log.error("发送短信失败: ", e);
        }
        return false;
    }

    /**
     * 异步发送短信
     *
     * @param phones       接收者手机号, 多个手机号用逗号分割
     * @param templateCode 短信内容模板ID
     * @param jsonParam    短信内容参数JSON字符串
     */
    @Async
    public void sendSmsAsync(String phones, String jsonParam, String templateCode) {
        sendSms(phones, jsonParam, templateCode);
    }
}
