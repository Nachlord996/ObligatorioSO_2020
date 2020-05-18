package org.ucu.fit.so;

import java.util.LinkedList;

public class TaskReport {

    /**
     * The information about all tasks completed
     */
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

    /**
     * Returns a linked list with strings of al tasks performed in the simulation
     * @return LinkedList of Strings. Each string has data in csv format
     */
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
