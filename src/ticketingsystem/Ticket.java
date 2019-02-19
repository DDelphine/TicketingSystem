package ticketingsystem;

public class Ticket {
	    private static volatile long id = 1;
		private volatile long tid; //车票编号
		private String passenger; //乘客名字
		private int route; //列车车次
		private int coach; //车厢号
		private int seat; //座位号
		private int departure; //出发站编号
		private int arrival; //到达站编号
		
		public Ticket(String name,int rou, int dep, int ar){
			passenger = name;
			route = rou;
			departure = dep;
			arrival = ar;
		}
		
		public Ticket() {
			passenger = null;
			route = 0;
			departure = 0;
			arrival = 0;
		}

		public void setId(){
			tid = id;
			id++;
		}
		
		public void setSeat(int c, int s){
			coach = c;
			seat = s;
		}
		
		public int getRoute(){
			return route;
		}
		
		public int getCoach(){
			return coach;
		}
		
		public long getId(){
			return tid;
		}
		
		public int getSeat(){
			return seat;
		}
		
		public int getDeparture(){
			return departure;
		}
		
		public int getArrival(){
			return arrival;
		}	
		
		public String getName(){
			return passenger;
		}
		
		public static int getMaxTid(){
			return (int)id;
		}
		
		public void setRoute(int r){
			route = r;
		}
		
		public void setCoach(int c){
			coach = c;
		}
		
		public void setSeat(int s){
			seat = s;
		}
		
		public void setDep(int d){
			departure = d;
		}
		
		public void setArr(int a){
			arrival = a;
		}
}
