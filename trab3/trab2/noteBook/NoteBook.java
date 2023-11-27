package trab2.noteBook;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static trab2.noteBook.Utils.foreachV;
import static trab2.noteBook.Utils.greater;

/**
 * Agenda.
 */
public class NoteBook {
    // Contentor associativo, associa o nome ao contacto.
    // A chave � o nome do contacto.
    // N�o podem existir dois contactos com o mesmo nome.
    private Map<String, Contact> contacts = new TreeMap<>();
    // Contentor associativo de n�mero de telefones.
    // A chave � o n�mero de telefone o valor associado s�o os contactos que t�m o mesmo n�mero de telefone.
    private Map<String, SortedSet<Contact>> telephones = new HashMap<>(); //todo - Instanciar
    // Contentor associativo ordenado por datas de nascimento de contactos cujo
    // anivers�rio � no mesmo dia/mes.
    // A chave data de nascimento o valor associado s�o os contactos que fazem anos no mesmo n�mero de telefone.




    Comparator<Date> cmp = (d1, d2) -> {
        if (d1.getMonth() == d2.getMonth()) {
            return d1.getDay() - d2.getDay();
        }
        return d1.getMonth() - d2.getMonth();
    };
    private SortedMap<Date, SortedSet<Contact>> birthdays = new TreeMap<>(cmp); // todo - Instanciar

    /**
     * Adiciona um contacto ao contentor associativo de contactos contact.
     * - Caso n�o exista um contacto com o mesmo nome adiciona-o.
     * - Caso j� exista um contacto com o mesmo nome e data de nascimento,
     * adiciona os n�meros de telefone ao contacto j� existente.
     * - Caso j� exista um contacto com o mesmo nome e data de nascimento
     * diferente n�o adiciona.
     * Actualiza o contentor de anivers�rios com os telefones deste contacto.
     * Actualiza o contentor de telefones com os telefones deste contacto.
     *
     * @param contact contacto a adicionar
     * @return true caso tenha adicionado ou atualizado as estruturas.
     */
    public boolean add(Contact contact) {
        if (!contacts.containsKey(contact.getName())) {
            contacts.put(contact.getName(), contact);
        } else {
            Contact c = contacts.get(contact.getName());
            if (!c.join(contact))
                return false;
        }
        for (String s : contact.getTelephones()) {
            Supplier<String> key = () -> s;
            Supplier<SortedSet<Contact>> value = () -> {
                SortedSet<Contact> t = new TreeSet<>();
                t.add(contact);
                return t;
            };
            Function<SortedSet<Contact>, Boolean> add = (c) -> c.add(contact);
            Utils.actualize(telephones, key, value, add);
        }
        Date d = contact.getBirthDate();
        Supplier<Date> key = () -> d;
        Supplier<SortedSet<Contact>> value = () -> {
            SortedSet<Contact> t = new TreeSet<>();
            t.add(contact);
            return t;
        };
        Function<SortedSet<Contact>, Boolean> add = (c) -> c.add(contact);
        Utils.actualize(birthdays, key, value, add);
        return true;
    }



    /**
     * Adicionar todos os contactos doutra agenda.
     * N�o adiciona caso j� exista um contacto com o mesmo nome e data de nascimento, neste caso
     * adiciona os numeros de telefone ao contacto j� existente.
     *
     * @param nb agenda
     */
    public void add(NoteBook nb) {

        Iterable<Contact> w = nb.getAllContacts();
        w.forEach(this::add);
    }


    //throw new UnsupportedOperationException("NoteBook::add not implements");


    /**
     * Remove um contacto com determinado nome dos contactos,
     * dos telefones e das data de nascimento.
     *
     * @param name nome do contacto
     */
    public boolean remove(String name) {

        for (String key : contacts.keySet()) {
            Contact c = contacts.get(key);
            if (c.getName().equals(name)) {

                contacts.remove(key);
            }
            return true;
        }
        return false;
    }

    /**
     * Remove todos os contactos.
     */
    public void clear() {
        contacts.clear();
        telephones.clear();
        birthdays.clear();
    }


    /*********************************************************************
     * M�todos de consulta
     */
    /**
     * Obter o contacto dado o nome.
     *
     * @param name nome do contacto
     * @return o contacto ou null caso n�o exista.
     */
    public Contact getContact(String name) {
        return contacts.get(name);
    }

    /**
     * Obter os contactos.
     *
     * @return uma cole��o de contactos inalter�vel.
     */
    public Iterable<Contact> getAllContacts() {
        return contacts.values();
    }

    /**
     * Obter os contactos que fazem anos num determinado dia/mes.
     *
     * @param day   dia
     * @param month m�s
     * @return sequencia de contactos ordenada por data.
     */
    public Iterable<Contact> getBirthdays(int day, int month) {
        return birthdays.get(new Date(day, month, 0));
        // como queremos a data de aniversario de hoje ent�o o ano n�o tem que ser o hoje de hoje
        // criar um  s = new Date(), depois obter o s.Years;
    }


    /**
     * Obter os contactos que t�m determinado numero de telefone.
     *
     * @param phone n�mero de telefone
     * @return sequencia de contactos.
     */
    public Iterable<Contact> getContactsOf(String phone) {
        return telephones.get(phone);
    }



    /**
     * Obter os contactos que fazem anos num determinado mes.
     *
     * @param month m�s
     * @return sequencia de contactos ordenada por data.
     */

    public Iterable<Contact> getBirthdays(int month) {
        Map<Date, SortedSet<Contact>> c = birthdays.subMap(new Date(1, month, 0), new Date(32, month, 0));
        Collection<Contact> c1 = new ArrayList<>();
        Consumer<Contact> action = c1::add;
        foreachV(c, action);
        return c1;
    }




    /*********************************************************************
     * Leitura e escrita em ficheiro
     */
    /**
     * L� os contactos do ficheiro de texto para a agenda.
     *
     * @param file ficheiro de leitura.
     * @throws IOException
     */
    public void read(File file) throws IOException {

        String name;

        try (
                BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = in.readLine();
            while (line != null) {
                int i = line.indexOf("[");
                int q = line.indexOf(" ");
                if (i != -1) {
                    name = line.substring(q, i).trim();
                } else {
                    name = line.substring(q).trim();
                }
                String[] number = line.substring(line.indexOf("[") + 1, line.indexOf("]")).split(",");
                ArrayList<String> c1 = new ArrayList<>(Arrays.asList(number));
                Contact c = new Contact(name, new Date(line.substring(0, q).trim()));
                c.addTelephones(c1);
                this.add(c);
                line = in.readLine();

            }

        }

    }

    /**
     * Escreve todos os contactos da agenda num ficheiro de texto.
     *
     * @param file ficheiro de escrita.
     * @throws IOException
     */
    public void write(File file) throws IOException {
        try (PrintWriter outputStream = new PrintWriter(new FileWriter(file))) {

            for (Map.Entry<String, Contact> c : contacts.entrySet()) {
                Contact w = c.getValue();
                String date = w.getBirthDate().toString();
                String name = w.getName();
                Collection<String> c1 = new ArrayList<>();
                c1 = w.getTelephones();
                outputStream.println(date + " " + name + " " + c1);
            }
        }
    }


    /*********************************************************************
     * M�todos para obter os contactos com maior numero de telefones, os
     * telefones com maior n�mero de contactos ou as datas que existem mais
     * mais anivers�rios
     */
    // todo - USAR O M�TODO Utils.greater
    public Collection<String> getwithmorephones() {
        Comparator<Contact> cmp = (i1, i2) -> i1.getTelephones().size() - i2.getTelephones().size();

        return greater(contacts, cmp);
    }


    public Collection<String> getphoneswithmorecontacts() {
        Comparator<SortedSet<Contact>> cmp = (i1, i2) -> i1.size() - (i2.size());

        return greater(telephones, cmp);
    }


    public Collection<Date> listdateswithmorebirthdays() {
        Comparator<SortedSet<Contact>> cmp = (i1,i2) -> i1.size()- i2.size();
        return greater(birthdays,cmp);
    }



}