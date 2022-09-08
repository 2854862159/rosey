package com.rosey.cloud.grpc.user;

import com.rosey.cloud.entity.User;
import com.rosey.cloud.grpc.user.dto.AddUserRequest;
import com.rosey.cloud.grpc.user.dto.SearchUserRequest;
import com.rosey.cloud.grpc.user.dto.UserResponse;
import com.rosey.cloud.grpc.user.service.UserGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: UserGrpcImpl <br/>
 * Description: <br/>
 * date: 2022/9/8 5:05 下午<br/>
 *
 * @author tooru<br />
 */
@GrpcService
@Slf4j
public class UserGrpcImpl extends UserGrpc.UserImplBase {

    private final List<User> users = new ArrayList<>();

    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public void list(SearchUserRequest request, StreamObserver<UserResponse> responseObserver) {
        users.forEach(c -> {
            UserResponse ur = UserResponse.newBuilder().setAddress(c.getAddress()).setAge(c.getAge()).setId(c.getId())
                    .setName(c.getName()).build();
            responseObserver.onNext(ur);
        });
        responseObserver.onCompleted();
    }

    @Override
    public void add(AddUserRequest request, StreamObserver<UserResponse> responseObserver) {
        log.info("start add");

        User user = new User();
        user.setId(id.incrementAndGet());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setAddress(request.getAddress());
        users.add(user);

        //构造rpc响应参数
        UserResponse reply = UserResponse.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setAge(user.getAge())
                .setAddress(user.getAddress()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();

        log.info("end add");
    }
}
