package trab2.noteBook;

import java.util.*;
import trab2.noteBook.Date;

import static trab2.noteBook.Date.getDays;

/**
 * Contacto. Um contacto tem nome, data de nascimento e conjunto de n�meros de telefone.
 */
public class Contact implements Comparable<Contact> {
    private final Date birthDate;
    private final String name;
    // Os n�mero de telefones devem ser unicos e devem
    // ser obtidos pela ordem que foram adicionados.
    private Set< String > telephones = new LinkedHashSet<String>();// todo - INSTANCIAR UM SET

    public Contact( String n, Date d ) {
        this.name = n;
        this.birthDate = d;
    }


    public String getName()    { return name;      }


    public Date getBirthDate() { return birthDate; }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   public Collection<String> getTelephones() {
        return Collections.unmodifiableCollection( telephones );
    }


    public void addTelephones( Collection<String> telephones ) {
        this.telephones.addAll( telephones );
    }



    public boolean join( Contact c ) {
        if ( !this.getBirthDate().equals( c.getBirthDate() ) )
            return false;
        addTelephones(c.getTelephones());
        return true;
    }


    @Override
    public String toString() {
        if ( telephones.isEmpty() )
            return String.format("%s %-40s", birthDate.toString(), name );
        return String.format("%s %-40s %s", birthDate.toString(), name, telephones );
    }

    /**
     * Retorna o n�mero de anos do contacto.
     * @return
     */
    public int getAge() {
        Date d = new Date();
        int age= d.getYear() - birthDate.getYear();
        if(d.getMonth() >= birthDate.getMonth()) {
            if (d.getDay() >= birthDate.getDay()) {
                return age;
            } else{
                return age - 1;
            }
        }
    return age - 1;
    }

    /**
     * Dois contactos s�o iguais se t�m o nome e a data de nascimento iguais.
     * Nos nomes n�o se deve destinguir letras minusculas de maiusculas.
     * @param o contacto a comparar
     * @return true caso tenham o mesmo nome e data de nascimento.
     */
    @Override
    public boolean equals( Object o ) {
        if( o == null || this.getClass() != o.getClass())  return false;
        Contact c = (Contact) o;
        if( birthDate.equals(c.getBirthDate())  && name.equalsIgnoreCase(c.getName()) ){
            return true;
        }return false;

    }


    /**
     * Compara dois contactos.
     * A compara��o � por data de nascimento e para a mesma data de nascimento
     * por nome sem distinguir letras minusculas de maiusculas.
     * @param c contacto a comparar
     * @return  0 se this == c; >0 se this > c;  <0 this < c.
     */
    @Override
    public int compareTo( Contact c ) {

        if(birthDate.equals(c.getBirthDate())){
        return name.toUpperCase().compareTo(c.getName().toUpperCase());
        }
        return birthDate.compareTo(c.getBirthDate());
    }



    /**
     * Obter o hash code. O hash code de um contacto � a soma dos hash codes
     * dos membros (nome e data).
     * @return hash code
     */

    @Override
    public int hashCode() {
        // Ter em aten��o que o m�todo equals e o m�todo hashCode t�m que ser consistentes.
        // O m�todo equals() � consistente com o m�todo hashCode(), se e s� se, dois objectos
        // iguais tiverem o mesmo valor de hash.


         return birthDate.hashCode() + name.toUpperCase().hashCode();
    }
}

