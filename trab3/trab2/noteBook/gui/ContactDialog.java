package trab2.noteBook.gui;

import trab2.noteBook.Contact;
import trab2.noteBook.Date;

import javax.swing.*;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Janela de dialogo para ler os dados de um contacto, nome, data de nascimento
 * e números de telefone.
 */
public class ContactDialog extends AbstractDialog<Contact> {

    protected TextPanel name = new TextPanel( "name", 40 );
    protected DatePanel birthday = new DatePanel("date of birth");
    protected TelephonesPanel telephones = new TelephonesPanel( "telephones", 9 );

    public ContactDialog( JFrame f, Consumer<Contact> submit ) {
        super(f, "Contact", submit);

        add( name );
        add(birthday);
        add(telephones);

        pack();
    }

    public Contact getValue() {
        String n = name.getText().trim();
        Date d = birthday.getDate();
        if ( !n.isEmpty() &&  d != null ) {
            Contact c = new Contact(n, d);
            c.addTelephones( telephones.getTelephones() );
            return c;
        }
        return null;
    }

    public void setValue( Contact c ) {
        if (c == null) {
            set( "", null, null );
        } else {
            set( c.getName(), c.getBirthDate(), c.getTelephones() );
        }
    }

    private void set(String n, Date d, Collection<String> t) {
        name.setText(n);
        birthday.setDate(d);
        telephones.setTelephones(t);
    }

}
