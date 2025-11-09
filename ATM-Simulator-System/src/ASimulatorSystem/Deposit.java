
package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.sql.*;

public class Deposit extends JFrame implements ActionListener{
    
    JTextField t1;
    JButton b1,b2;
    JLabel l1;
    String pin;

    public Deposit(String pin){
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 960, 1080);
        add(l3);
        
        l1 = new JLabel("ENTER AMOUNT YOU WANT TO DEPOSIT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        
        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 22));
        
        b1 = new JButton("DEPOSIT");
        b2 = new JButton("BACK");
        
        setLayout(null);
        
        l1.setBounds(190,350,400,35);
        l3.add(l1);
        
        t1.setBounds(190,420,320,25);
        l3.add(t1);
        
        b1.setBounds(390,588,150,35);
        l3.add(b1);
        
        b2.setBounds(390,633,150,35);
        l3.add(b2);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        
        setSize(960,1080);
        setUndecorated(true);
        setLocation(500,0);
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
            JOptionPane.showMessageDialog(null, "Please enter the amount you want to deposit.");
            return;
        }

        int depositAmount;
        try {
            depositAmount = Integer.parseInt(amountStr);
            if (depositAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive amount.");
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

            // Update balance
            int newBalance = currentBalance + depositAmount;
            String updateBalanceQuery = "UPDATE account_balance SET balance = ? WHERE pin = ?";
            PreparedStatement updateBalancePstmt = conn.c.prepareStatement(updateBalanceQuery);
            updateBalancePstmt.setInt(1, newBalance);
            updateBalancePstmt.setString(2, pin);
            updateBalancePstmt.executeUpdate();

            // Record transaction
            String transactionQuery = "INSERT INTO bank (pin, date, mode, amount) VALUES (?, ?, 'Deposit', ?)";
            PreparedStatement transactionPstmt = conn.c.prepareStatement(transactionQuery);
            transactionPstmt.setString(1, pin);
            transactionPstmt.setString(2, new Date().toString());
            transactionPstmt.setInt(3, depositAmount);
            transactionPstmt.executeUpdate();

            conn.c.commit();

            JOptionPane.showMessageDialog(null, "Rs. " + depositAmount + " deposited successfully.");

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
        new Deposit("").setVisible(true);
    }
}