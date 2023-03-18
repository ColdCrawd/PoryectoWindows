package grupo.mavenproject1;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class Reproductor extends JFrame {

    private JList<String> playlist;
    private DefaultListModel<String> playlistModel;
    private JButton playButton;
    private JButton stopButton;
    private JButton nextButton;
    private JButton backButton;
    private JButton addButton;

    private ArrayList<String> songs;
    private int currentIndex;
    private Clip clip;
    private boolean isPaused;

    public Reproductor() {
        songs = new ArrayList<>();
        currentIndex = -1;
        isPaused = false;
        clip = null;

        // Configuración de la interfaz gráfica
        setTitle("Reproductor de música");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear la lista de reproducción
        playlistModel = new DefaultListModel<>();
        playlist = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlist);
        playlistScrollPane.setPreferredSize(new Dimension(200, 0));
        add(playlistScrollPane, BorderLayout.WEST);

        // Crear los botones
        playButton = new JButton("▶");
        stopButton = new JButton("⏹");
        nextButton = new JButton("▶▶");
        backButton = new JButton("◀◀");
        addButton = new JButton("+");

        // Configurar los botones
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (clip != null) {
                    if (isPaused) {
                        clip.start();
                        isPaused = false;
                    }
                } else {
                    playCurrentSong();
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopCurrentSong();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playNextSong();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playPreviousSong();
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSong();
            }
        });

        // Crear el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(backButton);
        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void playCurrentSong() {
        if (songs.size() > 0 && currentIndex >= 0) {
            try {
                for (int i = 0; i < playlistModel.size(); i++) {
                    playlistModel.set(i, playlistModel.get(i).replace("▶", ""));
                }
                playlistModel.set(currentIndex, "▶ " + playlistModel.get(currentIndex));
                playlist.setSelectedIndex(currentIndex);
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File(songs.get(currentIndex))));
                clip.start();
                clip.addLineListener(new LineListener() {
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            playNextSong();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopCurrentSong() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            isPaused = false;
        }
    }

    private void playNextSong() {
        if (songs.size() > 0) {
            if (currentIndex == songs.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex++;
            }
            stopCurrentSong();
            playCurrentSong();
        }
    }

    private void playPreviousSong() {
        if (songs.size() > 0) {
            if (currentIndex == 0) {
                currentIndex = songs.size() - 1;
            } else
                currentIndex--;
            stopCurrentSong();
            playCurrentSong();
        }
    }

    private void addSong() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                songs.add(file.getAbsolutePath());
                playlistModel.addElement(file.getName());
            }
            if (currentIndex == -1) {
                currentIndex = 0;
                playCurrentSong();
            }
        }
    }

    public static void main(String[] args) {
        new Reproductor();
    }

}


