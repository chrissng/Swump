package core;

public class TimerTest {

    public static void main(String args[]){
        new Thread(Timer.getInstance()).start();
        for(int i=0 ; i<5; i++){
            System.out.println("1: "+Timer.getInstance().getElapsedTime());
        }
        Timer.getInstance().resetTimer();
        for(int i=0 ; i<5; i++){
            System.out.println("2: "+Timer.getInstance().getElapsedTime());
        }
    }
}
