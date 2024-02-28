import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.Key;

public class GUI extends JFrame {
    private JCheckBox checkBox; //zakoupeno ano/ne
    private JRadioButton radBtn1;//oblibenost 1
    private JRadioButton radBtn2; //oblibenost 2
    private JRadioButton radBtn3;//oblibenost 3
    private JTextField textField;
    private JPanel mainPanel;
    private JButton btnPrevious;
    private JButton btnSave;
    private JButton btnNext;
    private JCheckBox upravCB;
    private JButton btnNew;
    private JMenuBar menuBar = new JMenuBar(); //samotný bar, automaticky nahoře obrazovky
    private JMenu menuAkce = new JMenu("ahoj"); //dropdown menu, položka v baru, muzu pridavat
    private JMenuItem menuItem = new JMenuItem("neahoj");//položka menu, aka. tlačítko

    private int indexAktualniDeskovky = 0;
    private final SpravceDeskovek spravceDeskovek;

    public GUI(SpravceDeskovek spravceDeskovek) {
        this.spravceDeskovek = spravceDeskovek;
        initComponents();
        initMenu();
        setBounds(500, 200, 500, 500);
        updateGUI();
        btnNext.addActionListener(e -> dalsiDeskovka());
        btnPrevious.addActionListener(e -> predchoziDeskovka());
        btnSave.addActionListener(e -> {
            try {
                ulozDeskovku();
            } catch (OblibenostException ex) {
                throw new RuntimeException(ex.getLocalizedMessage());
            }
        });
        checkBox.setEnabled(false);
        radBtn1.setEnabled(false);
        radBtn2.setEnabled(false);
        radBtn3.setEnabled(false);
        ImageIcon nextIcon = new ImageIcon("images/nextIcon.png");
        btnNext.setIcon(nextIcon);
        ImageIcon prevIcon = new ImageIcon("images/prevIcon.png");
        btnPrevious.setIcon(prevIcon);
        ImageIcon saveIcon = new ImageIcon("images/saveIcon.png");
        btnSave.setIcon(saveIcon);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);

        upravCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(upravCB.isSelected() == true){
                    textField.setEditable(true);
                    checkBox.setEnabled(true);
                    radBtn1.setEnabled(true);
                    radBtn2.setEnabled(true);
                    radBtn3.setEnabled(true);
                    btnSave.setEnabled(true);
                    btnNew.setEnabled(true);
                }else{
                    textField.setEditable(false);
                    checkBox.setEnabled(false);
                    radBtn1.setEnabled(false);
                    radBtn2.setEnabled(false);
                    radBtn3.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnNew.setEnabled(false);
                }
            }
        });
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    spravceDeskovek.pridejDeskovku(new Deskovka(textField.getText(), checkBox.isSelected(), 1));
                    textField.setText(textField.getText()+" - kopie");
                    upravCB.setSelected(true);
                } catch (OblibenostException ex) {
                    System.err.println("chyba " + ex);;
                }
            }
        });
    }

    private void initComponents() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hry");
        pack();
    }

    private void ulozDeskovku() throws OblibenostException {
        String nazevHry = textField.getText();
        boolean zakoupeno = checkBox.isSelected();
        int oblibenost = 1;
        if (radBtn2.isSelected()) {
            oblibenost = 2;
        } else if (radBtn3.isSelected()) {
            oblibenost = 3;
        }
        Deskovka aktualniDeskovka = spravceDeskovek.getDeskovka(indexAktualniDeskovky);
        aktualniDeskovka.setNazevHry(nazevHry);
        aktualniDeskovka.setZakoupeno(zakoupeno);
        aktualniDeskovka.setOblibenost(oblibenost);
        spravceDeskovek.setDeskovka(indexAktualniDeskovky, aktualniDeskovka);
    }

    private void predchoziDeskovka() {
        btnNext.setEnabled(true);
        if (indexAktualniDeskovky > 0) {
            indexAktualniDeskovky--;
            updateGUI();
        }
        if (spravceDeskovek.getPocetDeskovek() == 0)
        {
            btnPrevious.setEnabled(false);
            updateGUI();
        }
    }

    private void dalsiDeskovka() {
        btnPrevious.setEnabled(true);
        if (indexAktualniDeskovky < spravceDeskovek.getPocetDeskovek() - 1) {
            indexAktualniDeskovky++;
            updateGUI();
        }
        if (spravceDeskovek.getPocetDeskovek() == 0)
        {
            btnNext.setEnabled(false);
            updateGUI();
        }
    }

    private void updateGUI() {
        if (indexAktualniDeskovky == 0) {
            btnPrevious.setEnabled(false);
        }
        if(spravceDeskovek.getPocetDeskovek() == 0)
        {
            btnNext.setEnabled(false);
        }
        if (indexAktualniDeskovky == spravceDeskovek.getPocetDeskovek() - 1) {
            btnNext.setEnabled(false);
        }
        if (spravceDeskovek.getPocetDeskovek() == 0) {
            textField.setText("");
            checkBox.setSelected(false);
            radBtn1.setSelected(true);
        }else {
                Deskovka aktualniDeskovka = spravceDeskovek.getDeskovka(indexAktualniDeskovky);
                textField.setText(aktualniDeskovka.getNazevHry());
                checkBox.setSelected(aktualniDeskovka.isZakoupeno());
                switch (aktualniDeskovka.getOblibenost()) {
                    case 1:
                        radBtn1.setSelected(true);
                        break;
                    case 2:
                        radBtn2.setSelected(true);
                        break;
                    case 3:
                        radBtn3.setSelected(true);
                        break;
                }
        }
    }
    private void initMenu()
    {
        menuItem.addActionListener(e -> {JOptionPane.showMessageDialog( this,"Zdravíčko");});
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menuAkce.add(menuItem);
        menuAkce.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menuAkce);
        setJMenuBar(menuBar);//přidá panel s menu k oknu
        //JOptionPane.showMessageDialog(null, "Tohle píši ve vyskakovacím okně!", "Titulek okna", JOptionPane.WARNING_MESSAGE);
    }
}
