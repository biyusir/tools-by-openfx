package com.it.controller;

import com.it.Demo;
import com.it.bean.Bean;
import com.it.service.IJsonTransferService;
import com.it.service.ISqlService;
import com.it.service.impl.SqlServiceImpl;
import com.it.view.JwtView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {
    @FXML
    JFXTextArea originArea;
    @FXML
    JFXTextArea transferArea;
    @FXML
    Pane jwtPane;
    @FXML
    Pane rightPane;
    @Autowired
    SqlServiceImpl helloService;

    // type = 0 转化sql type=1 转化json
    private int type;
    @FXML
    public JFXButton transferBtn;
    @FXML
    public JFXListView<Bean> listView;
    @Autowired
    ISqlService sqlService;
    @Autowired
    IJsonTransferService jsonTransfer;

    public void transferToJson() {
        rightPane.setVisible(true);
        transferBtn.setText("转化JSON");
        jwtPane.setVisible(false);
        type = 0;
    }

    public void formatSql() {
        rightPane.setVisible(true);
        jwtPane.setVisible(false);
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
        } catch (Exception e) {
            this.rightPane.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.listView.setItems(FXCollections.observableArrayList());
//        jwtPane.setVisible(false);
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    this.originArea.setText(t1.getOrgText());
                    this.transferArea.setText(t1.getTransferText());
                    this.type = t1.getType();
                }
        );
    }

    public void toJwtBtn(){
        Demo.showView(JwtView.class,Modality.NONE);
    }


    /**
     * 格式化json串
     */
    public void transfer() {
        try {
            String text = originArea.getText();
            String transferValue;
            if (this.type == 1) {

                if (StringUtils.isEmpty(text)) {
                    transferArea.setText("");
                    return;
                }
                transferValue = sqlService.transfer(text);
            } else {
                if (StringUtils.isEmpty(text)) {
                    transferArea.setText("");
                    return;
                }
                transferValue = jsonTransfer.transfer(text);
            }

            transferArea.setText(transferValue);
            this.listView.getItems().add(new Bean(text, this.type, new Date(), transferValue));
            this.listView.getItems().sort(Bean::compareTo);

        } catch (Exception e) {
            e.printStackTrace();
            transferArea.setText(e.getMessage());
        }
    }

}
