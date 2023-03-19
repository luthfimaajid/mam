package com.etspplbo50.employeeservice.service;

import com.etspplbo50.employeeservicegrpc.GetAvailableEmployeeRequest;
import com.etspplbo50.employeeservicegrpc.GetAvailableEmployeeResponse;
import com.etspplbo50.inventoryservicegrpc.CheckStockRequest;
import com.etspplbo50.inventoryservicegrpc.CheckStockResponse;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@AllArgsConstructor
@Slf4j
public class EmployeeServiceGrpc extends com.etspplbo50.employeeservicegrpc.EmployeeServiceGrpc.EmployeeServiceImplBase {
    private final EmployeeService employeeService;

    @Override
    public void getAvailableEmployee(GetAvailableEmployeeRequest request, StreamObserver<GetAvailableEmployeeResponse> responseObserver) {
        String cafeId = request.getCafeId();


        GetAvailableEmployeeResponse response = GetAvailableEmployeeResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
