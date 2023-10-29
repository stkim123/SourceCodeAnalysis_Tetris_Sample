package kr.ac.jbnu.se.tetris;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BlockImage {

    private final BufferedImage blockImage;

    public BlockImage(Tetrominoes shape) {
        blockImage = getImage(getImageFile(shape));
    }

    public BufferedImage getImage() {
        return blockImage;
    }

    public BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 예외 발생 시 null 반환
    }

    public String getImageFile(Tetrominoes shape) {
        String imgPath = "";
        switch (shape) {
            case NoShape:
                imgPath = "image/blocks/Block0.png";
                break;
            case ZShape:
                imgPath = "image/blocks/Block1.png";
                break;
            case SShape:
                imgPath = "image/blocks/Block2.png";
                break;
            case LineShape:
                imgPath = "image/blocks/Block3.png";
                break;
            case TShape:
                imgPath = "image/blocks/Block4.png";
                break;
            case SquareShape:
                imgPath = "image/blocks/Block5.png";
                break;
            case LShape:
                imgPath = "image/blocks/Block6.png";
                break;
            case MirroredLShape:
                imgPath = "image/blocks/Block7.png";
                break;
            case BombBlock:
                imgPath = "image/blocks/BombIcon.png";
                break;
            case LockBlock:
                imgPath = "image/blocks/lockBlock.png";
                break;
        }
        return imgPath;
    }
}
