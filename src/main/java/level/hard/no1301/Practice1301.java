package practice;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 1301. 最大得分的路径数目
 * 困难
 * 给你一个正方形字符数组 board ，你从数组最右下方的字符 'S' 出发。
 * 你的目标是到达数组最左上角的字符 'E' ，数组剩余的部分为数字字符 1, 2, ..., 9 或者障碍 'X'。在每一步移动中，你可以向上、向左或者左上方移动，可以移动的前提是到达的格子没有障碍。
 * 一条路径的 「得分」 定义为：路径上所有数字的和。
 * 请你返回一个列表，包含两个整数：第一个整数是 「得分」 的最大值，第二个整数是得到最大得分的方案数，请把结果对 10^9 + 7 取余。
 * <p>
 * 如果没有任何路径可以到达终点，请返回 [0, 0] 。
 * <p>
 * 示例 1：
 * 输入：board = ["E23","2X2","12S"]
 * 输出：[7,1]
 * <p>
 * 示例 2：
 * 输入：board = ["E12","1X1","21S"]
 * 输出：[4,2]
 * <p>
 * 示例 3：
 * 输入：board = ["E11","XXX","11S"]
 * 输出：[0,0]
 * <p>
 * 提示：
 * 2 <= board.length == board[i].length <= 100
 */
public class Practice1301 {

    private static final int divisor = (int) Math.pow(10, 9) + 7;
    private static final int[][] direction = {{-1, 0}, {0, -1}, {-1, -1}};
    private static BlockingQueue<Point> priorityQueue = new PriorityBlockingQueue<>(100, Comparator.comparingLong(Point::getTotalScore).reversed());

    public static int[] pathsWithMaxScore(List<String> board) throws InterruptedException {
        int size = board.size();
        String[][] arr = parseBoard(board);
        Point currentPoint = new Point(size - 1, size - 1);
        currentPoint.setTotalScore(0);
        priorityQueue.add(currentPoint);

        calculateMaxScoreByStep(arr, currentPoint);

        long maxScore = 0;
        int maxScorePlans = 0;
        Point maxScorePoint = null;
        while ((maxScorePoint = priorityQueue.poll()) != null) {
            if (maxScorePoint.exited && maxScore == 0) {
                maxScore = maxScorePoint.getTotalScore() - 100;
                maxScorePlans++;
                continue;
            }
            if (maxScorePoint.exited && (maxScorePoint.getTotalScore() - 100) == maxScore) {
                maxScorePlans++;
            }
        }

        return new int[]{(int) (maxScore) % divisor, maxScorePlans};
    }

    private static void calculateMaxScoreByStep(String[][] arr, Point currentPoint) throws InterruptedException {
        for (int[] itemStep : direction) {
            int x = currentPoint.x + itemStep[0];
            int y = currentPoint.y + itemStep[1];
            if (x < 0 || y < 0 || "X".equalsIgnoreCase(arr[x][y])) {
                continue;
            }
            if ("E".equalsIgnoreCase(arr[x][y])) {
                long oldTotalScore = currentPoint.getTotalScore();
                Point newPoint = new Point(x, y);
                newPoint.setTotalScore(oldTotalScore + 100);
                newPoint.exited = true;
                priorityQueue.put(newPoint);
                continue;
            }
            long oldTotalScore = currentPoint.getTotalScore();
            Point newPoint = new Point(x, y);
            newPoint.setTotalScore(oldTotalScore + Integer.parseInt(arr[x][y]));
            priorityQueue.put(newPoint);
            calculateMaxScoreByStep(arr, newPoint);
        }
    }

    public static String[][] parseBoard(List<String> board) {
        int size = board.size();
        String[][] arr = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr[i][j] = board.get(i).charAt(j) + "";
            }
        }
        return arr;
    }

    static class Point {
        private int x;
        private int y;
        private boolean exited = false;
        private long totalScore;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public long getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(long score) {
            this.totalScore = score;
        }

        public boolean getExited() {
            return exited;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> board = Arrays.asList("E23", "2X2", "12S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
        priorityQueue.clear();
        board = Arrays.asList("E12", "1X1", "21S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
        priorityQueue.clear();
        board = Arrays.asList("E11", "XXX", "11S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
    }
}
