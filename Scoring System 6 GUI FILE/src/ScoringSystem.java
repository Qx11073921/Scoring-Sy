import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;

public class ScoringSystem {
    private static BufferedReader stdIn =
            new  BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter stdOut =
            new  PrintWriter(System.out, true);
    private static PrintWriter  stdErr =
            new  PrintWriter(System.err, true);
    private Vector<User> users;
    private Administrator administrator;
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel userInterfacePanel;
    private JPanel administratorInterfacePanel;
    private final CardLayout cardLayout;
    private /*final*/ JPanel overallPanel;//放置cardLayout布局器实现界面切换
    private String[] className;
    private String classChosen;
    private String ItemChosen;

//main函数
    public static void main(String[] args) throws IOException, DataFormatException {
        ScoringSystem application = new ScoringSystem();
       // application.run();
    }
//初始化
    private ScoringSystem() throws DataFormatException, IOException {
        //administrator = loadAdministrator();
        administrator = new FileAdministratorLoader().loadAdministrator("");
        users = new FileUserLoader().loadUser("");
        frame = new JFrame("评分系统");
        //设置卡片布局器
        cardLayout = new CardLayout();
        overallPanel = new JPanel();
        overallPanel.setLayout(cardLayout);
        //设置三个切换界面内容
        loginPanel = setLoginPanel();
        administratorInterfacePanel = setAdministratorInterfacePanel();
        userInterfacePanel = setUserInterfacePanel();
        //加入到卡片布局器中，提供限制条件方便其转换
        overallPanel.add(loginPanel,"login");
        overallPanel.add(userInterfacePanel,"user");
        overallPanel.add(administratorInterfacePanel,"administrator");
        //加入到frame中
        frame.setContentPane(overallPanel);
       // frame.add(overallPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    //设置登陆界面
    private JPanel setLoginPanel(){
        JPanel loginOverAllPanel = new JPanel();
        loginOverAllPanel.setLayout(new GridLayout(2,1));
        /*设置上部面板内容（显示大标题、按钮显示版权信息）*/
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        JTextField titleLabel = new JTextField("评分系统登陆界面");//字体字形大小未设置
        titleLabel.setFont(new Font("宋体",Font.BOLD,50));
        titleLabel.setHorizontalAlignment(JTextField.CENTER);
        titleLabel.setEditable(false);
        JButton copyRightButton = new JButton("显示版权信息");

        upperPanel.add(copyRightButton,"North");
        upperPanel.add(titleLabel,"Center");
        upperPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        /*设置中间登陆界面信息*/
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(3,1));
        //设置用户名项
        JPanel idPanel = new JPanel();
        JLabel id = new JLabel("用户名:");
        JTextField idText = new JTextField(20);
        idPanel.add(id);
        idPanel.add(idText);
        //设置密码项
        JPanel passwordPanel = new JPanel();
        JLabel password = new JLabel("密码:    ");
        JTextField passwordText = new JTextField(20);
        passwordPanel.add(password);
        passwordPanel.add(passwordText);
        //进入按钮
        JButton enterButton = new JButton("进入");

        middlePanel.add(idPanel);
        middlePanel.add(passwordPanel);
        middlePanel.add(enterButton);
        middlePanel.setBorder(BorderFactory.createLoweredBevelBorder());

        /*设置下部面板（勾选栏）*/
        JPanel bottomPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton userLogin = new JRadioButton("用户登录",true);
        JRadioButton userRegister = new JRadioButton("用户注册");
        JRadioButton administratorLogin = new JRadioButton("管理员登录");
        JRadioButton administratorRegister = new JRadioButton("管理员注册");
        userLogin.setActionCommand("1");
        userRegister.setActionCommand("2");
        administratorLogin.setActionCommand("3");
        administratorRegister.setActionCommand("4");
        group.add(userLogin);
        group.add(userRegister);
        group.add(administratorLogin);
        group.add(administratorRegister);
        bottomPanel.add(userLogin);
        bottomPanel.add(userRegister);
        bottomPanel.add(administratorLogin);
        bottomPanel.add(administratorRegister);
        bottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        /*3个面板加入到overallPanel*/
        JPanel middleAndBottom = new JPanel();
        middleAndBottom.setLayout(new BorderLayout());
        middleAndBottom.add(middlePanel,"Center");
        middleAndBottom.add(bottomPanel,"South");
        loginOverAllPanel.add(upperPanel);
        loginOverAllPanel.add(middleAndBottom);

        /*监听器部分*/
        //设置版权信息按钮监听器，显示对话框
        copyRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        copyRightInformation(),"版权信息",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        //设置enter按钮界面监听器
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = Integer.parseInt(group.getSelection().getActionCommand());
                String id = idText.getText();
                String password = passwordText.getText();
                if (id==null||password==null||id.equals("")||password.equals("")){
                    JOptionPane.showMessageDialog(null,
                            "部分信息未输入","错误警告",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        enter(userChoice,id,password);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }


                idText.setText("");
                passwordText.setText("");
            }
        });
        return loginOverAllPanel;
    }
    //版权信息
    private String copyRightInformation() {
        String time = "软件完成时间: 2024年6月\n";
        String coder = "软件创作人: 许天齐、李舜乾";
        return time+coder;
    }
    private void enter (int userChoice, String id, String password) throws IOException {
        int flag = 0;
        //用户登录
        if (userChoice == 1){
            for (Iterator i = users.iterator(); i.hasNext() ; ) {
                User user = (User) i.next();
                if (id.equals(user.getId()) &&
                        password.equals(user.getPassword())){
                    JOptionPane.showMessageDialog(null,
                            "用户登录成功","登录提示",
                            JOptionPane.PLAIN_MESSAGE);
                    flag++;
                    cardLayout.show(overallPanel,"user");
                }
            }
            if (flag == 0){
                JOptionPane.showMessageDialog(null,
                        "用户登录失败,用户名或密码错误","登录警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        //用户注册
        else if (userChoice == 2){
            User user = new User(id,password);
            users.add(user);
            JOptionPane.showMessageDialog(null,
                    "用户注册成功, 用户名: "+id+ "用户密码: "+password,
                    "注册提示",
                    JOptionPane.PLAIN_MESSAGE);
            writeRegisterInformationToFile("user.txt",id,password);

        }
        //管理员登录
        else if (userChoice == 3){
            if (id.equals(administrator.getId()) &&
                    password.equals(administrator.getPassword())){
                JOptionPane.showMessageDialog(null,
                        "管理员登录成功","登录提示",
                        JOptionPane.PLAIN_MESSAGE);
                cardLayout.show(overallPanel,"administrator");
            }else {
                JOptionPane.showMessageDialog(null,
                        "管理员登录失败","登录警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        //管理员注册
        else if (userChoice == 4){
            if (Administrator.getAdministratorNumber() == 0){
                if (id.equals("")||password.equals("")){
                    JOptionPane.showMessageDialog(null,
                            "未填写管理员用户名或密码","注册警告",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    //设置该管理员的id和密码
                    Administrator.getAdministrator().setInformation(id, password);
                    JOptionPane.showMessageDialog(null,
                            "已注册管理员, 用户名: "+Administrator.getAdministrator().getId()+
                                    " ,密码: "+Administrator.getAdministrator().getPassword()
                            ,"注册提示",
                            JOptionPane.PLAIN_MESSAGE);
                    writeRegisterInformationToFile("administrator.txt",id,password);
                }

            }else {
                JOptionPane.showMessageDialog(null,
                        "管理员数目已达上限","注册警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        //错误提示
        else {
            JOptionPane.showMessageDialog(null,
                    "未勾选复选框!","警告",
                    JOptionPane.WARNING_MESSAGE);
        }

    }
    private void writeRegisterInformationToFile(String fileName,String id,String password) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName,true));
        out.write(id+"_"+password+"\n");
        out.close();
    }

    private JPanel setUserInterfacePanel(){
        JPanel overallPanel = new JPanel();
        overallPanel.setLayout(new BorderLayout());
        JButton exit = new JButton("退出");
        //主体界面（不含退出键）
        JPanel userOverallPanel2 = new JPanel();
        userOverallPanel2.setLayout(new GridLayout(3,1));
        /*设置上部两选择列表*/
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1,2));
        topPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        //左：评分大类
        JPanel classPanel = new JPanel();
        JLabel classLabel = new JLabel("评分大类");
        classPanel.setLayout(new BorderLayout());
        classLabel.setFont(new Font("宋体",Font.BOLD,20));
        classLabel.setHorizontalAlignment(JTextField.CENTER);
        classLabel.setBorder(BorderFactory.createEtchedBorder());
        JPanel classListPanel = new JPanel();
        //设置大类选择列表
        String[] classname = {"大类"};
        JList classList = new JList(classname);
        classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        classListPanel.add(
                new JScrollPane(classList,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        classPanel.add(classLabel,"North");
        classPanel.add(classList,"Center");
        classPanel.setBorder(BorderFactory.createEtchedBorder());

        //右：评分对象
        JPanel ItemPanel = new JPanel();
        JLabel ItemLabel = new JLabel("评分对象");
        ItemPanel.setLayout(new BorderLayout());
        ItemLabel.setFont(new Font("宋体",Font.BOLD,20));
        ItemLabel.setHorizontalAlignment(JTextField.CENTER);
        ItemLabel.setBorder(BorderFactory.createEtchedBorder());
        JPanel ItemListPanel = new JPanel();
        JList ItemList = new JList<>();//待改 未完成
        ItemListPanel.add(
                new JScrollPane(ItemList,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        ItemPanel.add(ItemLabel,"North");
        ItemPanel.add(ItemListPanel,"Center");
        ItemPanel.setBorder(BorderFactory.createEtchedBorder());
        //加入大类选择栏和对象选择栏
        topPanel.add(classPanel);
        topPanel.add(ItemPanel);
        topPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        /*设置中部输入栏*/
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,1));
        //评分输入文本框
        JPanel scorePanel = new JPanel();
        JLabel scoreLabel = new JLabel("请输入评分(0-10分):");
        JTextField scoreText = new JTextField(10);
        scorePanel.add(scoreLabel);
        scorePanel.add(scoreText);
        scorePanel.setBorder(BorderFactory.createEtchedBorder());
        inputPanel.add(scorePanel);
        //评价输入文本框
        JPanel commentPanel = new JPanel();
        JLabel commentLabel = new JLabel("请输入文字评价:");
        JTextField commentText = new JTextField(10);
        commentPanel.add(commentLabel);
        commentPanel.add(commentText);
        commentPanel.setBorder(BorderFactory.createEtchedBorder());
        inputPanel.add(commentPanel);
        //中部按钮
        JButton enterButton = new JButton("完成");
        //加入中部框中
        middlePanel.add(inputPanel,"Center");
        middlePanel.add(enterButton,"South");
        middlePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        /*设置下部显示文本框*/
        JPanel downPanel = new JPanel();
        JTextArea showCommentArea = new JTextArea(10,80);
        showCommentArea.setEnabled(false);
        downPanel.add(
                new JScrollPane(showCommentArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        downPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        /*三个panel加入到overallPanel2中*/
        userOverallPanel2.add(topPanel);
        userOverallPanel2.add(middlePanel);
        userOverallPanel2.add(downPanel);
        //加入退出按钮
        overallPanel.add(exit,"North");
        overallPanel.add(userOverallPanel2,"Center");

        /*设置事件监听器*/
        //退出按钮监听器
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        //设置列表元素
        classList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                classList.setListData(getClassNameList());
                int b = classList.getLeadSelectionIndex();
                ListModel<String> model = classList.getModel();
                String className = model.getElementAt(b);

               ItemList.setListData(getItemNameList(className));
               //设置属性，存放选择的大类名称
               setClassNameChosen(className);
            }
        });
        ItemList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                showCommentArea.setText("");
                int b = ItemList.getLeadSelectionIndex();
                ListModel<String> model = ItemList.getModel();
                String itemName = model.getElementAt(b);
                //设置属性，存放选择的对象名称
                setItemChosen(itemName);
                //底部对话框打印大类信息
                ScoringItem scoringItem = getScoringItem();
                if (scoringItem==null){
                    showCommentArea.append("");
                }else {
                    showCommentArea.append(scoringItem.toString()+"\n");
                    showCommentArea.setFont(new Font("宋体",Font.BOLD,15));
                    showCommentArea.setForeground(Color.BLACK);
                    //底部对话框打印用户评价
                    int count = 1;
                    for(Iterator i = scoringItem.getUserCommentIterate(); i.hasNext();){
                        UserComment userComment = (UserComment) i.next();
                        showCommentArea.append(count+":    "+userComment.toString()+"\n\n");
                        showCommentArea.setFont(new Font("宋体",Font.BOLD,15));
                        showCommentArea.setForeground(Color.BLACK);
                        count++;
                    }
                }
            }
        });
        //设置”完成“按钮监听器，记录用户评价
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = Integer.parseInt(scoreText.getText());
                String comment = commentText.getText();
                if (score>=0&&score<=10&&comment!=null){
                    //调用函数写评价，并写入文件
                    try {
                        WriteUserComment(score,comment);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //提示框
                    JOptionPane.showMessageDialog(null,
                            "已完成用户评价添加","提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    //清空内容
                    scoreText.setText("");
                    commentText.setText("");
                }else {
                    JOptionPane.showMessageDialog(null,
                            "未按规范输入信息！","警告",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        return overallPanel;
    }
    private ScoringItem getScoringItem(){
        if (this.classChosen!=null||this.ItemChosen!=null){
            ScoringClass scoringClass = administrator.getScoringClass(this.classChosen);
            ScoringItem scoringItem = scoringClass.getScoringItem(this.ItemChosen);
            return scoringItem;
        }else {
            return null;
        }

    }
    private void WriteUserComment(int score, String comment) throws IOException {
        if (this.ItemChosen!=null&&this.classChosen!=null){
            //找到用户选择的评分对象
            ScoringClass scoringClass = administrator.getScoringClass(this.classChosen);
            ScoringItem scoringItem = scoringClass.getScoringItem(this.ItemChosen);
            //添加用户评价
            UserComment userComment = new UserComment();
            //设置评论的评分及内容,完成对useComment的初始化
            userComment.setScore(score);
            userComment.setComment(comment);
            //将userComment加入特定类（键盘输入）中的一个特定评分对象（键盘输入）中
            scoringItem.addUserComment(userComment);
            /*写入文件*/
            String classFile = scoringClass.getClassName()+".txt";//更新内容（后）
            String itemName = scoringItem.getItemName()+".txt";//在后面加（先）
            //item.txt
            BufferedWriter itemOut = new BufferedWriter(new FileWriter(itemName,true));
            itemOut.write(comment+"_"+score+"\n");
            itemOut.close();
            //class.txt
            deleteItemFile(classFile,scoringClass);

        }else {
            stdErr.println("error!");
        }
    }
    private String[] getClassNameList() {
        String[] scoringClassName = new String[administrator.getScoringClassNumber()];
        Iterator i = administrator.getClassIterator();
        for (int count = 0;i.hasNext();count++){
            ScoringClass scoringClass = (ScoringClass) i.next();
            scoringClassName[count] = scoringClass.getClassName();
        }
        return scoringClassName;
    }
    private String[] getItemNameList(String className){
        ScoringClass scoringClass = readScoringClass(className);
        if (scoringClass.getNumberOfItem()==0){
            String[] scoringItemName = new String[]{"未设立评分对象"};
            return scoringItemName;
        }else {
            String[] scoringItemName = new String[scoringClass.getNumberOfItem()];
            Iterator i2 = scoringClass.getItemIterator();
            //初始化存放评分大类名称的数组
            for (int count = 0;i2.hasNext();count++) {
                ScoringItem scoringItem = (ScoringItem) i2.next();
                scoringItemName[count] = scoringItem.getItemName();
            }
            return scoringItemName;
        }
    }
    private void setClassNameChosen(String classChosen){
        this.classChosen = classChosen;
    }
    private void setItemChosen(String itemChosen){
        this.ItemChosen = itemChosen;
    }
    private JPanel setAdministratorInterfacePanel(){
        JPanel overallPanel = new JPanel();
        overallPanel.setLayout(new BorderLayout());
        //设置最上方退出按钮
        JButton exitButton = new JButton("退出");
        //设置管理员事件四个按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        JButton addClass = new JButton("添加评分大类");
        JButton addItem = new JButton("添加评分对象");
        JButton deleteClass = new JButton("删除评分大类");
        JButton deleteItem = new JButton("删除评分对象");
        buttonPanel.add(addClass);
        buttonPanel.add(addItem);
        buttonPanel.add(deleteClass);
        buttonPanel.add(deleteItem);
        //加入到overallPanel中
        overallPanel.add(exitButton,"North");
        overallPanel.add(buttonPanel,"Center");

        /*设置按钮事件监听器*/
        //退出按钮
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        //添加大类按钮
        addClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addClass();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addItem();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteClass();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteItem();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return overallPanel;
    }
    private void exit (){
        cardLayout.show(overallPanel,"login");
    }
    //添加大类
    private void addClass() throws IOException {
        String className = JOptionPane.showInputDialog(null,
                "请输入评分大类\n",
                "添加评分大类",JOptionPane.INFORMATION_MESSAGE);
        String classDescription = JOptionPane.showInputDialog(null,
                "请输入对该评分大类的描述\n",
                "添加评分大类描述",JOptionPane.INFORMATION_MESSAGE);
        if (className == null || classDescription == null||
        className.equals("")||classDescription.equals("")){
            JOptionPane.showMessageDialog(null,
                    "未输入评分大类名称或描述"
                    ,"警告",
                    JOptionPane.WARNING_MESSAGE);
        }else {
            ScoringClass scoringClass = new ScoringClass(className,classDescription);
            administrator.addClass(scoringClass);
            JOptionPane.showMessageDialog(null,
                    "已添加评分大类: "+scoringClass.getClassName()
                    ,"提示",
                    JOptionPane.INFORMATION_MESSAGE);
            FileWriter(className,classDescription,"scoringClass.txt");
            addClassFile(scoringClass.getClassName()+".txt");
        }

    }
    //scoringItem.getItemName()+"_"+scoringItem.getItemDescription()+"_"+scoringItem.getAverageScore()+"_"+scoringItem.getScoringNumber()+"\n"
    private void FileWriter(String name, String description,String fileName)throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName,true));
        out.write(name+"_"+description+"\n");
        out.close();
    }
    private void addClassFile(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.close();
    }
    //添加评分对象
    private void addItem() throws IOException {
        //创建存放评分大类名称的数组
        Object[] scoringClassName = new String[administrator.getScoringClassNumber()];
        this.className = new String[administrator.getScoringClassNumber()];
        Iterator i = administrator.getClassIterator();
        if(!i.hasNext()){
            JOptionPane.showMessageDialog(null,
                    "还未新建大类，系统中无任何大类信息！"
                    ,"警告",
                    JOptionPane.WARNING_MESSAGE);
        }else {
            //初始化存放评分大类名称的数组
            for (int count = 0;i.hasNext();count++){
                ScoringClass scoringClass = (ScoringClass) i.next();
                scoringClassName[count] = scoringClass.getClassName();
                this.className[count] = scoringClass.getClassName();
            }
            //复选提示框（选择待加入评分对象的评分大类）
            Object classNameChosen = JOptionPane.showInputDialog(null,
                    "请选择待添加评分对象的评分大类\n","选择评分大类",
                    JOptionPane.QUESTION_MESSAGE,null, getClassNameList(),getClassNameList()[0]);
            if (classNameChosen != null){
                //输入评分对象名和描述
                String itemName = JOptionPane.showInputDialog(null,
                        "请输入评分对象名称\n",
                        "添加评分对象",JOptionPane.INFORMATION_MESSAGE);
                String itemDescription = JOptionPane.showInputDialog(null,
                        "请输入对该评分对象的描述\n",
                        "添加评分对象描述",JOptionPane.INFORMATION_MESSAGE);
                //找到评分大类
                ScoringClass scoringClass = readScoringClass((String) classNameChosen);
                //将该评分对象添加到评分大类中
                if (itemName==null||itemDescription==null||
                        itemName.equals("")||itemDescription.equals("")){
                    JOptionPane.showMessageDialog(null,
                            "未输入评分对象名称或描述"
                            ,"警告",
                            JOptionPane.WARNING_MESSAGE);
                }else{
                    ScoringItem scoringItem = new ScoringItem(itemName,itemDescription);
                    //将评分对象加入到对应的大类中
                    scoringClass.addItem(scoringItem);
                    JOptionPane.showMessageDialog(null,
                            "已添加评分对象: "+scoringItem.getItemName()
                            ,"提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    //写入文件（className1.txt）
                    String information = scoringItem.getItemName()+"_"+scoringItem.getItemDescription()+"_"+scoringItem.getAverageScore()+"_"+scoringItem.getScoringNumber()+"\n";
                    FileItemWriter(information,scoringClass.getClassName()+".txt",scoringItem.getItemName()+".txt");
                }
            }else {
                JOptionPane.showMessageDialog(null,
                        "未选择评分大类"
                        ,"警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

    }
    //scoringItem.getItemName()+"_"+scoringItem.getItemDescription()+"_"+scoringItem.getAverageScore()+"_"+scoringItem.getScoringNumber()+"\n"
    private void FileItemWriter(String information,String fileName,String itemName)throws IOException{
        //新建文件
        /*
        BufferedWriter out0 = new BufferedWriter(new FileWriter(fileName));
        out0.write("");
        out0.close();*/
        //更新到class.txt
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName,true));
        out.write(information);
        out.close();
        //新建Item.txt
        BufferedWriter out2 = new BufferedWriter(new FileWriter(itemName));
        out2.close();
    }
    private ScoringClass readScoringClass(String className){
        ScoringClass scoringClass = administrator.getScoringClass(className);
        return scoringClass;

    }
    //删除评分大类
    public void deleteClass() throws IOException {
        Object[] scoringClassName = new String[administrator.getScoringClassNumber()];
        Iterator i = administrator.getClassIterator();
        if(!i.hasNext()){
            JOptionPane.showMessageDialog(null,
                    "还未新建大类，系统中无任何大类信息！"
                    ,"警告",
                    JOptionPane.WARNING_MESSAGE);
        }else {
            //初始化存放评分大类名称的数组
            for (int count = 0;i.hasNext();count++){
                ScoringClass scoringClass = (ScoringClass) i.next();
                scoringClassName[count] = scoringClass.getClassName();
            }
            //复选提示框（选择待加入评分对象的评分大类）
            Object classNameChosen = JOptionPane.showInputDialog(null,
                    "请选择要删除的评分大类\n","删除评分大类",
                    JOptionPane.QUESTION_MESSAGE,null, scoringClassName,scoringClassName[0]);
            if (classNameChosen!=null){
                //删除评分大类
                ScoringClass scoringClass = administrator.getScoringClass((String) classNameChosen);
                //提示框
                JOptionPane.showMessageDialog(null,
                        "已删除评分大类: "+scoringClass.getClassName()
                        ,"提示",
                        JOptionPane.INFORMATION_MESSAGE);
                administrator.removeClass(scoringClass);
                //更新评分大类scoringClass.txt文件中内容（去掉一行）
                deleteClassFile();
                //删除大类class1.txt（删除该评分大类中的所有对象）
                deleteFile(scoringClass.getClassName()+".txt");
                //删除该大类中所有Item对应的item.txt（删除评分对象用于存放用户评价的文件）
                deleteAllItem(scoringClass);
            }else {
                JOptionPane.showMessageDialog(null,
                        "未选择评分大类"
                        ,"警告",
                        JOptionPane.WARNING_MESSAGE);
            }

        }
    }
    //更新评分大类scoringClass.txt文件中内容（去掉一行）
    private void deleteClassFile() throws IOException {
        int count = 0;
        BufferedWriter out1 = new BufferedWriter(new FileWriter("scoringClass.txt"));
        BufferedWriter out2 = new BufferedWriter(new FileWriter("scoringClass.txt",true));
        for(Iterator i = administrator.getClassIterator();i.hasNext();count++){
            ScoringClass scoringClass = (ScoringClass) i.next();
            if (count == 0){
                out1.write(scoringClass.getClassName()+"_"+scoringClass.getClassDescription()+"\n");
                out1.close();
            }else {
                out2.write(scoringClass.getClassName()+"_"+scoringClass.getClassDescription()+"\n");
            }
        }
        out2.close();
    }
    private void deleteAllItem(ScoringClass scoringClass){
        for (Iterator i = scoringClass.getItemIterator(); i.hasNext(); ) {
            ScoringItem scoringItem = (ScoringItem)i.next();
            deleteFile(scoringItem.getItemName()+".txt");
        }
    }

    private void deleteFile(String fileName){
        File file = new File(fileName);
        file.delete();
    }
    private void deleteItem() throws IOException {
        //创建存放评分大类名称的数组
        Object[] scoringClassName = new String[administrator.getScoringClassNumber()];
        Iterator i = administrator.getClassIterator();
        if(!i.hasNext()){
            JOptionPane.showMessageDialog(null,
                    "还未新建大类，系统中无任何大类信息！"
                    ,"警告",
                    JOptionPane.WARNING_MESSAGE);
        }else {
            //初始化存放评分大类名称的数组
            for (int count = 0;i.hasNext();count++){
                ScoringClass scoringClass = (ScoringClass) i.next();
                scoringClassName[count] = scoringClass.getClassName();
            }
            //复选提示框（选择待加入评分对象的评分大类）
            Object classNameChosen = JOptionPane.showInputDialog(null,
                    "请选择待删除评分对象的评分大类\n","选择评分大类",
                    JOptionPane.QUESTION_MESSAGE,null, scoringClassName,scoringClassName[0]);
            if (classNameChosen != null){
                //找到评分大类
                ScoringClass scoringClass = readScoringClass((String) classNameChosen);
                Object[] scoringItemName = new String[scoringClass.getNumberOfItem()];
                Iterator i2 = scoringClass.getItemIterator();
                //查看该评分大类中是否有评分对象
                if(!i2.hasNext()) {
                    JOptionPane.showMessageDialog(null,
                            "大类: "+scoringClass.getClassName()+" 中不含评分对象"
                            , "警告",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    //初始化存放评分大类名称的数组
                    for (int count = 0;i2.hasNext();count++){
                        ScoringItem scoringItem = (ScoringItem) i2.next();
                        scoringItemName[count] = scoringItem.getItemName();
                    }
                    //复选提示框（选择待加入评分对象的评分大类）
                    Object ItemNameChosen = JOptionPane.showInputDialog(null,
                            "请选择待删除的评分对象\n","选择评分对象",
                            JOptionPane.QUESTION_MESSAGE,null, scoringItemName,scoringItemName[0]);
                    if (ItemNameChosen != null){
                        //删除评分对象
                        ScoringItem scoringItem = scoringClass.getScoringItem((String) ItemNameChosen);
                        //提示框
                        JOptionPane.showMessageDialog(null,
                                "已删除评分对象: "+scoringItem.getItemName()
                                ,"提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        scoringClass.removeItem(scoringItem);
                        //删除所选item1.txt（内包含用户评价）
                        deleteFile(scoringItem.getItemName()+".txt");
                        //删除Class1.txt中指定行（更新class1.txt,去掉一行）
                        deleteItemFile(scoringClass.getClassName()+".txt",scoringClass);
                    }else {
                        JOptionPane.showMessageDialog(null,
                                "未选择评分对象"
                                ,"警告",
                                JOptionPane.WARNING_MESSAGE);
                    }

                }

            }else {
                JOptionPane.showMessageDialog(null,
                        "未选择评分大类"
                        ,"警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void deleteItemFile(String fileName, ScoringClass scoringClass) throws IOException {
        int count = 0;
        BufferedWriter out1 = new BufferedWriter(new FileWriter(fileName));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(fileName,true));
        for(Iterator i = scoringClass.getItemIterator();i.hasNext();count++){
            ScoringItem scoringItem = (ScoringItem) i.next();
            if (count == 0){
                out1.write(scoringItem.getItemName()+"_"+scoringItem.getItemDescription()+"_"+scoringItem.getAverageScore()+"_"+scoringItem.getScoringNumber()+"\n");
                out1.close();
            }else {
                out2.write(scoringItem.getItemName()+"_"+scoringItem.getItemDescription()+"_"+scoringItem.getAverageScore()+"_"+scoringItem.getScoringNumber()+"\n");
            }
        }
        out2.close();
    }
    private Administrator loadAdministrator(){

        return Administrator.getAdministrator();
        //return new Administrator("ad1","123");
    }

}
