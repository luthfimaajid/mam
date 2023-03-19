package com.etspplbo50.inventoryservice.service;

import com.etspplbo50.inventoryservice.model.Inventory;
import com.etspplbo50.inventoryservicegrpc.CheckStockRequest;
import com.etspplbo50.inventoryservicegrpc.CheckStockResponse;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@AllArgsConstructor
@Slf4j
public class InventoryServiceGrpc  extends com.etspplbo50.inventoryservicegrpc.InventoryServiceGrpc.InventoryServiceImplBase {
    private final InventoryService inventoryService;
    @Override
    public void checkStock(CheckStockRequest request, StreamObserver<CheckStockResponse> responseObserver) {
        String cafeId = request.getCafeId();
        List<String> menuIdList = request.getMenuIdList();

        Boolean isAvailable = inventoryService.checkStock(cafeId, menuIdList);

        CheckStockResponse response = CheckStockResponse.newBuilder()
                .setIsAvailable(isAvailable)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
