package client.ui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChannelPanel extends JPanel {
    private CardLayout cardLayout;

    public ChannelPanel(String channelName,CardLayout cardLayout,BufferedReader in,PrintWriter out) {

        setLayout(new BorderLayout());

        ChatPanel chatPanel = new ChatPanel(channelName,out);
        add(chatPanel, BorderLayout.CENTER);
        //RightPanel
        RightPanel rightPanel = new RightPanel(channelName);
        add(rightPanel, BorderLayout.EAST);
        this.cardLayout=cardLayout;

        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/members")) {
                        // 멤버 리스트 업데이트 메시지 처리
                        //String[] members = message.substring(9).split(",");
                        //rightPanel.updateMembers(Arrays.asList(members));
                    } else {
                        chatPanel.appendMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
