package keimo.keimoengine.ikkuna;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Legacy_Window extends Window {

    public Legacy_Window(String title, boolean fullscreen, int width, int height) {
        super(title, fullscreen, width, height);

        try {
            Label toteutusPuuttuuLabel = new Label("Legacy-ikkunan toteutus puuttuu.");
            Label toteutusPuuttuuLabel2 = new Label("Toteutus vaaatisi niin paljon vaivaa ja ylimääräisiä kirjastoja, että sitä ei luultavasti tulla tekemään.");

            Frame ikkuna = new Frame(title);
            ikkuna.setSize(width, height);
            ikkuna.setVisible(true);
            ikkuna.setLayout(new FlowLayout());
            ikkuna.setLocationRelativeTo(null);
            ikkuna.add(toteutusPuuttuuLabel);
            ikkuna.add(toteutusPuuttuuLabel2);
            ikkuna.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shouldClose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shouldClose'");
    }

    @Override
    public void swapBuffers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'swapBuffers'");
    }

    @Override
    public void setFullscreen(boolean fullscreen, boolean changeResolution) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFullscreen'");
    }

    @Override
    public void setVSync(boolean vsync) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVSync'");
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}
