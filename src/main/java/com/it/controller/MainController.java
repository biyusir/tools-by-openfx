package com.it.controller;

import com.it.Demo;
import com.it.view.FormatSqlView;
import com.it.view.LoadingView;
import com.it.view.TransferView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {

    @FXML
    LoadingView helloDialog;
    private Stage stage;

    public void transferToJson(){
        Demo.showView(TransferView.class,Modality.APPLICATION_MODAL);
    }

    public void formatSql(){
        Demo.showView(FormatSqlView.class,Modality.APPLICATION_MODAL);
    }
    public void checkUpdate() {
        try {
            OkHttpClient client = new OkHttpClient();
            String baidu_url = "https://www.baidu.com1";
            Request request = new Request.Builder()
                    .url(baidu_url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                Headers headers = response.headers();
                String body = response.body().string();
                System.out.println("--------headers--------\n" + headers);
                System.out.println("--------body--------\n" + body);
            }
        }catch (Exception e) {
           Demo.showView(LoadingView.class,Modality.NONE);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
