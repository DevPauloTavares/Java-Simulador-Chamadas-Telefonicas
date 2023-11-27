package trab2.noteBook.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by msousa on 12/12/2015.
 */
public class TextPanel extends JPanel {
    private JTextField textField;
    public TextPanel( String title, int columns ) {
        super( new BorderLayout() );
        setBorder( new TitledBorder( title ) );
        add( textField = new JTextField( columns ), BorderLayout.NORTH );
    }

      public String getText()         { return textField.getText(); }

    public void setText( String s ) { textField.setText( s );     }
}


