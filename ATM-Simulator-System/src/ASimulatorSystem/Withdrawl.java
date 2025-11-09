
package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.sql.*;

public class Withdrawl extends JFrame implements ActionListener{
    
    JTextField t1,t2;
    JButton b1,b2,b3;
    JLabel l1,l2,l3,l4;
    String pin;
    Withdrawl(String pin){
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 960, 1080);
        add(l3);
        
        l1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        
        l2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));
        
        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));
        
        b1 = new JButton("WITHDRAW");
        b2 = new JButton("BACK");
        
        setLayout(null);
        
        l1.setBounds(190,350,400,20);
        l3.add(l1);
        
        l2.setBounds(190,400,400,20);
        l3.add(l2);
        
        t1.setBounds(190,450,330,30);
        l3.add(t1);
        
        b1.setBounds(390,588,150,35);
        l3.add(b1);
        
        b2.setBounds(390,633,150,35);
        l3.add(b2);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        
        setSize(960,1080);
        setLocation(500,0);
        setUndecorated(true);
        setVisible(true);
    }
    
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==b2){ 
            setVisible(false);
            new Transactions(pin).setVisible(true);
            return;
        }

        String amountStr = t1.getText();
        if (amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the amount you want to withdraw.");
            return;
        }

        int withdrawalAmount;
        try {
            withdrawalAmount = Integer.parseInt(amountStr);
            if (withdrawalAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive amount.");
                return;
            }
            if (withdrawalAmount > 10000) {
                 JOptionPane.showMessageDialog(null, "Maximum withdrawal amount is Rs. 10,000.");
                 return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric amount.");
            return;
        }

        Conn conn = new Conn();
        try {
            conn.c.setAutoCommit(false);

            // Get current balance
            String balanceQuery = "SELECT balance FROM account_balance WHERE pin = ?";
            PreparedStatement balancePstmt = conn.c.prepareStatement(balanceQuery);
            balancePstmt.setString(1, pin);
            ResultSet rs = balancePstmt.executeQuery();
            
            int currentBalance = 0;
            if (rs.next()) {
                currentBalance = rs.getInt("balance");
            }

            if (currentBalance < withdrawalAmount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                return;
            }

            // Update balance
            int newBalance = currentBalance - withdrawalAmount;
            String updateBalanceQuery = "UPDATE account_balance SET balance = ? WHERE pin = ?";
            PreparedStatement updateBalancePstmt = conn.c.prepareStatement(updateBalanceQuery);
            updateBalancePstmt.setInt(1, newBalance);
            updateBalancePstmt.setString(2, pin);
            updateBalancePstmt.executeUpdate();

            // Record transaction
            String transactionQuery = "INSERT INTO bank (pin, date, mode, amount) VALUES (?, ?, 'Withdrawal', ?)";
            PreparedStatement transactionPstmt = conn.c.prepareStatement(transactionQuery);
            transactionPstmt.setString(1, pin);
            transactionPstmt.setString(2, new Date().toString());
            transactionPstmt.setInt(3, withdrawalAmount);
            transactionPstmt.executeUpdate();

            conn.c.commit();

            JOptionPane.showMessageDialog(null, "Rs. " + withdrawalAmount + " debited successfully.");

            setVisible(false);
            new Transactions(pin).setVisible(true);

        } catch (SQLException ex) {
            try {
                conn.c.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            ex.printStackTrace();
        } finally {
            try {
                if (conn.c != null) conn.c.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    
    
    public static void main(String[] args){
        new Withdrawl("").setVisible(true);
    }
}
