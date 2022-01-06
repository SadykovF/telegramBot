package ru.redut16;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import static ru.redut16.StaticVariable.URI_HTTP_GET_FILE;


public class GetFile {
    public SendDocument sendDocument(String userChatId) throws Exception {

        SendDocument sendDocument = new SendDocument();

        HttpGet httpGet = new HttpGet(URI_HTTP_GET_FILE);
        HttpResponse httpResponse = ApacheHttpClient.httpClient().execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();

        if ((statusCode == 200)) {

            InputFile inputFile = new InputFile();
            inputFile.setMedia(entity.getContent(), "Document.pdf");

            sendDocument.setDocument(inputFile);
            sendDocument.setChatId(userChatId.toString());

            return sendDocument;
        } else {
            System.out.println("no file... ");
            return null;
        }
    }
}
