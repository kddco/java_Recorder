//輸入文字控制的版本
package com.company;

//import com.recorder.test.AudioRecorder;
import java.io.File;
import java.util.Scanner;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class theRecorder {
    private static final long serialVersionUID = 1L;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    long startTime=0;

    public theRecorder(){//自動開始版本
        startTime = System.currentTimeMillis();
        captureAudio();//調用錄音方法

    }
    public theRecorder(boolean enble) {//文字控制版本
        System.out.println("y開始n結束");
        Scanner input = new Scanner(System.in);
        String Sinput = input.next();
        long testtime = System.currentTimeMillis();
        if(Sinput.equals("y")){
            captureAudio();//調用錄音方法
        }
        Scanner input_2 = new Scanner(System.in);
        String Sinput_2 = input_2.next();
        if(Sinput_2.equals("n")){
            targetDataLine.stop();
            targetDataLine.close();
        }
        System.out.println("錄製了"+(System.currentTimeMillis()-testtime)/1000+"秒！");
    }


    public void Stop(){
        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("錄製了"+(System.currentTimeMillis()-startTime)/1000+"秒！");
    }

    public void captureAudio(){
        try {
            audioFormat = getAudioFormat();//構造具有線性 PCM 編碼和給定參數的 AudioFormat。
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            //根據指定信息構造數據行的信息對象，這些信息包括單個音頻格式。此構造方法通常由應用程序用於描述所需的行。
            //lineClass - 該信息對象所描述的數據行的類
            //format - 所需的格式
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            //如果請求 DataLine，且 info 是 DataLine.Info 的實例（至少指定一種完全限定的音頻格式），
            //上一個數據行將用作返回的 DataLine 的默認格式。
            new CaptureThread().start();
            //開啟線程
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000F;
        // 8000,11025,16000,22050,44100 採樣率
        int sampleSizeInBits = 16;
        // 8,16 每個樣本中的位數
        int channels = 2;
        // 1,2 信道數（單聲道為 1，立體聲為 2，等等）
        boolean signed = true;
        // true,false
        boolean bigEndian = false;
        // true,false 指示是以 big-endian 順序還是以 little-endian 順序存儲音頻數據。
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);//構造具有線性 PCM 編碼和給定參數的 AudioFormat。
    }

    class CaptureThread extends Thread {
        public void run() {
            AudioFileFormat.Type fileType = null;
            //指定的文件類型
            File audioFile = null;
            //設置文件類型和文件擴展名
            //根據選擇的單選按鈕。
            fileType = AudioFileFormat.Type.WAVE;
            audioFile = new File("userInput.wav");
            try {
                targetDataLine.open(audioFormat);
                //format - 所需音頻格式
                targetDataLine.start();
                //當開始音頻捕獲或回放時，生成 START 事件。
                AudioSystem.write(new AudioInputStream(targetDataLine),fileType, audioFile);
                //new AudioInputStream(TargetDataLine line):構造從指示的目標數據行讀取數據的音頻輸入流。該流的格式與目標數據行的格式相同,line - 此流從中獲得數據的目標數據行。
                //stream - 包含要寫入文件的音頻數據的音頻輸入流
                //fileType - 要寫入的音頻文件的種類
                //out - 應將文件數據寫入其中的外部文件

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
