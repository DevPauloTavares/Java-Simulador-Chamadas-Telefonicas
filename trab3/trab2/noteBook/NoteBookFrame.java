package trab2.noteBook;


import trab2.noteBook.gui.ContactDialog;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import java.awt.*;
//import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.List;

/**
 * Interface gr?fica para visualizar e modificar a agenda.
 */
public class NoteBookFrame<d> extends JFrame {

    public static JTextField newJTextField( String title ) {
        JTextField tf = new JTextField( 10 ); tf.setBorder( new TitledBorder( title ) );
        return tf;
    }
    private JFileChooser fileChooser = new JFileChooser( );
    private NoteBook noteBook = new NoteBook();
    private ContactDialog contactDialog = new ContactDialog(this, this::addContact );
    private JTextArea listArea = new JTextArea( 15, 40 );
    private Map<String,ArrayList<Call>> chamadaRecebida = new TreeMap<>();
    private Map<String,ArrayList<Call>> chamadaefectuada = new TreeMap<>();
    private Map<String, ArrayList<Call>> naochamadaAtendida = new TreeMap<>();
    private int OrgenaChamada =0;
    private int count;
    private int sai =0;
    private JLabel ned ;
    Comparator<Call> cmp = (d1, d2) -> {
        if (d1.d.compareTo(d2.d) == 0) {
            return d1.ordenar - d2.ordenar;
        }
        return d1.d.compareTo(d2.d);
    };
   private    JTextField pathname, info;
   private    String  ori,dest;
   private long start;
   private long elapsed ;
   private final ArrayList<Call> listnaochamadaAtendida = new ArrayList<>();
   private final ArrayList<Call> listchamadaRecebida = new ArrayList<>();
   private final ArrayList<Call> listchamadaefectuada = new ArrayList<>();
   private   String conctatOrig;
   private   String conctatDest;
   private JFrame ad;
   private Timer timer;
   private JPanel pbh = new JPanel(new BorderLayout());
   private int controL =0;
   private int count1;


    public static class ItensMenu extends
        AbstractMap.SimpleEntry<String, ActionListener> {
        public ItensMenu(String s, ActionListener l){ super(s,l);}
    }
    public ItensMenu[] fileMenus = {
            new ItensMenu("load", this::load),
            new ItensMenu("save", this::save),
            new ItensMenu("exit", this::exit)};
    ItensMenu[] fileMenus1 = {
            new ItensMenu("addContact", this::addContact),
            new ItensMenu("addPhone", this::addPhone),
            new ItensMenu("Delete Contact", this::removeContact)};
    ItensMenu[] fileMenus2 = {
            new ItensMenu("all contacts", this::listAll),
            new ItensMenu("names with this phone", this::listNamesWithThisPhone),
            new ItensMenu("today birthdays", this::listTodayBirthdays),
            new ItensMenu("this month birthdays", this::listMonthBirthdays)
    };
    ItensMenu[] fileMenus3 = {
            new ItensMenu("contacts with more phones", this::listcontactswithmorephones),
            new ItensMenu("phones with more contacts", this::listphoneswithmorecontacts),
            new ItensMenu("dates with more birthdays", this::listdateswithmorebirthdays)
    };

    ItensMenu[] fileMenus5 = {
            new ItensMenu("Chamada Efetuadas", this::chamadaEfetuadas),
            new ItensMenu("Chamada Recebidas", this::chamadaRecebidas),
            new ItensMenu("Chamada n?o atendidas", this::chamadaNaoAtendida)
    };


    public NoteBookFrame(){
        super("NoteBook");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Adicionar a TextArea para a listagem, com barra de scroll
        listArea.setBorder(new TitledBorder("list"));
        JScrollPane sp = new JScrollPane(listArea);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(sp);

        // Adicionar o but?o para adicionar contacto
        JPanel buttons = new JPanel();
        ((FlowLayout) buttons.getLayout()).setAlignment(FlowLayout.CENTER);
        JButton b = new JButton("Make Call");
        b.addActionListener(this::makeCall);
        buttons.add(b);
        b = new JButton("Answer Call");
        b.addActionListener(this::addPhone);
        buttons.add(b);
        ((FlowLayout) buttons.getLayout()).setAlignment(FlowLayout.CENTER);
         b = new JButton("Add Conctat");
        b.addActionListener(this::addContact);
        buttons.add(b);
        b = new JButton("Add Phone");
        b.addActionListener(this::addPhone);
        buttons.add(b);
        add(buttons, BorderLayout.SOUTH);

        // Adicionar os menus
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createJMenu("File", fileMenus));
        setJMenuBar( menuBar );
        menuBar.add(createJMenu("Edit", fileMenus1));
        menuBar.add(createJMenu("List", fileMenus2));
        menuBar.add(createJMenu("With more", fileMenus3));
        menuBar.add(createJMenu("Informa??o",fileMenus5));
        pack();
    }

    /**
     * Instancia um menu e adiciona os itens descritos no array de itens
     * @param name nome do menu
     * @param itens array contendo a descri??o dos itens (nome e a??o a efetuar).
     * @return o menu instanciado
     */
    protected static JMenu createJMenu( String name, ItensMenu[] itens ){
        JMenu menu = new JMenu( name );
        for ( int i= 0; i < itens.length; ++i)  {
            JMenuItem mi = new JMenuItem( itens[i].getKey() );
            mi.addActionListener( itens[i].getValue() );
            menu.add( mi );
        }
        return menu;
    }


    /**
     * M?todo chamado quando ? premido o bot?o "add contact".
     * Coloca visivel uma janela de dialogo correspondente ? inser??o dos dados
     * do contacto, quando ? premido o bot?o submit ? chamada o m?todo apply
     * do Consumer passado por par?metro no construtor.
     * @param actionEvent evento do action listener.
     */
    private void addContact(ActionEvent actionEvent) {
         contactDialog.setValue( null );
         contactDialog.setVisible(true);
    }



    /**
     * M?todo chamado quando ? premido o bot?o "submit" da janela de di?logo
     * para introdu??o dos dados do contacto.
     * Adiciona o contacto na agenda e lista os contactos.
     * @param c contacto a adicionar
     */
    private void addContact(Contact c) {
        if (noteBook.add( c ) )
            list("Contact List", noteBook.getAllContacts(), Contact::toString );
    }


    /**
     * M?todo chamado quando ? premido o bot?o "add phone".
     * Obt?m o nome do contacto e caso exista um contacto com o nome, atualiza e
     * coloca visivel uma janela de dialogo correspondente ? inser??o dos dados.
     * @param actionEvent evento do action listener.
     */
    private void addPhone(ActionEvent actionEvent) {
        String name = JOptionPane.showInputDialog(this, "Contact Name", "Add phone", JOptionPane.QUESTION_MESSAGE);
        if ( name != null ) {
            Contact c = noteBook.getContact( name );
            if ( c != null ) {
                contactDialog.setValue(c);
                contactDialog.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(this, "Contact not exist", "Add phone",JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Remov da agenda o contacto com .
     * Obt?m o nome do contacto a remover e remove-o da agenda.
     * @param actionEvent evento do action listener.
     */
    private void removeContact(ActionEvent actionEvent) {
        String name = JOptionPane.showInputDialog(this, "Contact Name", "Delete", JOptionPane.QUESTION_MESSAGE);
        if ( name != null ) {
            if ( !noteBook.remove( name ) ) {
                JOptionPane.showMessageDialog(this, "Contact not exist", "Delete",JOptionPane.ERROR_MESSAGE);
            };
        }
    }



    /**
     * Limpa a area de texto e lista uma sequencia de contactos, um por linha.
     * @param title - titulo a colocar na cercadura da area de texto
     * @param seq sequ?ncia de Elementos.
     * @param toList - Fun??o para obter o valor a listar
     * @param <E>
     * @param <V>
     */
    private <E, V> void list(String title, Iterable<E> seq, Function<E, V> toList){
       ((TitledBorder)listArea.getBorder()).setTitle( title );
       listArea.setText( "" );
       if ( seq == null )
           listArea.append( "Not exist contacts \n");
       else for( E e : seq )
           listArea.append( toList.apply( e ) + "\n");
    }




    private <E, V> void list1(String title,List<E> seq){

        ((TitledBorder)listArea.getBorder()).setTitle( title );
        listArea.setText( "" );
        if ( seq.size() == 0 )
            listArea.append( "Not exist contacts \n");
        else {
            int cont =seq.size();

            for (E e : seq) {
                    if (e instanceof Call) { Call WW = (Call) e;
                        listArea.append(WW.toString1() + "\n\n");
                    }
                    }
                    }
                }



    /***************************************************
     *  M?todos associados aos itens do menu "File"
     *
     ***************************************************/
    private void exit( ActionEvent actionEvent ) {
        int res = JOptionPane.showConfirmDialog(this, "Save notebook", "save", JOptionPane.YES_NO_CANCEL_OPTION);
        if ( res != JOptionPane.CANCEL_OPTION ) {
            if ( res == JOptionPane.YES_OPTION )
                save(actionEvent);
            System.exit(0);
        }
    }


    private void save(ActionEvent actionEvent) {
        fileChooser.setCurrentDirectory(new File("."));
        if ( JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this) )
            try {
                noteBook.write(fileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error file: " + e.getMessage());
            }
    }


    private void load(ActionEvent actionEvent) {
        //todo
        fileChooser.setCurrentDirectory(new File("."));
        if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this) )
            try {
                noteBook.read(fileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error file: " + e.getMessage());
            }
    }


    /***************************************************
     *  M?todos associados aos itens do menu "List"
     *
     ***************************************************/
    /**
     * M?todo chamado quando ? selecionado o item "all contacts".
     * Lista todos os contactos.
     * @param actionEvent
     */
    private void listAll(ActionEvent actionEvent) {
        list("Contact List", noteBook.getAllContacts(), Contact::toString );
    }


    /**
     * M?todo chamado quando ? selecionado o item "month birthdays".
     * Coloca visivel uma janela de dialogo para a inser??o do m?s.
     * Ap?s ter a idade lista os contactos com a idade pretendida.
     * @param actionEvent
     */
    private void listMonthBirthdays( ActionEvent actionEvent ) {
        //todo
        String name =  JOptionPane.showInputDialog(this, "Contact month", "Add month", JOptionPane.QUESTION_MESSAGE).trim();
        try {
            int i = Integer.parseInt(name);
            list("Contact List", noteBook.getBirthdays(i), Contact::toString );
        } catch (NumberFormatException exception){ };
     }



    /**
     * M?todo chamado quando ? selecionado o item "names with this phone".
     * Coloca visivel uma janela de dialogo para a inser??o do n?mero de telefone.
     * Ap?s ter o nome lista os nomes que cont?m este n?mero de telefone.
     * @param actionEvent
     */
    private void listNamesWithThisPhone(ActionEvent actionEvent) {
        // todo
        String name = JOptionPane.showInputDialog(this, "Contact Number", "Add Number", JOptionPane.QUESTION_MESSAGE);
        if ( name != null) {
            list("Contact List", noteBook.getContactsOf(name), Contact::toString);
        }
    }

    /**
     * M?todo chamado quando ? selecionado o item "today birthdays".
     * @param actionEvent
     */
    private void listTodayBirthdays(ActionEvent actionEvent) {
        //todo
        Date d = new Date();
        list("Contact List", noteBook.getBirthdays(d.getDay(),d.getMonth()), Contact::toString );
    }


    /***************************************************
     *  M?todos associados aos itens do menu "With more"
     *
     ***************************************************/
     //todo
    private void listcontactswithmorephones(ActionEvent actionEvent) {

        list("Contact List", noteBook.getwithmorephones(),String::toString);
    }


    private void listphoneswithmorecontacts(ActionEvent actionEvent) {

        list("Contact List", noteBook.getphoneswithmorecontacts(), String::toString);
    }


    private void listdateswithmorebirthdays(ActionEvent actionEvent) {

        list("Contact List", noteBook.listdateswithmorebirthdays(), Date::toString);
    }



    private void chamadaNaoAtendida(ActionEvent actionEvent) {
        String name = JOptionPane.showInputDialog(this, "Contact Number", "Add Number", JOptionPane.QUESTION_MESSAGE);
        getArray(name,naochamadaAtendida);
    }


    private void chamadaRecebidas(ActionEvent actionEvent) {
        String name = JOptionPane.showInputDialog(this, "Contact Number", "Add Number", JOptionPane.QUESTION_MESSAGE);
        getArray(name,chamadaRecebida);
    }


    private void chamadaEfetuadas(ActionEvent actionEvent) {
        String name = JOptionPane.showInputDialog(this, "Contact Number", "Add Number", JOptionPane.QUESTION_MESSAGE);
        ArrayList<Call> ww = new ArrayList<>();
        if(name !=null) {
            ArrayList<Contact> newArray = new ArrayList<>();
            Iterable<Contact> verficaNumero = noteBook.getContactsOf(name);
            if(verficaNumero != null) for (Contact adx : verficaNumero){newArray.add(adx);}
            if(verficaNumero != null && newArray.size() == 1 && newArray.get(0).getTelephones().size() == 1 ){
                name = newArray.get(0).getName();
            }
            if (chamadaefectuada.get(name) != null) {
                List<Call> w = new ArrayList<>(chamadaefectuada.get(name));
                w.sort(cmp.reversed());
                System.out.println("\n");
                for (Call md : w) {
                    if (md.ori.equals(name)) {
                        ww.add(md);
                    }
                }

            }
            list1("Lista Chamada Efetuadas", (ww));
        }
    }


  public void   getArray(String name,Map<String,ArrayList<Call>> adr){
      if (name != null){
          ArrayList<Contact> newArray = new ArrayList<>();
          Iterable<Contact> verficaNumero = noteBook.getContactsOf(name);
          if(verficaNumero != null) for (Contact adx : verficaNumero){newArray.add(adx);}
          if(verficaNumero != null && newArray.size() == 1 && newArray.get(0).getTelephones().size() == 1 ){
              name = newArray.get(0).getName();

          }
          ArrayList<Call> ww = new ArrayList<>();

          if (adr.get(name) != null) {
              System.out.println("entrou entrou");
              List<Call> w = new ArrayList<>(adr.get(name));
              w.sort(cmp.reversed());
              System.out.println("\n");
              for (Call md : w) {
                  if (md.dest.equals(name)) {
                      ww.add(md);
                  }
              }
          }
          list1("Lista Chamada Nao Atendidas", (ww));
      }
  }



    private void merge( File dir, String filenameOut ) throws IOException {
            try (PrintWriter outputStream = new PrintWriter(new FileWriter(filenameOut + ".txt"))) {
                File [] mhd = dir.listFiles();
                 for (int i = 0; i < Objects.requireNonNull(mhd).length ; ++i){
                     BufferedReader in = new BufferedReader(new FileReader(mhd[i]));
                     String line = in.readLine();
                     while (line != null){
                          outputStream.println(line);
                          line = in.readLine();
                     }
                 }

            }

    }




    private void makeCall(ActionEvent actionEvent){

         JFrame tx = new JFrame();
         tx.setVisible(true);
         tx.setSize(300, 300);
         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         JPanel pb = new JPanel();
         JButton b = new JButton("Ligar");
         b.addActionListener(this::calllll);
         pb.add(b);
         tx.add(pb, BorderLayout.CENTER);
         JPanel pNorth = new JPanel(new BorderLayout());
         this.pathname = newJTextField("Contato Origem");
         this.info = newJTextField("Contato Destino");
         ori = pathname.getText();
         dest = info.getText();
         pNorth.add(pathname,BorderLayout.NORTH);
         pNorth.add(info,BorderLayout.SOUTH);
         tx.add(pNorth,BorderLayout.NORTH);
         pack();


     }


    private void calllll(ActionEvent actionEvent) {
        conctatDest = info.getText().trim();
        conctatOrig = pathname.getText().trim();
        ArrayList<Contact> dd = new ArrayList<>();
        ArrayList<Contact> dd1 = new ArrayList<>();
        Contact contactoOrgiemName = noteBook.getContact(pathname.getText());
        Contact contactoDestName = noteBook.getContact(info.getText());
        Iterable<Contact> contactoOrgiemtele = noteBook.getContactsOf(pathname.getText());
        Iterable<Contact> contactoDesttele = noteBook.getContactsOf(info.getText());
        if (conctatDest.equals("") || conctatOrig.equals("")) {
            JOptionPane.showInternalMessageDialog(null, "O contacto  origem ou de origem faltando",
                    "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (contactoOrgiemtele != null) {
                for (Contact mm : contactoOrgiemtele) {
                    dd.add(mm);
                }
            }
            if (contactoDesttele != null) {
                for (Contact mm : contactoDesttele) {
                    dd1.add(mm);
                }
            }
            if (contactoDesttele != null) System.out.println(dd1.get(0).getTelephones().size());
            System.out.println(contactoOrgiemName);

            if ((contactoOrgiemName == null || contactoOrgiemName.getTelephones().size() > 1) && (contactoOrgiemtele == null || dd.size() > 1)) {
                JOptionPane.showInternalMessageDialog(null, "O contacto  origem n?o Exite, ou Esta associado a mais que um numero",
                        "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (contactoDestName == null && contactoDesttele == null) {
                    int n = JOptionPane.showConfirmDialog(null, "Adicionar contato Destino  a agenda?", "", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        addContact(actionEvent);
                    }
                    if (dd.get(0).getTelephones().size() == 1) {
                        conctatOrig = dd.get(0).getName();
                    }
                    grafico();
                } else {
                    if (((contactoDestName == null || contactoDestName.getTelephones().size() > 1) && (contactoDesttele == null || dd1.size() > 1))) {
                        JOptionPane.showInternalMessageDialog(null, "O contacto n?o Exite, ou Esta associado a mais que um numero",
                                "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        if (contactoOrgiemtele != null && dd.get(0).getTelephones().size() == 1) {
                            conctatOrig = dd.get(0).getName();
                        }
                        if (contactoDesttele != null && dd1.get(0).getTelephones().size() == 1) {
                            conctatDest = dd1.get(0).getName();
                        }
                        grafico();
                    }
                }
            }
        }
    }


      public void grafico(){
          controL = 0;
          ad = new JFrame();
          ad.setSize(300,300);
          JLabel legenda = new JLabel("Recbendo Chamada de " + conctatOrig);
          legenda.setHorizontalAlignment(SwingConstants.CENTER);
          setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
          ad.add(legenda,BorderLayout.NORTH);
          JPanel p = new JPanel( new BorderLayout());
          JButton b = new JButton( "Atender Chamada" );
          b.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  ad.setVisible(false);
              }
          });
          b.addActionListener(this::chamadaAtend);
          p.add(b,BorderLayout.WEST);
          b = new JButton("N?o Atender Chamada");
          p.add(b,BorderLayout.EAST);
          b.addActionListener(this::chamadanaoatendida);
          ad.add(p,BorderLayout.SOUTH);
          ad.setVisible(true);
          pack();
          count =0;
         tempoChamaNormal();


      }

    public void tempoChamaNormal () {
        //final int[] c = {0};
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count++;
                if (count < 15 ) {
                             sai++;
                } else {
                    if(controL ==0) {
                        metodoChamadanaoatendida();}
                        ((Timer) (e.getSource())).stop();


                }
            }});

         timer.setInitialDelay(0); timer.start();
         }



    private void chamadanaoatendida(ActionEvent actionEvent){
          controL = 1;
           tempoChamaNormal();
           metodoChamadanaoatendida();


    }

    private void metodoChamadanaoatendida(){

        Call revesor = new Call(conctatOrig,conctatDest,0,retHora(),new Date(),0, OrgenaChamada);
        OrgenaChamada++;
        adicionaInformacao(revesor,naochamadaAtendida,listnaochamadaAtendida,conctatDest);
        Call revesor2 = new Call(conctatOrig,conctatDest,0,retHora(),new Date(),1, OrgenaChamada);
        OrgenaChamada++;
        for (Call mdh : listchamadaefectuada){
            if(mdh.dest.equals(conctatDest) && (mdh.ori.equals(conctatOrig))){
                revesor2.tempo = revesor2.tempo + mdh.tempo;
            }
        }
        adicionaInformacao(revesor2,chamadaefectuada,listchamadaefectuada,conctatOrig);
        ad.setVisible(false);


        JOptionPane.showInternalMessageDialog(null, "Chamada n?o atendida",
                "", JOptionPane.INFORMATION_MESSAGE);


    }


    private void chamadaAtend (ActionEvent actionEvent) {

        controL = 1;
        start = System.currentTimeMillis();
        Call revesor = new Call(conctatOrig,conctatDest,0,retHora(),new Date(),0, OrgenaChamada);
        OrgenaChamada++;
        adicionaInformacao(revesor,chamadaRecebida,listchamadaRecebida,conctatDest);

        JFrame ad = new JFrame();
        ad.setSize(300,300);
        JLabel legenda = new JLabel("Chamada a Decorrer");
        JLabel legenda1 = new JLabel("Tempo");
        ned = new JLabel("hhfhfhf");
        legenda.setHorizontalAlignment(SwingConstants.CENTER);
        legenda1.setHorizontalAlignment(SwingConstants.CENTER);
        ned.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pbh.add(legenda,BorderLayout.NORTH);
        pbh.add(legenda1,BorderLayout.CENTER);
        pbh.add(ned,BorderLayout.SOUTH);
        ad.add(pbh,BorderLayout.NORTH);
        JButton b = new JButton( "Terminar Chamada" );
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count1 =24000000;
                tempoChamaatendida();
                elapsed = (System.currentTimeMillis() - start)/1000;
                Call revesor2 = new Call(conctatOrig,conctatDest,(int) elapsed,retHora(),new Date(),1, OrgenaChamada);
                OrgenaChamada++;
                for (Call mdh : listchamadaefectuada){
                    if(mdh.dest.equals(conctatDest) && (mdh.ori.equals(conctatOrig))){
                        revesor2.tempo = revesor2.tempo + mdh.tempo;
                    }
                }

               adicionaInformacao(revesor2,chamadaefectuada,listchamadaefectuada,conctatOrig);
                ad.setVisible(false);

            }
        });
        ad.add(b,BorderLayout.SOUTH);

        ad.setVisible(true);
        pack();
        count1 =0;
        tempoChamaatendida ();
     ///   System.out.println("agora este e o novo valor de" + count1);
      ///  foi a mehor programa??o que ja tevi na minha vida
    }





   public String retHora(){
       java.util.Date data = new java.util.Date();
       SimpleDateFormat formatarHora = new SimpleDateFormat("hh:mm:ss");
       return formatarHora.format(data);
   }


   public void tempoChamaatendida () {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count1++;
                if (count1 < 10 ) {
                      //  String r = "" + count +"s";
                        ned.setText(count1  + "s");
                } else {
                    ned.setText("");
                    ((Timer) (e.getSource())).stop();
                }
            }});
        timer.setInitialDelay(0);
        timer.start(); }


    public void adicionaInformacao(Call actu,Map<String,ArrayList<Call>> add,ArrayList<Call> in, String chave){
        in.removeIf((m) -> m.ori.equals(actu.ori) && m.dest.equals(actu.dest));
        in.add(actu);
        add.put(chave, in);
    }


        public static void main(String[] args) {
        new NoteBookFrame().setVisible( true );
            System.out.println();

    }
}
