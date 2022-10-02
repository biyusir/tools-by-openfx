package com.it.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.it.bean.JWTBean;
import com.it.service.IJwtService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class JwtServiceImpl implements IJwtService {

    @Override
    public String create(String sign, Map<String, Object> claims,String currentSign) {
        JWTCreator.Builder builder = JWT.create();
        builder.withPayload(claims);
        //"HS512", "HS256"
        if ("HS512".equals(currentSign)){
            return builder.sign(Algorithm.HMAC512(sign));
        }else {
            return builder.sign(Algorithm.HMAC256(sign));
        }
    }

    @Override
    public JWTBean decode(String token) {
        DecodedJWT decode = JWT.decode(token);
        String signature = decode.getAlgorithm();
        String type = decode.getType();

        JWTBean jwtBean = new JWTBean();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode1 = decoder.decode(decode.getPayload());
        String string = new String(decode1);
//        Map<String, String> head = new HashMap<>();
//        head.put("signature", signature);
//        head.put("type", type);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        JsonParser jsonParser = new JsonParser();
//
//        JsonObject o = jsonParser.parse(string).getAsJsonObject();

        jwtBean.setPayLoad(string);
        jwtBean.setSignature(signature);
        jwtBean.setType(type);
        return jwtBean;
    }
}
