package ticketingsystem;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;


public class Test {	
	
	public static void main(String[] args){
	//����1�����̳߳�����
		
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
				     //��Ʊ
				     case 5 :
				    	 int index = ThreadLocalRandom.current().nextInt(Ticket.getMaxTid());
				    	 if(TicketingDS.compareTo(index) != null)
				    		 tds.refundTicket(TicketingDS.compareTo(index));
				    	 break;
				     //��Ʊ
				     case 1 : case 6 : case 10:
				    	 StringBuilder builder = new StringBuilder(); //����˿����֣����ַ������ӵķ�ʽÿ��ƴ���ַ����������һ���µ�String����Ч�ʵͣ��˷�ʱ�䣬������StringBuilder
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
					 //��ѯ��Ʊ
				     case 2 : case 3 : case 4 : case 7 : case 8 : case 9:
				    	 int r = ThreadLocalRandom.current().nextInt(1,routenum)-1;
						 int d = ThreadLocalRandom.current().nextInt(1,stationnum)-1;
						 int a = ThreadLocalRandom.current().nextInt(d,stationnum-1);
						 while(a == d){
							 a = ThreadLocalRandom.current().nextInt(d,stationnum-1);
						 }
						 System.out.println("����  " + (r+1) + " ʼ��վ "+ (d+1) + "���յ�վ" + (a+1) + " ����Ʊ��ĿΪ     " + tds.inquiry(r, d, a));
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
		};
		executor.execute(r);
	}
	executor.shutdown();
	System.out.println("������");*/
	/*for(int i=0;i<threads.length;i++){
		try{
			threads[i].join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}*/
	
	//����2�������߳�����ִ��
		TicketingDS.setFlag(true);
		TicketingDS tds = new TicketingDS();
       
		Thread[] threads=new Thread[32]; //�߳���Ŀ
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


