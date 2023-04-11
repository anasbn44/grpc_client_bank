package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

public class BankGrpcClientTestUnary {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1997)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("mad")
                .setCurrencyTo("usd")
                .setAmount(4800)
                .build();

        Bank.ConvertCurrencyResponse response = blockingStub.convert(request);
        System.out.println(response);
    }
}
