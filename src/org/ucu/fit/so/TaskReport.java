package org.ucu.fit.so;

import java.util.LinkedList;

public class TaskReport {

    private final LinkedList<Task> completedTasks = new LinkedList<>();

    public void setInstant(int time) throws IllegalArgumentException {
        for(Task t : completedTasks){
            t.setInstant(time);
        }
    }

    public void addTask(Task task){
        completedTasks.add(task);
    }

    /*public void clearReport(){
        completedTasks.clear();
    }*/

    public LinkedList<String> getReportLines(){
        LinkedList<String> report = new LinkedList<>();
        for (Task task : completedTasks){
            String message = task.getReportMessage();
            if (message != null){
                report.add(message);
            }
        }
        return report;
    }
}
