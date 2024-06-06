import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Character {
    private final String DEFAULT_FRAME_FILE_PATH = "resources\\Images\\Stand.png";
    private final String DEFAULT_BACK_FRAME_PATH = "resources\\Images\\StandBack.png";
    private final String RUN_IMAGES_PATH = "resources\\Images\\Run";
    private final String RUN_BACK_IMAGES_PATH = "resources\\Images\\RunBack";
    private final String SHOOT_IMAGES_PATH = "resources\\Images\\Shoot";
    private final String SHOO_BACK_IMAGES_PATH = "resources\\Images\\ShootBack";

    private final int CHARACTER_WIDTH = 200;
    private final int CHARACTER_HEIGHT = 250;
    private final int CHARACTER_SPEED = 15;
    private final int RIGHT_BOUNDARY_X = 1740;
    private final int LEFT_BOUNDARY_X = -45;
    private final int UPPER_BOUNDARY_Y = 165;
    private final int LOWER_BOUNDARY_Y = 790;

    private int x;
    private int y;
    private int dx;
    private int dy;
    private Rectangle hitBox;
    private HashSet<Rectangle> obstacles;
    private int runFrameIndex;
    private int shootFrameIndex;

    private int paintType;
    private final int STAND_BACK_CODE = -1;
    private boolean isCharacterMoving;
    private final int MOVE_CODE = 1;
    private boolean isCharacterMovingBack;
    private final int MOVE_BACK_CODE = 2;
    private boolean isCharacterShooting;
    private final int SHOOT_CODE = 3;
    private final int SHOOT_BACK_CODE = 4;

    private Image defaultFrame;
    private Image defaultFrameBack;
    private List<Image> runFrames;
    private List<Image> runBackFrames;
    private List<Image> shootFrames;
    private List<Image> shootBackFrames;

    public Character(int startX, int startY, HashSet<Rectangle> obstacles){
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        this.dy = 0;
        this.hitBox = new Rectangle(this.x + CHARACTER_WIDTH / 4 - 10,
                this.y + 150, CHARACTER_WIDTH / 2 + 15, (CHARACTER_HEIGHT - 50)/2);
        this.obstacles = obstacles;
        this.runFrameIndex = 0;
        this.shootFrameIndex = 0;
        this.defaultFrame = new ImageIcon(DEFAULT_FRAME_FILE_PATH).getImage();
        this.defaultFrameBack = new ImageIcon(DEFAULT_BACK_FRAME_PATH).getImage();
        this.runFrames = loadFrames(8, RUN_IMAGES_PATH);
        this.runBackFrames = loadFrames(8, RUN_BACK_IMAGES_PATH);
        this.shootFrames = loadFrames(4, SHOOT_IMAGES_PATH);
        this.shootBackFrames = loadFrames(4, SHOO_BACK_IMAGES_PATH);
    }
    public void paint(Graphics g){
        //g.fillRect(this.x + CHARACTER_WIDTH / 4 - 10, this.y + 150, CHARACTER_WIDTH / 2 + 15, (CHARACTER_HEIGHT - 50)/2);
        switch (this.paintType){
            case 1:
                g.drawImage(this.runFrames.get(this.runFrameIndex),
                        this.x, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
            case 2:
                g.drawImage(this.runBackFrames.get(this.runFrameIndex),
                        this.x, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
            case 3:
                g.drawImage(this.shootFrames.get(this.shootFrameIndex),
                        this.x + 30, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
            case 4:
                g.drawImage(this.shootBackFrames.get(this.shootFrameIndex),
                        this.x - 30, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
            case 0:
                g.drawImage(this.defaultFrame,
                        this.x, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
            case -1:
                g.drawImage(this.defaultFrameBack,
                        this.x, this.y, CHARACTER_WIDTH, CHARACTER_HEIGHT,null);
                break;
        }
        this.paintType = 0;
    }
    public void update(){
        if (this.isCharacterMoving){
            if(this.canMove()){
                this.move();
                if (this.isCharacterMovingBack){
                    this.paintType = MOVE_BACK_CODE;
                }
                else{
                    this.paintType = MOVE_CODE;
                }
                Main.sleep(40);
                this.loopBetweenFrames();
            }
        }
        else if (this.isCharacterShooting) {
            if (this.isCharacterMovingBack){
                this.paintType = SHOOT_BACK_CODE;
            }
            else {
                this.paintType = SHOOT_CODE;
            }
            Main.sleep(40);
            this.loopBetweenFrames();
        }
        else {
            if (this.isCharacterMovingBack){
                this.paintType = STAND_BACK_CODE;
            }
        }
    }

    private void loopBetweenFrames(){
        if (this.isCharacterMoving){
            this.setRunFrameIndex(this.getRunFrameIndex() + 1);
            if (this.getRunFrameIndex() % 8 == 0){
                this.setRunFrameIndex(0);
            }
        }
        if (this.isCharacterShooting){
            this.setShootFrameIndex(this.getShootFrameIndex() + 1);
            if (this.getShootFrameIndex() % 4 == 0){
                this.setShootFrameIndex(0);
            }
        }
    }

    private List<Image> loadFrames(int frames, String fileName){
        List<Image> frameList = new ArrayList<>(frames);
        for (int i = 1; i <= frames; i++){
            Image frame = new ImageIcon(fileName+i+".png").getImage();
            frameList.add(frame);
        }
        return frameList;
    }

    public void move(){
        this.x += this.dx;
        this.y += this.dy;
    }

    public boolean canMove(){
        int xPosition = this.x + dx;
        int yPosition = this.y + dy;
        boolean xPositionOk = xPosition >= LEFT_BOUNDARY_X && xPosition <= RIGHT_BOUNDARY_X;
        boolean yPositionOk = yPosition <= LOWER_BOUNDARY_Y && yPosition >= UPPER_BOUNDARY_Y;
        this.hitBox.setBounds(xPosition + CHARACTER_WIDTH / 4 - 10,
                yPosition + 150, CHARACTER_WIDTH / 2 + 15, (CHARACTER_HEIGHT - 50)/2);
        for (Rectangle obstacle : this.obstacles){
            if (this.hitBox.intersects(obstacle)){
                System.out.println("Hit");
                return false;
            }
        }
        return xPositionOk && yPositionOk;
    }

    public int getDx() {
        return dx;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCHARACTER_SPEED() {
        return CHARACTER_SPEED;
    }

    public int getCHARACTER_WIDTH() {
        return CHARACTER_WIDTH;
    }

    public int getCHARACTER_HEIGHT() {
        return CHARACTER_HEIGHT;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setShootFrameIndex(int shootFrameIndex) {
        this.shootFrameIndex = shootFrameIndex;
    }

    public int getShootFrameIndex() {
        return shootFrameIndex;
    }

    public int getRunFrameIndex() {
        return runFrameIndex;
    }

    public void setRunFrameIndex(int runFrameIndex) {
        this.runFrameIndex = runFrameIndex;
    }

    public boolean isCharacterMoving() {
        return isCharacterMoving;
    }

    public void setCharacterMoving(boolean characterMoving) {
        isCharacterMoving = characterMoving;
    }

    public boolean isCharacterMovingBack() {
        return isCharacterMovingBack;
    }

    public void setCharacterMovingBack(boolean characterMovingBack) {
        isCharacterMovingBack = characterMovingBack;
    }

    public void setCharacterShooting(boolean characterShooting) {
        isCharacterShooting = characterShooting;
    }
}
