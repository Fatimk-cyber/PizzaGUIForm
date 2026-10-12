
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame {

    private JRadioButton thinButton;
    private JRadioButton regularButton;
    private JRadioButton deepDishButton;

    private JComboBox<String> sizeComboBox;

    private JCheckBox[] toppingBoxes;
    private String[] toppings = {
            "Pepperoni",
            "Mushrooms",
            "Extra Cheese",
            "Chocolate Chips",
            "Pickles",
            "Monster Eyes"
    };

    private JTextArea receiptArea;

    private final double TAX_RATE = 0.07;
    private final DecimalFormat money =
            new DecimalFormat("$0.00");

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Crust panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(
                BorderFactory.createTitledBorder("Choose Crust"));

        thinButton = new JRadioButton("Thin");
        regularButton = new JRadioButton("Regular", true);
        deepDishButton = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinButton);
        crustGroup.add(regularButton);
        crustGroup.add(deepDishButton);

        crustPanel.add(thinButton);
        crustPanel.add(regularButton);
        crustPanel.add(deepDishButton);

        // Size panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(
                BorderFactory.createTitledBorder("Pizza Size"));

        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeComboBox = new JComboBox<>(sizes);
        sizePanel.add(sizeComboBox);

        // Toppings panel
        JPanel toppingPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        toppingPanel.setBorder(
                BorderFactory.createTitledBorder("Toppings ($1.00 each)"));

        toppingBoxes = new JCheckBox[toppings.length];

        for (int i = 0; i < toppings.length; i++) {
            toppingBoxes[i] = new JCheckBox(toppings[i]);
            toppingPanel.add(toppingBoxes[i]);
        }

        // Top section: three panels side by side
        JPanel choicesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        choicesPanel.add(crustPanel);
        choicesPanel.add(sizePanel);
        choicesPanel.add(toppingPanel);

        add(choicesPanel, BorderLayout.NORTH);

        // Receipt panel
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(
                BorderFactory.createTitledBorder("Order Receipt"));

        receiptArea = new JTextArea(15, 50);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);

        add(receiptPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        orderButton.addActionListener(e -> placeOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmQuit());

        // Also confirm if the window's close button is clicked
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmQuit();
            }
        });
    }

    private void placeOrder() {
        String crust;

        if (thinButton.isSelected()) {
            crust = "Thin";
        } else if (regularButton.isSelected()) {
            crust = "Regular";
        } else if (deepDishButton.isSelected()) {
            crust = "Deep-dish";
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please select a crust.");
            return;
        }

        String size = (String) sizeComboBox.getSelectedItem();

        double basePrice;

        switch (size) {
            case "Small":
                basePrice = 8.00;
                break;
            case "Medium":
                basePrice = 12.00;
                break;
            case "Large":
                basePrice = 16.00;
                break;
            case "Super":
                basePrice = 20.00;
                break;
            default:
                basePrice = 0.00;
        }

        int toppingCount = 0;

        for (JCheckBox toppingBox : toppingBoxes) {
            if (toppingBox.isSelected()) {
                toppingCount++;
            }
        }

        if (toppingCount == 0) {
            JOptionPane.showMessageDialog(
                    this, "Please select at least one topping.");
            return;
        }

        double subtotal = basePrice + toppingCount;
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        StringBuilder receipt = new StringBuilder();

        receipt.append("=========================================\n");
        receipt.append(String.format("%-29s %8s%n",
                crust + " Crust / " + size, money.format(basePrice)));
        receipt.append("-----------------------------------------\n");
        receipt.append(String.format("%-29s %8s%n",
                "Ingredient", "Price"));

        for (JCheckBox toppingBox : toppingBoxes) {
            if (toppingBox.isSelected()) {
                receipt.append(String.format("%-29s %8s%n",
                        toppingBox.getText(), money.format(1.00)));
            }
        }

        receipt.append("-----------------------------------------\n");
        receipt.append(String.format("%-29s %8s%n",
                "Sub-total:", money.format(subtotal)));
        receipt.append(String.format("%-29s %8s%n",
                "Tax:", money.format(tax)));
        receipt.append("-----------------------------------------\n");
        receipt.append(String.format("%-29s %8s%n",
                "Total:", money.format(total)));
        receipt.append("=========================================\n");

        receiptArea.setText(receipt.toString());
        receiptArea.setCaretPosition(0);
    }

    private void clearForm() {
        regularButton.setSelected(true);
        sizeComboBox.setSelectedIndex(0);

        for (JCheckBox toppingBox : toppingBoxes) {
            toppingBox.setSelected(false);
        }

        receiptArea.setText("");
    }

    private void confirmQuit() {
        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}