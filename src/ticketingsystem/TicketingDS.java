package ticketingsystem;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TicketingDS implements TicketingSystem,Runnable {
	
	private int routeNum;//��������
	private int coachNum;//�г�������
	private int seatNum;//��λ��
	private int stationNum;//���ξ�ͣվ������
	private int[][][][] seatMap;
	//private static LinkedList<Ticket> tickets = new LinkedList<Ticket>();
	private Vector<Ticket> tickets = new Vector<Ticket>();//��ΪLinkedLisr�̲߳���ȫ�����Ի���Vector
	static boolean Flag;
	//���ڿ����Ƿ��ӡ��Ϣ
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
	
//result����������һ����λ����Ϣ
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
//�������һ����ѡ��λ������result����
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
			System.out.println("�������ĳ˳������Ƿ���ȷ��");
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
			//��Ϊ����λ���У���������Ʊ֮��Ҫ������λ�ӳ���վ���յ�վ��ռ��
			for(int k = departure; k <= arrival; k++){
				seatMap[route][r.coachNo][r.seatNo][k] = 1;
			}
			tickets.addElement(t);
			if(Flag){
				System.out.println(t.getName()+" ��Ʊ�ɹ� :  ");
				System.out.println("����վ  "+ departure +" �յ�վ " + arrival);
				System.out.println("||��Ʊ��   "+ t.getId() + " ||����   " + (route+1) + " ||����   "+ (r.coachNo+1) +" ||��λ��   " + (r.seatNo+1) );
				
			}
			return t;
		}
		else{
			System.out.println("��Ʊʱ����δ֪����");
			return t;
		}
	}
	
	@Override
	public synchronized int inquiry(int route,int departure,int arrival){
		int count = 0;
		int a = 0; 
		int d = departure;
		try{
		//�ӵ�һ�������һ����λ��ʼ�����������λ������ĳ��վ��ռ�ã�����һ����λ������һ�����ᶼû���ҵ���λ���򻻵��ڶ������ᣬ�Դ����ơ�
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
			System.out.println("����  " + (route+1) + " ʼ��վ "+ (departure+1) + "���յ�վ" + (arrival+1) + " ����Ʊ��ĿΪ     " +count);
		return count;
		}
		catch(Exception e){
			System.out.println("��ѯ��Ʊʱ����δ֪����");
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
				System.out.println("�û�    " + name +" ��Ʊ�ɹ�  ");
		}
		catch(Exception e){
			System.out.println("��Ʊʱ����δ֪����");
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
			//System.out.println("��Ʊ����");
			tmp = ticketIter.next();
			if(tmp.getId() == id)
				return tmp;
			}
		}
		catch(Exception e){
			throw e;
		}
		//System.out.println("��Ʊ����");
		return null;
	}*/
	
	//�ӳ�Ʊ���������ȡһ��Ʊ������Ʊ
	
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
			//random�̲߳���ȫ������ʹ��ThreadLocalRandom�����������
			int random = ThreadLocalRandom.current().nextInt(1,10);
			int rou = ThreadLocalRandom.current().nextInt(1,routeNum)-1;
			int depart = ThreadLocalRandom.current().nextInt(1,stationNum)-1;
			int arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
			while( depart == arr){
				 arr = ThreadLocalRandom.current().nextInt(depart,stationNum-1);
			 }
			switch(random){
			     //��Ʊ
			     case 5 :
			    	 refundTicket(findToRefund());
			    	 //int index = ThreadLocalRandom.current().nextInt(Ticket.getMaxTid());
			    	 //if(compareTo(index) != null){
			    	//	 synchronized(tickets){
			    	//		 refundTicket(compareTo(index));
			    	//	 }
			    	 //}
			    	 //else
			    	//	 System.out.println("��Ʊʧ��");
			    	 //refundTicket(findToRefund());
			    	 break;
			     //��Ʊ
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
				 //��ѯ��Ʊ
			     case 2 : case 3 : case 4 : case 7 : case 8 : case 9:
			    	 /*int r = ThreadLocalRandom.current().nextInt(1,routeNum)-1;
					 int d = ThreadLocalRandom.current().nextInt(1,stationNum)-1;
					 int a = ThreadLocalRandom.current().nextInt(d,stationNum-1);
					 while(a == d){
						 a = ThreadLocalRandom.current().nextInt(d,stationNum-1);
					 }*/
					 inquiry(rou,depart,arr);
					 /*synchronized(tickets){
						 System.out.println("����  " + (r+1) + " ʼ��վ "+ (d+1) + "���յ�վ" + (a+1) + " ����Ʊ��ĿΪ     " + inquiry(r, d, a));
					 }*/
					 break;
				 default : 
					 System.out.println("default : ��������!");
					 break;
			}	
		}
		catch(Exception e){
			throw e;
		}
	  }
	}
	
	public synchronized String createName(){
		 StringBuilder builder = new StringBuilder(); //����˿����֣����ַ������ӵķ�ʽÿ��ƴ���ַ����������һ���µ�String����Ч�ʵͣ��˷�ʱ�䣬������StringBuilder
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