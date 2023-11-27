package trab2.noteBook.gui;

import trab2.noteBook.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * JPanel para leitura de uma data. Contem tr?s caixas de texto para escrita do dia, do mes e do ano.
 */
public class DatePanel extends JPanel {
    /**
     * Caixas de texto para escrever os valores num?ricos correspondentes ao dia, mes e ano
     */
    private JTextField day = new JTextField( new DocumentNumber(2, 1, 31), "", 2),
                     month = new JTextField( new DocumentNumber(2, 1, 12), "", 2),
                      year = new JTextField( new DocumentNumber(4), "", 4);

    public DatePanel( String title ) {
        // Alterar o alinhamento para que os elementos fiquem alinhados ? esquerda
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEFT);
        // Colocar uma moldura com titulo
        setBorder( new TitledBorder( title ) );
        // Adicionar as caixas de texto etiquetadas
        add(new JLabel("day"));  add(day);
        add(new JLabel("month")); add(month);
        add(new JLabel("year")); add(year);
    }

    /**
     * Obter a data correspondente aos valores presentes na caixa de texto
     * @return data correspondente aos valores presentes na caixa de texto
     */
    public Date getDate() {
       return new Date( toInt(day.getText()), toInt(month.getText()),toInt(year.getText()));
    }
    private int toInt(String s) {
        try { return Integer.parseInt( s); } catch (NumberFormatException e) { return 0; }
    }

    /**
     * Escrever nas caixas de texto os correspondentes valores da data. Caso o par?metro seja null
     * coloca todas as caixas vazias.
     * @param d
     */
    public void setDate( Date d ) {
        if ( d == null ) {
            day.setText("");
            month.setText("");
            year.setText("");
        }
        else {
            day.setText(d.getDay()+ "");
            month.setText(d.getMonth()+ "");
            year.setText(d.getYear() + "");
        }
    }
}
