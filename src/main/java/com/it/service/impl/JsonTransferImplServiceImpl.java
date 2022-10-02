package com.it.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.it.service.IJsonTransferService;
import org.springframework.stereotype.Service;

@Service
public class JsonTransferImplServiceImpl implements IJsonTransferService {
    @Override
    public String transfer(String originText) {
        JsonParser jsonParser = new JsonParser();
        Object o;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (originText.charAt(0) != '[') {
            o = jsonParser.parse(originText).getAsJsonObject();
        } else {
            o = jsonParser.parse(originText).getAsJsonArray();
        }
        return gson.toJson(o);
    }
}
