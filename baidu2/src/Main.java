import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static int N, M, K;
	private static boolean[][] MATRIX;
	private static List<Path> pathes = new LinkedList<>();

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[] params = in.nextLine().split(" ");
		N = Integer.parseInt(params[0]);
		M = Integer.parseInt(params[1]);
		K = Integer.parseInt(params[2]);
		MATRIX = new boolean[N][M];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < M; j++)
				MATRIX[i][j] = false;
		for (int k = 0; k < K; k++) {
			String[] coor = in.nextLine().split(" ");
			MATRIX[Integer.parseInt(coor[0]) - 1][Integer.parseInt(coor[1]) - 1] = true;
		}
		// printMatrix();
		in.close();
		buildPathes(0, 0, new Path());
		// System.out.println(pathes);
		int safePathes = 0;
		for (Path p : pathes) {
			if (p.isSafe())
				safePathes++;
		}
		// System.out.println(safePathes);
		System.out.println(String.format("%.6f",
				(double) safePathes / pathes.size()));
	}

	private static void buildPathes(int i, int j, Path p) {
		p.path.add(new Node(i, j));
		if (i == N - 1 && j == M - 1)
			pathes.add(p);
		else {
			if (i < N - 1)
				buildPathes(i + 1, j, new Path(p));
			if (j < M - 1)
				buildPathes(i, j + 1, new Path(p));
		}
	}

	public static void printMatrix() {
		System.out.println("Matrix: ");
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(MATRIX[i]));
		}
	}

	public static class Node {
		public final int x, y;

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public boolean isSafe() {
			return !MATRIX[x][y];
		}

		public String toString() {
			return String.format("(%d,%d)", x, y);
		}

	}

	public static class Path {
		public List<Node> path;

		public Path() {
			path = new LinkedList<Node>();
		}

		public Path(Path other) {
			path = new LinkedList<Node>(other.path);
		}

		public boolean isSafe() {
			for (Node n : path) {
				if (!n.isSafe())
					return false;
			}
			return true;
		}

		public String toString() {
			return path.toString();
		}

	}

}
