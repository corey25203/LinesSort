package com.company;

public class LineUnit {
    private int lineNumber =-1;
    private int lineLength =-1;
    private long lineBeginPointer =-1;
    private long lineEndPointer =-1;
    private byte eofSym=0;

    public byte getEofSym() {
        return eofSym;
    }

    public void setEofSym(byte eofSym) {
        this.eofSym = eofSym;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineLength() {
        return lineLength;
    }

    public long getLineBeginPointer() {
        return lineBeginPointer;
    }

    public long getLineEndPointer() {
        return lineEndPointer;
    }

    public LineUnit(int lineNumber, int lineLength, long lineBeginPointer, long lineEndPointer) {
        this.lineNumber = lineNumber;
        this.lineLength = lineLength;
        this.lineBeginPointer = lineBeginPointer;
        this.lineEndPointer = lineEndPointer;
    }

}
