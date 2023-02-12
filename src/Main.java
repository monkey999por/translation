import monkey999.tools.Setting;
import thread.ClipBoardObservers;
import thread.StandardInObserver;
import tools.cmd.InnerCommands;

/**
 * see sequence.drawio
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Setting.init(args[0]);
        welcomePrint();

        // Monitor the clipboard and call the translator (Translation Worker)
        // if there is a chane and execute it in another thread.
        ClipBoardObservers.getInstance().start();

        // Monitor the standard in.
        // if there is input and execute process in another thread.
        new Thread(new StandardInObserver()).start();

    }

    /**
     * write welcome massage.
     */
    private static void welcomePrint() {
        System.out.println("------------------- Hello Translation -------------------");
        System.out.println("@see: https://github.com/monkey999por/translation.git");
        System.out.println("@see: https://github.com/monkey999por/language-detection.git");
        System.out.println();
        System.out.println("■ Setting: " + Setting.getFilePath());
        System.out.println(Setting.getAllToString());
        System.out.println();
        System.out.println("■■■内部コマンド一覧■■■");
        InnerCommands.getNameAll().forEach(c -> System.out.println("・" + c.getCommand() + " : " + c.getDescription()));
        System.out.println();
        System.out.println("■■■機能■■■");
        System.out.println("  ・クリップボードを監視し、変更があった場合は翻訳します。");
        System.out.println("  ・コンソールに入力した文字を翻訳します。");
        System.out.println();
    }
}
