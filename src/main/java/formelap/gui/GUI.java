package formelap.gui;

import formelap.entity.*;
import formelap.exceptions.InvalidFileExtensionException;
import formelap.exceptions.OrganismOutOfBordersException;
import formelap.world.Position;
import formelap.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GUI {
    private static final World world = World.getInstance();
    private final JFrame frame;
    private PositionButton[][] board;
    private JLabel numberOfNewAnimalsLabel, numberOfKilledAnimalsLabel, numberOfAnimalsLabel;
    private boolean isChoiceButtonGroup = false;
    private JComboBox<String> organismsComboBox;
    private Map<String, Function<Position, Organism>> organismsMap;
    private final int span = 30;
    private final int labelWidth = 140;
    private final int labelHeight = 25;
    private final int buttonWidth = 150;
    private final int buttonHeight = 30;
    private final int minWidth = span + buttonWidth + span + buttonWidth + span;
    private final int customWidth = span + world.getSize() * PositionButton.getButtonSize() + span;
    private final int frameWidth = (customWidth > minWidth) ? customWidth : minWidth;
    private final int frameHeight = span + world.getSize() * PositionButton.getButtonSize() + span
            + buttonHeight + span + 3 * labelHeight + span;
    private final String filePath = ".\\saved_worlds";
    private final String fileExtension = ".wrld";
    private final String fileDescription = "World File";

    public GUI() {
        frame = new JFrame();

        initializeMenuBar();
        initializeBoard();
        initializeNextTurnButton();
        initializeAddNewOrganismButton();

        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem save = new JMenuItem("Zapisz");
        save.addActionListener(event -> {
            JFileChooser fc = new JFileChooser(new File(filePath));
            fc.setDialogTitle("Zapisz do pliku");
            fc.setFileFilter(new FileTypeFilter(fileExtension, fileDescription));
            int returnValue = fc.showSaveDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                if (!file.getName().endsWith(fileExtension))
                    file = new File(file.getAbsolutePath() + fileExtension);

                try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                    for (Organism o : world.getOrganisms()) {
                        out.writeObject(o);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        JMenuItem open = new JMenuItem("Otwórz");
        open.addActionListener(event -> {
            JFileChooser fc = new JFileChooser(new File(filePath));
            fc.setDialogTitle("Otwórz zapisaną rozgrywkę");
            fc.setFileFilter(new FileTypeFilter(fileExtension, fileDescription));
            int returnValue = fc.showOpenDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    if (!file.getName().endsWith(fileExtension))
                        throw new InvalidFileExtensionException(fileExtension);

                    try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {

                        world.clear();
                        int numberOfOrganismsOutOfBounds = 0;

                        boolean eof = false;
                        while (!eof) {
                            try {
                                Organism o = (Organism) in.readObject();

                                if ((o.getPosition().getRow() >= world.getSize()) || (o.getPosition().getColumn() >= world.getSize()))
                                    ++numberOfOrganismsOutOfBounds;
                                else
                                    world.addNewOrganism(o);
                            } catch (EOFException e) {
                                eof = true;
                            }
                        }

                        world.update();
                        numberOfNewAnimalsLabel.setText(String.valueOf(world.getNumberOfNewOrganisms()));
                        numberOfKilledAnimalsLabel.setText(String.valueOf(world.getNumberOfKilledOrganisms()));
                        numberOfAnimalsLabel.setText(String.valueOf(world.getNumberOfOrganisms()));
                        updateBoard();

                        if (numberOfOrganismsOutOfBounds != 0)
                            throw new OrganismOutOfBordersException(numberOfOrganismsOutOfBounds);

                    } catch (IOException | OrganismOutOfBordersException | ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }

                } catch (InvalidFileExtensionException e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
            }
        });

        JMenuItem close = new JMenuItem("Zamknij");
        close.addActionListener(event -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        JMenuItem info = new JMenuItem("Info");
        info.addActionListener(event -> JOptionPane.showMessageDialog(frame, "Projekt przygotowany przez:\nPiotr Formela\nnr albumu: 191002"));

        menu.add(open);
        menu.add(save);
        menu.add(close);
        menu.add(info);

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    private void initializeBoard() {
        board = new PositionButton[world.getSize()][world.getSize()];
        for (int i = 0; i < world.getSize(); i++) {
            for (int j = 0; j < world.getSize(); j++) {
                board[i][j] = new PositionButton(i, j);
                board[i][j].addActionListener(event -> {
                    PositionButton button = (PositionButton) event.getSource();
                    Position position = button.getPosition();
                    Organism organism = world.getOrganismFromGrid(position);

                    if (isChoiceButtonGroup && (organism == null)) {
                        organism = organismsMap.get(organismsComboBox.getSelectedItem()).apply(position);

                        if (organism != null) {
                            world.update();
                            button.setBackground(organism.getColor());
                            int number = Integer.parseInt(numberOfNewAnimalsLabel.getText());
                            ++number;
                            numberOfNewAnimalsLabel.setText(String.valueOf(number));
                            numberOfAnimalsLabel.setText(String.valueOf(world.getNumberOfOrganisms()));
                        }
                    }
                });

                frame.add(board[i][j]);
            }
        }

        updateBoard();
    }

    private void initializeNextTurnButton() {
        JButton nextTurnButton = new JButton("Kolejna tura");
        nextTurnButton.setBounds(span, (span + world.getSize() * PositionButton.getButtonSize() + 5),
                buttonWidth, buttonHeight);

        JLabel newAnimalsLabel = new JLabel("Nowe organizmy: ");
        newAnimalsLabel.setBounds(nextTurnButton.getX(), nextTurnButton.getY() + 10 + labelHeight,
                labelWidth, labelHeight);

        numberOfNewAnimalsLabel = new JLabel("0");
        numberOfNewAnimalsLabel.setBounds(newAnimalsLabel.getX() + labelWidth, newAnimalsLabel.getY(),
                30, labelHeight);

        JLabel killedAnimalsLabel = new JLabel("Zniszczone organizmy: ");
        killedAnimalsLabel.setBounds(newAnimalsLabel.getX(), newAnimalsLabel.getY() + labelHeight,
                labelWidth, labelHeight);

        numberOfKilledAnimalsLabel = new JLabel("0");
        numberOfKilledAnimalsLabel.setBounds(killedAnimalsLabel.getX() + labelWidth, killedAnimalsLabel.getY(),
                30, labelHeight);

        JLabel totalAnimalsLabel = new JLabel("Ilość organizmów: ");
        totalAnimalsLabel.setBounds(killedAnimalsLabel.getX(), killedAnimalsLabel.getY() + labelHeight,
                labelWidth, labelHeight);

        numberOfAnimalsLabel = new JLabel("0");
        numberOfAnimalsLabel.setBounds(totalAnimalsLabel.getX() + labelWidth, totalAnimalsLabel.getY(),
                30, labelHeight);

        nextTurnButton.addActionListener(event -> {
            world.nextTurn();
            updateBoard();
            numberOfNewAnimalsLabel.setText(String.valueOf(world.getNumberOfNewOrganisms()));
            numberOfKilledAnimalsLabel.setText(String.valueOf(world.getNumberOfKilledOrganisms()));
            numberOfAnimalsLabel.setText(String.valueOf(world.getNumberOfOrganisms()));
        });

        frame.add(newAnimalsLabel);
        frame.add(numberOfNewAnimalsLabel);
        frame.add(killedAnimalsLabel);
        frame.add(numberOfKilledAnimalsLabel);
        frame.add(totalAnimalsLabel);
        frame.add(numberOfAnimalsLabel);
        frame.add(nextTurnButton);
    }

    private void initializeAddNewOrganismButton() {
        JButton addNewOrganismButton = new JButton("Dodaj organizm");

        addNewOrganismButton.setBounds((span + buttonWidth + span), (span + world.getSize() * PositionButton.getButtonSize() + 5),
                buttonWidth, buttonHeight);

        addNewOrganismButton.addActionListener(event -> {
            if (isChoiceButtonGroup) {
                organismsComboBox.setVisible(false);

                isChoiceButtonGroup = false;

                addNewOrganismButton.setText("Dodaj organizm");
            } else {
                organismsComboBox.setVisible(true);

                isChoiceButtonGroup = true;

                addNewOrganismButton.setText("Zwiń listę");
            }
        });

        organismsMap = new HashMap<>();

        organismsMap.put("Koka", Coca::new);
        organismsMap.put("Wilcza Jagoda", DeadlyNightshade::new);
        organismsMap.put("Lis", Fox::new);
        organismsMap.put("Trawa", Grass::new);
        organismsMap.put("Mysz", Mouse::new);
        organismsMap.put("Królik", Rabbit::new);
        organismsMap.put("Owca", Sheep::new);
        organismsMap.put("Wilk", Wolf::new);

        String[] organismLabels = organismsMap.keySet().toArray(new String[0]);

        organismsComboBox = new JComboBox<>(organismLabels);
        organismsComboBox.setBounds(addNewOrganismButton.getX(), addNewOrganismButton.getY() + buttonHeight + 5,
                labelWidth, labelHeight);
        organismsComboBox.setVisible(false);

        frame.add(organismsComboBox);
        frame.add(addNewOrganismButton);
    }

    private void updateBoard() {
        for (int i = 0; i < world.getSize(); i++) {
            for (int j = 0; j < world.getSize(); j++) {
                Organism data = world.getBoard()[i][j];

                if (data == null)
                    board[i][j].setBackground(Color.WHITE);
                else
                    board[i][j].setBackground(data.getColor());
            }
        }
    }
}
