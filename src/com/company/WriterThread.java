package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;

public class WriterThread implements Runnable{

    private BlockingQueue<RecordUnit> blockingQueue = null;
    private final String pathProducer;
    private final String pathConsumer;

    public WriterThread(BlockingQueue<RecordUnit> blockingQueue,String pathProducer,String pathConsumer){
        this.blockingQueue = blockingQueue;
        this.pathProducer = pathProducer;
        this.pathConsumer = pathConsumer;
    }

    @Override
    public void run() {

        try (
                RandomAccessFile fileConsumer = new RandomAccessFile(pathConsumer, "rw");
                RandomAccessFile file = new RandomAccessFile(pathProducer, "r")
        ){

            while(true){

                RecordUnit unit = blockingQueue.take();

                if(unit.getEofSym()==(byte)1) {
                    RecordUnit eof = new RecordUnit(-1,-1,-1,-1);
                    eof.setEofSym((byte)1);
                    blockingQueue.add(eof);
                    System.out.println("end processing -> "+new Timestamp(System.currentTimeMillis()));
                    return;
                }

                try {

                    fileConsumer.seek(unit.getOffset());
                    file.seek(unit.getSupplierLineBeginPoint());
                    int length = (int)(unit.getSupplierLineEndPoint()-unit.getSupplierLineBeginPoint());
                    byte[] tmp = new byte[length];
                    for (int i = 0; i < length; i++)
                    {
                        tmp[i]=file.readByte();
                    }
                    fileConsumer.write(tmp);

//                    if((unit.getLineNumber() % 1000)==0)System.out.println("write->" + unit.getLineNumber());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}