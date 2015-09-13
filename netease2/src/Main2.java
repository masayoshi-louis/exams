import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main2 {
	
	private static Map<Integer, Channel> channel_map = new HashMap<Integer, Channel>();
	private static Map<Integer, Player> player_map = new HashMap<Integer, Player>();
	private static Map<Integer, Prepaid> prepaid_map = new HashMap<Integer, Prepaid>();
	
	private static Map<Integer, Integer[]> join_ret_map = new HashMap<Integer, Integer[]>();
	
	// key:ctype; value {ctype, pount, cash}
	private static Map<Integer, Integer[]> select_ret_map = new HashMap<Integer, Integer[]>();
	
	// {ctype, pount, cash}
	private static List<SQLRecord> sql_records = new ArrayList<SQLRecord>();

	// input
	private static void create_tables(Scanner in) {
        String str_line = "";
        while(true) {
        	// Channel table
        	// 1st line
        	str_line = in.nextLine();
        	String[] line_1st = str_line.split(" ");
        	String table_name = line_1st[0];
        	int table_length = Integer.parseInt(line_1st[1]);
        	
        	// 2nd line
        	str_line = in.nextLine();
        	String[] col_names = str_line.split(" ");
        	
        	// from 3rd line, table data
        	for(int i = 0; i < table_length; i++) {
        		Channel new_cha = new Channel();
        		str_line = in.nextLine();
            	String[] row_data = str_line.split(" ");
            	new_cha.cid = Integer.parseInt(row_data[0]);
            	new_cha.name = row_data[1];
            	new_cha.ctype = row_data[2].equalsIgnoreCase("NULL") ? -1 : Integer.parseInt(row_data[2]);
            	channel_map.put(new_cha.cid, new_cha);
        	}
        	
        	
        	// Player table
        	// 1st line
        	str_line = in.nextLine();
        	String[] player_1st = str_line.split(" ");
        	String player_tname = player_1st[0];
        	int player_tlength = Integer.parseInt(player_1st[1]);
        	
        	// 2nd line
        	str_line = in.nextLine();
        	String[] player_colnames = str_line.split(" ");
        	
        	// from 3rd line, table data
        	for(int i = 0; i < player_tlength; i++) {
        		Player new_player = new Player();
        		str_line = in.nextLine();
            	String[] row_data = str_line.split(" ");
            	new_player.pid = Integer.parseInt(row_data[0]);
            	new_player.name = row_data[1];
            	new_player.cid = Integer.parseInt(row_data[2]);
            	player_map.put(new_player.pid, new_player);
        	}
        	
        	
        	// Prepaid table
        	// 1st line
        	str_line = in.nextLine();
        	String[] prepaid_1st = str_line.split(" ");
        	String prepaid_tname = prepaid_1st[0];
        	int prepaid_tlength = Integer.parseInt(prepaid_1st[1]);
        	
        	// 2nd line
        	str_line = in.nextLine();
        	String[] prepaid_colnames = str_line.split(" ");
        	
        	// from 3rd line, table data
        	for(int i = 0; i < prepaid_tlength; i++) {
        		Prepaid new_prepaid = new Prepaid();
        		str_line = in.nextLine();
            	String[] row_data = str_line.split(" ");
            	new_prepaid.id = Integer.parseInt(row_data[0]);
            	new_prepaid.pid = Integer.parseInt(row_data[1]);
            	new_prepaid.cash = Integer.parseInt(row_data[2]);
            	prepaid_map.put(new_prepaid.id, new_prepaid);
        	}
        	break;
        }
	}
	
	private static void pid_ctype_join() {
		Set player_keys = player_map.keySet();
		Iterator player_it = player_keys.iterator();
		
		while (player_it.hasNext()) {
			
			Player p = player_map.get(player_it.next());
			if (channel_map.containsKey(p.cid)) {
				Integer[] join_ret = {p.pid, channel_map.get(p.cid).ctype, 0};
				join_ret_map.put(p.pid, join_ret);
			} else {
				Integer[] join_ret = {p.pid, -1, 0};
				join_ret_map.put(p.pid, join_ret);
			}
		}		
	}
	
	private static void pid_sumcash_join() {
		Set prepaid_keys = prepaid_map.keySet();
		Iterator prepaid_it = prepaid_keys.iterator();
		
		while (prepaid_it.hasNext()) {
			Prepaid pp = prepaid_map.get(prepaid_it.next());
			if (join_ret_map.containsKey(pp.pid))
				join_ret_map.get(pp.pid)[2] += pp.cash;
		}
	}
	
	private static void execute_select() {
		Set player_keys = join_ret_map.keySet();
		Iterator player_it = player_keys.iterator();
		
		while (player_it.hasNext()) {
			Integer[] join_ret = join_ret_map.get(player_it.next());
			if (select_ret_map.containsKey(join_ret[1])) {
				select_ret_map.get(join_ret[1])[1]++;
				select_ret_map.get(join_ret[1])[2] += join_ret[2];
			} else {
				Integer[] select_ret = {join_ret[1], 1, join_ret[2]};
				select_ret_map.put(join_ret[1], select_ret);
			}
		}
	}
	
	private static void execute_orderby() {
		Set select_keys = select_ret_map.keySet();
		Iterator select_it = select_keys.iterator();
		
		while (select_it.hasNext()) {
			Integer[] rec_array = select_ret_map.get(select_it.next());
			SQLRecord rec_obj = new SQLRecord();
			rec_obj.setCtype(rec_array[0]);
			rec_obj.setPcnt(rec_array[1]);
			rec_obj.setCash(rec_array[2]);
			sql_records.add(rec_obj);
		}
		
		ComparatorSQLRecord comparator = new ComparatorSQLRecord();
		Collections.sort(sql_records, comparator);
		
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        create_tables(in);
        pid_ctype_join();
        pid_sumcash_join();
        execute_select();
        execute_orderby();
        	
        for (SQLRecord rec: sql_records){
        	System.out.println(rec.getCtype() + " " + rec.getPcnt() + " " + rec.getCash());
        }
        
    }
}


class Channel{
	public int cid;
	public String name;
	public int ctype;
}


class Player{
	public int pid;
	public String name;
	public int cid;
}


class Prepaid{
	public int id;
	public int pid;
	public int cash;
}

class SQLRecord {
	public Integer ctype;
	public Integer pcnt;
	public Integer cash;
	
	public Integer getCtype() {
		return ctype;
	}
	public void setCtype(Integer ctype) {
		this.ctype = ctype;
	}
	public Integer getPcnt() {
		return pcnt;
	}
	public void setPcnt(Integer pcnt) {
		this.pcnt = pcnt;
	}
	public Integer getCash() {
		return cash;
	}
	public void setCash(Integer cash) {
		this.cash = cash;
	}	
}

class ComparatorSQLRecord implements Comparator{

	public int compare(Object arg0, Object arg1) {
		SQLRecord rec0 = (SQLRecord)arg0;
		SQLRecord rec1 = (SQLRecord)arg1;
		
		int flag = (rec1.getCash()).compareTo(rec0.getCash());
		
		if(flag == 0) {
			return (rec0.getCtype()).compareTo(rec1.getCtype());
		} else {
			return flag;
		}
	}
}






