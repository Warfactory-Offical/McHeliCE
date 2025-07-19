package com.norwood.mcheli;

@Deprecated
public class MCH_CheckTime {
    public int x = 0;
    public long[][] pointTimeList = new long[1][1];
    public int MAX_Y = 0;
    private long startTime = 0L;
    private int y = 0;
    private int MAX_X = 0;

    public MCH_CheckTime() {
        this.MAX_Y = 100;
        this.MAX_X = 40;
        this.pointTimeList = new long[this.MAX_Y + 1][this.MAX_X];
        this.y = this.MAX_Y - 1;
    }

    public void start() {
        this.startTime = System.nanoTime();
        this.x = 0;
        this.y = (this.y + 1) % this.MAX_Y;
        if (this.y == 0) {
            for (int j = 0; j < this.MAX_X; j++) {
                this.pointTimeList[this.MAX_Y][j] = 0L;

                for (int i = 0; i < this.MAX_Y; i++) {
                    this.pointTimeList[this.MAX_Y][j] = this.pointTimeList[this.MAX_Y][j] + this.pointTimeList[i][j];
                }
            }
        }
    }

    public void timeStamp() {
        if (this.x < this.MAX_X) {
            this.pointTimeList[this.y][this.x] = System.nanoTime() - this.startTime;
            this.x++;
        }
    }
}
