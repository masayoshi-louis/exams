import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) {
		// Node example = new Node("Fruit", 1);
		// Node apple = new Node("apple", 2);
		// example.children.add(apple);
		// apple.children.add(new Node("shape", 3));
		// apple.children.add(new Node("color", 3));
		// System.out.println(example);
		// process("Fruit{apple{shape,color},orange{taste,price}}", "Fruit");
		Scanner in = new Scanner(System.in);
		try {
			while (in.hasNext()) {
				String data = in.nextLine();
				String query = in.nextLine();
				process(data, query);
				System.gc();
			}
		} finally {
			in.close();
		}
	}

	private static void process(String data, String query) {
		Node parsed = parse(data);
		// System.out.println(parsed);
		TreeSet<Integer> out = new TreeSet<Integer>();
		query(parsed, query, out);
		if (out.isEmpty())
			System.out.println("-1");
		else {
			StringBuilder sb = new StringBuilder();
			for (Integer i : out)
				sb.append(i).append(',');
			sb.deleteCharAt(sb.length() - 1);
			System.out.println(sb);
		}
	}

	private static Node parse(String data) {
		final StringBuilder symbolBuilder = new StringBuilder();
		int currentDepth = 1;
		final Node root = new Node();
		Node currentNode = root;
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			// System.out.println(c);
			if (c == '{') {
				currentNode.symbol = symbolBuilder.toString();
				symbolBuilder.setLength(0);
				Node child = new Node();
				child.depth = ++currentDepth;
				child.parent = currentNode;
				currentNode.children.add(child);
				currentNode = child;
			} else if (c == '}') {
				if (symbolBuilder.length() > 0) {
					currentNode.symbol = symbolBuilder.toString();
					symbolBuilder.setLength(0);
				}
				if (currentNode.symbol == null) {
					currentNode.parent.children.remove(currentNode);
				}
				currentNode = currentNode.parent;
				--currentDepth;
			} else if (c == ',') {
				if (symbolBuilder.length() > 0) {
					currentNode.symbol = symbolBuilder.toString();
					symbolBuilder.setLength(0);
				}
				Node newNode = new Node();
				currentNode.parent.children.add(newNode);
				newNode.parent = currentNode.parent;
				newNode.depth = currentDepth;
				currentNode = newNode;
			} else {
				symbolBuilder.append(c);
			}
		}
		if (symbolBuilder.length() > 0)
			root.symbol = symbolBuilder.toString();
		return root;
	}

	private static void query(Node root, String query, TreeSet<Integer> out) {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node n = q.poll();
			if (query.equals(n.symbol)) {
				out.add(n.depth);
			}
			for (Node c : n.children) {
				q.add(c);
			}
		}
	}

	private static class Node {
		public int depth = 1;
		public String symbol;
		public Node parent;
		public List<Node> children = new LinkedList<Node>();

		// public Node(String s, int d) {
		// this.symbol = s;
		// this.depth = d;
		// }
		//
		// public Node() {
		// }

		public String toString() {
			StringBuilder sb = new StringBuilder(symbol);
			if (!children.isEmpty()) {
				sb.append('{');
				Iterator<Node> it = children.iterator();
				do {
					Node n = it.next();
					sb.append(n.toString());
					if (it.hasNext())
						sb.append(',');
				} while (it.hasNext());
				sb.append('}');
			}
			return sb.toString();
		}
	}

}
