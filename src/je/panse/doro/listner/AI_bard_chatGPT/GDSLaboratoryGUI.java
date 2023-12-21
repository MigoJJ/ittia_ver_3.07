package je.panse.doro.listner.AI_bard_chatGPT;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class GDSLaboratoryGUI extends JFrame implements ActionListener {

    // Declare components
    private static final JTextArea inputTextArea = new JTextArea(40, 35);
    private static final JTextArea outputTextArea = new JTextArea(40, 35);
    private static String[] southButtonLabels = {"A>", "Lab>", "Lab 231216", "Modify PI>","P>","Etc."};
    private static String[] eastButtonLabels = {"Rescue", "Copy to Clipboard", "Clear Input", "Clear Output", "Clear All", "Save and Quit"};
    private JButton[] mainButtons;

    // String constants for commands (replace with actual content)
    private static final String bardorderlab = """
			clear previous values;
			make table
			if parameter does not exist -> remove the row;
			Parameter Value Unit 
			using value format
			merge parameters like below
			do not calculate between values\n
			the row titles ;----------------------
            """;
    private static final String bardorderlab1 = """
			Execute next step by step;
			clear previous input data;
			
			you are a physician special assistant for EMR interface.
			i would like to make EMR clinical laboratory result table;
			
			make table ;
			Column titles - > Parameter, Value, Unit ;
			Parameter row titles - >
            """;
    private static final String bardorderlist = """
			i would like to make EMR interface for physician.
			clear previous input data;
			
			organize and make summary list using table format;
			the list will be classified
			    using Mesh main heading classifications;
			and sort the list using disease base; 
			
			modify table like column titles;
			#	,   MeSH Main Heading	,    Date	,    Details

            """;;
    private static final String bardorderpro = "// ... (content of bardorderpro)...";
 
	  
  public GDSLaboratoryGUI() {
        initializeFrame();
        createTextAreas();
        createButtons();
        layoutComponents();
    }

    private void initializeFrame() {
        setTitle("GDS Bard chatGPT4.0");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color backgroundColor = new Color(0xf2f3cb);
        inputTextArea.setBackground(backgroundColor);
        outputTextArea.setBackground(backgroundColor);
        inputTextArea.setBorder(BorderFactory.createRaisedBevelBorder());
        outputTextArea.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void createTextAreas() {
        outputTextArea.setEditable(true);
    }

    public static void appendTextAreas(String value) {
        outputTextArea.append(value);
    }

    private void createButtons() {
        mainButtons = createButtons(southButtonLabels);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(this);
        button.setBackground(Color.decode("#9db6e3"));
        return button;
    }

    private JButton[] createButtons(String[] buttonLabels) {
        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createButton(buttonLabels[i]);
        }
        return buttons;
    }

    private void layoutComponents() {
        JPanel eastButtonPanel = new JPanel();
        eastButtonPanel.setLayout(new BoxLayout(eastButtonPanel, BoxLayout.Y_AXIS));

        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new FlowLayout());

        JButton[] eastButtons = createButtons(eastButtonLabels);

        for (int i = 0; i < eastButtons.length; i++) {
            eastButtonPanel.add(eastButtons[i]);
            if (i != eastButtons.length - 1) {
                eastButtonPanel.add(Box.createVerticalStrut(5));
            }
        }

        // Determine the maximum width across all buttons
        int maxWidth = 0;
        for (JButton[] buttons : Arrays.asList(mainButtons, eastButtons)) {
            for (JButton button : buttons) {
                maxWidth = Math.max(maxWidth, button.getPreferredSize().width);
            }
        }
        for (JButton button : eastButtons) {
            maxWidth = Math.max(maxWidth, button.getPreferredSize().width);
            button.setBorder(BorderFactory.createLoweredBevelBorder());
        }

        
        // Create south buttons
        for (String label : southButtonLabels) {
            JButton southButton = createButton(label);
            southButton.setPreferredSize(new Dimension(maxWidth, 40)); // Set fixed size
            southButton.setBorder(BorderFactory.createLoweredBevelBorder());
            mainButtonPanel.add(southButton);
        }

        // Set all buttons to the maximum width and fixed height
        int fixedHeight = 40; // Fixed height in pixels
        for (JButton button : mainButtons) {
            Dimension size = new Dimension(maxWidth, fixedHeight);
            button.setPreferredSize(size);
            button.setMaximumSize(size);
        }
        for (JButton button : eastButtons) {
            Dimension size = new Dimension(maxWidth, fixedHeight);
            button.setPreferredSize(size);
            button.setMaximumSize(size);
        }
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        // Adding JTextAreas and other components to the contentPanel
        addComponent(contentPanel, new JLabel("Input Data:"), 0, 0, GridBagConstraints.NORTH);
        addComponent(contentPanel, new JScrollPane(inputTextArea), 1, 0, GridBagConstraints.BOTH);
        addComponent(contentPanel, new JLabel("Output Data:"), 2, 0, GridBagConstraints.NORTH);
        addComponent(contentPanel, new JScrollPane(outputTextArea), 3, 0, GridBagConstraints.BOTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 4; // Changed the y coordinate to place the buttons below the JTextAreas
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(mainButtonPanel, constraints);

        // Using BorderLayout to add both the GridBagLayout and the east buttons
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(eastButtonPanel, BorderLayout.EAST);
    }

    private void addComponent(JPanel panel, Component comp, int x, int y, int fill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = fill;
        panel.add(comp, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle common commands iteratively
        if (Arrays.asList("Clear Input", "Clear Output", "Clear All").contains(command)) {
            clearTextAreas(command);
        } else {
            switch (command) {
                case "A>":
                    modifyActionlab();
                    break;
                case "Lab>":
                    modifyActionlab1();
                    break;
                case "Lab 231216":
                    modifyActionlist();
                    break;
                case "Copy to Clipboard":
                    copyToClipboardAction();
                    break;
                case "Save and Quit":
                    inputTextArea.setText("");
                    outputTextArea.setText("");
                    dispose();
                    break;
            }
        }
    }

    private void clearTextAreas(String command) {
        switch (command) {
            case "Clear Input":
                inputTextArea.setText("");
                break;
            case "Clear Output":
                outputTextArea.setText("");
                break;
            case "Clear All":
                inputTextArea.setText("");
                outputTextArea.setText("");
                break;
        }
    }

    private void modifyActionlab() {
        // Add your actual logic for this button action here, replacing the placeholder text
        String textFromInputArea = inputTextArea.getText();

        outputTextArea.append("\nStarting input data --------------------------\n" + textFromInputArea + "\nFinishing input data --------------------------\n");
        outputTextArea.append("\n" + bardorderlab);

        //GDSLaboratoryDataModify.main(textFromInputArea);
        // Replace this with your actual implementation for processing the data
        copyToClipboardAction();
    }

    private void modifyActionlab1() {
        // Add your actual logic for this button action here, replacing the placeholder text
        String textFromInputArea = inputTextArea.getText();
        outputTextArea.append("\nStarting input data --------------------------\n" + textFromInputArea + "\nthe dataset finished --------------------------\n");
        outputTextArea.append("\n" + bardorderlab1);

        //GDSLaboratoryDataModify.main(textFromInputArea);
        // Replace this with your actual implementation for processing the data
        copyToClipboardAction();
    }
    
    private void modifyActionlist() {
        // Add your actual logic for this button action here, replacing the placeholder text
        String textFromInputArea = inputTextArea.getText();
        outputTextArea.append("\nStarting input data --------------------------\n" + textFromInputArea + "\nthe dataset finished --------------------------\n");
        outputTextArea.append("\n" + bardorderlab1);

        //GDSLaboratoryDataModify.main(textFromInputArea);
        // Replace this with your actual implementation for processing the data
        copyToClipboardAction();
    }
    
    private void copyToClipboardAction() {
        String textToCopy = outputTextArea.getText();
        StringSelection selection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
    
    public static void main(String[] args) {
        new GDSLaboratoryGUI().setVisible(true);
    }
}
