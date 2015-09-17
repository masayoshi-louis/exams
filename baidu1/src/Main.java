import java.util.Arrays;
import java.util.Scanner;

public class Main {

	private int M, N, X, Y, T;
	private double[][] MATRIX;

	public void process(String[] params, Scanner in) {
		// System.out.println("Params: \n" + Arrays.toString(params));
		N = Integer.parseInt(params[0]);
		M = Integer.parseInt(params[1]);
		X = Integer.parseInt(params[2]) - 1;
		Y = Integer.parseInt(params[3]) - 1;
		T = Integer.parseInt(params[4]);
		MATRIX = new double[N][M];
		for (int i = 0; i < N; i++) {
			String[] cells = in.nextLine().split(" ");
			for (int j = 0; j < M; j++) {
				MATRIX[i][j] = Double.parseDouble(cells[j]);
			}
		}
		// printMatrix();
		double cc = 1 - Math.pow(1 - MATRIX[X][Y], T);
		double pAvg = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				pAvg += MATRIX[i][j];
			}
		}
		pAvg = pAvg / (N * M);
		double ss = 1 - Math.pow(1 - pAvg, T);
		// System.out.println(String.format("ss=%.4f,cc=%.4f", ss, cc));
		if (ss == cc) {
			System.out.println("equal");
			System.out.println(String.format("%.4f", ss));
		} else if (ss > cc) {
			System.out.println("ss");
			System.out.println(String.format("%.4f", ss));
		} else {
			System.out.println("cc");
			System.out.println(String.format("%.4f", cc));
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (!"".equals(line.trim())) {
				String[] params = line.split(" ");
				Main obj = new Main();
				obj.process(params, in);
			}
		}
		in.close();
	}

	public void printMatrix() {
		System.out.println("Matrix: ");
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(MATRIX[i]));
		}
	}

}
