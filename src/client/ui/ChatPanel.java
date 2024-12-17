package client.ui;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ChatPanel extends JPanel {
    private JTextPane chatArea; // JTextArea 대신 JTextPane 사용
    private JTextField chatInput;
    private JPanel emojiPanel; // 이모티콘 패널
    private boolean isEmojiPanelVisible = false; // 이모티콘 패널의 표시 상태

    // 이모티콘 코드와 이미지 경로 매핑
    private Map<String, String> emojiMap;

    public ChatPanel(String channelName,PrintWriter out) { //chats.txt에서 내용 불러오기
        setLayout(new BorderLayout());
        setBackground(new Color(47, 49, 54));

        // 이모티콘 맵 초기화
        emojiMap = new HashMap<>();
        emojiMap.put(":emoji1:", "resources/emoji/emoticon.png");
        emojiMap.put(":emoji2:", "resources/emoji/emoticon2.png");

        // 채팅 영역 설정
        chatArea = new JTextPane(); // JTextArea에서 JTextPane으로 변경
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(47, 49, 54));
        chatArea.setForeground(new Color(220, 221, 222));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(chatScrollPane, BorderLayout.CENTER);

        // 채팅 입력 영역 설정
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(47, 49, 54));

        chatInput = new JTextField();
        chatInput.setBackground(new Color(64, 68, 75));
        chatInput.setForeground(new Color(220, 221, 222));
        chatInput.setCaretColor(new Color(220, 221, 222));
        inputPanel.add(chatInput, BorderLayout.CENTER);

        // 이모티콘 버튼
        JButton emojiButton = new JButton("😊");  // 이모티콘 버튼
        emojiButton.setBackground(new Color(47, 49, 54));
        emojiButton.setForeground(Color.WHITE);
        emojiButton.setFocusPainted(false);
        emojiButton.addActionListener(e -> toggleEmojiPanel()); // 버튼 클릭 시 이모티콘 패널 토글

        // 이모티콘 리스트 패널
        emojiPanel = new JPanel();
        emojiPanel.setLayout(new GridLayout(1, 2)); // 2개의 이모티콘 버튼을 가로로 배치
        emojiPanel.setBackground(new Color(47, 49, 54));
        emojiPanel.setVisible(false); // 처음에는 숨김

        JButton emojiButton1 = new JButton(new ImageIcon("resources/emoji/emoticon.png"));
        emojiButton1.addActionListener(e -> insertEmoji(":emoji1:")); // 이모티콘 코드 삽입
        emojiPanel.add(emojiButton1);

        JButton emojiButton2 = new JButton(new ImageIcon("resources/emoji/emoticon2.png"));
        emojiButton2.addActionListener(e -> insertEmoji(":emoji2:")); // 이모티콘 코드 삽입
        emojiPanel.add(emojiButton2);

        // 이모티콘 버튼과 메시지 전송 버튼을 포함하는 패널
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(47, 49, 54));

        // 이모티콘 버튼을 send 버튼의 왼쪽에 배치
        buttonPanel.add(emojiButton, BorderLayout.WEST);

        // 메시지 전송 버튼
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(88, 101, 242));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        buttonPanel.add(sendButton, BorderLayout.EAST);

        inputPanel.add(buttonPanel, BorderLayout.EAST); // 입력 패널에 버튼 패널 추가

        add(inputPanel, BorderLayout.SOUTH);
        add(emojiPanel, BorderLayout.NORTH); // 이모티콘 패널을 상단에 추가

        // 채팅 메시지 전송 이벤트
        chatInput.addActionListener(e -> sendMessage(out));
        sendButton.addActionListener(e -> sendMessage(out));
    }

    // 이모티콘 패널 표시/숨김
    private void toggleEmojiPanel() {
        isEmojiPanelVisible = !isEmojiPanelVisible;
        emojiPanel.setVisible(isEmojiPanelVisible);
    }

    // 이모티콘을 채팅 입력란에 추가하는 메서드
    private void insertEmoji(String emojiCode) {
        chatInput.setText(chatInput.getText() + " " + emojiCode);
    }

    public void appendMessage(String message) {
        StyledDocument doc = chatArea.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);

        // 이모티콘 코드가 포함된 메시지를 처리
        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            if (message.contains(entry.getKey())) {
                // 이모티콘 코드가 포함되어 있으면 이미지로 교체
                message = message.replace(entry.getKey(), " "); // 이미지로 대체할 공백 추가
                try {
                    doc.insertString(doc.getLength(), " ", attrs); // 공백 추가
                    chatArea.insertIcon(new ImageIcon(entry.getValue())); // 이미지 추가
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            doc.insertString(doc.getLength(), message + "\n", attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(PrintWriter out) {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            out.println(message); // 이모티콘 코드 포함된 메시지를 서버로 전송
            chatInput.setText("");
        }
    }
}
