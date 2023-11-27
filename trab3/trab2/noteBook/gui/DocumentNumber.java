package trab2.noteBook.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Documento a usar em JTextField que só permite inserir um determinado número de caracters dígito
 * num determinado intervalo.
 *
 */
public class DocumentNumber extends PlainDocument {
    private int numberDigits;
    private long maxValue, minValue;

    public DocumentNumber( int numberDigits, long minValue, long maxValue ) {
        this.numberDigits = numberDigits; this.maxValue = maxValue; this.minValue = minValue;
    }

    public DocumentNumber(int numberDigits ) { this( numberDigits, 0, Long.MAX_VALUE); }

    @Override
    public void insertString(int i, String s, AttributeSet a) throws BadLocationException {
        boolean sign = i ==0 && s.equals("+");
        if ( sign ) super.insertString(i, s, a);
        else if(getLength() < numberDigits && isNumeric(s) ) {
            super.insertString(i, s, a);
            if ( getLength() > 0  ) {
                long v = Long.parseLong( getText(0, getLength()) );
                if ( v < minValue || v > maxValue )
                    this.remove(i, s.length());
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isNumeric(String var1) {
        try {
            Long.valueOf (var1 );
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }

}
