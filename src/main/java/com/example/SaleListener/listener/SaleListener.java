/*
 * Copyright © 2023 by Seven System Viet Nam, JSC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 *
 * Write clean code and you can sleep well at night ¯\(ツ)/¯
 *
 * Written by long.vt@7-eleven.vn
 */

package com.example.SaleListener.listener;

import com.example.SaleListener.dto.SaleSummaryDto;
import com.example.SaleListener.service.WebSocketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class SaleListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebSocketService webSocketService;
    @KafkaListener(topics = "sales", groupId = "sale-backend")
    private void listenSales(String kafkaMessage) throws IOException {

        JSONObject message = new JSONObject(kafkaMessage);
        List<SaleSummaryDto> saleSummaryDtos = objectMapper.readValue(message.get("data").toString(), new TypeReference<List<SaleSummaryDto>>() {});
        log.info("Sale summary list size: {}" , saleSummaryDtos.size());
        webSocketService.sendNotification("/topic/sale_topic", saleSummaryDtos);

    }

}
