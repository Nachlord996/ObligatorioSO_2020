package org.ucu.fit.so;

import javax.swing.*;

public abstract class Gate extends Thread {

    protected String uuid;

    public boolean hasWorked() {
        return hasWorked;
    }

    public void setHasWorked(boolean value) {
        this.hasWorked = value;
    }

    protected boolean hasWorked;

    Gate(int gateNumber){
        this.uuid = "G" + gateNumber;
        this.hasWorked = false;
    }

    @Override
    public void run(){
        int i = 3;
        while(i > 0){
            try {
                Program.PROCESS_MANAGER.takeTurn(this.uuid);
                this.consume();
                Program.PROCESS_MANAGER.reportFinish(this.uuid);
                Program.PROCESS_MANAGER.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;
        }
    }

    abstract void consume();
}
