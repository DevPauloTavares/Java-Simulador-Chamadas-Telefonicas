package trab2.noteBook.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Classe base para as janelas de diálogo que correspondem a obtenção de dados.
 * Contem um JPanel para adicionar os componentes de dialogo e um botão de "submit".
 * No construtor é passado no segundo parâmetro a função que deve ser aplicada quando
 * for premido o botão de "submit". Quando é premido o botão de "submit"  caso seja
 * possivel obter um objecto correspondente aos dados, é chamada a função passada no
 * construtor e é fechada a janela.
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
     * Obter o objecto correspondente aos dados introduzidos nos componentes gráficos.
     * @return o objecto correspondente aos dados introduzidos ou
     *         null caso não existam dados para instanciar o objecto.
     */
    public abstract V getValue();

    /**
     * Inicia os componentes com o valor do objecto. Caso seja null coloca os
     * valores por omissão nos componentes gráficos.
     * @param v
     */
    public abstract void setValue( V v );

 }
