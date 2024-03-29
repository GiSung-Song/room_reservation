package com.study.reservation.admin.service;

import com.study.reservation.admin.dto.AdminSignUpDto;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BusinessValidateService {

    private final String BUSINESS_MAN_BASE_URL = "http://api.odcloud.kr";
    private final String BUSINESS_MAN_PATH = "/api/nts-businessman/v1/validate";
    private final String SERVICE_KEY = "serviceKey";
    private final String RETURN_TYPE = "returnType";

    @Value("${open.api.business.decoding}")
    private String serviceKey;

    public boolean getValidate(AdminSignUpDto adminSignUpDto) {
        URI uri = UriComponentsBuilder
                .fromUriString(BUSINESS_MAN_BASE_URL)
                .path(BUSINESS_MAN_PATH)
                .queryParam(SERVICE_KEY, serviceKey)
                .queryParam(RETURN_TYPE, "JSON")
                .build()
                .toUri();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        //Body
        Map<String, Object> jsonMap = makeMap(adminSignUpDto);
        List<Map<String, Object>> jsonList = new ArrayList<>();
        jsonList.add(jsonMap);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businesses", jsonList);

        RequestBody requestBody = RequestBody.create(jsonObject.toString(), mediaType);

        try {
            //Request
            Request request = new Request.Builder()
                    .url(uri.toURL())
                    .method("POST", requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            //Response
            Response response = client.newCall(request).execute();

            //Response Body -> json list -> json -> map -> valid 값 가져오기
            String responseBody = response.body().string();

            JSONObject jsonData = new JSONObject(responseBody);
            JSONArray jsonArray = (JSONArray) jsonData.get("data");
            JSONObject resultJson = (JSONObject) jsonArray.get(0);

            String valid = (String) resultJson.get("valid");

            if (StringUtils.hasText(valid) && valid.equals("01")) {
                JSONObject status = (JSONObject) resultJson.get("status");
                String closeYn = (String) status.get("b_stt_cd");

                if (StringUtils.hasText(closeYn) && !closeYn.equals("01")) {
                    throw new CustomException(ErrorCode.CLOSE_COMPANY_NUMBER);
                }

                return true;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.OPEN_API_INTERNAL_ERROR);
        }

        return false;
    }

    private Map<String, Object> makeMap(AdminSignUpDto dto) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("b_no", dto.getCompanyNumber());
        map.put("start_dt", dto.getOpenDate());
        map.put("p_nm", dto.getOwner());
        map.put("p_nm2", "");
        map.put("b_nm", "");
        map.put("corp_no", "");
        map.put("b_sector", "");
        map.put("b_type", "");
        map.put("b_adr", "");

        return map;
    }

}
