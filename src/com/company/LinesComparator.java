package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.Map;

public class LinesComparator implements Comparator<LineUnit> {

    private final Map<Integer,String> cache;
    private final String filePath;

    public LinesComparator(Map<Integer,String> cache,String filePath) {
        this.cache = cache;
        this.filePath = filePath;
    }

    public int compare(LineUnit lineUnitFirst, LineUnit lineUnitSecond){

        String first = "";
        String second = "";

        if(lineUnitFirst.getEofSym() == (byte)1) return 10;
        if(lineUnitSecond.getEofSym() == (byte)1) return -10;

        //try use cache
        if(cache.containsKey(lineUnitFirst.getLineNumber())){first = cache.get(lineUnitFirst.getLineNumber());}
        if(cache.containsKey(lineUnitSecond.getLineNumber())){second = cache.get(lineUnitSecond.getLineNumber());}

        if(cache.size()>1000){cache.clear();}

        try(
            RandomAccessFile file = new RandomAccessFile(filePath, "r")
        ){
            if(first.isEmpty()) {
                file.seek(lineUnitFirst.getLineBeginPointer());
                first = file.readLine();
                cache.put(lineUnitFirst.getLineNumber(),first);
            }

            if(second.isEmpty()) {
                file.seek(lineUnitSecond.getLineBeginPointer());
                second = file.readLine();
                cache.put(lineUnitSecond.getLineNumber(),second);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int comparsionResult = first.compareTo(second);
        if(comparsionResult==0) return  (lineUnitFirst.getLineNumber()-lineUnitSecond.getLineNumber());

        return comparsionResult;

    }

}