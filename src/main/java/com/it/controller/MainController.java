package com.it.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.it.bean.Bean;
import com.it.util.SqlUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {
    @FXML
    Pane rightPane;

    // type = 0 转化sql type=1 转化json
    private int type;
    @FXML
    public JFXButton transferBtn;
    @FXML
    public JFXListView<Bean> listView;

    public void transferToJson(){
        rightPane.setVisible(true);
        transferBtn.setText("转化JSON");
        type = 0;
    }

    public void formatSql(){
        rightPane.setVisible(true);
        transferBtn.setText("转化SQL");
        type = 1;
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
            this.rightPane.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.listView.setItems(FXCollections.observableArrayList());
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    this.originArea.setText(t1.getOrgText());
                    this.transferArea.setText(t1.getTransferText());
                    this.type = t1.getType();
                }
        );
    }

    @FXML
    JFXTextArea originArea;
    @FXML
    JFXTextArea transferArea;

    /**
     * 格式化json串
     */
    public void transfer() {
        try {
            String text = originArea.getText();
            String transferValue;
            if (this.type == 1){

                if (StringUtils.isEmpty(text)) {
                    transferArea.setText("");
                    return;
                }

                String[] sql = text.split("\n");
                String sqls;
                String params;
                if (StringUtils.contains(sql[0],"Parameters")){
                    sqls = sql[1].replace(sql[1].substring(0, sql[1].indexOf(">")), "");
                    params = sql[0].replace(sql[0].substring(0, sql[0].indexOf(">")), "");
                }else {
                    params = sql[1].replace(sql[1].substring(0, sql[1].indexOf(">")), "");
                    sqls = sql[0].replace(sql[0].substring(0, sql[0].indexOf(">")), "");

                }


                int i = sqls.indexOf(":");
                int i1 = params.indexOf(":");
                sqls = sqls.replace(sqls.substring(0, i + 1), "");
                params = params.replace(params.substring(0, i1 + 1), "");



                String value = SqlUtils.parseSql(sqls, params);
                transferValue = value;
            }else {
                if (StringUtils.isEmpty(text)) {
                    transferArea.setText("");
                    return;
                }
                JsonParser jsonParser = new JsonParser();
                Object o;
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                if (text.charAt(0) != '['){
                    o= jsonParser.parse(text).getAsJsonObject();
                }else {
                    o = jsonParser.parse(text).getAsJsonArray();
                }
                transferValue = gson.toJson(o);
            }

            transferArea.setText(transferValue);
            this.listView.getItems().add(new Bean(text,this.type,new Date(),transferValue));
            this.listView.getItems().sort(Bean::compareTo);

        } catch (Exception e) {
            e.printStackTrace();
            transferArea.setText(e.getMessage());
        }
    }
}
