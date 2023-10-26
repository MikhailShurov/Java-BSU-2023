package by.MikhailShurov.quizer.task_generators;

import by.MikhailShurov.quizer.Task;
import by.MikhailShurov.quizer.TaskGenerator;
import by.MikhailShurov.quizer.tasks.TextTask;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class EquationTaskGenerator implements TaskGenerator {
    ArrayList<Character> operationsList = new ArrayList<>();
    int minNumber;
    int maxNumber;


    /**
     * @param minNumber              минимальное число
     * @param maxNumber              максимальное число
     * @param generateSum            разрешить генерацию с оператором +
     * @param generateDifference     разрешить генерацию с оператором -
     * @param generateMultiplication разрешить генерацию с оператором *
     * @param generateDivision       разрешить генерацию с оператором /
     */
    public EquationTaskGenerator(
            int minNumber,
            int maxNumber,
            boolean generateSum,
            boolean generateDifference,
            boolean generateMultiplication,
            boolean generateDivision
    ) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        if (generateSum) {
            operationsList.add('+');
        }
        if (generateDifference) {
            operationsList.add('-');
        }
        if (generateMultiplication) {
            operationsList.add('*');
        }
        if (generateDivision) {
            operationsList.add('/');
        }
    }

    @Override
    public Task generate() {
        Random random = new Random();

        int firstNumber = random.nextInt(minNumber, maxNumber + 1);
        int secondNumber = random.nextInt(minNumber, maxNumber + 1);
        // swap if secondNumber > firstNumber
        if (secondNumber > firstNumber) {
            firstNumber = firstNumber ^ secondNumber;
            secondNumber = firstNumber ^ secondNumber;
            firstNumber = firstNumber ^ secondNumber;
        }

        int randomNumber = random.nextInt(2);
        boolean xInFirstPosition = (randomNumber != 0);
        char selectedOperation = operationsList.get(random.nextInt(operationsList.size()));
        Task task = null;

        if (selectedOperation == '/' && secondNumber == 0) {
            return generate();
        }

        if (xInFirstPosition) {
            if (selectedOperation == '/') {
                // n
                String taskStr = "x/" + String.valueOf(firstNumber) + "=" + String.valueOf(secondNumber);
                task = new TextTask(taskStr, String.valueOf(firstNumber * secondNumber));
            } else {
                String taskStr = "x" + selectedOperation + secondNumber + "=" + calculateSolution(firstNumber, secondNumber, selectedOperation);
                // x = firstNumber, secondNumber, solution = <firstNumber><operation><secondNumber>
                task = new TextTask(taskStr, String.valueOf(firstNumber));
            }
        } else {
            if (selectedOperation == '/') {
                List<Integer> divisors = getDivisors(firstNumber);
                int randomIndex = random.nextInt(divisors.size());
                int answer = divisors.get(randomIndex);
                String taskStr = String.valueOf(firstNumber) + selectedOperation + "x=" + String.valueOf(answer);
                task = new TextTask(taskStr, String.valueOf(firstNumber / answer));
            } else {
                String taskStr = String.valueOf(firstNumber) + selectedOperation + "x=" + calculateSolution(firstNumber, secondNumber, selectedOperation);
                // firstNumber, x = secondNumber, solution = <firstNumber><operation><secondNumber>
                task = new TextTask(taskStr, String.valueOf(secondNumber));
            }
        }
        return task;
    }

    private int calculateSolution(int firstNumber, int secondNumber, char operation) {
        switch (operation) {
            case '+':
                return firstNumber + secondNumber;
            case '-':
                return firstNumber - secondNumber;
            case '*':
                return firstNumber * secondNumber;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operation);
        }
    }

    private static List<Integer> getDivisors(int number) {
        List<Integer> divisors = new ArrayList<>();
        int absNumber = Math.abs(number); // Получаем модуль числа
        for (int i = 1; i <= absNumber; i++) {
            if (absNumber % i == 0) {
                divisors.add(i);
                if (i != absNumber / i) {
                    divisors.add(-i);
                }
            }
        }
        return divisors;
    }
//    /**   //ToDo implement this function
//     * return задание типа {@link EquationTask}
//     */
//    EquationTask generate() {
//        // ...
//    }
}