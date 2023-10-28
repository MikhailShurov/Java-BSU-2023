package by.MikhailShurov.quizer.tasks;

import by.MikhailShurov.quizer.Result;
import by.MikhailShurov.quizer.Task;
import by.MikhailShurov.quizer.task_generators.PoolTaskGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Задание с заранее заготовленным текстом.
 * Можно использовать {@link PoolTaskGenerator}, чтобы задавать задания такого типа.
 */
public class TextTask implements Task {
    /**
     * @param text   текст задания
     * @param answer ответ на задание
     */
    String text;
    String answer;

    public TextTask(
            String text,
            String answer
    ) {
        this.text = text;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task other = (Task) obj;
        return this.text != null ? this.text.equals(other.getText()) : other.getText() == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getAnswer() {
        return this.answer;
    }

    @Override
    public Result validate(String answer) {
        String task = this.text;
        boolean checkAnswerByCalculating = true; // true if 4/2=?
        Map<String, Integer> operations = new HashMap<String, Integer>() {{
            put("/", 1);
            put("*", 1);
            put("+", 1);
            put("-", 1);
        }};

        if (task.contains("x")) {
            checkAnswerByCalculating = false;
            task = task.replace("x", String.valueOf(answer));
        } else {
            task = task.replace("?", String.valueOf(answer));
        }

        String firstNum = "";
        String secondNum = "";
        String thirdNum = "";

        boolean isSecond = false;
        boolean isThird = false;
        for (int i = 0; i < task.length(); ++i) {
            if (task.charAt(i) == '=') {
                isThird = true;
                continue;
            }
            if (operations.get(String.valueOf(task.charAt(i))) != null && i != 0 && !isSecond) {
                isSecond = true;
            } else if (!isSecond && !isThird) {
                firstNum += task.charAt(i);
            } else if (isSecond && !isThird) {
                secondNum += task.charAt(i);
            } else if (isThird) {
                thirdNum += task.charAt(i);
            }
        }

//        System.out.println(firstNum + " " + secondNum + " " + thirdNum);

        int solution = checkAnswerByCalculating(Integer.parseInt(firstNum), Integer.parseInt(secondNum), task);
        if (solution == Integer.parseInt(thirdNum)) {
            return Result.OK;
        }

        return Result.WRONG;
    }

    private int checkAnswerByCalculating(int firstnum, int secondNum, String task) {
        if (task.contains("*")) {
            return firstnum*secondNum;
        } else if (task.contains("/")) {
            return firstnum/secondNum;
        } else if (task.contains("+")) {
            return firstnum + secondNum;
        } else {
            return firstnum - secondNum;
        }
    }
}
