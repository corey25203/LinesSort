package com.company;

public class RecordUnit{

        private int lineNumber = -1;
        private long offset = -1;

        private long supplierLineBeginPoint = -1;
        private long supplierLineEndPoint = -1;
        private int supplierLineNumber =-1;

        private byte eofSym=0;
        public byte getEofSym() {
            return eofSym;
        }

        public void setEofSym(byte eofSym) {
            this.eofSym = eofSym;
        }

        public long getSupplierLineBeginPoint() {
            return supplierLineBeginPoint;
        }

        public void setSupplierLineBeginPoint(long supplierLineBeginPoint) {
            this.supplierLineBeginPoint = supplierLineBeginPoint;
        }

        public long getSupplierLineEndPoint() {
            return supplierLineEndPoint;
        }

        public void setSupplierLineEndPoint(long supplierLineEndPoint) {
            this.supplierLineEndPoint = supplierLineEndPoint;
        }

        public int getSupplierLineNumber() {
            return supplierLineNumber;
        }

        public void setSupplierLineNumber(int supplierLineNumber) {
            this.supplierLineNumber = supplierLineNumber;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public RecordUnit(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public RecordUnit(int lineNumber, long supplierLineBeginPoint, long supplierLineEndPoint, int supplierLineNumber) {
            this.lineNumber = lineNumber;
            this.supplierLineBeginPoint = supplierLineBeginPoint;
            this.supplierLineEndPoint = supplierLineEndPoint;
            this.supplierLineNumber = supplierLineNumber;
        }
}