package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcClientTestClientStreaming {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1997)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub stub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("mad")
                .setCurrencyTo("usd")
                .setAmount(4800)
                .build();

        StreamObserver<Bank.ConvertCurrencyRequest> performStream = stub.performStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("*******************");
                System.out.println(convertCurrencyResponse);
                System.out.println("*******************");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("the end");
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest currencyRequest = Bank.ConvertCurrencyRequest.newBuilder()
                        .setAmount(Math.random() * 1000)
                        .build();
                performStream.onNext(currencyRequest);
                System.out.println("=> counter = " + counter);
                ++counter;
                if(counter == 5){
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
        System.in.read();
    }
}
