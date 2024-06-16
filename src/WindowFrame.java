import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowFrame extends JFrame {
    public static final int DEFAULT_POSITION = 0;

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    public static int panelChoice;
    public static boolean switchPanels;

    private GamePanel gamePanel;
    private MenuPanel menu;
    private InstructionsPanel instructionsPanel;
    private SettingsPanel settingsPanel;
    private List<JPanel> panels;

    private MusicPlayer musicPlayer;

    public WindowFrame(){
        this.setTitle("Soldier Survival Game");
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.panels = new ArrayList<>();
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.setVolumeSoundFx(0.7f); // starting soundFx volume 70%
        this.musicPlayer.setVolumeBackgroundMusic(0.6f); // starting music Background 60%

        this.menu = new MenuPanel(WIDTH, HEIGHT, this.musicPlayer); // 0 index in the list
        this.add(this.menu);
        this.panels.add(this.menu);
        this.showOnlyOnePanel();

        this.gamePanel = new GamePanel(WIDTH, HEIGHT, this.musicPlayer,this.buttonListener); // 1 index in the list
        this.add(gamePanel);
        this.panels.add(this.gamePanel);
        this.showOnlyOnePanel();

        this.instructionsPanel = new InstructionsPanel(WIDTH, HEIGHT, this.musicPlayer); // 2 index in the list
        this.add(this.instructionsPanel);
        this.panels.add(this.instructionsPanel);
        this.showOnlyOnePanel();

        this.settingsPanel = new SettingsPanel(WIDTH,HEIGHT, this.musicPlayer);  // 3 index in the list
        this.add(this.settingsPanel);
        this.panels.add(this.settingsPanel);

        this.showOnlyOnePanel();
        this.mainWindowLoop();
    }

    private void showOnlyOnePanel(){
        for (JPanel panel : this.panels){
            panel.setVisible(WindowFrame.panelChoice == this.panels.indexOf(panel));
            panel.setFocusable(WindowFrame.panelChoice == this.panels.indexOf(panel));
            panel.requestFocus();
        }
        WindowFrame.switchPanels = false;
    }
    private void mainWindowLoop(){
        new Thread(() -> {
            while(true){
                if (WindowFrame.switchPanels){
                    this.showOnlyOnePanel();
                }
            }
        }).start();
    }
}


