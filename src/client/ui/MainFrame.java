package client.ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private CardLayout cardLayout; // 카드 레이아웃
    private JPanel cardPanel;      // 카드 패널
    private List<ChannelPanel> channelPanelList;

    public MainFrame(String username) {
        setTitle("Chat - " + username);
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        channelPanelList = new ArrayList<>();

        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // 채널 목록 패널에 카드 레이아웃과 패널 전달
            ChannelListPanel channelListPanel = new ChannelListPanel(in,out, cardPanel, cardLayout);
            add(channelListPanel, BorderLayout.WEST);

            add(cardPanel, BorderLayout.CENTER);

            out.println(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
