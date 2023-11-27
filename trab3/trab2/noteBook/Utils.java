package trab2.noteBook;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * M�todos utilit�rios
 */
public class Utils {

    /**
     * todo COMENTAR
     *
     * @param m
     * @param key
     * @param value
     * @param add
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean actualize(Map<K, V> m,
                                           Supplier<K> key, Supplier<V> value,
                                           Function<V, Boolean> add) {
        K k = key.get();
        V c = m.get(k);
        if (c != null)
            return add.apply(c);
        m.put(k, value.get());
        return true;
    }

    /**
     * Aplicar a a��o a todos os valores contidos nas cole��es associadas �s chaves.
     *
     * @param m      contentor que associa a cada chave uma cole��o de valores.
     * @param action a��o a executar sobre cada valor V
     * @param <K>    tipo da chave do contentor associativo.
     * @param <V>    tipo do valor de cada elemento da cole��o associada.
     * @return
     */
    public static <K, V> void foreachV(Map<K, ? extends Collection<V>> m, Consumer<V> action) {
        for( Map.Entry<K,? extends Collection<V>> linha : m.entrySet()){

            Collection<V> a = linha.getValue();
            for (V b : a) {
                action.accept(b);
            }
        }

    }



    /**
     * Num contentor associativo obter a cole��o de chaves cujos valores associados
     * s�o os maiores segundo um determinado comparador.
     *
     * @param m   contntor associativo
     * @param cmp comparador
     * @return
     */
    public static <K, V> Collection<K> greater(Map<K, V> m, Comparator<V> cmp) {
        Collection<K> w = new ArrayList<>();
        Iterator<Map.Entry<K,V>>itr = m.entrySet().iterator();
            V aux = null;
        while(itr.hasNext()){
            Map.Entry<K,V> chave = itr.next();
            if( aux == null|| cmp.compare(chave.getValue(),aux) > 0){
                aux = chave.getValue();
            }
        }
        V aux1 = aux;
        m.forEach((k,v) -> {

               if( cmp.compare(v,aux1)== 0){ w.add(k);}
        });

        // dificuldade em fazer ???????????????????????--------------------????????????--------??????
        // Converte o map em set para se poder percorrer num ciclo
        // 161


        return w;
       }


    }


