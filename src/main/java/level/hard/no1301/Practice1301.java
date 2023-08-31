package level.hard.no1301;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 题目信息：
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
 * <p>
 * 解法分析：
 * 边界：     f(x_max, y_max) = 1;
 * 转移方程：  f(x, y) = max(f(x, y+1), f(x+1, y), f(x+1, y+1)) + s(x, y)
 * 最优子结构：f(x, y+1),f(x+1, y),f(x+1, y+1)
 */
public class Practice1301 {

    private static final int divisor = (int) Math.pow(10, 9) + 7;

    /**
     * @param x
     * @param y
     * @param points
     * @return array[0] = score, array[1] = solution
     */
    private static int[] calculateCurrent(int x, int y, Point[][] points) {

        if (x + 1 < points.length && y + 1 < points.length) {
            Point left = points[x + 1][y];
            Point pointBellow = points[x][y + 1];
            if (left.solutionCount > 0 || pointBellow.solutionCount > 0) {
                if (left.score > pointBellow.score) {
                    return new int[]{(int) left.score, (int) left.solutionCount};
                } else if (left.score < pointBellow.score) {
                    return new int[]{(int) pointBellow.score, (int) pointBellow.solutionCount};
                } else {
                    return new int[]{(int) left.score, (int) ((left.solutionCount + pointBellow.solutionCount) % divisor)};
                }
            }
            Point pointLeftAndBellow = points[x + 1][y + 1];
            return new int[]{(int) pointLeftAndBellow.score, (int) pointLeftAndBellow.solutionCount};
        } else if (x + 1 < points.length) {
            Point left = points[x + 1][y];
            return new int[]{(int) left.score, (int) left.solutionCount};
        } else {
            Point pointBellow = points[x][y + 1];
            return new int[]{(int) pointBellow.score, (int) pointBellow.solutionCount};
        }
    }

    public static int[] pathsWithMaxScore(List<String> board) {
        char[][] boardChar = new char[board.size()][board.size()];
        for (int i = 0; i < boardChar.length; i++) {
            boardChar[i] = board.get(i).toCharArray();
        }
        Map<Character, Integer> scoreMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            scoreMap.put((char) (i + '1'), i + 1);
        }
        int size = board.size();
        Point[][] dp = new Point[size][size];

        // 边界值

        for (int x = size - 1; x >= 0; x--) {
            for (int y = size - 1; y >= 0; y--) {
                if (boardChar[x][y] == 'X') {
                    dp[x][y] = new Point(0, 0);
                } else if (boardChar[x][y] == 'S') {
                    dp[x][y] = new Point(1, 0);
                } else if (boardChar[x][y] == 'E') {
                    int[] preOutput = calculateCurrent(x, y, dp);
                    dp[x][y] = new Point(preOutput[1], preOutput[0]);
                } else {
                    // 数字
                    int[] preOutput = calculateCurrent(x, y, dp);
                    if (preOutput[1] == 0) {
                        dp[x][y] = new Point(0, 0);
                    } else {
                        dp[x][y] = new Point(preOutput[1], preOutput[0] + scoreMap.get(boardChar[x][y]));
                    }
                }
            }
        }
        if (dp[0][0].solutionCount < 0) {
            return new int[]{0, 0};
        }
        long s = dp[0][0].score % divisor;
        long sol = dp[0][0].solutionCount % divisor;
        return new int[]{(int) s, (int) sol};
    }


    static class Point {

        private long solutionCount;
        private long score;

        public Point(long solutionCount, long score) {
            this.solutionCount = solutionCount;
            this.score = score;
        }

    }

    public static void main(String[] args) {
        List<String> board = Arrays.asList("E23", "2X2", "12S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
        board = Arrays.asList("E12", "1X1", "21S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
        board = Arrays.asList("E11", "XXX", "11S");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
        board = Arrays.asList("EX", "XS");
        System.out.println(Arrays.toString(pathsWithMaxScore(board)));
    }
}
