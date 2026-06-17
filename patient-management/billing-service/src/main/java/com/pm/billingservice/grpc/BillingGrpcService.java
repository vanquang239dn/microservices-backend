package com.pm.billingservice.grpc;

import org.springframework.grpc.server.service.GrpcService;

import com.pm.billingservice.grpc.BillingServiceGrpc.BillingServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@GrpcService
@Slf4j(topic = "BILLING-GRPC-SERVICE")
public class BillingGrpcService extends BillingServiceImplBase {

    @Override
    public void createBillingAccount(BillingRequest req, StreamObserver<BillingResponse> resObs) {

        // Received billing account received
        log.info("Create billing account request received with id = {}", req.getPatientId());

        // TODO : implement business logic

        BillingResponse res = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        // Send a response from gRPC service back to client
        resObs.onNext(res);

        // The response is completed then end the cycle in this response
        resObs.onCompleted();
    }

}
