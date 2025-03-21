package com.zrd.springbootinit.utils;


import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;

import static com.aliyun.teautil.Common.toJSONString;

/**
 * 短信服务类
 *
 * @author <a href="https://github.com/shark688">shark688</a>

 */
public class CommentUtils {

    /**
     * 发送短信
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    public static String sendMessage(String phoneNumber) throws Exception {

        Config config = new Config()
                // 请确保代码运行环境配置了相应环境变量
                .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));

        // 配置 Endpoint
        config.endpoint = "dysmsapi.aliyuncs.com";

        // 初始化请求客户端
        Client client = new Client(config);

        //生成随机验证码
        String checkNumber = generateCaptcha(6);

        String message = String.format("{\"code\": %s}", checkNumber);

        // 构造请求对象，请替换请求参数值
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName("无慌")
                .setTemplateCode("SMS_480545074")
                .setTemplateParam(message); // TemplateParam为序列化后的JSON字符串

        // 获取响应对象
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        // 响应包含服务端响应的 body 和 headers
        return toJSONString(sendSmsResponse);

        //System.out.println(toJSONString(sendSmsResponse));
    }

    /**
     * 生成指定长度的数字字母混合随机验证码
     * @param length 验证码长度
     * @return 随机验证码
     */
    public static String generateCaptcha(int length) {
        // 定义字符集合（数字和字母）
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        StrBuilder captcha = new StrBuilder();
        for (int i = 0; i < length; i++) {
            // 随机选取一个字符并添加到验证码中
            captcha.append(RandomUtil.randomChar(chars));
        }

        return captcha.toString();
    }
}
