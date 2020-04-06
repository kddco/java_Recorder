package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        theRecorder recoder=new theRecorder();
        System.out.println("錄音開始:");
        //模擬錄音4秒的延遲等待
        try{
            Thread.currentThread().sleep(4000);
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }


        recoder.Stop();
    }
}
