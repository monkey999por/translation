package worker;

/**
 * assign task.
 */
public class TaskMediator {

    /**
     * assign task.
     *
     * @param taskType task type.
     * @param args     args passed task executor in target task.
     */
    public static void order(TaskType taskType, Object... args) {
        switch (taskType) {
            case TRANSLATE:
                TranslationWorker.run((String) args[0]);
                break;
            case COMMAND:
                CommandExecutor.run((String) args[0]);
                break;
        }
    }
}
