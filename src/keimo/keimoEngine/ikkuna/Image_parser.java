package keimo.keimoEngine.ikkuna;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.stb.STBImage.*;

public class Image_parser {
    public ByteBuffer get_image() {
        return image;
    }

    public int get_width() {
        return width;
    }

    public int get_height() {
        return heigh;
    }

    private ByteBuffer image;
    private int width, heigh;

    Image_parser(int width, int heigh, ByteBuffer image) {
        this.image = image;
        this.heigh = heigh;
        this.width = width;
    }
    public static Image_parser load_image(String path) {
        ByteBuffer image;
        int width, heigh;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(path, w, h, comp, 4);
            width = w.get();
            heigh = h.get();
        }
        return new Image_parser(width, heigh, image);
    }
}
