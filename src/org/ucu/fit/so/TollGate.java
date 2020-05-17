package org.ucu.fit.so;

public class TollGate extends Gate {

    TollGate(int gateNumber) {
        super(gateNumber);
    }

    @Override
    TaskReport consume() {
        System.out.println(this.uuid);
        return new TaskReport();
    }

}
