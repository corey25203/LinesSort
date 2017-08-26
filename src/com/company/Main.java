package com.company;

import java.io.*;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;

public class Main {

    private static void setOffset(TreeMap<LineUnit,RecordUnit> linesMap){

        long offset = 0;

        for(Map.Entry<LineUnit,RecordUnit> entry : linesMap.entrySet()) {
            LineUnit key = entry.getKey();
            RecordUnit value = entry.getValue();
            value.setOffset(offset);
            offset = key.getLineLength()+offset;

//            if((key.getLineNumber() % 1000)==0) System.out.println("calc offset->" + key.getLineNumber());
        }
        System.out.println(" offset calculated -> "+new Timestamp(System.currentTimeMillis()));
    }

    private static void resultWrite(TreeMap<LineUnit,RecordUnit> linesMap, BlockingQueue<RecordUnit> queue) throws InterruptedException {
            for(Map.Entry<LineUnit,RecordUnit> entry : linesMap.entrySet())queue.put(entry.getValue());
    }

    private static TreeMap<LineUnit,RecordUnit> fileScan(String path) {

        ConcurrentHashMap<Integer,String> cache = new ConcurrentHashMap<>();
        TreeMap<LineUnit,RecordUnit> linesMap = new  TreeMap<>(new LinesComparator(cache,path));

        try (
                RandomAccessFile file = new RandomAccessFile(path, "rw")
                ){

            System.out.println(" file size -> "+file.length());

            String line;
            int i =0;
            long offsetPointer =0;
            while ((line = file.readLine()) != null) {

                //string size trim to int(useless arg)
                LineUnit lineUnit = new  LineUnit(i,(int)(file.getFilePointer()-offsetPointer),offsetPointer,file.getFilePointer());
                linesMap.put(lineUnit,new RecordUnit(i,offsetPointer,file.getFilePointer(),i));
                i=++i;
                offsetPointer = file.getFilePointer();

                if(file.getFilePointer()==file.length()){
                    file.seek(file.length()-2);
                    if((file.readByte()!=13)&
                            (file.readByte())!=10) {
                        System.out.println("add cr lf -> " + lineUnit.getLineNumber());
                        file.write(13);
                        file.write(10);
                    }
                }

                if((lineUnit.getLineNumber() % 1000)==0) {
                    System.out.println("scan line -> " + lineUnit.getLineNumber());
                }

            }

            LineUnit lineUnit0 = new  LineUnit(i+1,0,file.getFilePointer(),file.getFilePointer());
            lineUnit0.setEofSym((byte)1);
            RecordUnit rec = new RecordUnit(i+1,file.getFilePointer(),file.getFilePointer(),i+1);
            rec.setEofSym((byte)1);
            linesMap.put(lineUnit0,rec);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("map created -> "+new Timestamp(System.currentTimeMillis()));

        return linesMap;
    }

    public static void main(String[] args) {
        String pathProducer = args[0];
        String pathConsumer = args[1];
        Integer threadsNumber = Integer.parseInt(args[2]);//1

        System.out.println("begin processing -> "+new Timestamp(System.currentTimeMillis()));

        BlockingQueue<RecordUnit> queue = new ArrayBlockingQueue<>(7000);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        TreeMap<LineUnit,RecordUnit> linesMap = fileScan(pathProducer);
        System.out.println("number of lines -> "+linesMap.size());

        setOffset(linesMap);

        for (int i = 0; i <= threadsNumber; i++)
        {
            WriterThread  writer = new WriterThread(queue,pathProducer,pathConsumer);
            executor.execute(writer);
        }

        executor.shutdown();

        try {
            resultWrite(linesMap,queue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

