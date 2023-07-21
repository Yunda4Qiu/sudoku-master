import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Heuristic {

    /**
     * Minimum Remaining Values (MRV) heuristic.
     * It prioritizes the variables (fields) with the smallest domain size.
     *
     * @param queue The priority queue of arcs to process
     */
    public static void minimumRemainingValues(Queue<Field> queue) {
        List<Field> tempList = new ArrayList<>(queue);
        tempList.sort(Comparator.comparingInt(Field::getDomainSize));
        queue.clear();
        queue.addAll(tempList);
    }

    /**
     * Priority to constraints that have arcs to finalized fields heuristic.
     * It prioritizes the arcs that involve a variable (field) 
     * that already has a finalized (assigned) value.
     *
     * @param queue The priority queue of arcs to process
     */
    public static void priorityToFinalizedFields(Queue<Field> queue) {
        Set<Field> finalizedFields = new HashSet<>();
        Queue<Field> newQueue = new ArrayDeque<>();

        while (!queue.isEmpty()) {
            Field field = queue.poll();
            if (field.getDomainSize() == 1 && field.getValue() == 0) {
                finalizedFields.add(field);
            } else {
                newQueue.add(field);
            }
        }

        for (Field field : finalizedFields) {
            newQueue.add(field);
        }

        queue.clear();
        queue.addAll(newQueue);
    }

    public static void applyHeuristic(Queue<Field> queue, String heuristic) {
        switch (heuristic) {
            case "minimumRemainingValues":
                minimumRemainingValues(queue);
                break;
            case "priorityToFinalizedFields":
                priorityToFinalizedFields(queue);
                break;
        }
    }
}
