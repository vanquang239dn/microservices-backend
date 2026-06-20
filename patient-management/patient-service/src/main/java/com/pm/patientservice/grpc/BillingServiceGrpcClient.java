package com.pm.patientservice.grpc;

import com.pm.billingservice.grpc.BillingRequest;
import com.pm.billingservice.grpc.BillingResponse;
import com.pm.billingservice.grpc.BillingServiceGrpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "PATIENT-GRPC-SERVICE")
public class BillingServiceGrpcClient {

    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // Constructor
    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort) {

        log.info("Connecting to billing service gRPC service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {

        BillingRequest req = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse res = blockingStub.createBillingAccount(req);

        log.info("Received response from billing service via gRPC with patient id : {} ", res.getAccountId());

        return res;
    }
}
