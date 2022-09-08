package com.rosey.cloud;

import com.google.common.collect.Lists;
import com.rosey.cloud.grpc.user.dto.AddUserRequest;
import com.rosey.cloud.grpc.user.dto.SearchUserRequest;
import com.rosey.cloud.grpc.user.dto.UserResponse;
import com.rosey.cloud.grpc.user.service.UserGrpc;
import io.grpc.StatusRuntimeException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: RoseyGrpcClientApplication <br/>
 * Description: <br/>
 * date: 2022/9/8 5:19 下午<br/>
 *
 * @author tooru<br />
 */
@Slf4j
@RestController
@SpringBootApplication
public class RoseyGrpcClientApplication {

    //注入阻塞的stub
    @GrpcClient("userClient")
    private UserGrpc.UserBlockingStub blockingStub;

    @RequestMapping(value = "add")
    public Integer add() {
        AddUserRequest userRequest = AddUserRequest.newBuilder()
                .setAddress("长沙")
                .setAge(28)
                .setName("tooru").build();
        UserResponse response;
        try {
            response = blockingStub.add(userRequest);
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: " + e.getMessage(), e);
            throw e;
        }
        log.info("response:{}", response);
        return response.getId();
    }

    @RequestMapping(value = "list")
    public List<UserVo> list() {
        SearchUserRequest request = SearchUserRequest.newBuilder().build();
        Iterator<UserResponse> response;
        try {
            response = blockingStub.list(request);
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: " + e.getMessage(), e);
            throw e;
        }

        ArrayList<UserResponse> userResponses = Lists.newArrayList(response);
        return userResponses.stream().map(res -> {
            UserVo userVo = new UserVo();
            userVo.setId(res.getId());
            userVo.setName(res.getName());
            userVo.setAge(res.getAge());
            userVo.setAddress(res.getAddress());
            return userVo;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        SpringApplication.run(RoseyGrpcClientApplication.class, args);
    }

    @Data
    public class UserVo {

        private Integer id;

        private String name;

        private Integer age;

        private String address;

    }


}
