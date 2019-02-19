package ticketingsystem;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;


public class Test {	
	
	public static void main(String[] args){
	//方法1：用线程池来做
		
	/*static int routenum = 10;
	static int coachnum = 8;
	static int seatnum = 100;
	static int stationnum = 10;
	ExecutorService executor = Executors.newFixedThreadPool(4);
	for(int i = 0; i < 10000 ; i++){
		Runnable r = new Runnable(){@Override
		public void run(){
			try{
				int random = ThreadLocalRandom.current().nextInt(1,10);
				switch(random){
				     //退票
				     case 5 :
				    	 int index = ThreadLocalRandom.current().nextInt(Ticket.getMaxTid());
				    	 if(TicketingDS.compareTo(index) != null)
				    		 tds.refundTicket(TicketingDS.compareTo(index));
				    	 break;
				     //买票
				     case 1 : case 6 : case 10:
				    	 StringBuilder builder = new StringBuilder(); //构造乘客名字，用字符串连接的方式每次拼接字符串都会产生一个新的String对象，效率低，浪费时间，所以用StringBuilder
						 builder.append((char)ThreadLocalRandom.current().nextInt(65,90));
						 for(int i = 0; i < 5; i++){
							builder.append((char)ThreadLocalRandom.current().nextInt(97,122));
						 }
						 builder.append((char)ThreadLocalRandom.current().nextInt(0,100));
						 String name = builder.toString();
						 int rou = ThreadLocalRandom.current().nextInt(1,routenum)-1;
						 int depart = ThreadLocalRandom.current().nextInt(1,stationnum)-1;
						 int arr = ThreadLocalRandom.current().nextInt(depart,stationnum-1);
						 while( depart == arr)
							 arr = ThreadLocalRandom.current().nextInt(depart,stationnum-1);
						 tds.buyTicket(name, rou, depart, arr);
						 break;
					 //查询余票
				     case 2 : case 3 : case 4 : case 7 : case 8 : case 9:
				    	 int r = ThreadLocalRandom.current().nextInt(1,routenum)-1;
						 int d = ThreadLocalRandom.current().nextInt(1,stationnum)-1;
						 int a = ThreadLocalRandom.current().nextInt(d,stationnum-1);
						 while(a == d){
							 a = ThreadLocalRandom.current().nextInt(d,stationnum-1);
						 }
						 System.out.println("车次  " + (r+1) + " 始发站 "+ (d+1) + "到终点站" + (a+1) + " 的余票数目为     " + tds.inquiry(r, d, a));
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
		};
		executor.execute(r);
	}
	executor.shutdown();
	System.out.println("结束！");*/
	/*for(int i=0;i<threads.length;i++){
		try{
			threads[i].join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}*/
	
	//方法2：创建线程组来执行
		TicketingDS.setFlag(true);
		TicketingDS tds = new TicketingDS();
       
		Thread[] threads=new Thread[32]; //线程数目
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(tds);
		}
		long start=System.currentTimeMillis();
		for(Thread thread:threads){
			thread.start();
		}
		System.out.println("whole run time is "+((System.currentTimeMillis()-start))+" ms.");
	}
}


