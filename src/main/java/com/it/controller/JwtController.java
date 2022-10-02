package com.it.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.it.ObjectTypeAdapterRewrite;
import com.it.bean.JWTBean;
import com.it.service.IJwtService;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class JwtController implements Initializable {

    @Autowired
    IJwtService jwtService;
    List<String> sings = Arrays.asList("HS512", "HS256");

    @FXML
    JFXTextArea jwtArea;
    @FXML
    JFXTextArea jwtHeaderText;
    @FXML
    JFXTextArea jwtPlayloadText;
    // 当前谁在修改数据，如果jwtPane修改数据，不允许某个修改
    private static Byte type = 1;
    private String currentSign;
    @FXML
    JFXComboBox<String> signBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jwtArea.setOnMouseClicked(mouseEvent -> type = 1);
        jwtArea.textProperty().addListener((observableValue, old, newValue) -> {
            System.out.println("old===>" + old);
            System.out.println("newValue===>" + newValue);
            if (JwtController.type == 1) {
                try {
                    JWTBean decode = jwtService.decode(newValue);

                    int index = getIndex(decode.getSignature());

                    signBox.getSelectionModel().select(index);
                    log.info("decode===>" + decode);
                    Map<String, String> head = new HashMap<>();
                    head.put("signature", decode.getSignature());
                    currentSign = sings.get(index);
                    head.put("type", decode.getType());
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    JsonParser jsonParser = new JsonParser();

                    JsonObject o = jsonParser.parse(decode.getPayLoad()).getAsJsonObject();
                    String payLoad = gson.toJson(o);
                    String headJSON = gson.toJson(head);
                    jwtHeaderText.setText(headJSON);
                    jwtPlayloadText.setText(payLoad);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signBox.setItems(FXCollections.observableArrayList(sings));
        signBox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            currentSign = observableValue.getValue();
        });
        jwtPlayloadText.setOnMouseClicked(mouseEvent -> JwtController.type = 2);
        jwtPlayloadText.textProperty().addListener((observableValue, s, t1) -> {
            if (JwtController.type == 2) {
                Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<HashMap<String, Object>>() {
                }.getType(), new ObjectTypeAdapterRewrite()).create();
                Map<String, Object> stringObjectMap = gson.fromJson(t1.replaceAll("\n", ""), new TypeToken<HashMap<String, Object>>() {
                }.getType());
                convertDate(stringObjectMap);
                String hello = jwtService.create("hello", stringObjectMap, currentSign);
                jwtArea.setText(hello);
            }
        });
    }

    private int getIndex(String a) {
        for (int i = 0; i < sings.size(); i++) {
            if (StringUtils.equalsAnyIgnoreCase(a, sings.get(i))) {
                return i;
            }
        }
        return 0;
    }

    private void convertDate(Map<String, Object> map) {
        Object iat = map.get("iat");
        if (iat != null) {
            Date date = new Date(Long.parseLong(iat.toString()) * 1000);
            map.put("iat", date);
        }
    }
}
