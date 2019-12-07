package leikr.screens;

class TitleScreenPixel {
    public int x = 0;
    public int y = 0;
    public int color = 1;
    public int height = 5;
    public int delay = 0;

    TitleScreenPixel(int xPos, int yPos, int colorIndex, int columnHeight, int delayAmt) {
        x = xPos;
        y = yPos;
        color = colorIndex;
        delay = delayAmt;
        height = columnHeight;
    }

}