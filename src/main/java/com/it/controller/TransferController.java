package com.it.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import org.apache.commons.lang3.StringUtils;

@FXMLController
public class TransferController {


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

            if (StringUtils.isEmpty(text)) {
                transferArea.setText("");
                return;
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject asJsonObject = jsonParser.parse(text).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            transferArea.setText(gson.toJson(asJsonObject));
        } catch (Exception e) {
            transferArea.setText(e.getMessage());
        }
    }
}
