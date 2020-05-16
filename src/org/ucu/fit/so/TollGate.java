package org.ucu.fit.so;

public class TollGate extends Gate {

    TollGate(int gateNumber) {
        super(gateNumber);
    }

    @Override
    void consume() {
        System.out.println(this.uuid);
    }

}
