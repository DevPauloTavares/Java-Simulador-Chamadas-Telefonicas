package trab2.noteBook.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Classe base para as janelas de di�logo que correspondem a obten��o de dados.
 * Contem um JPanel para adicionar os componentes de dialogo e um bot�o de "submit".
 * No construtor � passado no segundo par�metro a fun��o que deve ser aplicada quando
 * for premido o bot�o de "submit". Quando � premido o bot�o de "submit"  caso seja
 * possivel obter um objecto correspondente aos dados, � chamada a fun��o passada no
 * construtor e � fechada a janela.
 */
public abstract class AbstractDialog<V> extends JDialog{
    protected JPanel contentDialog = new JPanel();
    public AbstractDialog(JFrame f, String title, Consumer<V> submit) {
        super(f, title, true);
        contentDialog.setLayout(new BoxLayout(contentDialog, BoxLayout.Y_AXIS));
        setLocation(f.getWidth() / 2, f.getHeight() / 2);

        super.add(contentDialog);
        JPanel  buttonsPanel = new JPanel( new BorderLayout());
        JButton b = new JButton("submit");
        b.addActionListener( e -> {
                V v = getValue();
                if ( v != null ) {
                    submit.accept(v);
                    dispose();
                }
        });
        buttonsPanel.add(b, BorderLayout.EAST);
        super.add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Redefinido para adicionar o componente no JPanel contentDialog.
     * @param c
     * @return
     */
    @Override
    public Component add(Component c) {
        contentDialog.add( c );
        return c;
    }

    /**
     * Obter o objecto correspondente aos dados introduzidos nos componentes gr�ficos.
     * @return o objecto correspondente aos dados introduzidos ou
     *         null caso n�o existam dados para instanciar o objecto.
     */
    public abstract V getValue();

    /**
     * Inicia os componentes com o valor do objecto. Caso seja null coloca os
     * valores por omiss�o nos componentes gr�ficos.
     * @param v
     */
    public abstract void setValue( V v );

 }
