package formelap.gui;

import formelap.world.Position;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

class PositionButton extends JButton {
    private final Position position;
    private static final int buttonSize = 18;

    PositionButton(int row, int column) {
        position = new Position(row, column);
        this.setBounds((20 + column * buttonSize), (20 + row * buttonSize), buttonSize, buttonSize);
        this.setBorder(new LineBorder(new Color(0.85f, 0.84f, 0.75f, 1.00f), 1));
    }

    Position getPosition() {
        return new Position(position);
    }

    static int getButtonSize() {
        return buttonSize;
    }
}
