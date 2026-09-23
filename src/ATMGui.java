import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ATMGui extends JFrame {

    // =========================================================
    // MINIMALIST LIGHT THEME
    // =========================================================

    private final Color BACKGROUND = new Color(244, 246, 248);
    private final Color WHITE = new Color(255, 255, 255);
    private final Color SOFT = new Color(250, 250, 250);
    private final Color DARK = new Color(17, 24, 39);
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color LIGHT_MUTED = new Color(156, 163, 175);
    private final Color BORDER = new Color(229, 231, 235);
    private final Color BORDER_DARK = new Color(209, 213, 219);
    private final Color SUCCESS = new Color(22, 163, 74);
    private final Color DANGER = new Color(220, 38, 38);

    // =========================================================
    // LOGIN COMPONENTS
    // =========================================================

    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JLabel messageLabel;

    // =========================================================
    // TRANSACTION COMPONENTS
    // =========================================================

    private JTextField amountField;
    private JTextField transferAmountField;
    private JTextField recipientAccountField;

    /*
     * This keeps track of which text field the ATM keypad
     * should currently control.
     */
    private JTextField activeInputField;

    private JLabel transactionMessageLabel;
    private JLabel transferMessageLabel;

    private String currentTransactionType;

    // =========================================================
    // APPLICATION STATE
    // =========================================================

    private Account currentAccount;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ATMGui() {

        setTitle("ATM Simulator");

        setSize(
                1100,
                760
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        buildLoginScreen();

        setVisible(true);
    }

    // =========================================================
    // LOGIN SCREEN
    // =========================================================

    private void buildLoginScreen() {

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "WELCOME"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        42,
                        42,
                        42,
                        42
                )
        );

        JPanel loginContainer =
                new JPanel();

        loginContainer.setOpaque(false);

        loginContainer.setLayout(
                new BoxLayout(
                        loginContainer,
                        BoxLayout.Y_AXIS
                )
        );

        JPanel welcome =
                createWelcomeSection();

        loginContainer.add(welcome);

        loginContainer.add(
                Box.createVerticalStrut(30)
        );

        JPanel form =
                createLoginForm();

        loginContainer.add(form);

        content.add(
                loginContainer,
                BorderLayout.CENTER
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();

        if (accountNumberField != null) {
            accountNumberField.requestFocusInWindow();
        }
    }

    private JPanel createWelcomeSection() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("💳");

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setOpaque(true);

        icon.setBackground(
                new Color(
                        243,
                        244,
                        246
                )
        );

        icon.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        18,
                        12,
                        18
                )
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        28
                )
        );

        panel.add(icon);

        panel.add(
                Box.createVerticalStrut(18)
        );

        JLabel title =
                createTitle(
                        "Welcome",
                        28,
                        DARK
                );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(8)
        );

        JLabel subtitle =
                createCenteredText(
                        "Please enter your account number and PIN.",
                        14,
                        MUTED
                );

        panel.add(subtitle);

        return panel;
    }

    private JPanel createLoginForm() {

        JPanel wrapper =
                new JPanel();

        wrapper.setOpaque(false);

        wrapper.setLayout(
                new BoxLayout(
                        wrapper,
                        BoxLayout.Y_AXIS
                )
        );

        wrapper.setMaximumSize(
                new Dimension(
                        420,
                        300
                )
        );

        wrapper.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel accountLabel =
                createFieldLabel(
                        "ACCOUNT NUMBER"
                );

        wrapper.add(accountLabel);

        wrapper.add(
                Box.createVerticalStrut(7)
        );

        accountNumberField =
                new JTextField();

        styleTextField(
                accountNumberField
        );

        accountNumberField.setMaximumSize(
                new Dimension(
                        420,
                        48
                )
        );

        wrapper.add(
                accountNumberField
        );

        wrapper.add(
                Box.createVerticalStrut(14)
        );

        JLabel pinLabel =
                createFieldLabel(
                        "PIN"
                );

        wrapper.add(pinLabel);

        wrapper.add(
                Box.createVerticalStrut(7)
        );

        pinField =
                new JPasswordField();

        styleTextField(
                pinField
        );

        pinField.setMaximumSize(
                new Dimension(
                        420,
                        48
                )
        );

        wrapper.add(pinField);

        wrapper.add(
                Box.createVerticalStrut(14)
        );

        JButton loginButton =
                createPrimaryButton(
                        "INSERT CARD / LOGIN"
                );

        loginButton.setMaximumSize(
                new Dimension(
                        420,
                        46
                )
        );

        loginButton.addActionListener(
                event -> login()
        );

        wrapper.add(loginButton);

        wrapper.add(
                Box.createVerticalStrut(8)
        );

        messageLabel =
                new JLabel(" ");

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        messageLabel.setForeground(
                DANGER
        );

        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        wrapper.add(messageLabel);

        return wrapper;
    }

    private void login() {

        String accountNumber =
                accountNumberField
                        .getText()
                        .trim();

        String pin =
                new String(
                        pinField.getPassword()
                ).trim();

        if (accountNumber.isEmpty()
                || pin.isEmpty()) {

            messageLabel.setText(
                    "Please enter your account number and PIN."
            );

            messageLabel.setForeground(
                    DANGER
            );

            return;
        }

        List<Account> accounts =
                Storage.loadAccounts();

        Account account = null;

        for (Account item : accounts) {

            if (item.getAccountNumber()
                    .equals(accountNumber)) {

                account = item;

                break;
            }
        }

        if (account == null) {

            messageLabel.setText(
                    "Account not found."
            );

            messageLabel.setForeground(
                    DANGER
            );

            return;
        }

        if (!account.checkPin(pin)) {

            messageLabel.setText(
                    "Incorrect PIN."
            );

            messageLabel.setForeground(
                    DANGER
            );

            return;
        }

        currentAccount = account;

        showDashboard();
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void showDashboard() {

        activeInputField = null;

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "DASHBOARD"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        30,
                        42,
                        30,
                        42
                )
        );

        JPanel dashboardHeader =
                new JPanel(
                        new BorderLayout(
                                25,
                                0
                        )
                );

        dashboardHeader.setOpaque(false);

        JPanel userPanel =
                new JPanel();

        userPanel.setOpaque(false);

        userPanel.setLayout(
                new BoxLayout(
                        userPanel,
                        BoxLayout.Y_AXIS
                )
        );

        userPanel.add(
                createSmallLabel(
                        "ACCOUNT HOLDER"
                )
        );

        userPanel.add(
                Box.createVerticalStrut(5)
        );

        userPanel.add(
                createTitle(
                        currentAccount.getName(),
                        24,
                        DARK
                )
        );

        dashboardHeader.add(
                userPanel,
                BorderLayout.WEST
        );

        JPanel balance =
                createBalanceBox();

        dashboardHeader.add(
                balance,
                BorderLayout.EAST
        );

        content.add(
                dashboardHeader,
                BorderLayout.NORTH
        );

        JPanel menu =
                createMenuGrid();

        content.add(
                menu,
                BorderLayout.CENTER
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Account: "
                                + currentAccount
                                .getAccountNumber()
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();
    }

    private JPanel createBalanceBox() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                SOFT
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                14,
                                20,
                                14,
                                20
                        )
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label =
                createSmallLabel(
                        "AVAILABLE BALANCE"
                );

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(6)
        );

        JLabel balance =
                new JLabel(
                        formatCurrency(
                                currentAccount
                                        .getBalance()
                        )
                );

        balance.setForeground(DARK);

        balance.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        panel.add(balance);

        return panel;
    }

    private JPanel createMenuGrid() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout()
                );

        wrapper.setOpaque(false);

        wrapper.setBorder(
                new EmptyBorder(
                        30,
                        0,
                        0,
                        0
                )
        );

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
                                14,
                                14
                        )
                );

        grid.setOpaque(false);

        grid.add(
                createMenuButton(
                        "💰",
                        "Check Balance",
                        "View available funds",
                        () -> showBalance()
                )
        );

        grid.add(
                createMenuButton(
                        "➕",
                        "Deposit",
                        "Add money",
                        () -> showDepositScreen()
                )
        );

        grid.add(
                createMenuButton(
                        "💵",
                        "Withdraw",
                        "Withdraw cash",
                        () -> showWithdrawScreen()
                )
        );

        grid.add(
                createMenuButton(
                        "↗",
                        "Transfer",
                        "Send money",
                        () -> showTransferScreen()
                )
        );

        grid.add(
                createMenuButton(
                        "🧾",
                        "Transactions",
                        "View transaction history",
                        () -> showTransactionHistory()
                )
        );

        grid.add(
                createMenuButton(
                        "👤",
                        "Account Info",
                        "View account details",
                        () -> showAccountInformation()
                )
        );

        grid.add(
                createMenuButton(
                        "🔐",
                        "Change PIN",
                        "Update security PIN",
                        () -> showChangePinScreen()
                )
        );

        grid.add(
                createLogoutButton()
        );

        wrapper.add(
                grid,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    // =========================================================
    // BALANCE
    // =========================================================

    private void showBalance() {

        activeInputField = null;

        showResultScreen(
                "Balance Available",
                "Your current available balance is shown below.",
                formatCurrency(
                        currentAccount
                                .getBalance()
                ),
                true
        );
    }

    // =========================================================
    // DEPOSIT SCREEN
    // =========================================================

    private void showDepositScreen() {

        currentTransactionType =
                "DEPOSIT";

        showTransactionScreen(
                "Deposit",
                "Enter the amount you want to deposit.",
                "Deposit",
                false
        );
    }

    // =========================================================
    // WITHDRAW SCREEN
    // =========================================================

    private void showWithdrawScreen() {

        currentTransactionType =
                "WITHDRAW";

        showTransactionScreen(
                "Withdraw",
                "Enter the amount you want to withdraw.",
                "Withdraw",
                false
        );
    }

    // =========================================================
    // TRANSACTION SCREEN
    // =========================================================

    private void showTransactionScreen(
            String title,
            String description,
            String confirmText,
            boolean transfer
    ) {

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        title.toUpperCase()
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                40,
                                0
                        )
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        42,
                        42,
                        42,
                        42
                )
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel(
                        currentTransactionType
                                .equals("WITHDRAW")
                                ? "💵"
                                : "➕"
                );

        icon.setOpaque(true);

        icon.setBackground(
                new Color(
                        243,
                        244,
                        246
                )
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        26
                )
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setMaximumSize(
                new Dimension(
                        58,
                        58
                )
        );

        left.add(icon);

        left.add(
                Box.createVerticalStrut(18)
        );

        left.add(
                createTitle(
                        title,
                        24,
                        DARK
                )
        );

        left.add(
                Box.createVerticalStrut(8)
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );

        descriptionLabel.setForeground(
                MUTED
        );

        descriptionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        left.add(
                descriptionLabel
        );

        left.add(
                Box.createVerticalStrut(25)
        );

        JPanel amount =
                createAmountPanel(
                        false
                );

        left.add(amount);

        transactionMessageLabel =
                new JLabel(" ");

        transactionMessageLabel.setForeground(
                DANGER
        );

        transactionMessageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        left.add(
                Box.createVerticalStrut(8)
        );

        left.add(
                transactionMessageLabel
        );

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);

        actions.setMaximumSize(
                new Dimension(
                        600,
                        46
                )
        );

        JButton cancel =
                createSecondaryButton(
                        "Cancel"
                );

        cancel.addActionListener(
                event -> showDashboard()
        );

        JButton confirm =
                createPrimaryButton(
                        confirmText
                );

        confirm.addActionListener(
                event -> processTransaction()
        );

        actions.add(cancel);
        actions.add(confirm);

        left.add(
                Box.createVerticalStrut(20)
        );

        left.add(actions);

        JPanel keypad =
                createKeypad(
                        false
                );

        content.add(
                left,
                BorderLayout.CENTER
        );

        content.add(
                keypad,
                BorderLayout.EAST
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        /*
         * Important:
         * The amount field is the default active field
         * whenever Deposit or Withdraw opens.
         */
        activeInputField = amountField;

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            if (amountField != null) {
                amountField.requestFocusInWindow();
            }
        });
    }

    // =========================================================
    // AMOUNT FIELD
    // =========================================================

    private JPanel createAmountPanel(
            boolean transfer
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setPreferredSize(
                new Dimension(
                        450,
                        70
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        70
                )
        );

        JLabel peso =
                new JLabel("₱");

        peso.setForeground(DARK);

        peso.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        panel.add(
                peso,
                BorderLayout.WEST
        );

        JTextField field;

        if (transfer) {

            field =
                    new JTextField();

            transferAmountField =
                    field;

        } else {

            field =
                    new JTextField();

            amountField =
                    field;
        }

        /*
         * FIX:
         * The old code used setEditable(false), which meant
         * the physical keyboard could not type into the amount.
         */
        field.setEditable(true);

        field.setFocusable(true);

        field.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        field.setBackground(
                WHITE
        );

        field.setForeground(
                DARK
        );

        field.setCaretColor(
                DARK
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                2,
                                0,
                                DARK
                        ),
                        new EmptyBorder(
                                0,
                                10,
                                0,
                                0
                        )
                )
        );

        /*
         * Whenever the user clicks or tabs into this field,
         * the keypad will target this field.
         */
        field.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
                    public void focusGained(
                            java.awt.event.FocusEvent event
                    ) {

                        activeInputField =
                                field;
                    }
                }
        );

        field.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mousePressed(
                            java.awt.event.MouseEvent event
                    ) {

                        activeInputField =
                                field;

                        field.requestFocusInWindow();
                    }
                }
        );

        panel.add(
                field,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // KEYPAD
    // =========================================================

    private JPanel createKeypad(
            boolean transfer
    ) {

        JPanel keypad =
                new JPanel(
                        new GridLayout(
                                4,
                                3,
                                10,
                                10
                        )
                );

        keypad.setOpaque(false);

        keypad.setPreferredSize(
                new Dimension(
                        330,
                        278
                )
        );

        String[] keys = {
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "CLEAR",
                "0",
                "⌫"
        };

        for (String key : keys) {

            JButton button =
                    new JButton(key);

            button.setFocusPainted(false);

            /*
             * FIX:
             * Prevent the keypad button from stealing focus
             * from the currently selected text field.
             */
            button.setFocusable(false);

            button.setBackground(
                    WHITE
            );

            button.setForeground(
                    DARK
            );

            button.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            key.equals("CLEAR")
                                    ? 10
                                    : 18
                    )
            );

            button.setBorder(
                    BorderFactory.createLineBorder(
                            BORDER
                    )
            );

            button.setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            button.addActionListener(
                    event -> {

                        if (activeInputField != null) {
                            activeInputField
                                    .requestFocusInWindow();
                        }

                        handleKeypadInput(
                                key,
                                transfer
                        );
                    }
            );

            keypad.add(button);
        }

        return keypad;
    }

    // =========================================================
    // KEYPAD INPUT HANDLER
    // =========================================================

    private void handleKeypadInput(
            String key,
            boolean transfer
    ) {

        /*
         * Use the field the user most recently selected.
         */
        JTextField field =
                activeInputField;

        /*
         * If no field has been selected yet,
         * choose the appropriate default.
         */
        if (field == null) {

            if (transfer) {

                if (recipientAccountField != null) {

                    field =
                            recipientAccountField;

                    activeInputField =
                            recipientAccountField;
                }

            } else {

                if (amountField != null) {

                    field =
                            amountField;

                    activeInputField =
                            amountField;
                }
            }
        }

        if (field == null) {
            return;
        }

        String current =
                field.getText();

        // =====================================================
        // CLEAR
        // =====================================================

        if (key.equals("CLEAR")) {

            field.setText("");

            field.requestFocusInWindow();

            return;
        }

        // =====================================================
        // BACKSPACE
        // =====================================================

        if (key.equals("⌫")) {

            if (!current.isEmpty()) {

                int caretPosition =
                        field.getCaretPosition();

                if (caretPosition > 0) {

                    String newValue =
                            current.substring(
                                    0,
                                    caretPosition - 1
                            )
                            +
                            current.substring(
                                    caretPosition
                            );

                    field.setText(
                            newValue
                    );

                    field.setCaretPosition(
                            Math.max(
                                    0,
                                    caretPosition - 1
                            )
                    );

                } else {

                    field.setText(
                            current.substring(
                                    0,
                                    current.length() - 1
                            )
                    );
                }
            }

            field.requestFocusInWindow();

            return;
        }

        // =====================================================
        // ONLY ALLOW DIGITS
        // =====================================================

        if (!key.matches("\\d")) {
            return;
        }

        // Prevent extremely long values.
        if (current.length() >= 10) {
            return;
        }

        int caretPosition =
                field.getCaretPosition();

        String newValue =
                current.substring(
                        0,
                        caretPosition
                )
                +
                key
                +
                current.substring(
                        caretPosition
                );

        field.setText(
                newValue
        );

        field.setCaretPosition(
                caretPosition + 1
        );

        field.requestFocusInWindow();
    }

    // =========================================================
    // PROCESS TRANSACTION
    // =========================================================

    private void processTransaction() {

        if (amountField == null) {
            return;
        }

        String input =
                amountField
                        .getText()
                        .trim();

        if (input.isEmpty()) {

            transactionMessageLabel.setText(
                    "Please enter an amount."
            );

            return;
        }

        try {

            double amount =
                    Double.parseDouble(input);

            if (!Double.isFinite(amount)
                    || amount <= 0) {

                throw new IllegalArgumentException(
                        "Amount must be greater than zero."
                );
            }

            if (currentTransactionType
                    .equals("DEPOSIT")) {

                currentAccount.deposit(
                        amount
                );

                saveCurrentAccount();

                showResultScreen(
                        "Deposit Successful",
                        "Your deposit has been completed successfully.",
                        formatCurrency(
                                currentAccount
                                        .getBalance()
                        ),
                        true
                );

            } else {

                currentAccount.withdraw(
                        amount
                );

                saveCurrentAccount();

                showResultScreen(
                        "Withdrawal Successful",
                        "Your withdrawal has been completed successfully.",
                        formatCurrency(
                                currentAccount
                                        .getBalance()
                        ),
                        true
                );
            }

        } catch (NumberFormatException error) {

            transactionMessageLabel.setText(
                    "Please enter a valid amount."
            );

        } catch (IllegalArgumentException error) {

            transactionMessageLabel.setText(
                    error.getMessage()
            );
        }
    }

    // =========================================================
    // TRANSFER SCREEN
    // =========================================================

    private void showTransferScreen() {

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "TRANSFER"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                40,
                                0
                        )
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        42,
                        42,
                        42,
                        42
                )
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("↗");

        icon.setOpaque(true);

        icon.setBackground(
                new Color(
                        243,
                        244,
                        246
                )
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setMaximumSize(
                new Dimension(
                        58,
                        58
                )
        );

        left.add(icon);

        left.add(
                Box.createVerticalStrut(18)
        );

        left.add(
                createTitle(
                        "Transfer Money",
                        24,
                        DARK
                )
        );

        left.add(
                Box.createVerticalStrut(8)
        );

        JLabel description =
                new JLabel(
                        "<html>Enter the recipient account number<br>"
                                + "and transfer amount.</html>"
                );

        description.setForeground(
                MUTED
        );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        left.add(description);

        left.add(
                Box.createVerticalStrut(20)
        );

        left.add(
                createFieldLabel(
                        "RECIPIENT ACCOUNT"
                )
        );

        left.add(
                Box.createVerticalStrut(7)
        );

        recipientAccountField =
                new JTextField();

        styleTextField(
                recipientAccountField
        );

        recipientAccountField.setEditable(true);

        recipientAccountField.setFocusable(true);

        recipientAccountField.setMaximumSize(
                new Dimension(
                        500,
                        46
                )
        );

        /*
         * FIX:
         * Clicking the recipient field makes it the active
         * field for the keypad.
         */
        recipientAccountField.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
                    public void focusGained(
                            java.awt.event.FocusEvent event
                    ) {

                        activeInputField =
                                recipientAccountField;
                    }
                }
        );

        recipientAccountField.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mousePressed(
                            java.awt.event.MouseEvent event
                    ) {

                        activeInputField =
                                recipientAccountField;

                        recipientAccountField
                                .requestFocusInWindow();
                    }
                }
        );

        left.add(
                recipientAccountField
        );

        left.add(
                Box.createVerticalStrut(20)
        );

        left.add(
                createFieldLabel(
                        "TRANSFER AMOUNT"
                )
        );

        left.add(
                Box.createVerticalStrut(7)
        );

        left.add(
                createAmountPanel(
                        true
                )
        );

        transferMessageLabel =
                new JLabel(" ");

        transferMessageLabel.setForeground(
                DANGER
        );

        transferMessageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        left.add(
                Box.createVerticalStrut(8)
        );

        left.add(
                transferMessageLabel
        );

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);

        actions.setMaximumSize(
                new Dimension(
                        500,
                        46
                )
        );

        JButton cancel =
                createSecondaryButton(
                        "Cancel"
                );

        cancel.addActionListener(
                event -> showDashboard()
        );

        JButton transfer =
                createPrimaryButton(
                        "Transfer"
                );

        transfer.addActionListener(
                event -> processTransfer()
        );

        actions.add(cancel);
        actions.add(transfer);

        left.add(
                Box.createVerticalStrut(20)
        );

        left.add(actions);

        JPanel keypad =
                createKeypad(
                        true
                );

        content.add(
                left,
                BorderLayout.CENTER
        );

        content.add(
                keypad,
                BorderLayout.EAST
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        /*
         * Default to recipient account number.
         *
         * This means when Transfer opens, clicking
         * 1, 2, 3, etc. enters the recipient account.
         */
        activeInputField =
                recipientAccountField;

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {

            if (recipientAccountField != null) {

                recipientAccountField
                        .requestFocusInWindow();
            }
        });
    }

    // =========================================================
    // PROCESS TRANSFER
    // =========================================================

    private void processTransfer() {

        if (recipientAccountField == null
                || transferAmountField == null) {

            return;
        }

        String recipientNumber =
                recipientAccountField
                        .getText()
                        .trim();

        String amountText =
                transferAmountField
                        .getText()
                        .trim();

        if (recipientNumber.isEmpty()) {

            transferMessageLabel.setText(
                    "Please enter a recipient account."
            );

            return;
        }

        if (amountText.isEmpty()) {

            transferMessageLabel.setText(
                    "Please enter a transfer amount."
            );

            return;
        }

        List<Account> accounts =
                Storage.loadAccounts();

        Account recipient = null;

        for (Account account : accounts) {

            if (account.getAccountNumber()
                    .equals(recipientNumber)) {

                recipient = account;

                break;
            }
        }

        if (recipient == null) {

            transferMessageLabel.setText(
                    "Recipient account not found."
            );

            return;
        }

        if (recipient.getAccountNumber()
                .equals(
                        currentAccount
                                .getAccountNumber()
                )) {

            transferMessageLabel.setText(
                    "You cannot transfer money to your own account."
            );

            return;
        }

        try {

            double amount =
                    Double.parseDouble(
                            amountText
                    );

            if (!Double.isFinite(amount)
                    || amount <= 0) {

                throw new IllegalArgumentException(
                        "Transfer amount must be greater than zero."
                );
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Recipient: "
                                    + recipient.getName()
                                    + "\nAccount: "
                                    + recipient.getAccountNumber()
                                    + "\nAmount: "
                                    + formatCurrency(amount)
                                    + "\n\nProceed with transfer?",
                            "Confirm Transfer",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmation
                    != JOptionPane.YES_OPTION) {

                return;
            }

            currentAccount.transferTo(
                    recipient,
                    amount
            );

            /*
             * Save both the sender and recipient.
             */
            List<Account> allAccounts =
                    Storage.loadAccounts();

            for (Account account : allAccounts) {

                if (account.getAccountNumber()
                        .equals(
                                currentAccount
                                        .getAccountNumber()
                        )) {

                    copyAccountData(
                            currentAccount,
                            account
                    );
                }

                if (account.getAccountNumber()
                        .equals(
                                recipient
                                        .getAccountNumber()
                        )) {

                    copyAccountData(
                            recipient,
                            account
                    );
                }
            }

            Storage.saveAccounts(
                    allAccounts
            );

            showResultScreen(
                    "Transfer Successful",
                    "Your money has been transferred successfully.",
                    formatCurrency(
                            currentAccount
                                    .getBalance()
                    ),
                    true
            );

        } catch (NumberFormatException error) {

            transferMessageLabel.setText(
                    "Please enter a valid amount."
            );

        } catch (IllegalArgumentException error) {

            transferMessageLabel.setText(
                    error.getMessage()
            );
        }
    }

    // =========================================================
    // ACCOUNT INFORMATION
    // =========================================================

    private void showAccountInformation() {

        activeInputField = null;

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "ACCOUNT INFORMATION"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        35,
                        42,
                        35,
                        42
                )
        );

        JPanel card =
                new JPanel();

        card.setOpaque(false);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel avatar =
                new JLabel("👤");

        avatar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        avatar.setOpaque(true);

        avatar.setBackground(
                new Color(
                        243,
                        244,
                        246
                )
        );

        avatar.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        30
                )
        );

        avatar.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        avatar.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        18,
                        12,
                        18
                )
        );

        card.add(avatar);

        card.add(
                Box.createVerticalStrut(18)
        );

        card.add(
                createTitle(
                        "Account Information",
                        24,
                        DARK
                )
        );

        JPanel details =
                new JPanel();

        details.setOpaque(false);

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        details.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        25,
                        0
                )
        );

        details.add(
                createDetailRow(
                        "Account Holder",
                        currentAccount.getName()
                )
        );

        details.add(
                createDetailRow(
                        "Account Number",
                        currentAccount
                                .getAccountNumber()
                )
        );

        details.add(
                createDetailRow(
                        "Account Type",
                        currentAccount
                                .getAccountType()
                )
        );

        details.add(
                createDetailRow(
                        "Available Balance",
                        formatCurrency(
                                currentAccount
                                        .getBalance()
                        )
                )
        );

        card.add(details);

        JButton back =
                createPrimaryButton(
                        "RETURN TO DASHBOARD"
                );

        back.setMaximumSize(
                new Dimension(
                        260,
                        46
                )
        );

        back.addActionListener(
                event -> showDashboard()
        );

        card.add(back);

        content.add(
                card,
                BorderLayout.CENTER
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();
    }

    private JPanel createDetailRow(
            String label,
            String value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout()
                );

        row.setOpaque(false);

        row.setPreferredSize(
                new Dimension(
                        650,
                        58
                )
        );

        row.setMaximumSize(
                new Dimension(
                        650,
                        58
                )
        );

        row.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        JLabel left =
                new JLabel(label);

        left.setForeground(
                MUTED
        );

        left.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        JLabel right =
                new JLabel(value);

        right.setForeground(
                DARK
        );

        right.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        row.add(
                left,
                BorderLayout.WEST
        );

        row.add(
                right,
                BorderLayout.EAST
        );

        return row;
    }

    // =========================================================
    // TRANSACTION HISTORY
    // =========================================================

    private void showTransactionHistory() {

        activeInputField = null;

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "TRANSACTION HISTORY"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        35,
                        42,
                        35,
                        42
                )
        );

        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.add(
                createTitle(
                        "Transaction History",
                        24,
                        DARK
                )
        );

        heading.add(
                Box.createVerticalStrut(6)
        );

        heading.add(
                createLeftText(
                        "Recent account activity",
                        13,
                        MUTED
                )
        );

        content.add(
                heading,
                BorderLayout.NORTH
        );

        JTextArea area =
                new JTextArea();

        area.setEditable(false);

        area.setBackground(
                WHITE
        );

        area.setForeground(
                TEXT
        );

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        12
                )
        );

        area.setLineWrap(true);

        area.setWrapStyleWord(true);

        area.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        List<Transaction> transactions =
                currentAccount
                        .getTransactions();

        if (transactions.isEmpty()) {

            area.setText(
                    "No transactions found."
            );

        } else {

            StringBuilder builder =
                    new StringBuilder();

            for (int i = 0;
                 i < transactions.size();
                 i++) {

                Transaction transaction =
                        transactions.get(i);

                builder.append(
                        "TRANSACTION #"
                );

                builder.append(
                        i + 1
                );

                builder.append(
                        "\n----------------------------------------"
                );

                builder.append(
                        "\nType: "
                );

                builder.append(
                        transaction.getType()
                );

                builder.append(
                        "\nAmount: "
                );

                builder.append(
                        formatCurrency(
                                transaction
                                        .getAmount()
                        )
                );

                builder.append(
                        "\nDescription: "
                );

                builder.append(
                        transaction
                                .getDescription()
                );

                if (transaction
                        .getRelatedAccount()
                        != null) {

                    builder.append(
                            "\nRelated Account: "
                    );

                    builder.append(
                            transaction
                                    .getRelatedAccount()
                    );
                }

                builder.append(
                        "\nDate: "
                );

                builder.append(
                        transaction.getDate()
                );

                builder.append(
                        "\nBalance After: "
                );

                builder.append(
                        formatCurrency(
                                transaction
                                        .getBalanceAfter()
                        )
                );

                builder.append(
                        "\n\n"
                );
            }

            area.setText(
                    builder.toString()
            );
        }

        JScrollPane scrollPane =
                new JScrollPane(area);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        content.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JButton back =
                createPrimaryButton(
                        "RETURN TO DASHBOARD"
                );

        back.setMaximumSize(
                new Dimension(
                        230,
                        46
                )
        );

        back.addActionListener(
                event -> showDashboard()
        );

        JPanel bottom =
                new JPanel();

        bottom.setOpaque(false);

        bottom.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        0,
                        0
                )
        );

        bottom.add(back);

        content.add(
                bottom,
                BorderLayout.SOUTH
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();
    }

    // =========================================================
    // CHANGE PIN
    // =========================================================

    private void showChangePinScreen() {

        activeInputField = null;

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "CHANGE PIN"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        35,
                        42,
                        35,
                        42
                )
        );

        JPanel form =
                new JPanel();

        form.setOpaque(false);

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );

        form.setMaximumSize(
                new Dimension(
                        440,
                        500
                )
        );

        form.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel icon =
                new JLabel("🔐");

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        28
                )
        );

        form.add(icon);

        form.add(
                Box.createVerticalStrut(15)
        );

        form.add(
                createTitle(
                        "Change PIN",
                        24,
                        DARK
                )
        );

        form.add(
                Box.createVerticalStrut(7)
        );

        form.add(
                createCenteredText(
                        "Create a new 4-digit PIN.",
                        13,
                        MUTED
                )
        );

        form.add(
                Box.createVerticalStrut(22)
        );

        JPasswordField current =
                new JPasswordField();

        JPasswordField newPin =
                new JPasswordField();

        JPasswordField confirm =
                new JPasswordField();

        addPinField(
                form,
                "CURRENT PIN",
                current
        );

        addPinField(
                form,
                "NEW PIN",
                newPin
        );

        addPinField(
                form,
                "CONFIRM NEW PIN",
                confirm
        );

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);

        actions.setMaximumSize(
                new Dimension(
                        440,
                        46
                )
        );

        JButton cancel =
                createSecondaryButton(
                        "Cancel"
                );

        cancel.addActionListener(
                event -> showDashboard()
        );

        JButton change =
                createPrimaryButton(
                        "Change PIN"
                );

        change.addActionListener(
                event -> {

                    String currentValue =
                            new String(
                                    current.getPassword()
                            );

                    String newValue =
                            new String(
                                    newPin.getPassword()
                            );

                    String confirmation =
                            new String(
                                    confirm.getPassword()
                            );

                    if (!currentAccount
                            .checkPin(
                                    currentValue
                            )) {

                        showError(
                                "Incorrect current PIN."
                        );

                        return;
                    }

                    if (!newValue
                            .equals(
                                    confirmation
                            )) {

                        showError(
                                "New PINs do not match."
                        );

                        return;
                    }

                    try {

                        currentAccount.changePin(
                                newValue
                        );

                        saveCurrentAccount();

                        showResultScreen(
                                "PIN Changed",
                                "Your security PIN has been updated successfully.",
                                "",
                                false
                        );

                    } catch (
                            IllegalArgumentException error
                    ) {

                        showError(
                                error.getMessage()
                        );
                    }
                }
        );

        actions.add(cancel);
        actions.add(change);

        form.add(actions);

        content.add(
                form,
                BorderLayout.CENTER
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();
    }

    private void addPinField(
            JPanel parent,
            String labelText,
            JPasswordField field
    ) {

        parent.add(
                createFieldLabel(
                        labelText
                )
        );

        parent.add(
                Box.createVerticalStrut(7)
        );

        styleTextField(field);

        field.setMaximumSize(
                new Dimension(
                        440,
                        46
                )
        );

        parent.add(field);

        parent.add(
                Box.createVerticalStrut(15)
        );
    }

    // =========================================================
    // RESULT SCREEN
    // =========================================================

    private void showResultScreen(
            String title,
            String message,
            String balance,
            boolean showBalance
    ) {

        activeInputField = null;

        JPanel root =
                createRootPanel();

        root.add(
                createBrandPanel(),
                BorderLayout.NORTH
        );

        JPanel screen =
                createScreenPanel();

        screen.add(
                createScreenHeader(
                        "COMPLETE"
                ),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        35,
                        42,
                        35,
                        42
                )
        );

        JPanel result =
                new JPanel();

        result.setOpaque(false);

        result.setLayout(
                new BoxLayout(
                        result,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("✓");

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        icon.setOpaque(true);

        icon.setBackground(
                new Color(
                        240,
                        253,
                        244
                )
        );

        icon.setForeground(
                SUCCESS
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        32
                )
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        result.add(icon);

        result.add(
                Box.createVerticalStrut(20)
        );

        result.add(
                createTitle(
                        title,
                        24,
                        DARK
                )
        );

        result.add(
                Box.createVerticalStrut(8)
        );

        result.add(
                createCenteredText(
                        message,
                        13,
                        MUTED
                )
        );

        if (showBalance) {

            result.add(
                    Box.createVerticalStrut(28)
            );

            JPanel balanceBox =
                    new JPanel();

            balanceBox.setBackground(
                    SOFT
            );

            balanceBox.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    BORDER
                            ),
                            new EmptyBorder(
                                    18,
                                    35,
                                    18,
                                    35
                            )
                    )
            );

            balanceBox.setLayout(
                    new BoxLayout(
                            balanceBox,
                            BoxLayout.Y_AXIS
                    )
            );

            balanceBox.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            balanceBox.add(
                    createCenteredText(
                            "AVAILABLE BALANCE",
                            11,
                            MUTED
                    )
            );

            balanceBox.add(
                    Box.createVerticalStrut(8)
            );

            balanceBox.add(
                    createCenteredText(
                            balance,
                            28,
                            DARK
                    )
            );

            result.add(balanceBox);
        }

        result.add(
                Box.createVerticalStrut(28)
        );

        JButton continueButton =
                createPrimaryButton(
                        "RETURN TO DASHBOARD"
                );

        continueButton.setMaximumSize(
                new Dimension(
                        250,
                        46
                )
        );

        continueButton.addActionListener(
                event -> showDashboard()
        );

        result.add(
                continueButton
        );

        content.add(
                result,
                BorderLayout.CENTER
        );

        screen.add(
                content,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.CENTER
        );

        root.add(
                createFooter(
                        "ATM SIMULATOR",
                        "Simulation Only • Not a Real Banking System"
                ),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        revalidate();
        repaint();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (result
                != JOptionPane.YES_OPTION) {

            return;
        }

        currentAccount = null;

        activeInputField = null;

        buildLoginScreen();
    }

    // =========================================================
    // STORAGE
    // =========================================================

    private void saveCurrentAccount() {

        if (currentAccount == null) {
            return;
        }

        List<Account> accounts =
                Storage.loadAccounts();

        for (Account account : accounts) {

            if (account.getAccountNumber()
                    .equals(
                            currentAccount
                                    .getAccountNumber()
                    )) {

                copyAccountData(
                        currentAccount,
                        account
                );

                break;
            }
        }

        Storage.saveAccounts(
                accounts
        );
    }

    private void copyAccountData(
            Account source,
            Account target
    ) {

        /*
         * Save the source transactions into the target.
         */
        target.getTransactions().clear();

        target.getTransactions().addAll(
                source.getTransactions()
        );

        /*
         * Adjust the target balance so that it matches
         * the source balance.
         */
        double difference =
                source.getBalance()
                        - target.getBalance();

        if (difference > 0) {

            target.depositWithoutTransaction(
                    difference
            );

        } else if (difference < 0) {

            target.withdrawWithoutTransaction(
                    Math.abs(difference)
            );
        }
    }

    // =========================================================
    // MAIN UI HELPERS
    // =========================================================

    private JPanel createRootPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                0
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        return panel;
    }

    private JPanel createBrandPanel() {

        JPanel brand =
                new JPanel(
                        new BorderLayout(
                                16,
                                0
                        )
                );

        brand.setBackground(
                WHITE
        );

        brand.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                42,
                                20,
                                42
                        )
                )
        );

        JLabel icon =
                new JLabel("ATM");

        icon.setOpaque(true);

        icon.setBackground(
                DARK
        );

        icon.setForeground(
                WHITE
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        icon.setPreferredSize(
                new Dimension(
                        46,
                        46
                )
        );

        brand.add(
                icon,
                BorderLayout.WEST
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                createLeftText(
                        "ATM SIMULATOR",
                        20,
                        DARK
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        text.add(title);

        text.add(
                Box.createVerticalStrut(3)
        );

        text.add(
                createLeftText(
                        "Secure Banking Terminal",
                        12,
                        MUTED
                )
        );

        brand.add(
                text,
                BorderLayout.CENTER
        );

        return brand;
    }

    private JPanel createScreenPanel() {

        JPanel screen =
                new JPanel(
                        new BorderLayout()
                );

        screen.setBackground(
                WHITE
        );

        screen.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                0,
                                0,
                                0
                        )
                )
        );

        return screen;
    }

    private JPanel createScreenHeader(
            String title
    ) {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                SOFT
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        54
                )
        );

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                24,
                                0,
                                24
                        )
                )
        );

        JLabel left =
                createLeftText(
                        title,
                        12,
                        new Color(
                                55,
                                65,
                                81
                        )
                );

        left.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        JLabel status =
                createLeftText(
                        "● SYSTEM READY",
                        11,
                        SUCCESS
                );

        status.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        header.add(
                left,
                BorderLayout.WEST
        );

        header.add(
                status,
                BorderLayout.EAST
        );

        return header;
    }

    private JPanel createFooter(
            String leftText,
            String rightText
    ) {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        footer.setBorder(
                new EmptyBorder(
                        0,
                        42,
                        18,
                        42
                )
        );

        footer.add(
                createLeftText(
                        leftText,
                        10,
                        LIGHT_MUTED
                ),
                BorderLayout.WEST
        );

        JLabel right =
                createLeftText(
                        rightText,
                        10,
                        LIGHT_MUTED
                );

        right.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        footer.add(
                right,
                BorderLayout.EAST
        );

        return footer;
    }

    // =========================================================
    // BUTTON HELPERS
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(
                DARK
        );

        button.setForeground(
                WHITE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setFocusable(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(
                WHITE
        );

        button.setForeground(
                TEXT
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setFocusable(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_DARK
                        ),
                        new EmptyBorder(
                                9,
                                20,
                                9,
                                20
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    private JButton createMenuButton(
            String icon,
            String title,
            String description,
            Runnable action
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout(
                        0,
                        5
                )
        );

        button.setBackground(
                WHITE
        );

        button.setForeground(
                DARK
        );

        button.setFocusPainted(false);

        button.setFocusable(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        23
                )
        );

        button.add(
                iconLabel,
                BorderLayout.NORTH
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                createLeftText(
                        title,
                        14,
                        DARK
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(
                createLeftText(
                        description,
                        11,
                        MUTED
                )
        );

        button.add(
                text,
                BorderLayout.CENTER
        );

        button.addActionListener(
                event -> action.run()
        );

        return button;
    }

    private JButton createLogoutButton() {

        JButton button =
                createMenuButton(
                        "↪",
                        "Logout",
                        "End this session",
                        () -> logout()
                );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        254,
                                        202,
                                        202
                                )
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        return button;
    }

    // =========================================================
    // TEXT HELPERS
    // =========================================================

    private JLabel createTitle(
            String text,
            int size,
            Color color
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                color
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        size
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return label;
    }

    private JLabel createCenteredText(
            String text,
            int size,
            Color color
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                color
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        size
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        label.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        return label;
    }

    private JLabel createLeftText(
            String text,
            int size,
            Color color
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                color
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        size
                )
        );

        return label;
    }

    private JLabel createSmallLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                LIGHT_MUTED
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        return label;
    }

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                MUTED
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setBackground(
                WHITE
        );

        field.setForeground(
                DARK
        );

        field.setCaretColor(
                DARK
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_DARK
                        ),
                        new EmptyBorder(
                                5,
                                14,
                                5,
                                14
                        )
                )
        );
    }

    // =========================================================
    // CURRENCY
    // =========================================================

    private String formatCurrency(
            double amount
    ) {

        return String.format(
                "₱%.2f",
                amount
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}