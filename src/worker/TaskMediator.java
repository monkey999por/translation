package worker;

/**
 * クリップボード監視や標準入力監視のスレッド等からの入力から呼ばれ、タスクを分散する
 */
public class TaskMediator {
    public static void order(TaskType taskType, Object... args){
        switch (taskType){
            case TRANSLATE:
                TranslationWorker.run((String) args[0]);
                break;
            case COMMAND:
                CommandExcuter.run((String) args[0]);
                break;
        }
    }
}
