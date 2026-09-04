package view.game;

import view.common.UITheme;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class ChatSection extends JPanel {

    private JTextField chatInput;
    private JTextPane chatList;

    public ChatSection(){

        setOpaque(false);

        chatInput = new JTextField();
        Dimension inputSize = new Dimension(GamePanel.CHAT_WIDTH_HALF*2, 35);
        chatInput.setFont(UITheme.CHAT_INPUT_FONT);
        chatInput.setMinimumSize(inputSize);
        chatInput.setMaximumSize(inputSize);
        chatInput.setPreferredSize(inputSize);

        chatList = new JTextPane();
        Dimension listSize = new Dimension(GamePanel.CHAT_WIDTH_HALF*2, GamePanel.CHAT_HEIGHT-50);

        DefaultCaret caret = (DefaultCaret) chatList.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(chatList);
        scrollPane.setPreferredSize(listSize);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        chatList.setFont(UITheme.CHAT_LIST_FONT);
        chatList.setEditable(false);
        chatList.setFocusable(false);

        chatInput.setMargin(new Insets(5, 8, 5, 8));

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        add(scrollPane);
        add(chatInput);

        hideChat();

    }

    public JTextField getChatInput() {
        return chatInput;
    }

    public JTextPane getChatList() {
        return chatList;
    }

    public void hideChat(){
        this.setVisible(false);
    }

    public void showChat(){
        this.setVisible(true);
    }

    public void addChat(String name, String message){
        StyledDocument doc = chatList.getStyledDocument();

        SimpleAttributeSet nameAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(nameAttr, new Color(100, 160, 255));
        StyleConstants.setBold(nameAttr, true);

        try {
            doc.insertString(doc.getLength(), name + ": ", nameAttr);
            doc.insertString(doc.getLength(), message + "\n", new SimpleAttributeSet());
        } catch (BadLocationException ignored) {}

        chatList.setCaretPosition(doc.getLength());
    }

}
