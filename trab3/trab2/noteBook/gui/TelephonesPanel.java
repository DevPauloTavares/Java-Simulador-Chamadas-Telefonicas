package trab2.noteBook.gui;

import trab2.noteBook.gui.DocumentNumber;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * JPanel para introdução de números de telefone. Contem uma caixa de texto para
 * introduzir os números, uma área de texto para listar os números introduzidos e
 * um Set que armazena os números introduzidos pelo que não permite repetições.
 */
public class TelephonesPanel extends JPanel {
    private static final int NUMBER_OF_LINES = 3 ;
    private Set<String> telephones = new HashSet<>();
    private JTextField number;
    private JTextArea listOfTelephones = new JTextArea(3, 9);
    private final int numberDigits;

    public TelephonesPanel( String title, int numberDigits  ) {
        super( new BorderLayout() );
        setBorder(new TitledBorder( title ));
        this.numberDigits = numberDigits;
        JPanel p = new JPanel();
        ((FlowLayout) p.getLayout()).setAlignment(FlowLayout.LEFT);
        p.add(new JLabel("number"));
        p.add(number = new JTextField(new DocumentNumber(13), "", numberDigits));
        number.addActionListener(e -> addPhone());
        add(p, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(listOfTelephones=new JTextArea(NUMBER_OF_LINES, numberDigits));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(sp);
    }

    public Collection<String> getTelephones() {
        addPhone( );
        return telephones;
    }

    private void addPhone() {
        String t = number.getText();
        if (t.length() >= numberDigits && telephones.add(t))
            listOfTelephones.append(t + '\n');
    }

    public void setTelephones( Collection<String> t ) {
        number.setText("");
        listOfTelephones.setText("");
        telephones.clear();
        if ( t != null ) {
            telephones.addAll( t );
            for ( String s : telephones )
                listOfTelephones.append(s + "\n");
        }
    }
}
