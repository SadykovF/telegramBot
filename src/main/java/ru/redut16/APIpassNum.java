package ru.redut16;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;
import static ru.redut16.StaticVariable.URI_PASP_NUM;


public class APIpassNum {

    public String QueryPassNum(String paspnum, String userid) throws Exception {

        HttpGet httpGet = new HttpGet(URI_PASP_NUM + paspnum);

        HttpResponse httpResponse = ApacheHttpClient.httpClient().execute(httpGet);

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        HttpEntity entity = httpResponse.getEntity();
        String answer = EntityUtils.toString(entity, StandardCharsets.UTF_8);

        if ((statusCode == 200) && (!answer.contains("err"))) {
            System.out.println("UserId: " + userid + " паспорт ans = " + answer);
            return "1";
        } else {
            System.out.println("UserId: " + userid + " паспорт не найден в базе... ");
            return "2";
        }
    }
}
