package com.it.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.it.util.SqlUtils;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import org.apache.commons.lang3.StringUtils;

@FXMLController
public class FormatSqlController {

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

            String[] sql = text.split("\n");
            String s = SqlUtils.parseSql(sql[0], sql[1]);

            transferArea.setText(s);
        } catch (Exception e) {
            transferArea.setText(e.getMessage());
        }
    }
}
