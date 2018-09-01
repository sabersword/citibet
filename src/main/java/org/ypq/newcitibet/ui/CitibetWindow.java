package org.ypq.newcitibet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.ypq.newcitibet.data.BetEatStatus;
import org.ypq.newcitibet.data.Type;
import org.ypq.newcitibet.event.ButtonHandler;
import org.ypq.newcitibet.task.Login;
import org.ypq.newcitibet.timer.CitibetTask;
import org.ypq.newcitibet.utils.CitibetUtils;

/**
 * 
* @ClassName: CitibetWindow 
* @Description: citibet系统的主界面 
* @author god
* @date 2017年2月22日 下午12:39:21
 */
public class CitibetWindow {

    @Autowired(required=true)
    private JFrame frame;
    private static ApplicationContext context;
    private JTable qPlaceBetTable;
    private JTable qWinBetTable;
    private JTable qWinEatTable;
    private JTable qPlaceEatTable;
    private JTextField tbUser;
    private JPasswordField tbPassword;
    private JPasswordField tbPIN;
    private JTextField tbDate;
    private JTextField tbRaceType;
    
    private static final Logger logger = LoggerFactory.getLogger(CitibetWindow.class);
    private JTable qOrderTable;
    private JTextField tbBetHorse1;
    private JTextField tbBetHorse2;
    private JTextField tbEatHorse1;
    private JTextField tbEatHorse2;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
//                    CitibetWindow window = new CitibetWindow();
                        CitibetWindow window  = (CitibetWindow) context.getBean("citibetWindow");
                        window.initialize();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CitibetWindow() {
//        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
//        frame = new JFrame();         //如需打开视图设计,请去掉这个注释
        frame.getContentPane().setLocation(0, 0);
        frame.setBounds(0, 0, 1074, 711);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(420, 134, 218, 230);
        frame.getContentPane().add(scrollPane1);
        
        qWinBetTable = new JTable();
        qWinBetTable.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "2", "3", "4", "5"},
            },
            new String[] {
                "\u573A\u6B21", "\u9A6C", "\u91D1\u989D", "\u6298\u6263", "\u6781\u9650"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        qWinBetTable.setName("qWinBetTable");
        scrollPane1.setViewportView(qWinBetTable);
        
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(642, 134, 218, 230);
        frame.getContentPane().add(scrollPane2);
        
        qWinEatTable = new JTable();
        qWinEatTable.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "2", "3", "4", "5"},
            },
            new String[] {
                "\u573A\u6B21", "\u9A6C", "\u91D1\u989D", "\u6298\u6263", "\u6781\u9650"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        qWinEatTable.setName("qWinEatTable");
        scrollPane2.setViewportView(qWinEatTable);
        
        JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setBounds(420, 399, 218, 230);
        frame.getContentPane().add(scrollPane3);
        
        qPlaceBetTable = new JTable();
        scrollPane3.setViewportView(qPlaceBetTable);
        qPlaceBetTable.setName("qPlaceBetTable");
        qPlaceBetTable.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "2", "3", "4", "5"},
                {"6", "7", "8", "9", "10"},
                {"11", "12", "13", "14", "15"},
            },
            new String[] {
                "\u573A\u6B21", "\u9A6C", "\u91D1\u989D", "\u6298", "\u6781\u9650"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        
        JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.setBounds(642, 399, 218, 230);
        frame.getContentPane().add(scrollPane4);
        
        qPlaceEatTable = new JTable();
        qPlaceEatTable.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "2", "3", "4", "5"},
            },
            new String[] {
                "\u573A\u6B21", "\u9A6C", "\u91D1\u989D", "\u6298\u6263", "\u6781\u9650"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        qPlaceEatTable.setName("qPlaceEatTable");
        scrollPane4.setViewportView(qPlaceEatTable);
        
        JScrollPane scrollPane5 = new JScrollPane();
        scrollPane5.setBounds(0, 113, 230, 516);
        frame.getContentPane().add(scrollPane5);
        
        qOrderTable = new JTable();
        qOrderTable.setModel(new DefaultTableModel(
            new Object[][] {
                {"B", "2", "3", "4", "5", "6"},
                {"E", "2", "3", "4", "5", "6"},
            },
            new String[] {
                "\u8D4C/\u5403", "\u573A\u6B21", "\u9A6C", "\u91D1\u989D", "\u6298\u6263", "\u6781\u9650"
            }
        ) {
            Class[] columnTypes = new Class[] {
                Object.class, String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        qOrderTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        qOrderTable.getColumnModel().getColumn(0).setMinWidth(0);
        qOrderTable.getColumnModel().getColumn(0).setMaxWidth(0);
        // 设定订单表格的渲染,赌显示青色,吃显示橙色
        DefaultTableCellRenderer orderRenderer = new DefaultTableCellRenderer(){  
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                if (table.getValueAt(row, 0) != null && table.getValueAt(row, 0).toString().equals("B"))
                    setBackground(new Color(0x99CCFF));
                else
                    setBackground(new Color(0xFFFF99));
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        for (int i = 0; i < qOrderTable.getColumnModel().getColumnCount(); i++) //对每一列都进行渲染
            qOrderTable.getColumnModel().getColumn(i).setCellRenderer(orderRenderer);
        qOrderTable.setName("qOrderTable");
        scrollPane5.setViewportView(qOrderTable);
        
        JPanel panel1 = new JPanel();
        panel1.setBounds(10, 20, 130, 31);
        frame.getContentPane().add(panel1);
        
        JLabel lblUser = new JLabel("用户名");
        panel1.add(lblUser);
        
        tbUser = new JTextField();
        panel1.add(tbUser);
        tbUser.setColumns(5);
        
        JPanel panel2 = new JPanel();
        panel2.setBounds(10, 53, 130, 31);
        frame.getContentPane().add(panel2);
        
        JLabel lblPassword = new JLabel("密码");
        panel2.add(lblPassword);
        
        tbPassword = new JPasswordField();
        tbPassword.setColumns(5);
        panel2.add(tbPassword);
        
        JPanel panel3 = new JPanel();
        panel3.setBounds(139, 20, 100, 31);
        frame.getContentPane().add(panel3);
        
        JLabel lblPIN = new JLabel("PIN");
        panel3.add(lblPIN);
        
        tbPIN = new JPasswordField();
        tbPIN.setColumns(5);
        panel3.add(tbPIN);
        
        JPanel panel4 = new JPanel();
        panel4.setBounds(240, 20, 130, 31);
        frame.getContentPane().add(panel4);
        
        JLabel lblDate = new JLabel("日期");
        panel4.add(lblDate);
        
        tbDate = new JTextField();
        tbDate.setName("tbDate");
        tbDate.setColumns(8);
        panel4.add(tbDate);
        
        JPanel panel5 = new JPanel();
        panel5.setBounds(368, 20, 130, 31);
        frame.getContentPane().add(panel5);
        
        JLabel lblRaceType = new JLabel("比赛类型");
        panel5.add(lblRaceType);
        
        tbRaceType = new JTextField();
        tbRaceType.setName("tbRaceType");
        tbRaceType.setText("3H");
        tbRaceType.setColumns(5);
        panel5.add(tbRaceType);
        
        JButton btnLogin = new JButton("登录");
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Login login = (Login) context.getBean("login");
                login.setUid(tbUser.getText());
                login.setPass(new String(tbPassword.getPassword()));
                login.setPinCode(new String(tbPIN.getPassword()));
                login.testImg();
            }
        });
        
        JPanel panel7 = new JPanel();
        panel7.setBounds(228, 178, 186, 58);
        frame.getContentPane().add(panel7);
        
        JSpinner spinnerWinBet1 = new JSpinner();
        spinnerWinBet1.setName("spinnerWinBet1");
        spinnerWinBet1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        panel7.add(spinnerWinBet1);
        
        JLabel lbl1 = new JLabel("≤WIN赌≤");
        lbl1.setFont(new Font("幼圆", Font.BOLD, 12));
        panel7.add(lbl1);
        
        JSpinner spinnerWinBet2 = new JSpinner();
        spinnerWinBet2.setName("spinnerWinBet2");
        spinnerWinBet2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        panel7.add(spinnerWinBet2);
        
        
        ButtonHandler winBetBtnHandler = (ButtonHandler) context.getBean("winBetBtnHandler");
        JButton btnWinBet = new JButton("开始Q赌");
        btnWinBet.addMouseListener(winBetBtnHandler);
        btnWinBet.setFont(new Font("幼圆", Font.BOLD, 12));
        btnWinBet.setForeground(Color.BLACK);
        btnWinBet.setName("btnWinBet");
        btnWinBet.setBackground(Color.CYAN);
        panel7.add(btnWinBet);
        
        JSpinner spinnerWinBetAmount = new JSpinner();
        spinnerWinBetAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerWinBetAmount.setName("spinnerWinBetAmount");
        panel7.add(spinnerWinBetAmount);
        
        JPanel panel8 = new JPanel();
        panel8.setBounds(861, 178, 186, 58);
        frame.getContentPane().add(panel8);
        
        JSpinner spinnerWinEat1 = new JSpinner();
        spinnerWinEat1.setName("spinnerWinEat1");
        spinnerWinEat1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        panel8.add(spinnerWinEat1);
        
        JLabel lbl2 = new JLabel("≤WIN吃≤");
        lbl2.setFont(new Font("幼圆", Font.BOLD, 12));
        panel8.add(lbl2);
        
        JSpinner spinnerWinEat2 = new JSpinner();
        spinnerWinEat2.setName("spinnerWinEat2");
        spinnerWinEat2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        panel8.add(spinnerWinEat2);
        
        ButtonHandler winEatBtnHandler = (ButtonHandler) context.getBean("winEatBtnHandler");
        JButton btnWinEat = new JButton("开始Q吃");
        btnWinEat.addMouseListener(winEatBtnHandler);
        btnWinEat.setName("btnWinEat");
        btnWinEat.setForeground(Color.BLACK);
        btnWinEat.setFont(new Font("幼圆", Font.BOLD, 12));
        btnWinEat.setBackground(Color.ORANGE);
        panel8.add(btnWinEat);
        
        JSpinner spinnerWinEatAmount = new JSpinner();
        spinnerWinEatAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerWinEatAmount.setName("spinnerWinEatAmount");
        panel8.add(spinnerWinEatAmount);
        
        JPanel panel9 = new JPanel();
        panel9.setBounds(228, 369, 196, 58);
        frame.getContentPane().add(panel9);
        
        JSpinner spinnerPlaceBet1 = new JSpinner();
        spinnerPlaceBet1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerPlaceBet1.setName("spinnerPlaceBet1");
        panel9.add(spinnerPlaceBet1);
        
        JLabel lbl3 = new JLabel("≤PLACE赌≤");
        lbl3.setFont(new Font("幼圆", Font.BOLD, 12));
        panel9.add(lbl3);
        
        JSpinner spinnerPlaceBet2 = new JSpinner();
        spinnerPlaceBet2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerPlaceBet2.setName("spinnerPlaceBet2");
        panel9.add(spinnerPlaceBet2);
        
        ButtonHandler placeBetBtnHandler = (ButtonHandler) context.getBean("placeBetBtnHandler");
        JButton btnPlaceBet = new JButton("开始PQ赌");
        btnPlaceBet.addMouseListener(placeBetBtnHandler);
        btnPlaceBet.setName("btnPlaceBet");
        btnPlaceBet.setForeground(Color.BLACK);
        btnPlaceBet.setFont(new Font("幼圆", Font.BOLD, 12));
        btnPlaceBet.setBackground(Color.CYAN);
        panel9.add(btnPlaceBet);
        
        JSpinner spinnerPlaceBetAmount = new JSpinner();
        spinnerPlaceBetAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerPlaceBetAmount.setName("spinnerPlaceBetAmount");
        panel9.add(spinnerPlaceBetAmount);
        
        JPanel panel10 = new JPanel();
        panel10.setBounds(861, 369, 196, 58);
        frame.getContentPane().add(panel10);
        
        JSpinner spinnerPlaceEat1 = new JSpinner();
        spinnerPlaceEat1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerPlaceEat1.setName("spinnerPlaceEat1");
        panel10.add(spinnerPlaceEat1);
        
        JLabel lbl4 = new JLabel("≤PLACE吃≤");
        lbl4.setFont(new Font("幼圆", Font.BOLD, 12));
        panel10.add(lbl4);
        
        JSpinner spinnerPlaceEat2 = new JSpinner();
        spinnerPlaceEat2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerPlaceEat2.setName("spinnerPlaceEat2");
        panel10.add(spinnerPlaceEat2);
        
        ButtonHandler placeEatBtnHandler = (ButtonHandler) context.getBean("placeEatBtnHandler");
        JButton btnPlaceEat = new JButton("开始QP吃");
        btnPlaceEat.addMouseListener(placeEatBtnHandler);
        btnPlaceEat.setName("btnPlaceEat");
        btnPlaceEat.setForeground(Color.BLACK);
        btnPlaceEat.setFont(new Font("幼圆", Font.BOLD, 12));
        btnPlaceEat.setBackground(Color.ORANGE);
        panel10.add(btnPlaceEat);
        
        JSpinner spinnerPlaceEatAmount = new JSpinner();
        spinnerPlaceEatAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerPlaceEatAmount.setName("spinnerPlaceEatAmount");
        panel10.add(spinnerPlaceEatAmount);
        
        JPanel panel11 = new JPanel();
        panel11.setBounds(228, 557, 196, 58);
        frame.getContentPane().add(panel11);
        
        JSpinner spinnerFollowWinBet1 = new JSpinner();
        spinnerFollowWinBet1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerFollowWinBet1.setName("spinnerFollowWinBet1");
        panel11.add(spinnerFollowWinBet1);
        
        JLabel lbl9 = new JLabel("≤WIN赌≤");
        lbl9.setFont(new Font("幼圆", Font.BOLD, 12));
        panel11.add(lbl9);
        
        JSpinner spinnerFollowWinBet2 = new JSpinner();
        spinnerFollowWinBet2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerFollowWinBet2.setName("spinnerFollowWinBet2");
        panel11.add(spinnerFollowWinBet2);
        
        ButtonHandler followWinBetBtnHandler = (ButtonHandler) context.getBean("followWinBetBtnHandler");
        JButton btnFollowWinBet = new JButton("开始跟随Q赌");
        btnFollowWinBet.addMouseListener(followWinBetBtnHandler);
        btnFollowWinBet.setName("btnFollowWinBet");
        btnFollowWinBet.setForeground(Color.BLACK);
        btnFollowWinBet.setFont(new Font("幼圆", Font.BOLD, 12));
        btnFollowWinBet.setBackground(Color.CYAN);
        panel11.add(btnFollowWinBet);
        
        JSpinner spinnerFollowWinBetAmount = new JSpinner();
        spinnerFollowWinBetAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerFollowWinBetAmount.setName("spinnerFollowWinBetAmount");
        panel11.add(spinnerFollowWinBetAmount);
        
        JPanel panel12 = new JPanel();
        panel12.setBounds(861, 557, 196, 58);
        frame.getContentPane().add(panel12);
        
        JSpinner spinnerFollowWinEat1 = new JSpinner();
        spinnerFollowWinEat1.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerFollowWinEat1.setName("spinnerFollowWinEat1");
        panel12.add(spinnerFollowWinEat1);
        
        JLabel lbl10 = new JLabel("≤WIN吃≤");
        lbl10.setFont(new Font("幼圆", Font.BOLD, 12));
        panel12.add(lbl10);
        
        JSpinner spinnerFollowWinEat2 = new JSpinner();
        spinnerFollowWinEat2.setModel(new SpinnerNumberModel(80.0, 80.0, 100.0, 1.0));
        spinnerFollowWinEat2.setName("spinnerFollowWinEat2");
        panel12.add(spinnerFollowWinEat2);
        
        ButtonHandler followWinEatBtnHandler = (ButtonHandler) context.getBean("followWinEatBtnHandler");
        JButton btnFollowWinEat = new JButton("开始跟随Q吃");
        btnFollowWinEat.addMouseListener(followWinEatBtnHandler);
        btnFollowWinEat.setName("btnFollowWinEat");
        btnFollowWinEat.setForeground(Color.BLACK);
        btnFollowWinEat.setFont(new Font("幼圆", Font.BOLD, 12));
        btnFollowWinEat.setBackground(Color.ORANGE);
        panel12.add(btnFollowWinEat);
        
        JSpinner spinnerFollowWinEatAmount = new JSpinner();
        spinnerFollowWinEatAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerFollowWinEatAmount.setName("spinnerFollowWinEatAmount");
        panel12.add(spinnerFollowWinEatAmount);
        
        JLabel label = new JLabel("尚未登录成功");
        label.setName("test");
        label.setBounds(378, 61, 93, 15);
        frame.getContentPane().add(label);
        btnLogin.setBounds(139, 61, 93, 23);
        frame.getContentPane().add(btnLogin);
        
        tbDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));      //设置日期初始值
//        tbDate.setText("22-02-2017");
//        tbUser.setText("yuvbn");
//        tbPassword.setText("mkop*2323");
//        tbPIN.setText("11989");
        
        JLabel lbl5 = new JLabel("连赢赌");
        lbl5.setForeground(Color.BLUE);
        lbl5.setFont(new Font("幼圆", Font.BOLD, 12));
        lbl5.setBounds(510, 113, 44, 15);
        frame.getContentPane().add(lbl5);
        
        JLabel lbl6 = new JLabel("连赢吃");
        lbl6.setForeground(new Color(210, 105, 30));
        lbl6.setFont(new Font("幼圆", Font.BOLD, 12));
        lbl6.setBounds(716, 113, 44, 15);
        frame.getContentPane().add(lbl6);
        
        JLabel lbl7 = new JLabel("位置连赢赌");
        lbl7.setForeground(Color.BLUE);
        lbl7.setFont(new Font("幼圆", Font.BOLD, 12));
        lbl7.setBounds(501, 374, 82, 15);
        frame.getContentPane().add(lbl7);
        
        JLabel lbl8 = new JLabel("位置连赢吃");
        lbl8.setForeground(new Color(210, 105, 30));
        lbl8.setFont(new Font("幼圆", Font.BOLD, 12));
        lbl8.setBounds(703, 374, 82, 15);
        frame.getContentPane().add(lbl8);
        
        JLabel lblBlance = new JLabel("信用余额");
        lblBlance.setName("lblBlance");
        lblBlance.setFont(new Font("幼圆", Font.BOLD, 12));
        lblBlance.setBounds(30, 5, 169, 15);
        frame.getContentPane().add(lblBlance);
        
        JLabel lblPL = new JLabel("输赢");
        lblPL.setName("lblPL");
        lblPL.setFont(new Font("幼圆", Font.BOLD, 12));
        lblPL.setBounds(239, 5, 169, 15);
        frame.getContentPane().add(lblPL);
        
        JLabel lblStartTime = new JLabel("开始时间");
        lblStartTime.setName("lblStartTime");
        lblStartTime.setFont(new Font("幼圆", Font.BOLD, 12));
        lblStartTime.setBounds(445, 5, 169, 15);
        frame.getContentPane().add(lblStartTime);
        
        JLabel lblLeftTime = new JLabel("剩余时间");
        lblLeftTime.setName("lblLeftTime");
        lblLeftTime.setFont(new Font("幼圆", Font.BOLD, 12));
        lblLeftTime.setBounds(683, 5, 143, 15);
        frame.getContentPane().add(lblLeftTime);
        
        
        JPanel panel6 = new JPanel();
        panel6.setBounds(240, 53, 130, 31);
        frame.getContentPane().add(panel6);
        
        JLabel lblRace = new JLabel("场次");
        panel6.add(lblRace);
        
        JComboBox cbRace = new JComboBox();
        cbRace.addItemListener(new ItemListener() { //将所有按钮复位,清除记录的下单状态
            public void itemStateChanged(ItemEvent e) {
                BetEatStatus winBetStatus = (BetEatStatus) context.getBean("winBetStatus");
                BetEatStatus winEatStatus = (BetEatStatus) context.getBean("winEatStatus");
                BetEatStatus placeBetStatus = (BetEatStatus) context.getBean("placeBetStatus");
                BetEatStatus placeEatStatus = (BetEatStatus) context.getBean("placeEatStatus");
                BetEatStatus followWinBetStatus = (BetEatStatus) context.getBean("followWinBetStatus");
                BetEatStatus followWinEatStatus = (BetEatStatus) context.getBean("followWinEatStatus");
                winBetStatus.clear();
                winEatStatus.clear();
                placeBetStatus.clear();
                placeEatStatus.clear();
                followWinBetStatus.clear();
                followWinEatStatus.clear();
                //按钮复位
                btnWinBet.setText("开始" + btnWinBet.getText().substring(2));
                btnWinBet.setBackground(Color.CYAN);
                btnWinEat.setText("开始" + btnWinEat.getText().substring(2));
                btnWinEat.setBackground(Color.ORANGE);
                btnPlaceBet.setText("开始" + btnPlaceBet.getText().substring(2));
                btnPlaceBet.setBackground(Color.CYAN);
                btnPlaceEat.setText("开始" + btnPlaceEat.getText().substring(2));
                btnPlaceEat.setBackground(Color.ORANGE);
                btnFollowWinBet.setText("开始" + btnFollowWinBet.getText().substring(2));
                btnFollowWinBet.setBackground(Color.CYAN);
                btnFollowWinEat.setText("开始" + btnFollowWinEat.getText().substring(2));
                btnFollowWinEat.setBackground(Color.ORANGE);
            }
        });
        cbRace.setName("cbRace");
        cbRace.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"}));
        cbRace.setMaximumRowCount(14);
        panel6.add(cbRace);
        
        JPanel panel13 = new JPanel();
        panel13.setBounds(503, 30, 257, 73);
        frame.getContentPane().add(panel13);
        panel13.setLayout(null);
        
        JLabel lblBetHorse1 = new JLabel("马1");
        lblBetHorse1.setBounds(10, 10, 25, 15);
        panel13.add(lblBetHorse1);
        
        JLabel lblBetHorse2 = new JLabel("马2");
        lblBetHorse2.setBounds(66, 10, 30, 15);
        panel13.add(lblBetHorse2);
        
        tbBetHorse1 = new JTextField();
        tbBetHorse1.setBounds(30, 7, 18, 21);
        tbBetHorse1.setColumns(2);
        panel13.add(tbBetHorse1);
        
        tbBetHorse2 = new JTextField();
        tbBetHorse2.setDocument((PlainDocument) context.getBean("space2PlusDocument"));
        tbBetHorse2.addFocusListener((FocusAdapter) context.getBean("multiHorseAdapter"));
        tbBetHorse2.setBounds(86, 7, 80, 21);
        tbBetHorse2.setColumns(50);
        panel13.add(tbBetHorse2);
        
        JLabel lblBetAmount = new JLabel("金额");
        lblBetAmount.setBounds(10, 44, 37, 15);
        panel13.add(lblBetAmount);
        
        JSpinner spinnerBetAmount = new JSpinner();
        spinnerBetAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerBetAmount.setBounds(43, 41, 53, 22);
        panel13.add(spinnerBetAmount);
        spinnerBetAmount.setName("spinnerWinBetAmount");
        
        JButton manualWinBet = new JButton("Q赌");
        manualWinBet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String secondHorse : CitibetUtils.parseMultiHorse(tbBetHorse2.getText().trim())) {
                    String hss = "";
                    hss += tbBetHorse1.getText().trim();
                    hss += "_";
                    hss += secondHorse;
                    int amount = (Integer) spinnerBetAmount.getValue();
                    CitibetUtils.manualBetAndEat(Type.WIN_BET, amount, hss);
                }
            }
        });
        manualWinBet.setFont(new Font("幼圆", Font.BOLD, 12));
        manualWinBet.setForeground(Color.BLUE);
        manualWinBet.setBounds(111, 41, 59, 23);
        panel13.add(manualWinBet);
        
        JButton manualPlaceBet = new JButton("PQ赌");
        manualPlaceBet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String secondHorse : CitibetUtils.parseMultiHorse(tbBetHorse2.getText().trim())) {
                    String hss = "";
                    hss += tbBetHorse1.getText().trim();
                    hss += "_";
                    hss += secondHorse;
                    int amount = (Integer) spinnerBetAmount.getValue();
                    CitibetUtils.manualBetAndEat(Type.PLACE_BET, amount, hss);
                }
            }
        });
        manualPlaceBet.setForeground(Color.BLUE);
        manualPlaceBet.setFont(new Font("幼圆", Font.BOLD, 12));
        manualPlaceBet.setBounds(180, 41, 67, 23);
        panel13.add(manualPlaceBet);
        
        JButton clearBetHorse = new JButton("清空");
        clearBetHorse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tbBetHorse1.setText("");
                tbBetHorse2.setText("");
            }
        });
        clearBetHorse.setForeground(Color.BLACK);
        clearBetHorse.setFont(new Font("幼圆", Font.BOLD, 12));
        clearBetHorse.setBounds(176, 7, 73, 23);
        panel13.add(clearBetHorse);
        
        JPanel panel14 = new JPanel();
        panel14.setLayout(null);
        panel14.setBounds(763, 30, 274, 73);
        frame.getContentPane().add(panel14);
        
        JLabel lblEatHorse1 = new JLabel("马1");
        lblEatHorse1.setBounds(10, 10, 25, 15);
        panel14.add(lblEatHorse1);
        
        JLabel lblEatHorse2 = new JLabel("马2");
        lblEatHorse2.setBounds(71, 10, 30, 15);
        panel14.add(lblEatHorse2);
        
        tbEatHorse1 = new JTextField();
        tbEatHorse1.setColumns(2);
        tbEatHorse1.setBounds(33, 7, 18, 21);
        panel14.add(tbEatHorse1);
        
        tbEatHorse2 = new JTextField();
        tbEatHorse2.setDocument((PlainDocument) context.getBean("space2PlusDocument"));
        tbEatHorse2.addFocusListener((FocusAdapter) context.getBean("multiHorseAdapter"));
        tbEatHorse2.setColumns(50);
        tbEatHorse2.setBounds(95, 7, 80, 21);
        panel14.add(tbEatHorse2);
        
        JLabel lblEatAmount = new JLabel("金额");
        lblEatAmount.setBounds(10, 44, 37, 15);
        panel14.add(lblEatAmount);
        
        JSpinner spinnerEatAmount = new JSpinner();
        spinnerEatAmount.setModel(new SpinnerNumberModel(10, 10, 1000, 10));
        spinnerEatAmount.setName("spinnerWinBetAmount");
        spinnerEatAmount.setBounds(48, 41, 53, 22);
        panel14.add(spinnerEatAmount);
        
        JButton manualWinEat = new JButton("Q吃");
        manualWinEat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String secondHorse : CitibetUtils.parseMultiHorse(tbEatHorse2.getText().trim())) {
                    String hss = "";
                    hss += tbEatHorse1.getText().trim();
                    hss += "_";
                    hss += secondHorse;
                    int amount = (Integer) spinnerBetAmount.getValue();
                    CitibetUtils.manualBetAndEat(Type.WIN_EAT, amount, hss);
                }
            }
        });
        manualWinEat.setForeground(new Color(210, 105, 30));
        manualWinEat.setFont(new Font("幼圆", Font.BOLD, 12));
        manualWinEat.setBounds(120, 41, 59, 23);
        panel14.add(manualWinEat);
        
        JButton manualPlaceEat = new JButton("PQ吃");
        manualPlaceEat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String secondHorse : CitibetUtils.parseMultiHorse(tbEatHorse2.getText().trim())) {
                    String hss = "";
                    hss += tbEatHorse1.getText().trim();
                    hss += "_";
                    hss += secondHorse;
                    int amount = (Integer) spinnerBetAmount.getValue();
                    CitibetUtils.manualBetAndEat(Type.PLACE_EAT, amount, hss);
                }
            }
        });
        manualPlaceEat.setForeground(new Color(210, 105, 30));
        manualPlaceEat.setFont(new Font("幼圆", Font.BOLD, 12));
        manualPlaceEat.setBounds(189, 41, 67, 23);
        panel14.add(manualPlaceEat);
        
        JButton clearEatHorse = new JButton("清空");
        clearEatHorse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tbEatHorse1.setText("");
                tbEatHorse2.setText("");
            }
        });
        clearEatHorse.setForeground(Color.BLACK);
        clearEatHorse.setFont(new Font("幼圆", Font.BOLD, 12));
        clearEatHorse.setBounds(185, 7, 71, 23);
        panel14.add(clearEatHorse);
        
        CitibetTask citibetTask = (CitibetTask) context.getBean("citibetTask");
        new Timer().schedule(citibetTask, 500000, 1000);
        
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JFrame getFrame() {
        return frame;
    }
}
