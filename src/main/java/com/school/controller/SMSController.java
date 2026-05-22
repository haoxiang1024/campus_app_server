package com.school.controller;

import com.school.utils.ServerResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Random;

@Controller
public class SMSController {



    @ResponseBody
    @RequestMapping("/sms")
    public ServerResponse SMS(String phone, String codes, String opCode) {
        String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);
        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");
        if (opCode.equals("send")) {
            Random random = new Random();
            String mobile_code = Integer.toString(random.nextInt(900000) + 100000);

            String content = "您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。";

            NameValuePair[] data = {
                    new NameValuePair("account", "C27056597"),
                    new NameValuePair("password", "dd3fa0ffe431b5fc5e50b2f70ecae9cd"),
                    new NameValuePair("mobile", phone),
                    new NameValuePair("content", content),
            };
            method.setRequestBody(data);
            try {
                client.executeMethod(method);
                String SubmitResult = method.getResponseBodyAsString();
                Document doc = DocumentHelper.parseText(SubmitResult);
                Element root = doc.getRootElement();
                String code = root.elementText("code");
                String msg = root.elementText("msg");
                String smsid = root.elementText("smsid");
                System.err.println(code);
                System.err.println(msg);
                System.err.println(smsid);
                if ("2".equals(code)) {
                    return ServerResponse.createServerResponseBySuccess("验证码发送成功");
                } else {
                    return ServerResponse.createServerResponseByFail(1, "验证码发送失败");
                }

            } catch (IOException | DocumentException e) {

                e.printStackTrace();
            }
        }
        return ServerResponse.createServerResponseBySuccess("未知操作");
    }

}
