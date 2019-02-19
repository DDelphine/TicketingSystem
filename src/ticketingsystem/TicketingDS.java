package ticketingsystem;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TicketingDS implements TicketingSystem,Runnable {
	
	private int routeNum;//车次总数
	private int coachNum;//列车车厢数
	private int seatNum;//座位数
	private int stationNum;//车次经停站的数量
	private int[][][][] seatMap;
	//private static LinkedList<Ticket> tickets = new LinkedList<Ticket>();
	private Vector<Ticket> tickets = new Vector<Ticket>();//因为LinkedLisr线程不安全，所以换成Vector
	static boolean Flag;
	//用于控制是否打印信息
	public static void setFlag(boolean flag){
		Flag = flag;
	}
	
	public TicketingDS(){
		routeNum = 10;
		coachNum = 8;
		seatNum = 100;
		stationNum = 10;
		seatMap = new int[routeNum][][][];
		for(int x = 0; x < routeNum; x++){
			seatMap[x] = new int[coachNum][][];
			for(int y = 0; y < coachNum; y++){
				seatMap[x][y] = new int[seatNum][];
				for(int z = 0; z < seatNum; z++){
					seatMap[x][y][z] = new int[stationNum];
				}
			}
		}
	}
	
	public TicketingDS(int routenum,int coachnum,int seatnum,int stationnum){
		routeNum = routenum;
		coachNum = coachnum;
		seatNum = seatnum;
		stationNum = stationnum;
		seatMap = new int[routeNum][][][];
		for(int x = 0; x < routeNum; x++){
			seatMap[x] = new int[coachNum][][];
			for(int y = 0; y < coachNum; y++){
				seatMap[x][y] = new int[seatNum][];
				for(int z = 0; z < seatNum; z++){
					seatMap[x][y][z] = new int[stationNum];
				}
			}
		}	
	}
	
//result类用于描述一个座位的信息
	class result{
		boolean find;
		int coachNo;
		int seatNo;
		public result(boolean f, int c, int s){
			find = f;
			coachNo = c;
			seatNo = s;
		}
	}
//如果发现一个可选座位，就以result返回
	public synchronized result findSeat(int route, int departure, int arrival) {
		int m = 0; 
		int x = departure;
		result r = new result(false,0,0);
		try{
			while(m < getCoach()){
				int n = 0;
				while(n < getSeat()){
					while(x < arrival){
						if(seatMap[route][m][n][x] == 0 && x != arrival)
							x++;
						if(seatMap[route][m][n][x] == 0 && x == arrival){
							r.find = true;
							r.coachNo = m;
							r.seatNo = n;
							return r;
						}
						if(seatMap[route][m][n][x] != 0){
							x = departure;
							break;
						}
					}
				n++;
				}
			m++;
			}
			r.find = false;
			return r;
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("请检查您的乘车区间是否正确！");
			throw e;
		}
	}
	
	@Override
	public synchronized Ticket buyTicket(String passenger, int route, int departure, int arrival) {
		result r = findSeat(route,departure,arrival);
		Ticket t = new Ticket(passenger,route,departure,arrival);
		if(r.find){
			t.setSeat(r.coachNo, r.seatNo);
			t.setId();
			//因为该座位空闲，所以在买票之后要将该座位从出发站到终点站都占用
			for(int k = departure; k <= arrival; k++){
				seatMap[route][r.coachNo][r.seatNo][k] = 1;
			}
			tickets.addElement(t);
			if(Flag){
				System.out.println(t.getName()+" 买票成功 :  ");
				System.out.println("出发站  "+ departure +" 终点站 " + arrival);
				System.out.println("||车票号   "+ t.getId() + " ||车次   " + (route+1) + " ||车厢   "+ (r.coachNo+1) +" ||座位号   " + (r.seatNo+1) );
				
			}
			return t;
		}
		else{
			System.out.println("买票时出现未知错误！");
			return t;
		}
	}
	
	@Override
	public synchronized int inquiry(int route,int departure,int arrival){
		int count = 0;
		int a = 0; 
		int d = departure;
		try{
		//从第一个车厢第一个座位开始遍历，如果座位在其中某个站被占用，则换下一个座位；当第一个车厢都没有找到座位，则换到第二个车厢，以此类推。
		while(a < coachNum){
			int b = 0;
			while(b < seatNum){
				while(d <=arrival){
					if(seatMap[route][a][b][d] == 0 && d != arrival)
						d++;
					if(seatMap[route][a][b][d] == 0 && d == arrival){
						count++;
					    break;
					}
					if(seatMap[route][a][b][d] != 0){
						d = departure;
						break;
					}
				}
				b++;
				}
			a++;
			}
		if(Flag)
			System.out.println("车次  " + (route+1) + " 始发站 "+ (departure+1) + "到终点站" + (arrival+1) + " 的余票数目为     " +count);
		return count;
		}
		catch(Exception e){
			System.out.println("查询余票时出现未知错误！");
			throw e;
		}
	}

	@Override
	public synchronized boolean refundTicket(Ticket ticket) {
		if(ticket != null){
		int r = ticket.getRoute();
		int c = ticket.getCoach();
		int s = ticket.getSeat();
		int d = ticket.getDeparture();
		int a = ticket.getArrival();
		String name =ticket.getName();
		try{
			while(d <= a){
				seatMap[r][c][s][d] = 0;
				d++;
			}
			if(Flag)
				System.out.println("用户    " + name +" 退票成功  ");
		}
		catch(Exception e){
			System.out.println("退票时出现未知错误！");
			throw e;
		}
		return true;
		}
		else return false;
	}

	/*public Ticket compareTo(long id){
		try{
		ListIterator<Ticket> ticketIter = tickets.listIterator();
		Ticket tmp;// = new Ticket();
		while(ticketIter.hasNext()){
			//System.out.println("无票可退");
			tmp = ticketIter.next();
			if(tmp.getId() == id)
				return tmp;
			}
		}
		catch(Exception e){
			throw e;
		}
		//System.out.println("无票可退");
		return null;
	}*/
	
	//从车票库中随机抽取一张票用于退票
	
	public synchronized Ticket findToRefund(){
		if(tickets.size() == 0)
			return null;
		int index = ThreadLocalRandom.current().nextInt(tickets.size());
		Ticket tic = tickets.remove(index);
		return tic;
	}
	

	public void run() {
		for(int count = 1; count < 1000; count++){
		try{
			//random线程不安全，所以使用ThreadLocalRandom来生成随机数
			int random = ThreadLocalRandom.current().nextInt(1,10);
			int rou = ThreadLocalRandom.current().nextInt(1,routeNum)-1;
			int depart = ThreadLocalRandom.current().nextInt(1,stationNum)-1;
			int arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
			while( depart == arr){
				 arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
			 }
			switch(random){
			     //退票
			     case 5 :
			    	 refundTicket(findToRefund());
			    	 //int index = ThreadLocalRandom.current().nextInt(Ticket.getMaxTid());
			    	 //if(compareTo(index) != null){
			    	//	 synchronized(tickets){
			    	//		 refundTicket(compareTo(index));
			    	//	 }
			    	 //}
			    	 //else
			    	//	 System.out.println("退票失败");
			    	 //refundTicket(findToRefund());
			    	 break;
			     //买票
			     case 1 : case 6 : case 10:
			    	 String name = createName();
					 /*int rou = ThreadLocalRandom.current().nextInt(1,routeNum)-1;
					 int depart = ThreadLocalRandom.current().nextInt(1,stationNum)-1;
					 int arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
					 while( depart == arr){
						 arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
					 }*/
					 synchronized(tickets){
						 buyTicket(name, rou, depart, arr);
					 }
					 break;
				 //查询余票
			     case 2 : case 3 : case 4 : case 7 : case 8 : case 9:
			    	 /*int r = ThreadLocalRandom.current().nextInt(1,routeNum)-1;
					 int d = ThreadLocalRandom.current().nextInt(1,stationNum)-1;
					 int a = ThreadLocalRandom.current().nextInt(d,stationNum-1);
					 while(a == d){
						 a = ThreadLocalRandom.current().nextInt(d,stationNum-1);
					 }*/
					 inquiry(rou,depart,arr);
					 /*synchronized(tickets){
						 System.out.println("车次  " + (r+1) + " 始发站 "+ (d+1) + "到终点站" + (a+1) + " 的余票数目为     " + inquiry(r, d, a));
					 }*/
					 break;
				 default : 
					 System.out.println("default : 发生错误!");
					 break;
			}	
		}
		catch(Exception e){
			throw e;
		}
	  }
	}
	
	public synchronized String createName(){
		 StringBuilder builder = new StringBuilder(); //构造乘客名字，用字符串连接的方式每次拼接字符串都会产生一个新的String对象，效率低，浪费时间，所以用StringBuilder
		 builder.append((char)ThreadLocalRandom.current().nextInt(65,90));
		 for(int i = 0; i < 5; i++){
			builder.append((char)ThreadLocalRandom.current().nextInt(97,122));
		 }
		 builder.append((char)ThreadLocalRandom.current().nextInt(0,100));
		 String name = builder.toString();
		 return name;
	}

	public int[][][][] seatMap(){
		return seatMap;
	}
	
	public int getCoach(){
		return coachNum;
	}
	
	public int getSeat(){
		return seatNum;
	}
	
}