package com.p16021.ptixiaki.erotimatologio.controllers;

import com.p16021.ptixiaki.erotimatologio.models.AppResponse;
import com.p16021.ptixiaki.erotimatologio.repos.IdentifierRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.IdentifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/identifier")
@RequiredArgsConstructor
@Slf4j
public class IdentifierController {

    private final IdentifierRepo identifierService;

    @GetMapping("/all")
    public ResponseEntity<AppResponse> getAll(){

        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(generateMap("identifiers" , identifierService.findAll()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.toString());
            return error(401 ,null,null,null);
        }

    }

    private ResponseEntity<AppResponse> error(int status, @Nullable String developerMessage, @Nullable String reason, @Nullable String message){

        AppResponse errorResponse = AppResponse.builder()
                .timeStamp(LocalDateTime.now())
                .reason(reason)
                .developerMessage(developerMessage)
                .message(message)
                .status(HttpStatus.valueOf(status))
                .statusCode(status)
                .build();

        return new ResponseEntity<>(errorResponse,HttpStatus.valueOf(status));

    }
    private <K,V> HashMap<K,V> generateMap(K key, V value){
        return new HashMap<K,V>(){{
            put(key, value);
        }};
    }

}
