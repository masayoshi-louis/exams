import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) {
		Map<Integer, Channel> channels = new HashMap<>();
		Map<Integer, Player> players = new HashMap<>();
		Map<Integer, List<Prepaid>> prepaids = new HashMap<>();
		loadData(System.in, channels, players, prepaids);
		List<PlayerChannel> playerChannel = new ArrayList<>(players.size());
		for (Player pl : players.values()) {
			Channel c = channels.get(pl.cid);
			int ctype = c == null ? -1 : c.ctype;
			playerChannel.add(new PlayerChannel(pl.pid, ctype));
		}
		channels = null;
		players = null;
		System.gc();
		List<ChannelPrepaid> channelPrepaid = new ArrayList<>(
				playerChannel.size());
		for (PlayerChannel pc : playerChannel) {
			List<Prepaid> prs = prepaids.get(pc.pid);
			if (prs == null) {
				channelPrepaid.add(new ChannelPrepaid(pc.pid, pc.ctype, 0));
			} else {
				for (Prepaid pr : prs) {
					int cash = pr.cash;
					channelPrepaid.add(new ChannelPrepaid(pc.pid, pc.ctype,
							cash));
				}
			}

		}
		playerChannel = null;
		prepaids = null;
		System.gc();
		Map<Integer, List<ChannelPrepaid>> gpByCtype = new HashMap<>();
		for (ChannelPrepaid cp : channelPrepaid) {
			Integer ctype = cp.ctype;
			if (!gpByCtype.containsKey(ctype)) {
				gpByCtype.put(ctype, new ArrayList<ChannelPrepaid>());
			}
			gpByCtype.get(ctype).add(cp);
		}
		channelPrepaid = null;
		System.gc();
		List<Result> results = new ArrayList<>(gpByCtype.size());
		for (Entry<Integer, List<ChannelPrepaid>> gp : gpByCtype.entrySet()) {
			//TODO use HashSet
			TreeSet<Integer> distinctPids = new TreeSet<>();
			int sumCash = 0;
			for (ChannelPrepaid cp : gp.getValue()) {
				sumCash += cp.cash;
				distinctPids.add(cp.pid);
			}
			results.add(new Result(gp.getKey(), distinctPids.size(), sumCash));
		}
		gpByCtype = null;
		System.gc();
		Collections.sort(results, new Comparator<Result>() {

			@Override
			public int compare(Result a, Result b) {
				if (a.cash == b.cash)
					return a.ctype - b.ctype;
				else
					return b.cash - a.cash;
			}

		});
		for (Result r : results) {
			System.out.println(r);
		}
	}

	@SuppressWarnings("unused")
	private static void printTable(@SuppressWarnings("rawtypes") Map table) {
		for (Object obj : table.values()) {
			System.out.println(obj);
		}
	}

	private static void loadData(InputStream is,
			Map<Integer, Channel> channels, Map<Integer, Player> players,
			Map<Integer, List<Prepaid>> prepaids) {
		Scanner in = new Scanner(is);
		int tablesLoaded = 0;
		int itemsInCurrentTab = -1;
		try {
			while (in.hasNext() && tablesLoaded < 3) {
				String[] tabNameAndItems = in.nextLine().split(" ");
				String tabName = tabNameAndItems[0].trim();
				itemsInCurrentTab = Integer.parseInt(tabNameAndItems[1].trim());
				if ("channels".equals(tabName)) {
					loadChannels(in, channels, itemsInCurrentTab);
					tablesLoaded++;
				} else if ("players".equals(tabName)) {
					loadPlayers(in, players, itemsInCurrentTab);
					tablesLoaded++;
				} else if ("prepaids".equals(tabName)) {
					loadPrepaids(in, prepaids, itemsInCurrentTab);
					tablesLoaded++;
				} else {
					throw new AssertionError("Unexpected table name:"
							+ tabNameAndItems[0]);
				}
			}
		} finally {
			in.close();
		}
	}

	private static void loadChannels(Scanner in, Map<Integer, Channel> data,
			int n) {
		in.nextLine();// skip header row
		for (int i = 0; i < n; i++) {
			String[] line = in.nextLine().split(" ");
			int cid = Integer.parseInt(line[0].trim());
			String name = line[1].trim();
			int ctype = Integer.parseInt(line[2].trim());
			Channel item = new Channel(cid, name, ctype);
			data.put(cid, item);
		}
	}

	private static void loadPlayers(Scanner in, Map<Integer, Player> data, int n) {
		in.nextLine();// skip header row
		for (int i = 0; i < n; i++) {
			String[] line = in.nextLine().split(" ");
			int pid = Integer.parseInt(line[0].trim());
			String name = line[1].trim();
			int cid = Integer.parseInt(line[2].trim());
			Player item = new Player(pid, name, cid);
			data.put(pid, item);
		}
	}

	private static void loadPrepaids(Scanner in,
			Map<Integer, List<Prepaid>> data, int n) {
		in.nextLine();// skip header row
		for (int i = 0; i < n; i++) {
			String[] line = in.nextLine().split(" ");
			int id = Integer.parseInt(line[0].trim());
			int pid = Integer.parseInt(line[1].trim());
			int cash = Integer.parseInt(line[2].trim());
			Prepaid item = new Prepaid(id, pid, cash);
			if (!data.containsKey(pid)) {
				data.put(pid, new LinkedList<Prepaid>());
			}
			data.get(pid).add(item);
		}
	}

	private static class Channel {
		public final int cid;
		public final String name;
		public final int ctype;

		public Channel(int cid, String name, int ctype) {
			super();
			this.cid = cid;
			this.name = name;
			this.ctype = ctype;
		}

		public String toString() {
			return cid + " " + name + " " + ctype;
		}

	}

	private static class Prepaid {
		public final int id, pid, cash;

		public Prepaid(int id, int pid, int cash) {
			super();
			this.id = id;
			this.pid = pid;
			this.cash = cash;
		}

		public String toString() {
			return id + " " + pid + " " + cash;
		}

	}

	private static class Player {
		public final int pid, cid;
		public final String name;

		public Player(int pid, String name, int cid) {
			super();
			this.pid = pid;
			this.cid = cid;
			this.name = name;
		}

		public String toString() {
			return pid + " " + name + " " + cid;
		}

	}

	private static class PlayerChannel {
		public final int pid, ctype;

		public PlayerChannel(int pid, int ctype) {
			super();
			this.pid = pid;
			this.ctype = ctype;
		}

		public String toString() {
			return pid + " " + ctype;
		}
	}

	private static class ChannelPrepaid {
		public final int pid, ctype, cash;

		public ChannelPrepaid(int pid, int ctype, int cash) {
			super();
			this.pid = pid;
			this.ctype = ctype;
			this.cash = cash;
		}

		public String toString() {
			return pid + " " + ctype + " " + cash;
		}

	}

	private static class Result {
		public final int ctype, pcnt, cash;

		public Result(int ctype, int pcnt, int cash) {
			super();
			this.ctype = ctype;
			this.pcnt = pcnt;
			this.cash = cash;
		}

		public String toString() {
			return ctype + " " + pcnt + " " + cash;
		}

	}

}
